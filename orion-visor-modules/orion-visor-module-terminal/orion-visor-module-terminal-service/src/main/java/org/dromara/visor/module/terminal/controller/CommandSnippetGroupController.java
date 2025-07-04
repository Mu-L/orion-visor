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
package org.dromara.visor.module.terminal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.validator.group.Id;
import org.dromara.visor.framework.log.core.annotation.IgnoreLog;
import org.dromara.visor.framework.log.core.enums.IgnoreLogMode;
import org.dromara.visor.framework.web.core.annotation.RestWrapper;
import org.dromara.visor.module.terminal.entity.request.snippet.CommandSnippetGroupCreateRequest;
import org.dromara.visor.module.terminal.entity.request.snippet.CommandSnippetGroupDeleteRequest;
import org.dromara.visor.module.terminal.entity.request.snippet.CommandSnippetGroupUpdateRequest;
import org.dromara.visor.module.terminal.entity.vo.CommandSnippetGroupVO;
import org.dromara.visor.module.terminal.service.CommandSnippetGroupService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 命令片段分组 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024-1-24 12:28
 */
@Tag(name = "terminal - 命令片段分组服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/terminal/command-snippet-group")
public class CommandSnippetGroupController {

    @Resource
    private CommandSnippetGroupService commandSnippetGroupService;

    @PostMapping("/create")
    @Operation(summary = "创建命令片段分组")
    public Long createCommandSnippetGroup(@Validated @RequestBody CommandSnippetGroupCreateRequest request) {
        return commandSnippetGroupService.createCommandSnippetGroup(request);
    }

    @PutMapping("/update")
    @Operation(summary = "更新命令片段分组")
    public Integer updateCommandSnippetGroup(@Validated @RequestBody CommandSnippetGroupUpdateRequest request) {
        return commandSnippetGroupService.updateCommandSnippetGroupById(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/list")
    @Operation(summary = "查询全部命令片段分组")
    public List<CommandSnippetGroupVO> getCommandSnippetGroupList() {
        return commandSnippetGroupService.getCommandSnippetGroupList();
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除命令片段分组")
    public Integer deleteCommandSnippetGroup(@Validated(Id.class) @RequestBody CommandSnippetGroupDeleteRequest request) {
        return commandSnippetGroupService.deleteCommandSnippetGroup(request);
    }

}

