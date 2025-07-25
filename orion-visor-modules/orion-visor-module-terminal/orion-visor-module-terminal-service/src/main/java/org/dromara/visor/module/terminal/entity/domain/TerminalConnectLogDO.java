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
package org.dromara.visor.module.terminal.entity.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.dromara.visor.framework.mybatis.core.domain.BaseDO;

import java.util.Date;

/**
 * 终端连接日志 实体对象
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-12-26 22:09
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "terminal_connect_log", autoResultMap = true)
@Schema(name = "TerminalConnectLogDO", description = "终端连接日志 实体对象")
public class TerminalConnectLogDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户id")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "用户名")
    @TableField("username")
    private String username;

    @Schema(description = "主机id")
    @TableField("host_id")
    private Long hostId;

    @Schema(description = "主机名称")
    @TableField("host_name")
    private String hostName;

    @Schema(description = "主机地址")
    @TableField("host_address")
    private String hostAddress;

    @Schema(description = "类型")
    @TableField("type")
    private String type;

    @Schema(description = "sessionId")
    @TableField("session_id")
    private String sessionId;

    @Schema(description = "状态")
    @TableField("status")
    private String status;

    @Schema(description = "开始时间")
    @TableField("start_time")
    private Date startTime;

    @Schema(description = "结束时间")
    @TableField("end_time")
    private Date endTime;

    @Schema(description = "额外信息")
    @TableField("extra_info")
    private String extraInfo;

    @Schema(description = "创建人")
    @TableField(exist = false)
    private String creator;

    @Schema(description = "修改人")
    @TableField(exist = false)
    private String updater;

}
