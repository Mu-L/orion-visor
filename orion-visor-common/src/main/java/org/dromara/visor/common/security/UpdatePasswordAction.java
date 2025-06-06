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
package org.dromara.visor.common.security;

import java.io.Serializable;

/**
 * 更新密码操作
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/9/21 11:32
 */
public interface UpdatePasswordAction extends Serializable {

    /**
     * 是否使用新密码
     *
     * @return use
     */
    Boolean getUseNewPassword();

    /**
     * 获取密码
     *
     * @return password
     */
    String getPassword();

    /**
     * 设置密码
     *
     * @param password password
     */
    void setPassword(String password);

}
