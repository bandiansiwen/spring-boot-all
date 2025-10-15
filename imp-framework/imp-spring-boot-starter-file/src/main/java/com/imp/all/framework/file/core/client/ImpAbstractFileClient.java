package com.imp.all.framework.file.core.client;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Longlin
 * @date 2022/11/21 17:55
 * @description
 */
@Slf4j
public abstract class ImpAbstractFileClient<Config extends ImpFileClientConfig> implements ImpFileClient {

    /**
     * 配置编号
     */
    private final Long id;
    /**
     * 文件配置
     */
    protected Config config;

    public ImpAbstractFileClient(Long id, Config config) {
        this.id = id;
        this.config = config;
    }

    /**
     * 初始化
     */
    public final void init() {
        doInit();
        log.info("[init][配置({}) 初始化完成]", config);
    }

    protected abstract void doInit();

    public final void refresh(Config config) {
        // 判断是否更新
        if (config.equals(this.config)) {
            return;
        }
        log.info("[refresh][配置({})发生变化，重新初始化]", config);
        this.config = config;
        // 初始化
        this.init();
    }

    @Override
    public Long getId() {
        return id;
    }

    protected String formatFileUrl(String domain, String path) {
        return StrUtil.format("{}/admin-api/imp/file/{}/get/{}", domain, getId(), path);
    }
}
