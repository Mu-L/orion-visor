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
package org.dromara.visor.module.asset.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.visor.common.entity.TreeNode;

import java.io.Serializable;
import java.util.List;

/**
 * 主机密钥 视图响应对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-9-20 11:55
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "HostGroupTreeVO", description = "主机密钥 视图响应对象")
public class HostGroupTreeVO implements TreeNode<HostGroupTreeVO>, Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("key")
    @Schema(description = "id")
    private Long id;

    @Schema(description = "父id")
    private Long parentId;

    @JsonProperty("title")
    @Schema(description = "组名称")
    private String name;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "子节点")
    private List<HostGroupTreeVO> children;

}
