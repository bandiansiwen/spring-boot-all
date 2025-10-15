package com.imp.all.download;

import com.imp.all.download.progress.MultiThreadDownloadProgressPrinter;
import com.imp.all.download.util.Downloader;
import com.imp.all.download.util.MultiThreadFileDownloader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * @author Longlin
 * @date 2023/11/29 20:35
 * @description
 */
@Slf4j
public class DownloadTest {

    public static void main(String[] args) throws IOException {

//        String fileURL = "http://img.doutula.com/production/uploads/image/2017/10/19/20171019627498_uQtkcl.jpg";

        String fileURL = "https://az764295.vo.msecnd.net/stable/1a5daa3a0231a0fbba4f14db7ec463cf99d7768e/VSCodeUserSetup-x64-1.84.2.exe";

        //通过内存下载
//        Downloader downloader = new SimpleDownloader();
//        downloader.download(fileURL, "D:\\MyData\\lill169\\Desktop\\fsdownload");

        //单线程下载
//        Downloader downloader = new FileDownloader();
//        downloader.download(fileURL, "D:\\MyData\\lill169\\Desktop\\fsdownload");

        //多线程下载
        MultiThreadDownloadProgressPrinter downloadProgressPrinter = new MultiThreadDownloadProgressPrinter(5);
        CompletableFuture.runAsync(() -> {
            while (true) {
                long alreadyDownloadLength = downloadProgressPrinter.getAlreadyDownloadLength();
                long contentLength = downloadProgressPrinter.getContentLength();
                log.info(contentLength + "  =>  " + alreadyDownloadLength);
                if (alreadyDownloadLength != 0 && alreadyDownloadLength > contentLength) {
                    break;
                }
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Downloader downloader = new MultiThreadFileDownloader(5, downloadProgressPrinter);
        downloader.download(fileURL, "D:\\MyData\\lill169\\Desktop\\fsdownload");
    }
}
