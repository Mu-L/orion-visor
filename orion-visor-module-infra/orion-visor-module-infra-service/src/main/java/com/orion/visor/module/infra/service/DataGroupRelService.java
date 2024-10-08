package com.orion.visor.module.infra.service;

import com.orion.visor.module.infra.entity.domain.DataGroupRelDO;
import com.orion.visor.module.infra.entity.dto.DataGroupRelCacheDTO;

import java.util.List;
import java.util.Map;

/**
 * 数据分组关联 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-11-7 18:44
 */
public interface DataGroupRelService {

    /**
     * 设置分组关联
     *
     * @param groupId   groupId
     * @param relIdList relIdList
     */
    void updateGroupRel(Long groupId, List<Long> relIdList);

    /**
     * 设置关联分组
     *
     * @param type        type
     * @param userId      userId
     * @param groupIdList groupIdList
     * @param relId       relId
     */
    void updateRelGroup(String type, Long userId, List<Long> groupIdList, Long relId);

    /**
     * 添加关联 只新增
     *
     * @param type    type
     * @param userId  userId
     * @param groupId groupId
     * @param relId   relId
     */
    void addGroupRel(String type, Long userId, Long groupId, Long relId);

    /**
     * 添加关联 只新增
     *
     * @param type            type
     * @param userId          userId
     * @param groupRelListMap groupRelListMap
     */
    void addGroupRel(String type, Long userId, Map<Long, List<Long>> groupRelListMap);

    /**
     * 通过 type 查询 relId 缓存
     *
     * @param type   type
     * @param userId userId
     * @return rows
     */
    List<DataGroupRelCacheDTO> getGroupRelListByCache(String type, Long userId);

    /**
     * 通过 groupId 查询 relId 缓存
     *
     * @param type    type
     * @param groupId groupId
     * @return rows
     */
    List<Long> getGroupRelIdListByCache(String type, Long groupId);

    /**
     * 通过 relId 查询 groupRel
     *
     * @param type   type
     * @param userId userId
     * @param relId  relId
     * @return rows
     */
    List<DataGroupRelDO> getGroupRelByRelId(String type, Long userId, Long relId);

    /**
     * 删除数据分组关联
     *
     * @param type   type
     * @param userId userId
     * @param relId  relId
     * @return effect
     */
    Integer deleteByRelId(String type, Long userId, Long relId);

    /**
     * 批量删除数据分组关联
     *
     * @param type      type
     * @param userId    userId
     * @param relIdList relIdList
     * @return effect
     */
    Integer deleteByRelIdList(String type, Long userId, List<Long> relIdList);

    /**
     * 批量删除数据分组关联
     *
     * @param userId      userId
     * @param type        type
     * @param groupIdList groupIdList
     * @return effect
     */
    Integer deleteByGroupIdList(String type, Long userId, List<Long> groupIdList);

}
