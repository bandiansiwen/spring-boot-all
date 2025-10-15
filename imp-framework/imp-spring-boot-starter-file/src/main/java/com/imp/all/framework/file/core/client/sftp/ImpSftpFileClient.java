package com.imp.all.framework.file.core.client.sftp;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.ssh.Sftp;
import com.imp.all.framework.common.utils.io.ImpFileUtils;
import com.imp.all.framework.file.core.client.ImpAbstractFileClient;

import java.io.File;

/**
 * @author Longlin
 * @date 2022/11/21 16:52
 * @description
 */
public class ImpSftpFileClient extends ImpAbstractFileClient<ImpSftpFileClientConfig> {

    private Sftp sftp;

    public ImpSftpFileClient(Long id, ImpSftpFileClientConfig config) {
        super(id, config);
    }

    @Override
    protected void doInit() {
        // 补全风格。例如说 Linux 是 /，Windows 是 \
        if (!config.getBasePath().endsWith(File.separator)) {
            config.setBasePath(config.getBasePath() + File.separator);
        }
        // 初始化 Ftp 对象
        this.sftp = new Sftp(config.getHost(), config.getPort(), config.getUsername(), config.getPassword());
    }

    @Override
    public String upload(byte[] content, String path) {
        // 执行写入
        String filePath = getFilePath(path);
        File file = ImpFileUtils.createTempFile(content);
        sftp.upload(filePath, file);
        // 拼接返回路径
        return super.formatFileUrl(config.getDomain(), path);
    }

    @Override
    public void delete(String path) {
        String filePath = getFilePath(path);
        sftp.delFile(filePath);
    }

    @Override
    public byte[] getContent(String path) {
        String filePath = getFilePath(path);
        File destFile = ImpFileUtils.createTempFile();
        sftp.download(filePath, destFile);
        return FileUtil.readBytes(destFile);
    }

    private String getFilePath(String path) {
        return config.getBasePath() + path;
    }
}
