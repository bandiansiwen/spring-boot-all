package com.imp.all.download.ext;

import com.imp.all.download.progress.DownloadProgressPrinter;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Longlin
 * @date 2023/11/29 15:26
 * @description
 */
public class ByteArrayResponseExtractor extends AbstractDownloadProgressMonitorResponseExtractor<byte[]> {

    private final AtomicLong byteCount = new AtomicLong(0); //保存已经下载的字节数

    public ByteArrayResponseExtractor() {
        super();
    }

    public ByteArrayResponseExtractor(DownloadProgressPrinter downloadProgressPrinter) {
        super(downloadProgressPrinter);
    }

    @Override
    protected byte[] doExtractData(ClientHttpResponse response) throws IOException {

        long contentLength = response.getHeaders().getContentLength();
        ByteArrayOutputStream out = new ByteArrayOutputStream(contentLength >= 0 ? (int) contentLength : StreamUtils.BUFFER_SIZE);
        InputStream in = response.getBody();
        int bytesRead;
        byte[] buffer = new byte[StreamUtils.BUFFER_SIZE];
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
            byteCount.addAndGet(bytesRead);
        }
        in.close();
        out.flush();
        byte[] bytes = out.toByteArray();
        out.close();
        return bytes;
    }

    //返回已经下载的字节数
    @Override
    public long getAlreadyDownloadLength() {
        return byteCount.get();
    }
}
