package com.imp.all.file.utils;

import com.imp.all.file.core.FileSDK;
import com.imp.all.file.type.ThumbnailLevel;

import java.io.File;

public class FileKeyHelper {

    /**
     * 短连接可用
     *
     * @param filePath
     * @param md5
     * @return
     */
    public static String getFileKey(String filePath, String md5) {
        File file = new File(filePath);
        return "/chat/a/" + FileSDK.getUsername() + "/" + System.currentTimeMillis()
                + "/" + McFileMD5.getStrMd5(file.getName()) + "/" + md5;
    }

    public static String getFileKey(String filePath, long timestamp, String md5) {
        File file = new File(filePath);
        return "/chat/a/" + FileSDK.getUsername() + "/" + timestamp
                + "/" + McFileMD5.getStrMd5(file.getName()) + "/" + md5;
    }

    public static String getFileKey(String prefix, long timestamp, String fileNameMd5, String md5) {
        return String.format("%s/a/%s/%s/%s/%s", prefix, FileSDK.getUsername(), timestamp, fileNameMd5, md5);
    }

    public static String getThumFileKey(String orgFileKey, int level) {
        if (level == ThumbnailLevel.SRC) {
            return orgFileKey;
        }
        String tail = "#tn" + level;
        if (orgFileKey.endsWith(tail)) {
            return orgFileKey;
        }
        return orgFileKey + tail;
    }
}
