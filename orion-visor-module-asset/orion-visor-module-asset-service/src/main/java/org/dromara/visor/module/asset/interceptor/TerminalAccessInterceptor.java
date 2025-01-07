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
package org.dromara.visor.module.asset.interceptor;

import cn.orionsec.kit.lang.utils.Urls;
import lombok.extern.slf4j.Slf4j;
import org.dromara.visor.framework.common.constant.ExtraFieldConst;
import org.dromara.visor.framework.common.meta.TraceIdHolder;
import org.dromara.visor.framework.common.utils.Requests;
import org.dromara.visor.module.asset.entity.dto.TerminalAccessDTO;
import org.dromara.visor.module.asset.service.TerminalService;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 终端访问拦截器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/27 23:53
 */
@Slf4j
@Component
public class TerminalAccessInterceptor implements HandshakeInterceptor {

    @Resource
    private TerminalService terminalService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // 获取 accessToken
        String accessToken = Urls.getUrlSource(request.getURI().getPath());
        log.info("TerminalAccessInterceptor-beforeHandshake start accessToken: {}", accessToken);
        // 获取连接数据
        TerminalAccessDTO access = terminalService.getAccessInfoByToken(accessToken);
        if (access == null) {
            log.error("TerminalAccessInterceptor-beforeHandshake absent accessToken: {}", accessToken);
            return false;
        }
        // 设置参数
        attributes.put(ExtraFieldConst.USER_ID, access.getUserId());
        attributes.put(ExtraFieldConst.USERNAME, access.getUsername());
        attributes.put(ExtraFieldConst.TRACE_ID, TraceIdHolder.get());
        attributes.put(ExtraFieldConst.IDENTITY, Requests.getIdentity());
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }

}