/*
 * Copyright (c) 2023 - present Dromara, All rights reserved.
 *
 *   https://visor.dromara.org
 *   https://visor.dromara.org.cn
 *   https://visor.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dromara.visor.module.asset.service.impl;

import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.Valid;
import cn.orionsec.kit.lang.utils.collect.Lists;
import cn.orionsec.kit.spring.SpringHolder;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.security.LoginUser;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.framework.security.core.utils.SecurityUtils;
import org.dromara.visor.module.asset.convert.ExecConvert;
import org.dromara.visor.module.asset.convert.ExecHostLogConvert;
import org.dromara.visor.module.asset.convert.ExecLogConvert;
import org.dromara.visor.module.asset.dao.ExecHostLogDAO;
import org.dromara.visor.module.asset.dao.ExecLogDAO;
import org.dromara.visor.module.asset.dao.HostDAO;
import org.dromara.visor.module.asset.entity.domain.ExecHostLogDO;
import org.dromara.visor.module.asset.entity.domain.ExecLogDO;
import org.dromara.visor.module.asset.entity.domain.HostDO;
import org.dromara.visor.module.asset.entity.dto.ExecCommandExecDTO;
import org.dromara.visor.module.asset.entity.request.exec.ExecCommandRequest;
import org.dromara.visor.module.asset.entity.vo.ExecHostLogVO;
import org.dromara.visor.module.asset.entity.vo.ExecLogVO;
import org.dromara.visor.module.asset.enums.*;
import org.dromara.visor.module.asset.handler.host.exec.command.ExecTaskExecutors;
import org.dromara.visor.module.asset.service.AssetAuthorizedDataService;
import org.dromara.visor.module.asset.service.ExecCommandService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 批量执行服务实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/11 12:03
 */
@Slf4j
@Service
public class ExecCommandServiceImpl implements ExecCommandService {

    private static final int DESC_OMIT = 60;

    @Resource
    private ExecLogDAO execLogDAO;

    @Resource
    private ExecHostLogDAO execHostLogDAO;

    @Resource
    private HostDAO hostDAO;

    @Resource
    private AssetAuthorizedDataService assetAuthorizedDataService;

    @Override
    public ExecLogVO execCommand(ExecCommandRequest request) {
        log.info("ExecService.execCommand start params: {}", JSON.toJSONString(request));
        LoginUser user = Valid.notNull(SecurityUtils.getLoginUser());
        Long userId = user.getId();
        List<Long> hostIdList = request.getHostIdList();
        // 检查主机权限
        List<Long> authorizedHostIdList = assetAuthorizedDataService.getUserAuthorizedEnabledHostId(userId, HostTypeEnum.SSH.name());
        hostIdList.removeIf(s -> !authorizedHostIdList.contains(s));
        log.info("ExecService.startExecCommand host hostList: {}", hostIdList);
        Valid.notEmpty(hostIdList, ErrorMessage.CHECK_AUTHORIZED_HOST);
        // 创建命令
        ExecCommandExecDTO execRequest = ExecConvert.MAPPER.to(request);
        execRequest.setUserId(userId);
        execRequest.setUsername(user.getUsername());
        execRequest.setSource(ExecSourceEnum.BATCH.name());
        execRequest.setExecMode(ExecModeEnum.MANUAL.name());
        // 调用执行
        return this.execCommandWithSource(execRequest);
    }

    @Override
    public ExecLogVO reExecCommand(Long logId) {
        log.info("ExecService.reExecCommand start logId: {}", logId);
        // 获取执行记录
        ExecLogDO execLog = execLogDAO.selectByIdSource(logId, ExecSourceEnum.BATCH.name());
        Valid.notNull(execLog, ErrorMessage.DATA_ABSENT);
        // 获取执行主机
        List<ExecHostLogDO> hostLogs = execHostLogDAO.selectByLogId(logId);
        Valid.notEmpty(hostLogs, ErrorMessage.DATA_ABSENT);
        List<Long> hostIdList = hostLogs.stream()
                .map(ExecHostLogDO::getHostId)
                .collect(Collectors.toList());
        // 调用创建任务
        ExecCommandRequest request = ExecCommandRequest.builder()
                .description(execLog.getDescription())
                .timeout(execLog.getTimeout())
                .scriptExec(execLog.getScriptExec())
                .command(execLog.getCommand())
                .parameterSchema(execLog.getParameterSchema())
                .hostIdList(hostIdList)
                .build();
        // 调用执行
        return SpringHolder.getBean(ExecCommandService.class).execCommand(request);
    }

    @Override
    public ExecLogVO execCommandWithSource(ExecCommandExecDTO request) {
        // 上下文调用执行
        ExecLogVO result = SpringHolder.getBean(ExecCommandService.class).createCommandWithSource(request);
        // 执行命令
        ExecTaskExecutors.start(result.getId(), Lists.map(result.getHosts(), ExecHostLogVO::getId));
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public ExecLogVO createCommandWithSource(ExecCommandExecDTO request) {
        log.info("ExecService.createCommandWithSource start params: {}", JSON.toJSONString(request));
        String command = request.getCommand();
        List<Long> hostIdList = request.getHostIdList();
        // 查询主机信息
        List<HostDO> hosts = hostDAO.selectBatchIds(hostIdList);
        // 插入日志
        ExecLogDO execLog = ExecLogDO.builder()
                .userId(request.getUserId())
                .username(request.getUsername())
                .source(request.getSource())
                .sourceId(request.getSourceId())
                .execMode(request.getExecMode())
                .execSeq(request.getExecSeq())
                .description(Strings.ifBlank(request.getDescription(), () -> {
                    if (command.length() < DESC_OMIT + Const.OMIT.length()) {
                        return command;
                    } else {
                        return command.substring(0, DESC_OMIT) + Const.OMIT;
                    }
                }))
                .command(command)
                .parameterSchema(request.getParameterSchema())
                .timeout(request.getTimeout())
                .scriptExec(request.getScriptExec())
                .status(ExecStatusEnum.WAITING.name())
                .build();
        execLogDAO.insert(execLog);
        Long execId = execLog.getId();
        // 设置主机日志
        List<ExecHostLogDO> execHostLogs = hosts.stream()
                .map(s -> ExecHostLogDO.builder()
                        .logId(execLog.getId())
                        .hostId(s.getId())
                        .hostName(s.getName())
                        .hostAddress(s.getAddress())
                        .status(ExecHostStatusEnum.WAITING.name())
                        .build())
                .collect(Collectors.toList());
        execHostLogDAO.insertBatch(execHostLogs);
        // 操作日志
        OperatorLogs.add(OperatorLogs.LOG_ID, execId);
        // 返回
        ExecLogVO result = ExecLogConvert.MAPPER.to(execLog);
        List<ExecHostLogVO> resultHosts = ExecHostLogConvert.MAPPER.to(execHostLogs);
        result.setHosts(resultHosts);
        return result;
    }

}
