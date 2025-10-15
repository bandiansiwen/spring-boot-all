package com.imp.all.fileClient;

import com.imp.all.file.model.FileRes;
import com.imp.all.framework.common.pojo.CommonResult;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author Longlin
 * @date 2024/2/1 15:31
 * @description
 */
@Slf4j
@RequestMapping("/file")
@RestController
public class ClientController {

    @Autowired
    private FileClientService fileClientService;

    @PostMapping("/upload")
    @SneakyThrows
    public CommonResult<String> sendFile(@RequestPart("file") MultipartFile multipartFile, @RequestParam(required = false, defaultValue = "1") Integer times) {

        File file = new File("E:/tmp/" + multipartFile.getOriginalFilename());
        multipartFile.transferTo(file);

        int num = 0;
        for (int i = 0; i < times; i++) {
            FileRes res = fileClientService.sendFileReq(file.getPath(), i);
            if (res.isSuccess()) {
                log.info(res.getFileKey() + " 上传成功");
                num += 1;
            }
            else {
                log.error(res.getFileKey() + " 上传失败，原因：" + res.getMessage());
            }
        }
        return CommonResult.success("上传成功个数: " + num);
    }

    @GetMapping("/download")
    public CommonResult<String> downloadFile(@RequestParam(value = "fileKey") String fileKey, @RequestParam(required = false, defaultValue = "1") Integer times) {

        int num = 0;
        for (int i = 0; i < times; i++) {
            FileRes res = fileClientService.downloadFileReq(fileKey);
            if (res.isSuccess()) {
                log.info(res.getFileKey() + " 下载成功");
                num += 1;
            }
            else {
                log.error(res.getFileKey() + " 下载失败，原因：" + res.getMessage());
            }
        }
        return CommonResult.success("下载成功个数: " + num);
    }
}
