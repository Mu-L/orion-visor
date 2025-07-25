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
package org.dromara.visor.module.exec.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.biz.operator.log.core.annotation.OperatorLog;
import org.dromara.visor.framework.biz.operator.log.core.enums.ReturnType;
import org.dromara.visor.framework.web.core.annotation.RestWrapper;
import org.dromara.visor.module.exec.define.operator.ExecCommandOperatorType;
import org.dromara.visor.module.exec.entity.request.exec.ExecCommandRequest;
import org.dromara.visor.module.exec.entity.request.exec.ReExecCommandRequest;
import org.dromara.visor.module.exec.entity.vo.ExecLogVO;
import org.dromara.visor.module.exec.service.ExecCommandService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 批量执行
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/3/11 11:44
 */
@Tag(name = "exec - 批量执行服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/exec/exec-command")
public class ExecCommandController {

    @Resource
    private ExecCommandService execCommandService;

    @OperatorLog(value = ExecCommandOperatorType.EXEC, ret = ReturnType.IGNORE)
    @PostMapping("/exec")
    @Operation(summary = "批量执行命令")
    @PreAuthorize("@ss.hasPermission('exec:exec-command:exec')")
    public ExecLogVO execCommand(@Validated @RequestBody ExecCommandRequest request) {
        return execCommandService.execCommand(request);
    }

    @OperatorLog(value = ExecCommandOperatorType.EXEC, ret = ReturnType.IGNORE)
    @PostMapping("/re-exec")
    @Operation(summary = "重新执行命令")
    @PreAuthorize("@ss.hasPermission('exec:exec-command:exec')")
    public ExecLogVO reExecCommand(@Validated @RequestBody ReExecCommandRequest request) {
        return execCommandService.reExecCommand(request.getLogId());
    }

}
