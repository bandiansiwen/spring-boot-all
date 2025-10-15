package com.imp.all.download.ext;

import com.imp.all.download.progress.DownloadProgressPrinter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Longlin
 * @date 2023/11/29 19:45
 * @description
 */
@Slf4j
public class FileResponseExtractor extends AbstractDownloadProgressMonitorResponseExtractor<File> {

    private final AtomicLong byteCount = new AtomicLong(0);  //已下载的字节数
    private final String filePath; //文件的路径
    private final int index;  //多任务下载时用

    public FileResponseExtractor(int index, String filePath, DownloadProgressPrinter downloadProgressPrinter) {
        super(downloadProgressPrinter);
        this.index = index;
        this.filePath = filePath;
    }

    public FileResponseExtractor(String filePath, DownloadProgressPrinter downloadProgressPrinter) {
        this(0, filePath, downloadProgressPrinter);
    }

    @Override
    protected File doExtractData(ClientHttpResponse response) throws IOException {
        InputStream in = response.getBody();
        File file = new File(filePath);
        FileOutputStream out = new FileOutputStream(file);
        int bytesRead;
        byte[] buffer = new byte[StreamUtils.BUFFER_SIZE];
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
            byteCount.addAndGet(bytesRead);
        }
        out.flush();
        out.close();
        return file;
    }

    @Override
    public long getAlreadyDownloadLength() {
        return byteCount.get();
    }

    public int getIndex() {
        return index;
    }

    @Override
    protected String getTask() {
        return String.valueOf(this.getIndex());
    }
}
