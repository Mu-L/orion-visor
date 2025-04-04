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
package org.dromara.visor.module.infra.service;

import org.dromara.visor.module.infra.entity.domain.DataExtraDO;
import org.dromara.visor.module.infra.entity.request.data.DataExtraQueryRequest;
import org.dromara.visor.module.infra.entity.request.data.DataExtraSetRequest;

import java.util.List;
import java.util.Map;

/**
 * 数据拓展信息 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-19 18:05
 */
public interface DataExtraService {

    /**
     * 更新数据拓展信息 不存在则新增
     *
     * @param request request
     * @return effect
     */
    Integer setExtraItem(DataExtraSetRequest request);

    /**
     * 新增数据拓展信息
     *
     * @param request request
     * @return id
     */
    Long addExtraItem(DataExtraSetRequest request);

    /**
     * 批量新增数据拓展信息
     *
     * @param rows rows
     */
    void addExtraItems(List<DataExtraSetRequest> rows);

    /**
     * 更新数据拓展信息
     *
     * @param id    id
     * @param value value
     * @return effect
     */
    Integer updateExtraValue(Long id, String value);

    /**
     * 批量更新数据拓展信息
     *
     * @param map map
     */
    void batchUpdateExtraValue(Map<Long, String> map);

    /**
     * 查询额外配置项
     *
     * @param request request
     * @return item
     */
    String getExtraItemValue(DataExtraQueryRequest request);

    /**
     * 查询额外配置项
     *
     * @param request request
     * @return item
     */
    Map<Long, String> getExtraItemValues(DataExtraQueryRequest request);

    /**
     * 查询额外配置项 (查询缓存)
     *
     * @param userId userId
     * @param type   type
     * @param item   item
     * @param relId  relId
     * @return value
     */
    String getExtraItemValueByCache(Long userId, String type, String item, Long relId);

    /**
     * 查询额外配置项 (查询缓存)
     *
     * @param userId userId
     * @param type   type
     * @param item   item
     * @return relId:value
     */
    Map<Long, String> getExtraItemValuesByCache(Long userId, String type, String item);

    /**
     * 查询额外配置项 (查询缓存)
     *
     * @param userId userId
     * @param type   type
     * @param items  items
     * @return [relId:value, relId:value]
     */
    List<Map<Long, String>> getExtraItemsValuesByCache(Long userId, String type, List<String> items);

    /**
     * 查询额外配置
     *
     * @param request request
     * @return rows
     */
    DataExtraDO getExtraItem(DataExtraQueryRequest request);

    /**
     * 查询额外配置
     *
     * @param request request
     * @return rows
     */
    List<DataExtraDO> getExtraItems(DataExtraQueryRequest request);

    /**
     * 通过 userId 删除
     *
     * @param userId userId
     * @return effect
     */
    Integer deleteByUserId(Long userId);

    /**
     * 通过 userId 删除
     *
     * @param userIdList userIdList
     * @return effect
     */
    Integer deleteByUserIdList(List<Long> userIdList);

    /**
     * 通过 relId 删除
     *
     * @param type  type
     * @param relId relId
     * @return effect
     */
    Integer deleteByRelId(String type, Long relId);

    /**
     * 通过 relId 删除
     *
     * @param type      type
     * @param relIdList relIdList
     * @return effect
     */
    Integer deleteByRelIdList(String type, List<Long> relIdList);

}
