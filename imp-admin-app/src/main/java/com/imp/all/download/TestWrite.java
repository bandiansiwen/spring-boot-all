package com.imp.all.download;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class TestWrite {
 
 
    private static final String FILE_PATH = "E:\\我的文件\\软件安装包\\";
    /**数组内容大小*/
    static int[] contentBytes = {32,64, 128, 512, 1024, 2048, 4096, 8192, 16384, 1048576, 4194304, 8388608};
    static String[] contentUnit = {"32","64 byte", "128  byte", "512  byte", "1 k", "2 k", "4 k", "8 k", "16 k", "1 M" ,"4 M", "8 M"};
    /**生成文件大小*/
    static long fileSize = 1024 * 1024 * 1024;
    /**测试内容*/
    static String content = "IORW读写测试";
    /**写入次数*/
    private static int totals = -1;
 
    /**
     * FileOutputStream
     */
    public static void fileOutputStream() {
        try {
            File file = getRandomFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] tempContent = content.getBytes(StandardCharsets.UTF_8);
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < totals; i++) {
                fos.write(tempContent);
            }
            fos.close();
            long interval = System.currentTimeMillis() - startTime;
            System.out.println("fileOutputStream cost :" + interval);
           // file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    /**
     * bufferedWriter
     */
    public static void bufferedWriter() {
        try{
            File file = getRandomFile();
            BufferedWriter bufferedWriter =
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    new FileOutputStream(file),
                                    "UTF-8"));
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < totals; i++) {
                bufferedWriter.write(content);
            }
            long interval = System.currentTimeMillis() - startTime;
            System.out.println("bufferedWriter cost :" + interval);
           // file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    /**
     * BufferedOutputStream
     */
    public static void bufferedOutputStream() {
        try {
            File file = getRandomFile();
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            byte[] tempContent = content.getBytes(StandardCharsets.UTF_8);
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < totals; i++) {
                bufferedOutputStream.write(tempContent);
            }
            bufferedOutputStream.close();
            long interval = System.currentTimeMillis() - startTime;
            System.out.println("bufferedOutputStream cost :" + interval);
            //file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    /**
     * FileWriter
     */
    public static void fileWriter() {
        File f = getRandomFile();
        FileWriter fw = null;
        try {
            //true表示可以追加新内容
            fw = new FileWriter(f.getAbsoluteFile(), true);
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < totals; i++) {
                fw.write(content);
            }
            fw.close();
            long interval = System.currentTimeMillis() - startTime;
            System.out.println("fileWriter cost :" + interval);
           // f.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    /**
     * RandomAccessFile
     */
    public static void randomAccessFile() {
        try {
            File file = getRandomFile();
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            byte[] tempContent = content.getBytes(StandardCharsets.UTF_8);
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < totals; i++) {
                raf.write(tempContent);
            }
            long interval = System.currentTimeMillis() - startTime;
            System.out.println("randomAccessFile cost :" + interval);
           // file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    /**
     * FileChannel
     */
    public static void fileChannel() {
 
        try {
            File file = getRandomFile();
            FileChannel fc = new RandomAccessFile(file, "rw").getChannel();
            byte[] tempContent = content.getBytes(StandardCharsets.UTF_8);
            ByteBuffer buffer = ByteBuffer.wrap(tempContent);
 
            long startTime = System.currentTimeMillis();
            buffer.flip();
            for (int i = 0; i < totals; i++) {
                buffer.clear();  //
                fc.write(buffer);
                buffer.flip();
            }
            fc.close();
 
            long interval = System.currentTimeMillis() - startTime;
            System.out.println("FileChannel cost :" + interval);
            //file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    /**
     * MappedByteBuffer
     */
    public static void mappedByteBuffer() {
        try {
            File file = getRandomFile();
            MappedByteBuffer mbb = new RandomAccessFile(file, "rw").getChannel().
                    map(FileChannel.MapMode.READ_WRITE, 0, fileSize);
            byte[] tempContent = content.getBytes(StandardCharsets.UTF_8);
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < totals; i++) {
                mbb.put(tempContent);
            }
            long interval = System.currentTimeMillis() - startTime;
            System.out.println("MappedByteBuffer cost :" + interval);
            //file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    /**
     * 生成随机文件，避免pageCache
     * @return
     */
    private static File getRandomFile() {
        String fileName = FILE_PATH + UUID.randomUUID().toString();
        return new File(fileName);
    }
 
    public static void main(String[] args) throws Exception {
 
        Thread.sleep(20000);
 
        for(int i=0;i<contentBytes.length;i++){
            StringBuilder sbCont = new StringBuilder();
            //拼接每次写入内容
            for(int n=0;n<(contentBytes[i]/content.getBytes(StandardCharsets.UTF_8).length);n++){
                sbCont.append(content);
            }
            content = sbCont.toString();
            //计算循环次数，控制文件大小为1G
            totals = (int) (fileSize/content.getBytes(StandardCharsets.UTF_8).length);
 
            File tempList = new File(FILE_PATH);
 
 
            for(File f:tempList.listFiles()){
                if(f.isDirectory()){
                    continue;
                }
                f.delete();
            }
 
            for(int x=0;x<5;x++){
                File temp = new File(FILE_PATH+i+"-"+x);
                temp.createNewFile();
            }
            Thread.sleep(1000);
 
            System.out.println();
            System.out.println("*********开始写入***** "+i+" --- "+contentBytes[i]+" ********");
            System.out.println();
 
            fileOutputStream();
            Thread.sleep(3000);
            bufferedWriter();
            Thread.sleep(3000);
            bufferedOutputStream();
            Thread.sleep(3000);
            fileWriter();
            Thread.sleep(3000);
            randomAccessFile();
            Thread.sleep(3000);
            fileChannel();
            Thread.sleep(3000);
            mappedByteBuffer();
            Thread.sleep(3000);
        }
 
    }
}