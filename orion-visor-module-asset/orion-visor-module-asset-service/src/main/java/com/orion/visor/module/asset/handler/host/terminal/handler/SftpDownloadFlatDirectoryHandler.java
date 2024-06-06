package com.orion.visor.module.asset.handler.host.terminal.handler;

import com.alibaba.fastjson.JSON;
import com.orion.lang.utils.collect.Lists;
import com.orion.visor.framework.common.enums.BooleanBit;
import com.orion.visor.module.asset.handler.host.terminal.enums.OutputTypeEnum;
import com.orion.visor.module.asset.handler.host.terminal.model.request.SftpDownloadFlatDirectoryRequest;
import com.orion.visor.module.asset.handler.host.terminal.model.response.SftpDownloadFlatDirectoryResponse;
import com.orion.visor.module.asset.handler.host.terminal.model.response.SftpFileVO;
import com.orion.visor.module.asset.handler.host.terminal.session.ISftpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Arrays;
import java.util.List;

/**
 * sftp 下载文件夹展开文件
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/2/19 11:13
 */
@Slf4j
@Component
public class SftpDownloadFlatDirectoryHandler extends AbstractTerminalHandler<SftpDownloadFlatDirectoryRequest> {

    @Override
    public void handle(WebSocketSession channel, SftpDownloadFlatDirectoryRequest payload) {
        // 获取会话
        String sessionId = payload.getSessionId();
        ISftpSession session = hostTerminalManager.getSession(channel.getId(), sessionId);
        String[] paths = payload.getPath().split("\\|");
        log.info("SftpDownloadFlatDirectoryHandler-handle start sessionId: {}, paths: {}", sessionId, Arrays.toString(paths));
        Exception ex = null;
        List<SftpFileVO> files = Lists.empty();
        // 展开文件夹内的全部文件
        try {
            files = session.flatDirectory(paths);
            log.info("SftpDownloadFlatDirectoryHandler-handle success sessionId: {}, paths: {}", sessionId, Arrays.toString(paths));
        } catch (Exception e) {
            log.error("SftpDownloadFlatDirectoryHandler-handle error sessionId: {}", sessionId, e);
            ex = e;
        }
        // 返回
        this.send(channel,
                OutputTypeEnum.SFTP_DOWNLOAD_FLAT_DIRECTORY,
                SftpDownloadFlatDirectoryResponse.builder()
                        .sessionId(sessionId)
                        .currentPath(payload.getCurrentPath())
                        .body(JSON.toJSONString(files))
                        .result(BooleanBit.of(ex == null).getValue())
                        .msg(this.getErrorMessage(ex))
                        .build());
    }

}
