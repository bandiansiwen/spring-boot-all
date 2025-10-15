package com.imp.all.file.utils;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * 仅限于文件
 */
public class McFileMD5 {

    public final static boolean toUpperCase = true;

    public static String fileMd5(String filePath) {
        return fileMd5(new File(filePath));
    }

    public synchronized static String fileMd5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        int bufSize = 1024 * 4;
        long ALL_MD5_SIZE = 1024 * 1024 * 30L;
        byte buffer[] = new byte[bufSize];
        long fileSize = file.length();
        if (fileSize <= ALL_MD5_SIZE) {//小于界限进行全量md5
            int len;
            try {
                digest = MessageDigest.getInstance("MD5");
                in = new FileInputStream(file);
                while ((len = in.read(buffer, 0, bufSize)) != -1) {
                    digest.update(buffer, 0, len);
                }
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {//大于界限读首尾字节流 + 文件大小
            int len;
            try {
                digest = MessageDigest.getInstance("MD5");
                in = new FileInputStream(file);
                if ((len = in.read(buffer, 0, bufSize)) != -1) {
                    digest.update(buffer, 0, len);
                }
                in.skip(fileSize - bufSize * 2);
                if ((len = in.read(buffer, 0, bufSize)) != -1) {
                    digest.update(buffer, 0, len);
                }
                in.close();
                byte[] fileByte = (fileSize + "").getBytes();
                digest.update(fileByte, 0, fileByte.length);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        BigInteger bigInt = new BigInteger(1, digest.digest());
        String md5 = bigInt.toString(16);
        if (md5.length() < 32) {
            int d = 32 - md5.length();
            StringBuilder sb = new StringBuilder(32 - md5.length());
            for (int i = 0; i < d; i++) {
                sb.append("0");
            }
            md5 = sb.append(md5).toString();
        }
        if (toUpperCase) {
            return md5.toUpperCase();
        }
        return md5;
    }

    public synchronized static String getStrMd5(String src) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(src.getBytes());
            if (toUpperCase) {
                return bytesToHex(md.digest()).toUpperCase();
            }
            return bytesToHex(md.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

}
