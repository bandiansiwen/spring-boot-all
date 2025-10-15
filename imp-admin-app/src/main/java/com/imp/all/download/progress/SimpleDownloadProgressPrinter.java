package com.imp.all.download.progress;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Longlin
 * @date 2023/11/29 17:17
 * @description
 */
@Slf4j
public class SimpleDownloadProgressPrinter implements DownloadProgressPrinter {

    private long contentLength;
    private long alreadyDownloadLength;

    @Override
    public void print(String task, long contentLength, long alreadyDownloadLength, long speed) {
        this.contentLength = contentLength;
        this.alreadyDownloadLength = alreadyDownloadLength;
        log.info("{} 文件总大小：{} KB，已下载：{} KB，下载速度：{} KB", task, contentLength/1000.0, alreadyDownloadLength/1000.0, speed/1000.0);
    }

    @Override
    public long getContentLength() {
        return contentLength;
    }

    @Override
    public long getAlreadyDownloadLength() {
        return alreadyDownloadLength;
    }
}
