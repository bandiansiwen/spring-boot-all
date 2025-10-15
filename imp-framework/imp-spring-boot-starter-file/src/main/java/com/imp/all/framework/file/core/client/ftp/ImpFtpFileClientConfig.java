package com.imp.all.framework.file.core.client.ftp;

import com.imp.all.framework.file.core.client.ImpFileClientConfig;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Longlin
 * @date 2022/11/23 14:31
 * @description
 */
@Data
public class ImpFtpFileClientConfig implements ImpFileClientConfig {

    /**
     * 基础路径
     */
    @NotEmpty(message = "基础路径不能为空")
    private String basePath;

    /**
     * 自定义域名
     */
    @NotEmpty(message = "domain 不能为空")
    @URL(message = "domain 必须是 URL 格式")
    private String domain;

    /**
     * 主机地址
     */
    @NotEmpty(message = "host 不能为空")
    private String host;
    /**
     * 主机端口
     */
    @NotNull(message = "port 不能为空")
    private Integer port;
    /**
     * 用户名
     */
    @NotEmpty(message = "用户名不能为空")
    private String username;
    /**
     * 密码
     */
    @NotEmpty(message = "密码不能为空")
    private String password;
    /**
     * 连接模式
     * 使用 {@link  cn.hutool.extra.ftp.FtpMode} 对应的字符串
     */
    @NotEmpty(message = "连接模式不能为空")
    private String mode;
}
