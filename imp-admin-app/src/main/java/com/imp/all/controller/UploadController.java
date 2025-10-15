package com.imp.all.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Longlin
 * @date 2022/10/27 9:12
 * @description
 * 实现思路
 * 分片：按照自定义缓冲区大小，将大文件分成多个小文件片段。
 *
 * 断点续传：根据分片数量，给每个小文件通过循环起对应名称，当文件下载中断在续传时，判断小文件名称若存在则不存了，此时还需要判断文件若不是最后一个分片则大小为缓冲区固定大小，若没达到则证明小文件没传完需要重新传输。
 *
 * 合并：下载时通过线程池创建任务进行下载或上传、当判断最后一个分片传完时，调用合并方法，根据之前定义的文件名称顺序进行合并，肯能出现最后一个分片传完，之前分片未传完的情况，需要使用while循环进行判断，多文件未传输完，则等待一会继续判断。
 *
 * 大文件秒传：实际上是根据文件名称区一个唯一的md5值存储，传文件时进行判断，若存在则不传。
 */
@RestController
@RequestMapping("/upload")
public class UploadController {

    @PostMapping("/add")
    @ResponseBody
    public String upload (HttpServletRequest request, HttpServletResponse response) {
        return "搭建成功";
    }
}
