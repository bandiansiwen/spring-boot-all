package com.imp.all.download.util;

import com.imp.all.download.ext.FileResponseExtractor;
import com.imp.all.download.progress.DownloadProgressPrinter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.io.File;
import java.io.IOException;

/**
 * @author Longlin
 * @date 2023/11/29 16:14
 * @description
 */
@Slf4j
public class FileDownloader extends AbstractDownloader {

    public FileDownloader(DownloadProgressPrinter downloadProgressPrinter) {
        super(downloadProgressPrinter);
    }

    public FileDownloader() {
        super(DownloadProgressPrinter.defaultDownloadProgressPrinter());
    }

    @Override
    protected void doDownload(String decodeFileURL, String dir, String fileName, HttpHeaders headers) throws IOException {
        String filePath = dir + File.separator + fileName;
        FileResponseExtractor extractor = new FileResponseExtractor(filePath + ".download", downloadProgressPrinter); //创建临时下载文件
        File tmpFile = restTemplate.execute(decodeFileURL, HttpMethod.GET, null, extractor);
        tmpFile.renameTo(new File(filePath)); //修改临时下载文件名称
    }

}
