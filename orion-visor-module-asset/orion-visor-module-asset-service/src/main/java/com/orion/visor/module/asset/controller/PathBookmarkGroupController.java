/*
 * Copyright (c) 2023 - present Jiahang Li (visor.orionsec.cn ljh1553488six@139.com).
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
package com.orion.visor.module.asset.controller;

import com.orion.visor.framework.common.validator.group.Id;
import com.orion.visor.framework.log.core.annotation.IgnoreLog;
import com.orion.visor.framework.log.core.enums.IgnoreLogMode;
import com.orion.visor.framework.web.core.annotation.RestWrapper;
import com.orion.visor.module.asset.entity.request.path.PathBookmarkGroupCreateRequest;
import com.orion.visor.module.asset.entity.request.path.PathBookmarkGroupDeleteRequest;
import com.orion.visor.module.asset.entity.request.path.PathBookmarkGroupUpdateRequest;
import com.orion.visor.module.asset.entity.vo.PathBookmarkGroupVO;
import com.orion.visor.module.asset.service.PathBookmarkGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 路径标签分组 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024-1-24 12:28
 */
@Tag(name = "asset - 路径标签分组服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/asset/path-bookmark-group")
public class PathBookmarkGroupController {

    @Resource
    private PathBookmarkGroupService pathBookmarkGroupService;

    @PostMapping("/create")
    @Operation(summary = "创建路径标签分组")
    public Long createPathBookmarkGroup(@Validated @RequestBody PathBookmarkGroupCreateRequest request) {
        return pathBookmarkGroupService.createPathBookmarkGroup(request);
    }

    @PutMapping("/update")
    @Operation(summary = "更新路径标签分组")
    public Integer updatePathBookmarkGroup(@Validated @RequestBody PathBookmarkGroupUpdateRequest request) {
        return pathBookmarkGroupService.updatePathBookmarkGroupById(request);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/list")
    @Operation(summary = "查询全部路径标签分组")
    public List<PathBookmarkGroupVO> getPathBookmarkGroupList() {
        return pathBookmarkGroupService.getPathBookmarkGroupList();
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除路径标签分组")
    public Integer deletePathBookmarkGroup(@Validated(Id.class) @RequestBody PathBookmarkGroupDeleteRequest request) {
        return pathBookmarkGroupService.deletePathBookmarkGroup(request);
    }

}

