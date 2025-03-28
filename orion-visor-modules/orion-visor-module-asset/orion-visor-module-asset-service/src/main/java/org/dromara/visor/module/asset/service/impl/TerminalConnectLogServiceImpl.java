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

import cn.orionsec.kit.lang.define.wrapper.DataGrid;
import cn.orionsec.kit.lang.utils.Arrays1;
import cn.orionsec.kit.lang.utils.collect.Lists;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.utils.SqlUtils;
import org.dromara.visor.common.utils.Valid;
import org.dromara.visor.framework.biz.operator.log.core.utils.OperatorLogs;
import org.dromara.visor.framework.security.core.utils.SecurityUtils;
import org.dromara.visor.module.asset.convert.TerminalConnectLogConvert;
import org.dromara.visor.module.asset.dao.TerminalConnectLogDAO;
import org.dromara.visor.module.asset.entity.domain.TerminalConnectLogDO;
import org.dromara.visor.module.asset.entity.dto.TerminalConnectLogExtraDTO;
import org.dromara.visor.module.asset.entity.request.host.TerminalConnectLogClearRequest;
import org.dromara.visor.module.asset.entity.request.host.TerminalConnectLogCreateRequest;
import org.dromara.visor.module.asset.entity.request.host.TerminalConnectLogQueryRequest;
import org.dromara.visor.module.asset.entity.vo.TerminalConnectLogVO;
import org.dromara.visor.module.asset.enums.TerminalConnectStatusEnum;
import org.dromara.visor.module.asset.enums.TerminalConnectTypeEnum;
import org.dromara.visor.module.asset.handler.host.terminal.manager.TerminalManager;
import org.dromara.visor.module.asset.handler.host.terminal.model.TerminalConfig;
import org.dromara.visor.module.asset.handler.host.terminal.session.ITerminalSession;
import org.dromara.visor.module.asset.service.TerminalConnectLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 终端连接日志 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-26 22:09
 */
@Slf4j
@Service
public class TerminalConnectLogServiceImpl implements TerminalConnectLogService {

    @Resource
    private TerminalConnectLogDAO terminalConnectLogDAO;

    @Resource
    private TerminalManager terminalManager;

    @Override
    public TerminalConnectLogDO create(TerminalConnectTypeEnum type, TerminalConnectLogCreateRequest request) {
        TerminalConnectLogDO record = TerminalConnectLogConvert.MAPPER.to(request);
        record.setType(type.name());
        String status = request.getStatus();
        record.setStatus(status);
        record.setStartTime(new Date());
        record.setExtraInfo(JSON.toJSONString(request.getExtra()));
        // 失败直接设置结束时间
        if (TerminalConnectStatusEnum.FAILED.name().equals(status)) {
            record.setEndTime(new Date());
        }
        terminalConnectLogDAO.insert(record);
        return record;
    }

    @Override
    public DataGrid<TerminalConnectLogVO> getTerminalConnectLogPage(TerminalConnectLogQueryRequest request) {
        // 条件
        LambdaQueryWrapper<TerminalConnectLogDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return terminalConnectLogDAO.of()
                .wrapper(wrapper)
                .page(request)
                .order(request, TerminalConnectLogDO::getId)
                .dataGrid(s -> {
                    TerminalConnectLogVO vo = TerminalConnectLogConvert.MAPPER.to(s);
                    vo.setExtra(JSON.parseObject(s.getExtraInfo(), TerminalConnectLogExtraDTO.class));
                    return vo;
                });
    }

    @Override
    public List<TerminalConnectLogVO> getTerminalConnectSessions(TerminalConnectLogQueryRequest request) {
        // 查询全部
        List<Long> idList = terminalManager.getChannelSessions()
                .values()
                .stream()
                .map(ConcurrentHashMap::values)
                .flatMap(Collection::stream)
                .filter(s -> !s.isClosed())
                .map(ITerminalSession::getConfig)
                .filter(Objects::nonNull)
                .map(TerminalConfig::getLogId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (idList.isEmpty()) {
            return Lists.empty();
        }
        // 条件
        request.setIdList(idList);
        request.setStatus(TerminalConnectStatusEnum.CONNECTING.name());
        LambdaQueryWrapper<TerminalConnectLogDO> wrapper = this.buildQueryWrapper(request)
                .orderByDesc(TerminalConnectLogDO::getId);
        // 查询
        return terminalConnectLogDAO.of(wrapper)
                .list(s -> {
                    TerminalConnectLogVO vo = TerminalConnectLogConvert.MAPPER.to(s);
                    vo.setExtra(JSON.parseObject(s.getExtraInfo(), TerminalConnectLogExtraDTO.class));
                    return vo;
                });
    }

    @Override
    public Integer updateStatusById(Long id, TerminalConnectStatusEnum status, Map<String, Object> partial) {
        log.info("TerminalConnectLogService-updateStatusById start id: {}, status: {}", id, status);
        // 查询
        TerminalConnectLogDO record = terminalConnectLogDAO.of()
                .createWrapper()
                .eq(TerminalConnectLogDO::getId, id)
                .orderByDesc(TerminalConnectLogDO::getId)
                .then()
                .getOne();
        if (record == null) {
            log.info("TerminalConnectLogService-updateStatusById no record id: {}", id);
            return Const.N_0;
        }
        return this.updateStatus(record, status, partial);
    }

    /**
     * 更新状态
     *
     * @param record  record
     * @param status  status
     * @param partial partial
     * @return effect
     */
    private int updateStatus(TerminalConnectLogDO record, TerminalConnectStatusEnum status, Map<String, Object> partial) {
        // 更新
        TerminalConnectLogDO update = new TerminalConnectLogDO();
        update.setId(record.getId());
        update.setStatus(status.name());
        update.setEndTime(new Date());
        if (partial != null) {
            Map<String, Object> extra = JSON.parseObject(record.getExtraInfo());
            if (extra == null) {
                extra = partial;
            } else {
                extra.putAll(partial);
            }
            update.setExtraInfo(JSON.toJSONString(extra));
        }
        return terminalConnectLogDAO.updateById(update);
    }

    @Override
    public Set<Long> getLatestConnectHostId(TerminalConnectLogQueryRequest request) {
        // 查询最近连接的主机
        List<Long> hostIdList = terminalConnectLogDAO.selectLatestConnectHostId(SecurityUtils.getLoginUserId(), request.getType(), request.getLimit());
        return new LinkedHashSet<>(hostIdList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteTerminalConnectLog(List<Long> idList) {
        log.info("TerminalConnectLogService.deleteTerminalConnectLog start {}", JSON.toJSONString(idList));
        if (Lists.isEmpty(idList)) {
            OperatorLogs.add(OperatorLogs.COUNT, Const.N_0);
            return Const.N_0;
        }
        // 删除日志表
        int effect = terminalConnectLogDAO.deleteBatchIds(idList);
        log.info("TerminalConnectLogService.deleteTerminalConnectLog finish {}", effect);
        // 设置日志参数
        OperatorLogs.add(OperatorLogs.COUNT, effect);
        return effect;
    }

    @Override
    public Long getTerminalConnectLogCount(TerminalConnectLogQueryRequest request) {
        // 条件
        LambdaQueryWrapper<TerminalConnectLogDO> wrapper = this.buildQueryWrapper(request);
        // 查询
        return terminalConnectLogDAO.of()
                .wrapper(wrapper)
                .countMax(request.getLimit());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer clearTerminalConnectLog(TerminalConnectLogClearRequest request) {
        log.info("TerminalConnectLogService.clearTerminalConnectLog start {}", JSON.toJSONString(request));
        // 查询
        LambdaQueryWrapper<TerminalConnectLogDO> wrapper = this.buildQueryWrapper(request)
                .select(TerminalConnectLogDO::getId)
                .orderByAsc(TerminalConnectLogDO::getId)
                .last(SqlUtils.limit(request.getLimit()));
        List<TerminalConnectLogDO> list = terminalConnectLogDAO.selectList(wrapper);
        if (list.isEmpty()) {
            log.info("TerminalConnectLogService.clearTerminalConnectLog empty");
            // 设置日志参数
            OperatorLogs.add(OperatorLogs.COUNT, Const.N_0);
            return Const.N_0;
        }
        // 删除
        List<Long> idList = list.stream()
                .map(TerminalConnectLogDO::getId)
                .collect(Collectors.toList());
        return this.deleteTerminalConnectLog(idList);
    }

    @Override
    public Integer forceOffline(TerminalConnectLogQueryRequest request) {
        Long id = request.getId();
        // 查询数据是否存在
        TerminalConnectLogDO record = terminalConnectLogDAO.selectById(id);
        Valid.notNull(record, ErrorMessage.LOG_ABSENT);
        Valid.eq(record.getStatus(), TerminalConnectStatusEnum.CONNECTING.name(), ErrorMessage.ILLEGAL_STATUS);
        // 设置日志参数
        OperatorLogs.add(OperatorLogs.HOST_NAME, record.getHostName());
        // 获取会话
        TerminalConnectLogExtraDTO extra = JSON.parseObject(record.getExtraInfo(), TerminalConnectLogExtraDTO.class);
        ITerminalSession session = terminalManager.getSession(extra.getChannelId(), extra.getSessionId());
        if (session != null) {
            // 关闭会话
            session.forceOffline();
        }
        // 更新状态
        return this.updateStatus(record, TerminalConnectStatusEnum.FORCE_OFFLINE, null);
    }

    @Override
    public LambdaQueryWrapper<TerminalConnectLogDO> buildQueryWrapper(TerminalConnectLogQueryRequest request) {
        return terminalConnectLogDAO.wrapper()
                .eq(TerminalConnectLogDO::getId, request.getId())
                .in(TerminalConnectLogDO::getId, request.getIdList())
                .eq(TerminalConnectLogDO::getUserId, request.getUserId())
                .eq(TerminalConnectLogDO::getHostId, request.getHostId())
                .like(TerminalConnectLogDO::getHostAddress, request.getHostAddress())
                .eq(TerminalConnectLogDO::getType, request.getType())
                .like(TerminalConnectLogDO::getSessionId, request.getSessionId())
                .eq(TerminalConnectLogDO::getStatus, request.getStatus())
                .in(TerminalConnectLogDO::getStatus, request.getStatusList())
                .ge(TerminalConnectLogDO::getStartTime, Arrays1.getIfPresent(request.getStartTimeRange(), 0))
                .le(TerminalConnectLogDO::getStartTime, Arrays1.getIfPresent(request.getStartTimeRange(), 1))
                .le(TerminalConnectLogDO::getCreateTime, request.getCreateTimeLe());
    }

}
