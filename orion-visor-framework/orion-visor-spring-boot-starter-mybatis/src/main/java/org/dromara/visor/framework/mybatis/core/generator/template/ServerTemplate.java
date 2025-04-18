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

/**
 * 后端代码模板
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/26 1:14
 */
public class ServerTemplate extends Template {

    public ServerTemplate(Table table) {
        super(table);
        table.enableUnitTest = true;
    }

    public ServerTemplate(Table table, String tableName) {
        super(table);
        table.tableName = tableName;
        table.enableUnitTest = true;
    }

    public ServerTemplate(Table table, String tableName, String comment, String bizPackage) {
        super(table);
        table.tableName = tableName;
        table.comment = comment;
        table.bizPackage = bizPackage;
        table.enableUnitTest = true;
        table.enableOperatorLog = true;
        table.enableDemoApi = true;
    }

    /**
     * 设置表名称
     *
     * @param tableName tableName
     * @return this
     */
    public ServerTemplate tableName(String tableName) {
        table.tableName = tableName;
        return this;
    }

    /**
     * 设置业务注释
     *
     * @param comment comment
     * @return this
     */
    public ServerTemplate comment(String comment) {
        table.comment = comment;
        return this;
    }

    /**
     * 设置业务实体包名
     *
     * @param bizPackage bizPackage
     * @return this
     */
    public ServerTemplate bizPackage(String bizPackage) {
        table.bizPackage = bizPackage;
        return this;
    }

    /**
     * 是否生成 api http 文件
     *
     * @return this
     */
    public ServerTemplate enableApiHttp() {
        table.enableApiHttp = true;
        return this;
    }

    /**
     * 是否生成对外 api
     *
     * @return this
     */
    public ServerTemplate enableProviderApi() {
        table.enableProviderApi = true;
        return this;
    }

    /**
     * 不生成单元测试
     *
     * @return this
     */
    public ServerTemplate disableUnitTest() {
        table.enableUnitTest = false;
        return this;
    }

    /**
     * 不生成操作日志
     *
     * @return this
     */
    public ServerTemplate disableOperatorLog() {
        table.enableOperatorLog = false;
        return this;
    }

    /**
     * 是否生成演示模式 api 注解
     *
     * @return this
     */
    public ServerTemplate disableDemoApi() {
        table.enableDemoApi = false;
        return this;
    }

    /**
     * 设置 cache
     *
     * @return cache
     */
    public CacheTemplate cache() {
        return new CacheTemplate(table);
    }

    /**
     * 设置 cache
     *
     * @param key key
     * @return cache
     */
    public CacheTemplate cache(String key) {
        return new CacheTemplate(table, key);
    }

    /**
     * 设置 cache
     *
     * @param key  key
     * @param desc desc
     * @return cache
     */
    public CacheTemplate cache(String key, String desc) {
        return new CacheTemplate(table, key, desc);
    }

}
