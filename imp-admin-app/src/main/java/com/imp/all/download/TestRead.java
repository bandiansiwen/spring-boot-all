package com.imp.all.download;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
* @author Longlin
* @date 2023/12/02 01:35
* @description 文件读取测试
 */
public class TestRead {
 
    private static final String FILE_PATH = "E:\\我的文件\\软件安装包\\";
 
    /**数组内容大小*/
    static int[] contentBytes = {32,64, 128, 512, 1024, 2048, 4096, 8192, 16384, 1048576, 4194304, 8388608};
    static String[] contentUnit = {"32 byte","64 byte", "128  byte", "512  byte", "1 k", "2 k", "4 k", "8 k", "16 k", "1 M" ,"4 M", "8 M"};
    /**文件大小*/
    static long fileSize = 1024 * 1024 * 1024;
    /**读取字节长度*/
    static int DEFAULT_LENGTH = 0;
 
    /**
     * fileInputStream
     */
    public static void fileInputStream(String name) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(FILE_PATH + name);
            byte[] tempContent = new byte[DEFAULT_LENGTH];
            int readCount = 0;
            long startTime = System.currentTimeMillis();
            while ((readCount = fileInputStream.read(tempContent)) != -1) {
                String text = new String(tempContent, StandardCharsets.UTF_8);
            }
            long interval = System.currentTimeMillis() - startTime;
            System.out.println("fileInputStream cost :" + interval);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
 
    public static void bufferedInputStream(String name) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(FILE_PATH + name);
            BufferedInputStream bis=new BufferedInputStream(fileInputStream);
            byte[] tempContent = new byte[DEFAULT_LENGTH];
            int readCount = 0;
            long startTime = System.currentTimeMillis();
            while ((readCount = bis.read(tempContent)) != -1) {
               // String text = new String(tempContent, StandardCharsets.UTF_8);
            }
            long interval = System.currentTimeMillis() - startTime;
            System.out.println("bufferedInputStream cost :" + interval);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
 
    public static void randomAccessFile(String name) {
        File file = new File(FILE_PATH + name);
        RandomAccessFile ra;
        try {
            ra = new RandomAccessFile(file, "r");
            long startTime = System.currentTimeMillis();
            while (true) {
                byte[] arr = new byte[DEFAULT_LENGTH];
                int len = ra.read(arr);
                if (len == -1) {
                    break;
                }
            }
            long interval = System.currentTimeMillis() - startTime;
            System.out.println("randomAccessFile cost :" + interval);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    /**
     * fileChannel
     */
    public static void fileChannel(String name) {
        try {
            RandomAccessFile aFile = new RandomAccessFile(FILE_PATH + name, "r");
            FileChannel inChannel = aFile.getChannel();
            byte[] bytes = new byte[DEFAULT_LENGTH];
            ByteBuffer buf = ByteBuffer.allocate(DEFAULT_LENGTH);
            long startTime = System.currentTimeMillis();
            //read into buffer.
            int bytesRead = inChannel.read(buf);
            while (bytesRead != -1) {
                //make buffer ready for read
                buf.flip();
                while(buf.hasRemaining()){
                    buf.get(bytes);
                }
                buf.clear(); //make buffer ready for writing
                bytesRead = inChannel.read(buf);
            }
            long interval = System.currentTimeMillis() - startTime;
            System.out.println("fileChannel cost :" + interval);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    /**
     * mappedByteBuffer
     */
    public static void mappedByteBuffer(String name) {
 
        try {
            FileChannel fc = FileChannel.open(Paths.get(FILE_PATH + name),
                    StandardOpenOption.READ, StandardOpenOption.WRITE);
            byte[] bytes = new byte[DEFAULT_LENGTH];
            //  操作系统提供的一个内存映射的机制的类
            MappedByteBuffer map = fc.map(FileChannel.MapMode.READ_ONLY, 0, fileSize);
            long startTime = System.currentTimeMillis();
            while (map.hasRemaining()) {
                map.get(bytes);
                bytes = new byte[DEFAULT_LENGTH];
            }
            long interval = System.currentTimeMillis() - startTime;
            System.out.println("MappedByteBuffer cost :" + interval);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    public static void main(String[] args) throws InterruptedException {
 
 
        Thread.sleep(20000);
 
        for(int i=0;i<contentBytes.length;i++){
 
            DEFAULT_LENGTH = contentBytes[i];
 
            System.out.println();
            System.out.println("*********开始读取***** "+i+" --- "+contentUnit[i]+" ********");
            System.out.println();
            fileInputStream(i+"-0");
            Thread.sleep(3000);
            bufferedInputStream(i+"-1");
            Thread.sleep(3000);
            randomAccessFile(i+"-2");
            Thread.sleep(3000);
            fileChannel(i+"-3");
            Thread.sleep(3000);
            mappedByteBuffer(1+"-4");
            Thread.sleep(3000);
        }
 
    }
 
}