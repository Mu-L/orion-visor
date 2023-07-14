package com.orion.ops.framework.security.core.utils;

import com.orion.lang.constant.StandardHttpHeader;
import com.orion.ops.framework.common.security.LoginUser;
import com.orion.ops.framework.common.utils.Kits;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

/**
 * 安全工具类
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/7/7 11:13
 */
public class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * 获取 token
     *
     * @param request request
     * @return token
     */
    public static String obtainAuthorization(HttpServletRequest request) {
        return Kits.getAuthorization(request.getHeader(StandardHttpHeader.AUTHORIZATION));
    }

    /**
     * 获得当前认证信息
     *
     * @return 认证信息
     */
    public static Authentication getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return null;
        }
        return context.getAuthentication();
    }

    /**
     * 获取当前用户
     *
     * @return 当前用户
     */
    public static LoginUser getLoginUser() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        return authentication.getPrincipal() instanceof LoginUser ? (LoginUser) authentication.getPrincipal() : null;
    }

    /**
     * 获取当前用户id
     *
     * @return id
     */
    public static Long getLoginUserId() {
        LoginUser loginUser = getLoginUser();
        return loginUser != null ? loginUser.getId() : null;
    }

    /**
     * 设置当前用户
     *
     * @param loginUser 登录用户
     * @param request   请求
     */
    public static void setLoginUser(LoginUser loginUser, HttpServletRequest request) {
        // 创建 authentication
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginUser, null, Collections.emptyList());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        // 设置上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}