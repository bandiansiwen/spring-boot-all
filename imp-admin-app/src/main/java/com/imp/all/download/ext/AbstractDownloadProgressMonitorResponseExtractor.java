package com.imp.all.download.ext;

import com.imp.all.download.progress.DownloadProgressPrinter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseExtractor;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * @author Longlin
 * @date 2023/11/29 15:15
 * @description
 */
@Slf4j
public abstract class AbstractDownloadProgressMonitorResponseExtractor<T> implements ResponseExtractor<T>, DownloadProgressMonitor {

    protected DownloadProgressPrinter downloadProgressPrinter;

    public AbstractDownloadProgressMonitorResponseExtractor(DownloadProgressPrinter downloadProgressPrinter) {
        this.downloadProgressPrinter = downloadProgressPrinter;
    }

    public AbstractDownloadProgressMonitorResponseExtractor() {
        this.downloadProgressPrinter = DownloadProgressPrinter.defaultDownloadProgressPrinter();
    }

    @Override
    public T extractData(ClientHttpResponse response) throws IOException {

        long contentLength = response.getHeaders().getContentLength();
        this.calculateDownloadProgress(contentLength);

        return this.doExtractData(response);
    }

    protected abstract T doExtractData(ClientHttpResponse response) throws IOException;

    @Override
    public void calculateDownloadProgress(long contentLength) {

        String task = getTask();

        CompletableFuture.runAsync(() -> {
            long tmp = 0, speed;
            while (contentLength - tmp > 0) {
                speed = getAlreadyDownloadLength() - tmp;
                tmp = getAlreadyDownloadLength();
                downloadProgressPrinter.print(task, contentLength, tmp, speed);
                sleep();
            }
        }).exceptionally(e -> {
            e.printStackTrace();
            return null;
        });
    }

    protected String getTask() {
        return Thread.currentThread().getName();
    }
    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
