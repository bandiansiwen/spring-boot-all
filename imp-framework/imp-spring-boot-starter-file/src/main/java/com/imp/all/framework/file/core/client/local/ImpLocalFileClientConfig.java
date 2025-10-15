package com.imp.all.framework.file.core.client.local;

import com.imp.all.framework.file.core.client.ImpFileClientConfig;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;

/**
 * @author Longlin
 * @date 2022/11/23 14:41
 * @description
 */
@Data
public class ImpLocalFileClientConfig implements ImpFileClientConfig {

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
}
