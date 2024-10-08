package com.orion.visor.module.infra.service;

import com.orion.visor.module.infra.entity.domain.SystemRoleDO;
import com.orion.visor.module.infra.entity.dto.SystemMenuCacheDTO;
import com.orion.visor.module.infra.entity.vo.SystemMenuVO;
import com.orion.visor.module.infra.entity.vo.UserPermissionVO;

import java.util.List;
import java.util.Map;

/**
 * 用户权限服务
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/16 1:03
 */
public interface UserPermissionService {

    /**
     * 获取 角色缓存
     *
     * @return cache
     */
    Map<Long, SystemRoleDO> getRoleCache();

    /**
     * 获取 菜单缓存 以作角色权限直接引用
     *
     * @return cache
     */
    List<SystemMenuCacheDTO> getMenuCache();

    /**
     * 获取 角色菜单关联
     *
     * @return cache
     */
    Map<Long, List<SystemMenuCacheDTO>> getRoleMenuCache();

    /**
     * 初始化权限缓存
     */
    void initPermissionCache();

    /**
     * 检查当前用户是否含有此角色 (有效性判断)
     *
     * @param role role
     * @return 是否包含
     */
    boolean hasRole(String role);

    /**
     * 检查当前用户是否含有任意角色 (有效性判断)
     *
     * @param roles roles
     * @return 是否包含
     */
    boolean hasAnyRole(String... roles);

    /**
     * 检查当前用户是否含有此权限 (有效性判断)
     *
     * @param permission permission
     * @return 是否包含
     */
    boolean hasPermission(String permission);

    /**
     * 检查当前用户是否含任意权限 (有效性判断)
     *
     * @param permissions permissions
     * @return 是否包含
     */
    boolean hasAnyPermission(String... permissions);

    /**
     * 获取用户菜单
     *
     * @return 菜单
     */
    List<SystemMenuVO> getUserMenuList();

    /**
     * 获取用户权限
     *
     * @return 权限信息
     */
    UserPermissionVO getUserPermission();

}
