package com.imp.all.framework.file.core.client;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReflectUtil;
import com.imp.all.framework.file.core.enums.ImpFileStorageEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Longlin
 * @date 2022/11/21 17:54
 * @description
 */
@Slf4j
public class ImpFileClientFactoryImpl implements ImpFileClientFactory {


    /**
     * 文件客户端 Map
     * key：配置编号
     */
    private final ConcurrentMap<Long, ImpAbstractFileClient<?>> clients = new ConcurrentHashMap<>();

    @Override
    public ImpFileClient getFileClient(Long configId) {
        ImpAbstractFileClient<?> client = clients.get(configId);
        if (client == null) {
            log.error("[getFileClient][配置编号({}) 找不到客户端]", configId);
        }
        return client;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <Config extends ImpFileClientConfig> void createOrUpdateFileClient(Long configId, Integer storage, Config config) {
        ImpAbstractFileClient<Config> client = (ImpAbstractFileClient<Config>) clients.get(configId);
        if (client == null) {
            client = this.createFileClient(configId, storage, config);
            client.init();
            clients.put(client.getId(), client);
        } else {
            client.refresh(config);
        }
    }

    @SuppressWarnings("unchecked")
    private <Config extends ImpFileClientConfig> ImpAbstractFileClient<Config> createFileClient(
            Long configId, Integer storage, Config config) {
        ImpFileStorageEnum storageEnum = ImpFileStorageEnum.getByStorage(storage);
        Assert.notNull(storageEnum, String.format("文件配置(%s) 为空", storageEnum));
        // 创建客户端
        return (ImpAbstractFileClient<Config>) ReflectUtil.newInstance(storageEnum.getClientClass(), configId, config);
    }

}
