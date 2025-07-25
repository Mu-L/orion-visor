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
import cn.orionsec.kit.lang.utils.collect.Lists;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.common.validator.group.Page;
import org.dromara.visor.framework.biz.operator.log.core.annotation.OperatorLog;
import org.dromara.visor.framework.log.core.annotation.IgnoreLog;
import org.dromara.visor.framework.log.core.enums.IgnoreLogMode;
import org.dromara.visor.framework.web.core.annotation.DemoDisableApi;
import org.dromara.visor.framework.web.core.annotation.RestWrapper;
import org.dromara.visor.module.infra.define.operator.SystemUserOperatorType;
import org.dromara.visor.module.infra.entity.request.user.*;
import org.dromara.visor.module.infra.entity.vo.LoginHistoryVO;
import org.dromara.visor.module.infra.entity.vo.SystemUserVO;
import org.dromara.visor.module.infra.entity.vo.UserSessionVO;
import org.dromara.visor.module.infra.service.OperatorLogService;
import org.dromara.visor.module.infra.service.SystemUserManagementService;
import org.dromara.visor.module.infra.service.SystemUserRoleService;
import org.dromara.visor.module.infra.service.SystemUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户 api
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-13 18:42
 */
@Tag(name = "infra - 用户服务")
@Slf4j
@Validated
@RestWrapper
@RestController
@RequestMapping("/infra/system-user")
public class SystemUserController {

    @Resource
    private SystemUserService systemUserService;

    @Resource
    private SystemUserRoleService systemUserRoleService;

    @Resource
    private SystemUserManagementService systemUserManagementService;

    @Resource
    private OperatorLogService operatorLogService;

    @DemoDisableApi
    @OperatorLog(SystemUserOperatorType.CREATE)
    @PostMapping("/create")
    @Operation(summary = "创建用户")
    @PreAuthorize("@ss.hasPermission('infra:system-user:create')")
    public Long createSystemUser(@Validated @RequestBody SystemUserCreateRequest request) {
        return systemUserService.createSystemUser(request);
    }

    @DemoDisableApi
    @OperatorLog(SystemUserOperatorType.UPDATE)
    @PutMapping("/update")
    @Operation(summary = "通过 id 更新用户")
    @PreAuthorize("@ss.hasPermission('infra:system-user:update')")
    public Integer updateSystemUser(@Validated @RequestBody SystemUserUpdateRequest request) {
        return systemUserService.updateSystemUserById(request);
    }

    @DemoDisableApi
    @OperatorLog(SystemUserOperatorType.UPDATE_STATUS)
    @PutMapping("/update-status")
    @Operation(summary = "修改用户状态")
    @PreAuthorize("@ss.hasPermission('infra:system-user:update-status')")
    public Integer updateUserStatus(@Validated @RequestBody SystemUserUpdateStatusRequest request) {
        return systemUserService.updateUserStatus(request);
    }

    @DemoDisableApi
    @OperatorLog(SystemUserOperatorType.GRANT_ROLE)
    @PutMapping("/grant-role")
    @Operation(summary = "分配用户角色")
    @PreAuthorize("@ss.hasPermission('infra:system-user:grant-role')")
    public Integer updateUserRole(@Validated @RequestBody SystemUserUpdateRoleRequest request) {
        if (Lists.isEmpty(request.getRoleIdList())) {
            // 删除用户角色
            return systemUserRoleService.deleteUserRoles(request);
        } else {
            // 更新用户角色
            return systemUserRoleService.updateUserRoles(request);
        }
    }

    @DemoDisableApi
    @OperatorLog(SystemUserOperatorType.RESET_PASSWORD)
    @PutMapping("/reset-password")
    @Operation(summary = "重置用户密码")
    @PreAuthorize("@ss.hasPermission('infra:system-user:management:reset-password')")
    public Boolean resetUserPassword(@Validated @RequestBody UserResetPasswordRequest request) {
        systemUserService.resetPassword(request);
        return true;
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/get")
    @Operation(summary = "通过 id 查询用户")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('infra:system-user:query')")
    public SystemUserVO getSystemUser(@RequestParam("id") Long id) {
        return systemUserService.getSystemUserById(id);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/list")
    @Operation(summary = "查询所有用户")
    @PreAuthorize("@ss.hasPermission('infra:system-user:query')")
    public List<SystemUserVO> getSystemUserList() {
        return systemUserService.getSystemUserList();
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/get-roles")
    @Operation(summary = "查询用户的角色id")
    @PreAuthorize("@ss.hasPermission('infra:system-user:query')")
    public List<Long> getUserRoleIdList(@RequestParam("userId") Long userId) {
        return systemUserRoleService.getRoleIdListByUserId(userId);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @PostMapping("/query")
    @Operation(summary = "分页查询用户")
    @PreAuthorize("@ss.hasPermission('infra:system-user:query')")
    public DataGrid<SystemUserVO> getSystemUserPage(@Validated(Page.class) @RequestBody SystemUserQueryRequest request) {
        return systemUserService.getSystemUserPage(request);
    }

    @PostMapping("/count")
    @Operation(summary = "查询系统用户数量")
    @PreAuthorize("@ss.hasPermission('infra:system-user:query')")
    public Long getSystemUserCount(@Validated @RequestBody SystemUserQueryRequest request) {
        return systemUserService.getSystemUserCount(request);
    }

    @DemoDisableApi
    @OperatorLog(SystemUserOperatorType.DELETE)
    @DeleteMapping("/delete")
    @Operation(summary = "通过 id 删除用户")
    @Parameter(name = "id", description = "id", required = true)
    @PreAuthorize("@ss.hasPermission('infra:system-user:delete')")
    public Integer deleteSystemUser(@RequestParam("id") Long id) {
        return systemUserService.deleteSystemUserById(id);
    }

    @DemoDisableApi
    @OperatorLog(SystemUserOperatorType.DELETE)
    @DeleteMapping("/batch-delete")
    @Operation(summary = "批量删除用户")
    @Parameter(name = "idList", description = "idList", required = true)
    @PreAuthorize("@ss.hasPermission('infra:system-user:delete')")
    public Integer batchDeleteSystemUser(@RequestParam("idList") List<Long> idList) {
        return systemUserService.deleteSystemUserByIdList(idList);
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/session/list")
    @Operation(summary = "获取用户会话列表")
    @PreAuthorize("@ss.hasPermission('infra:system-user:query-session')")
    public List<UserSessionVO> getUserSessionList(@RequestParam("id") Long id) {
        return systemUserManagementService.getUserSessionList(id);
    }

    @DemoDisableApi
    @OperatorLog(SystemUserOperatorType.OFFLINE)
    @PutMapping("/session/offline")
    @Operation(summary = "下线用户会话")
    @PreAuthorize("@ss.hasPermission('infra:system-user:management:offline-session')")
    public Boolean offlineUserSession(@Validated @RequestBody UserSessionOfflineRequest request) {
        systemUserManagementService.offlineUserSession(request);
        return true;
    }

    @IgnoreLog(IgnoreLogMode.RET)
    @GetMapping("/login-history")
    @Operation(summary = "查询用户登录日志")
    @PreAuthorize("@ss.hasPermission('infra:system-user:login-history')")
    public List<LoginHistoryVO> getLoginHistory(@RequestParam("username") String username,
                                                @RequestParam("count") Integer count) {
        return operatorLogService.getLoginHistory(username, count);
    }

}
