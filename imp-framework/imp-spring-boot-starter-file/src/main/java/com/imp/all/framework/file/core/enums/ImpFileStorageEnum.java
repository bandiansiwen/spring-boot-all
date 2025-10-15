package com.imp.all.framework.file.core.enums;

import cn.hutool.core.util.ArrayUtil;
import com.imp.all.framework.file.core.client.ImpFileClient;
import com.imp.all.framework.file.core.client.ImpFileClientConfig;
import com.imp.all.framework.file.core.client.db.ImpDBFileClient;
import com.imp.all.framework.file.core.client.db.ImpDBFileClientConfig;
import com.imp.all.framework.file.core.client.ftp.ImpFtpFileClient;
import com.imp.all.framework.file.core.client.ftp.ImpFtpFileClientConfig;
import com.imp.all.framework.file.core.client.local.ImpLocalFileClient;
import com.imp.all.framework.file.core.client.local.ImpLocalFileClientConfig;
import com.imp.all.framework.file.core.client.s3.ImpS3FileClient;
import com.imp.all.framework.file.core.client.s3.ImpS3FileClientConfig;
import com.imp.all.framework.file.core.client.sftp.ImpSftpFileClient;
import com.imp.all.framework.file.core.client.sftp.ImpSftpFileClientConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Longlin
 * @date 2022/11/21 16:31
 * @description
 */
@AllArgsConstructor
@Getter
public enum ImpFileStorageEnum {

    DB(1, ImpDBFileClientConfig.class, ImpDBFileClient.class),

    LOCAL(10, ImpLocalFileClientConfig.class, ImpLocalFileClient.class),
    FTP(11, ImpFtpFileClientConfig.class, ImpFtpFileClient.class),
    SFTP(12, ImpSftpFileClientConfig.class, ImpSftpFileClient.class),

    S3(20, ImpS3FileClientConfig.class, ImpS3FileClient.class),
            ;

    /**
     * 存储器
     */
    private final Integer storage;

    /**
     * 配置类
     */
    private final Class<? extends ImpFileClientConfig> configClass;
    /**
     * 客户端类
     */
    private final Class<? extends ImpFileClient> clientClass;

    public static ImpFileStorageEnum getByStorage(Integer storage) {
        return ArrayUtil.firstMatch(o -> o.getStorage().equals(storage), values());
    }
}
