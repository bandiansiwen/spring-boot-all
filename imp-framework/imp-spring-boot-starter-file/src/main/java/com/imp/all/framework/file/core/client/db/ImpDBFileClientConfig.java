package com.imp.all.framework.file.core.client.db;

import com.imp.all.framework.file.core.client.ImpFileClientConfig;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;

/**
 * @author Longlin
 * @date 2022/11/21 18:10
 * @description
 */
@Data
public class ImpDBFileClientConfig implements ImpFileClientConfig {

    /**
     * 自定义域名
     */
    @NotEmpty(message = "domain 不能为空")
    @URL(message = "domain 必须是 URL 格式")
    private String domain;
}
