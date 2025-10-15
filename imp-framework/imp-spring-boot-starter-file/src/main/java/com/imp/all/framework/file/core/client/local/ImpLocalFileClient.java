package com.imp.all.framework.file.core.client.local;

import cn.hutool.core.io.FileUtil;
import com.imp.all.framework.file.core.client.ImpAbstractFileClient;

import java.io.File;

/**
 * @author Longlin
 * @date 2022/11/21 16:51
 * @description
 */
public class ImpLocalFileClient extends ImpAbstractFileClient<ImpLocalFileClientConfig> {

    public ImpLocalFileClient(Long id, ImpLocalFileClientConfig config) {
        super(id, config);
    }

    @Override
    protected void doInit() {
        // 补全风格。例如说 Linux 是 /，Windows 是 \
        if (!config.getBasePath().endsWith(File.separator)) {
            config.setBasePath(config.getBasePath() + File.separator);
        }
    }

    @Override
    public String upload(byte[] content, String path) {
        // 执行写入
        String filePath = getFilePath(path);
        FileUtil.writeBytes(content, filePath);
        // 拼接返回路径
        return super.formatFileUrl(config.getDomain(), path);
    }

    @Override
    public void delete(String path) {
        String filePath = getFilePath(path);
        FileUtil.del(filePath);
    }

    @Override
    public byte[] getContent(String path) {
        String filePath = getFilePath(path);
        return FileUtil.readBytes(filePath);
    }
    private String getFilePath(String path) {
        return config.getBasePath() + path;
    }

}
