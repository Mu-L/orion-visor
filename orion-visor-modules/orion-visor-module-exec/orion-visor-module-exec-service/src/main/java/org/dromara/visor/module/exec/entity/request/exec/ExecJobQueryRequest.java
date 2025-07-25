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
package org.dromara.visor.module.exec.entity.request.exec;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.dromara.visor.common.entity.BaseQueryRequest;

import javax.validation.constraints.Size;

/**
 * 计划任务 查询请求对象
 *
 * @author Jiahang Li
 * @version 1.0.3
 * @since 2024-3-28 12:03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(name = "ExecJobQueryRequest", description = "计划任务 查询请求对象")
public class ExecJobQueryRequest extends BaseQueryRequest {

    @Schema(description = "id")
    private Long id;

    @Size(max = 64)
    @Schema(description = "任务名称")
    private String name;

    @Schema(description = "执行命令")
    private String command;

    @Schema(description = "任务状态")
    private Integer status;

    @Schema(description = "执行用户id")
    private Long execUserId;

    @Schema(description = "是否查询最近执行任务")
    private Boolean queryRecentLog;

}
