package com.imp.all.file.utils;

import java.io.*;

/**
 */
public class FileUtil {

    /**
     * 解析文件全名,带扩展名
     *
     * @param filePath 文件路径
     * @return 文件全名
     */
    public static String getFileNameWithExt(String filePath) {
        if (IMCoreTextUtils.isEmpty(filePath)) {
            return null;
        }
        int last = filePath.lastIndexOf(File.separator);
        int end = filePath.length() - 1;
        if (last == -1) {
            return filePath;
        } else if (last < end) {
            return filePath.substring(last + 1);
        } else {
            return filePath.substring(last);
        }
    }

    /**
     * 获取文件名，不带扩展名
     *
     * @param fileName
     * @return
     */
    public static String getFileNameWithoutExt(String fileName) {
        if (IMCoreTextUtils.isEmpty(fileName)) {
            return null;
        }
        int index = fileName.lastIndexOf(".");
        if (index == -1) {
            return fileName;
        } else {
            return fileName.substring(0, index);
        }
    }

    /**
     * 获取文件大小
     *
     * @param path
     * @return
     */
    public static long getFileSize(String path) {
        if (IMCoreTextUtils.isEmpty(path)) {
            return 0;
        }
        File file = new File(path);
        if (!file.exists())
            return 0;

        long size = 0;
        try {
            if (file.exists()) {
                size = file.length();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return size;
    }

    public static boolean isExist(String filePath) {
        return new File(filePath).exists();
    }

    public static String renameConflict(String filePath) {
        File file = new File(filePath);
        String fileName = getFileNameWithoutExt(file.getName());
        String ext = getFileExt(filePath);
        int i = 1;
        while (file.exists()) {
            String temp = fileName + "(" + i++ + ")" + ext;
            file = new File(file.getParent(), temp);
        }
        return file.getAbsolutePath();
    }

    private static String getFileExt(String filePath) {
        if (IMCoreTextUtils.isEmpty(filePath)) {
            return null;
        }
        int index = filePath.lastIndexOf(".");
        if (index != -1) {
            return filePath.substring(index);
        }
        return "";
    }

    public static void copy(File file, String dest) throws IOException {
        int bytesum = 0;
        int byteread = 0;
        File destFile = new File(dest);
        if (file.exists()) { //文件存在时
            InputStream inStream = new FileInputStream(file); //读入原文件
            if (!destFile.exists()) {
                destFile.getParentFile().mkdirs();
                destFile.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(dest);
            byte[] buffer = new byte[2048];
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread; //字节数 文件大小
                fs.write(buffer, 0, byteread);
            }
            inStream.close();
        }
    }

    public static String getFileName(String filePath) {
        return new File(filePath).getName();
    }

    public static String getParentPath(String filePath){
        return new File(filePath).getParentFile().getPath();
    }

    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }
}
