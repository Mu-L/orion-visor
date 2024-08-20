package com.orion.visor.module.infra.api.impl;

import com.orion.visor.framework.common.constant.ErrorMessage;
import com.orion.visor.framework.common.utils.Valid;
import com.orion.visor.module.infra.api.AuthenticationApi;
import com.orion.visor.module.infra.entity.domain.SystemUserDO;
import com.orion.visor.module.infra.entity.dto.user.SystemUserAuthDTO;
import com.orion.visor.module.infra.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 认证服务实现
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/8/14 21:37
 */
@Slf4j
@Service
public class AuthenticationApiImpl implements AuthenticationApi {

    @Resource
    private AuthenticationService authenticationService;

    @Override
    public SystemUserAuthDTO authByPassword(String username, String password, boolean addFailedCount) {
        SystemUserAuthDTO result = new SystemUserAuthDTO();
        try {
            // 登录预检
            SystemUserDO user = authenticationService.preCheckLogin(username, password);
            result.setId(user.getId());
            result.setUsername(user.getUsername());
            result.setNickname(user.getNickname());
            // 检查用户密码
            boolean passRight = authenticationService.checkUserPassword(user, password, addFailedCount);
            result.setPassRight(passRight);
            Valid.isTrue(passRight, ErrorMessage.USERNAME_PASSWORD_ERROR);
            // 检查用户状态
            authenticationService.checkUserStatus(user);
            result.setAuthed(true);
        } catch (Exception e) {
            result.setAuthed(false);
            result.setErrorMessage(e.getMessage());
        }
        return result;
    }

}
