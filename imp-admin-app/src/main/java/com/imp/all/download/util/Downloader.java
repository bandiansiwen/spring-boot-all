package com.imp.all.download.util;

import java.io.IOException;

/**
 * @author Longlin
 * @date 2023/11/29 16:18
 * @description
 */
public interface Downloader {

    void download(String fileURL, String dir) throws IOException;

}
