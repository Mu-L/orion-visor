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
package org.dromara.visor.framework.mybatis.core.generator.template;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/17 10:44
 */
@Data
public class Table {

    // -------------------- 后端 --------------------

    /**
     * 表名称
     */
    protected String tableName;

    /**
     * 业务注释
     */
    protected String comment;

    /**
     * 业务实体包名
     * <p>
     * request dto 包
     */
    protected String bizPackage;

    /**
     * 是否生成 api http 文件
     */
    protected boolean enableApiHttp;

    /**
     * 是否生成对外 api
     */
    protected boolean enableProviderApi;

    /**
     * 是否生成单元测试
     */
    protected boolean enableUnitTest;

    /**
     * 是否可缓存
     */
    protected boolean enableCache;

    /**
     * 是否生成操作日志
     */
    protected boolean enableOperatorLog;

    /**
     * 是否生成演示模式 api 注解
     */
    protected boolean enableDemoApi;

    /**
     * 缓存的 key
     */
    protected String cacheKey;

    /**
     * 缓存描述
     */
    protected String cacheDesc;

    /**
     * 缓存是否会过期
     */
    protected boolean cacheExpired;

    /**
     * 缓存过期时间
     */
    protected Integer cacheExpireTime;

    /**
     * 缓存过期时间单位
     */
    protected TimeUnit cacheExpireUnit;

    // -------------------- 前端 --------------------

    /**
     * 是否生成 vue 代码
     */
    protected boolean enableVue;

    /**
     * 模块 用于文件名称生成
     */
    protected String module;

    /**
     * 功能 用于文件名称生成
     */
    protected String feature;

    /**
     * 使用抽屉表单
     */
    protected boolean enableDrawerForm;

    /**
     * 列表可多选
     */
    protected boolean enableRowSelection;

    /**
     * 使用卡片视图
     */
    protected boolean enableCardView;

    /**
     * 生成的字典数据
     */
    protected List<DictMeta> dictList;

    protected Table() {
        this.dictList = new ArrayList<>();
    }

}
