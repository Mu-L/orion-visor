package com.orion.ops.module.infra.service;

import com.orion.ops.module.infra.entity.request.user.SystemUserUpdateRoleRequest;

import java.util.List;

/**
 * 用户角色关联 服务类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023-7-16 01:19
 */
public interface SystemUserRoleService {

    /**
     * 删除用户角色
     *
     * @param request request
     * @return effect
     */
    Integer deleteUserRoles(SystemUserUpdateRoleRequest request);

    /**
     * 更新用户角色
     *
     * @param request request
     * @return effect
     */
    Integer updateUserRoles(SystemUserUpdateRoleRequest request);

    /**
     * 删除用户缓存中的角色
     *
     * @param roleCode   roleCode
     * @param userIdList userIdList
     */
    void asyncDeleteUserCacheRole(String roleCode, List<Long> userIdList);

}
