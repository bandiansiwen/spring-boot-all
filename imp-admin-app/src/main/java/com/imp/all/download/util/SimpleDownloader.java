package com.imp.all.download.util;

import com.imp.all.download.ext.ByteArrayResponseExtractor;
import com.imp.all.download.progress.DownloadProgressPrinter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * @author Longlin
 * @date 2023/11/29 20:11
 * @description
 */
public class SimpleDownloader extends AbstractDownloader {

    public SimpleDownloader(DownloadProgressPrinter downloadProgressPrinter) {
        super(downloadProgressPrinter);
    }

    public SimpleDownloader() {
        super(DownloadProgressPrinter.defaultDownloadProgressPrinter());
    }
    @Override
    protected void doDownload(String decodeFileURL, String dir, String fileName, HttpHeaders headers) throws IOException {

        String filePath = dir + File.separator + fileName;
        byte[] body = restTemplate.execute(decodeFileURL, HttpMethod.GET, null,
                new ByteArrayResponseExtractor(downloadProgressPrinter));
        Files.write(Paths.get(filePath), Objects.requireNonNull(body));

    }
}
