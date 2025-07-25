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
package org.dromara.visor.module.terminal.service.impl;

import cn.orionsec.kit.lang.utils.Booleans;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.constant.Const;
import org.dromara.visor.common.constant.ErrorMessage;
import org.dromara.visor.common.utils.Valid;
import org.dromara.visor.framework.security.core.utils.SecurityUtils;
import org.dromara.visor.module.infra.api.DataGroupApi;
import org.dromara.visor.module.infra.api.DataGroupUserApi;
import org.dromara.visor.module.infra.entity.dto.data.DataGroupCreateDTO;
import org.dromara.visor.module.infra.entity.dto.data.DataGroupDTO;
import org.dromara.visor.module.infra.entity.dto.data.DataGroupRenameDTO;
import org.dromara.visor.module.infra.enums.DataGroupTypeEnum;
import org.dromara.visor.module.terminal.convert.PathBookmarkGroupConvert;
import org.dromara.visor.module.terminal.dao.PathBookmarkDAO;
import org.dromara.visor.module.terminal.entity.domain.PathBookmarkDO;
import org.dromara.visor.module.terminal.entity.request.path.PathBookmarkGroupCreateRequest;
import org.dromara.visor.module.terminal.entity.request.path.PathBookmarkGroupDeleteRequest;
import org.dromara.visor.module.terminal.entity.request.path.PathBookmarkGroupUpdateRequest;
import org.dromara.visor.module.terminal.entity.vo.PathBookmarkGroupVO;
import org.dromara.visor.module.terminal.service.PathBookmarkGroupService;
import org.dromara.visor.module.terminal.service.PathBookmarkService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 路径标签分组 服务实现类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024-1-24 12:28
 */
@Slf4j
@Service
public class PathBookmarkGroupServiceImpl implements PathBookmarkGroupService {

    @Resource
    private DataGroupApi dataGroupApi;

    @Resource
    private DataGroupUserApi dataGroupUserApi;

    @Resource
    private PathBookmarkDAO pathBookmarkDAO;

    @Resource
    private PathBookmarkService pathBookmarkService;

    @Override
    public Long createPathBookmarkGroup(PathBookmarkGroupCreateRequest request) {
        Long userId = SecurityUtils.getLoginUserId();
        log.info("PathBookmarkGroupService-createPathBookmarkGroup request: {}", JSON.toJSONString(request));
        // 创建
        DataGroupCreateDTO create = PathBookmarkGroupConvert.MAPPER.to(request);
        create.setParentId(Const.ROOT_PARENT_ID);
        return dataGroupUserApi.createDataGroup(DataGroupTypeEnum.PATH_BOOKMARK, userId, create);
    }

    @Override
    public Integer updatePathBookmarkGroupById(PathBookmarkGroupUpdateRequest request) {
        Long id = Valid.notNull(request.getId(), ErrorMessage.ID_MISSING);
        log.info("PathBookmarkGroupService-updatePathBookmarkGroupById id: {}, request: {}", id, JSON.toJSONString(request));
        // 重命名
        DataGroupRenameDTO rename = PathBookmarkGroupConvert.MAPPER.to(request);
        return dataGroupApi.renameDataGroup(rename);
    }

    @Override
    public List<PathBookmarkGroupVO> getPathBookmarkGroupList() {
        Long userId = SecurityUtils.getLoginUserId();
        // 查询分组
        return dataGroupUserApi.getDataGroupList(DataGroupTypeEnum.PATH_BOOKMARK, userId)
                .stream()
                .sorted(Comparator.comparing(DataGroupDTO::getSort))
                .map(PathBookmarkGroupConvert.MAPPER::to)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deletePathBookmarkGroup(PathBookmarkGroupDeleteRequest request) {
        Long userId = SecurityUtils.getLoginUserId();
        Long id = request.getId();
        log.info("PathBookmarkGroupService-deletePathBookmarkGroupById id: {}", id);
        // 删除分组
        Integer effect = dataGroupApi.deleteDataGroupById(id);
        if (Booleans.isTrue(request.getDeleteItem())) {
            // 删除组内数据
            effect += pathBookmarkService.deleteByGroupId(userId, id);
        } else {
            // 移动到根节点
            effect += pathBookmarkService.setGroupNull(userId, id);
        }
        return effect;
    }

    @Override
    public void clearUnusedGroup() {
        // 查询全部 groupId
        Map<Long, List<Long>> userGroupMap = pathBookmarkDAO.of()
                .createWrapper()
                .select(PathBookmarkDO::getUserId, PathBookmarkDO::getGroupId)
                .isNotNull(PathBookmarkDO::getGroupId)
                .groupBy(PathBookmarkDO::getGroupId)
                .groupBy(PathBookmarkDO::getUserId)
                .then()
                .stream()
                .collect(Collectors.groupingBy(PathBookmarkDO::getUserId,
                        Collectors.mapping(PathBookmarkDO::getGroupId, Collectors.toList())));
        userGroupMap.forEach((k, v) -> {
            // 查询用户分组
            List<DataGroupDTO> groups = dataGroupUserApi.getDataGroupList(DataGroupTypeEnum.PATH_BOOKMARK, k);
            if (groups.isEmpty()) {
                return;
            }
            // 不存在的则移除
            List<Long> deleteGroupList = groups.stream()
                    .map(DataGroupDTO::getId)
                    .filter(s -> !v.contains(s))
                    .collect(Collectors.toList());
            if (deleteGroupList.isEmpty()) {
                return;
            }
            log.info("PathBookmarkGroupService.clearUnusedGroup userId: {}, deleteGroupList: {}", k, deleteGroupList);
            // 删除分组
            Integer effect = dataGroupUserApi.deleteDataGroupByIdList(DataGroupTypeEnum.PATH_BOOKMARK, k, deleteGroupList);
            log.info("PathBookmarkGroupService.clearUnusedGroup userId: {}, effect: {}", k, effect);
        });
    }

}
