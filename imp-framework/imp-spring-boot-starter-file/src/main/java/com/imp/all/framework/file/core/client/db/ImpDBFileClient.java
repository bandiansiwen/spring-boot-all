package com.imp.all.framework.file.core.client.db;

import cn.hutool.extra.spring.SpringUtil;
import com.imp.all.framework.file.core.client.ImpAbstractFileClient;

/**
 * @author Longlin
 * @date 2022/11/21 16:32
 * @description
 */
public class ImpDBFileClient extends ImpAbstractFileClient<ImpDBFileClientConfig> {

    private ImpDBFileContentFrameworkDao dao;

    public ImpDBFileClient(Long id, ImpDBFileClientConfig config) {
        super(id, config);
    }

    @Override
    protected void doInit() {

    }

    @Override
    public String upload(byte[] content, String path) {
        getDao().insert(getId(), path, content);
        return super.formatFileUrl(config.getDomain(), path);
    }

    @Override
    public void delete(String path) throws Exception {
        getDao().delete(getId(), path);
    }

    @Override
    public byte[] getContent(String path) throws Exception {
        return getDao().selectContent(getId(), path);
    }

    private ImpDBFileContentFrameworkDao getDao() {
        // 延迟获取，因为 SpringUtil 初始化太慢
        if (dao == null) {
            dao = SpringUtil.getBean(ImpDBFileContentFrameworkDao.class);
        }
        return dao;
    }
}
