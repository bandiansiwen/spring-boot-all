package com.imp.all.fileClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * @author Longlin
 * 解决报错
 * Unable to process parts as no multi-part configuration has been provided
 */
@Configuration
public class UploadConfig {
	@Bean(name="multipartResolver")
	public MultipartResolver multipartResolver(){
		return new CommonsMultipartResolver();
	}
}