package com.imp.all.framework.file.core.client;

import com.imp.all.framework.file.core.enums.ImpFileStorageEnum;

/**
 * @author Longlin
 * @date 2022/11/21 17:53
 * @description
 */
public interface ImpFileClientFactory {

    /**
     * 获得文件客户端
     *
     * @param configId 配置编号
     * @return 文件客户端
     */
    ImpFileClient getFileClient(Long configId);

    /**
     * 创建文件客户端
     *
     * @param configId 配置编号
     * @param storage 存储器的枚举 {@link ImpFileStorageEnum}
     * @param config 文件配置
     */
    <Config extends ImpFileClientConfig> void createOrUpdateFileClient(Long configId, Integer storage, Config config);
}
