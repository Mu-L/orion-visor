package com.orion.visor.module.asset.entity.dto;

import com.orion.visor.framework.desensitize.core.annotation.Desensitize;
import com.orion.visor.framework.desensitize.core.annotation.DesensitizeObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 主机终端连接参数
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/26 15:47
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DesensitizeObject
@Schema(name = "HostTerminalConnectDTO", description = "主机终端连接参数")
public class HostTerminalConnectDTO {

    @Schema(description = "logId")
    private Long logId;

    @Schema(description = "连接类型")
    private String connectType;

    @Schema(description = "hostId")
    private Long hostId;

    @Schema(description = "hostName")
    private String hostName;

    @Schema(description = "主机地址")
    private String hostAddress;

    @Schema(description = "主机端口")
    private Integer hostPort;

    @Schema(description = "系统类型")
    private String osType;

    @Schema(description = "超时时间")
    private Integer timeout;

    @Schema(description = "SSH输出编码")
    private String charset;

    @Schema(description = "文件名称编码")
    private String fileNameCharset;

    @Schema(description = "文件内容编码")
    private String fileContentCharset;

    @Schema(description = "用户名")
    private String username;

    @Desensitize(toEmpty = true)
    @Schema(description = "密码")
    private String password;

    @Schema(description = "密钥id")
    private Long keyId;

    @Desensitize(toEmpty = true)
    @Schema(description = "公钥文本")
    private String publicKey;

    @Desensitize(toEmpty = true)
    @Schema(description = "私钥文本")
    private String privateKey;

    @Desensitize(toEmpty = true)
    @Schema(description = "私钥密码")
    private String privateKeyPassword;

}
