package com.imp.all.fileClient;

import com.imp.all.file.core.FileSDK;
import com.imp.all.file.core.IMFileCore;
import com.imp.all.file.model.FileRes;
import com.imp.all.file.model.FileTaskInfoV5;
import com.imp.all.file.request.DownloadRequestV5;
import com.imp.all.file.request.From;
import com.imp.all.file.request.UploadRequestV5;
import com.imp.all.file.utils.FileUtil;
import com.imp.all.file.utils.McFileMD5;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author Longlin
 * @date 2024/2/2 15:24
 * @description
 */
@Slf4j
@Component
public class FileClientService {

    @SneakyThrows
    public FileRes sendFileReq(String filePath, int index) {

        FileSDK.initEnv("sit");

        if (!FileUtil.isExist(filePath)) {
            throw new FileNotFoundException();
        }

        File f = new File(filePath);
        String fName = f.getName();
        String fileMd5 = McFileMD5.fileMd5(f);
        long currentTime = System.currentTimeMillis() / 1000L;
        String timeStr = Long.toString(currentTime);
        String sIndex = String.format("%07d", index);
        String extraMsg = timeStr + "_" + sIndex;
        String file_key = "/jmeter/file-client/" + extraMsg + "/" + fileMd5 + "/" + fName;

        UploadRequestV5 uploadReq = new UploadRequestV5(From.IM, filePath, null, null);
        uploadReq.setFileKey(file_key);
        uploadReq.setMd5(fileMd5);
        uploadReq.setFileName(f.getName());
        uploadReq.setFileSize(f.length());
        uploadReq.setNeedThum(IMFileCore.isImage(filePath));

        CompletableFuture<Throwable> future = IMFileCore.onUploadStart(file_key, new FileTaskInfoV5(uploadReq));
        Throwable throwable = future.get();
        if (throwable == null) {
            return FileRes.success(file_key);
        }
        else {
            return FileRes.failed(file_key, throwable.getMessage());
        }
    }

    @SneakyThrows
    public FileRes downloadFileReq(String fileKey) {

        if (StringUtils.isEmpty(fileKey)) {
            throw new RuntimeException("fileKey 不能为空");
        }

        FileSDK.initEnv("sit");

        String fileName = fileKey.substring(fileKey.lastIndexOf("/") + 1);

        String traceId = UUID.randomUUID().toString();
        log.info("当前traceId为：{}", traceId);

        DownloadRequestV5 downloadReq = new DownloadRequestV5(From.IM, fileKey, null, "/jmeter/download/" + fileName, 0, null, null);
        downloadReq.setTraceId(traceId);

        CompletableFuture<Throwable> future = IMFileCore.onDownloadStart(traceId, new FileTaskInfoV5(downloadReq));
        Throwable throwable = future.get();
        if (throwable == null) {
            return FileRes.success(fileKey);
        }
        else {
            return FileRes.failed(fileKey, throwable.getMessage());
        }
    }
}
