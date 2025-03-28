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
package org.dromara.visor.module.infra.controller;

import cn.orionsec.kit.lang.define.wrapper.DataGrid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.validator.group.Page;
import org.dromara.visor.framework.biz.operator.log.core.annotation.OperatorLog;
import org.dromara.visor.framework.log.core.annotation.IgnoreLog;
import org.dromara.visor.framework.log.core.enums.IgnoreLogMode;
import org.dromara.visor.framework.web.core.annotation.RestWrapper;
import org.dromara.visor.module.infra.define.operator.OperatorLogOperatorType;
import org.dromara.visor.module.infra.entity.request.operator.OperatorLogClearRequest;
import org.dromara.visor.module.infra.entity.request.operator.OperatorLogQueryRequest;
import org.dromara.visor.module.infra.entity.vo.OperatorLogVO;
import org.dromara.visor.module.infra.service.OperatorLogService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 操作日志 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-10-10 17:08
 */
@Tag(name = "infra - 操作日志服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/infra/operator-log")
public class OperatorLogController {

    @Resource
    private OperatorLogService operatorLogService;

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/query")
    @Operation(summary = "分页查询操作日志")
    @PreAuthorize("@ss.hasPermission('infra:operator-log:query')")
    public DataGrid<OperatorLogVO> getOperatorLogPage(@Validated(Page.class) @RequestBody OperatorLogQueryRequest request) {
        return operatorLogService.getOperatorLogPage(request);
    }

    @OperatorLog(OperatorLogOperatorType.DELETE)
    @DeleteMapping("/delete")
    @Operation(summary = "删除操作日志")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('infra:operator-log:delete')")
    public Integer deleteOperatorLog(@RequestParam("idList") List<Long> idList) {
        return operatorLogService.deleteOperatorLog(idList);
    }

    @PostMapping("/count")
    @Operation(summary = "查询操作日志数量")
    @PreAuthorize("@ss.hasPermission('infra:operator-log:query')")
    public Long getOperatorLogCount(@Validated @RequestBody OperatorLogQueryRequest request) {
        return operatorLogService.getOperatorLogCount(request);
    }

    @OperatorLog(OperatorLogOperatorType.CLEAR)
    @PostMapping("/clear")
    @Operation(summary = "清空操作日志")
    @PreAuthorize("@ss.hasPermission('infra:operator-log:management:clear')")
    public Integer clearOperatorLog(@Validated @RequestBody OperatorLogClearRequest request) {
        return operatorLogService.clearOperatorLog(request);
    }

}

