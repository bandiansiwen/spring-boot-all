package com.imp.all.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * @author Longlin
 * @date 2023/11/29 19:03
 * @description
 */
@RestController
@RequestMapping("/download")
public class DownloadController {

    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/downloadFile")
    public void downloadFile(HttpServletResponse response) throws IOException {
        String filePath = "/path/to/largefile.mp4";
        File file = new File(filePath);

        response.setHeader("Content-Disposition", "attachment; filename=largefile.mp4");
        response.setContentType("application/octet-stream");
        response.setContentLength((int) file.length());

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
             OutputStream outputStream = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = randomAccessFile.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }

    @GetMapping("/getFileByName")
    public void getFileByName(@RequestParam("fileName") String fileName, HttpServletResponse response) {
        File file = new File(fileName);
        downloadFile(file, response);
    }

    /**
     * 使用 FileChannel 类读取文件流  (NIO实现)
     * @param file
     * @param response
     */
    public static void downloadFile(File file, HttpServletResponse response) {
        OutputStream os = null;
        try {
            // 取得输出流
            os = response.getOutputStream();
            String contentType = Files.probeContentType(Paths.get(file.getAbsolutePath()));
            response.setHeader("Content-Type", contentType);
            response.setHeader("Content-Disposition", "attachment;filename="+ new String(file.getName().getBytes(StandardCharsets.UTF_8),"ISO8859-1"));
            FileInputStream fileInputStream = new FileInputStream(file);
            WritableByteChannel writableByteChannel = Channels.newChannel(os);
            FileChannel fileChannel = fileInputStream.getChannel();
            fileChannel.transferTo(0,fileChannel.size(),writableByteChannel);
            fileChannel.close();
            os.flush();
            writableByteChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //文件的关闭放在finally中
        finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * RestTemplate流式处理下载大文件
     * @param url
     * @param productZipName
     */
    public void download(String url, String productZipName) {

        File file = new File(productZipName);
        try {
            //定义请求头的接收类型
            RequestCallback requestCallback = request -> request.getHeaders()
                    .setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL));
            //对响应进行流式处理而不是将其全部加载到内存中
            restTemplate.execute(url, HttpMethod.GET, requestCallback, clientHttpResponse -> {
                Files.copy(clientHttpResponse.getBody(), Paths.get(productZipName));
                return null;
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * BufferedInputStream 缓存流下载文件
     * @param downloadUrl
     * @param path
     */
    public static void downloadFile(String downloadUrl, String path){
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            URL url = new URL(downloadUrl);
            //这里没有使用 封装后的ResponseEntity 就是也是因为这里不适合一次性的拿到结果，放不下content,会造成内存溢出
            HttpURLConnection connection =(HttpURLConnection) url.openConnection();

            //使用bufferedInputStream 缓存流的方式来获取下载文件，不然大文件会出现内存溢出的情况
            inputStream = new BufferedInputStream(connection.getInputStream());
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            outputStream = Files.newOutputStream(file.toPath());
            //这里也很关键每次读取的大小为5M 不一次性读取完
            byte[] buffer = new byte[1024 * 1024 * 5];// 5MB
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            connection.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(inputStream);
        }
    }
}
