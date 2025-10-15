package com.imp.all.file.core;

import cn.hutool.core.util.StrUtil;
import com.imp.all.file.error.ImFileException;
import com.imp.all.file.model.FileTaskInfoV5;
import com.imp.all.file.pb.IMFileV5;
import com.imp.all.file.type.FileCmdType;
import com.imp.all.file.type.ThumbnailLevel;
import com.imp.all.file.type.TranMethod;
import com.imp.all.file.utils.FileKeyHelper;
import com.imp.all.file.utils.PBFormat;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 */
@Slf4j
public class IMFileResponseHandlerV5 {

    //3.2.4. 开始下载	接收文件请求的响应 (获取文件信息)
    public static void handleGetFileInfoRsp(int sq, String requestFileKey, IMFileV5.FileGetInfoRsp imReceiveFileRsp) {
        String traceId = imReceiveFileRsp.getTraceId();
        log.info(IMFileCore.LOG_PRE_UPLOAD_REC_FILE_RSP + PBFormat.format(imReceiveFileRsp));
        final String fileKey = imReceiveFileRsp.getFileKey();
        if (imReceiveFileRsp.getResultCode() != 0) {//获取文件信息失败
            log.error("获取文件信息失败, requestFileKey:{}, traceId:{}", requestFileKey, traceId);
            IMFileCore.onDownloadError(traceId, new RuntimeException("获取文件信息失败, requestFileKey:" + requestFileKey));
            return;
        }
        if (imReceiveFileRsp.getStatus() != 2) {//文件状态 0 , 1 都不能进行下载
            log.error("{} 文件状态信息为：{}, 不能下载！, traceId:{}", imReceiveFileRsp.getFileKey(), imReceiveFileRsp.getStatus(), traceId);
            IMFileCore.onDownloadError(traceId, new RuntimeException("文件状态信息为：" + imReceiveFileRsp.getStatus() + ", 不能下载！"));
            return;
        }

        FileTaskInfoV5 task = IMFileTaskQueueV5.getDownloadTask(traceId);

        if (task == null) {
            //task has cancel?
            return;
        }

        //请求的fileKey和服务器返回的不一样,缩略图不存在
        if (!StrUtil.equals(requestFileKey, fileKey)) {
            //服务器返回备选fileKey
            if (imReceiveFileRsp.getTnsList() != null && !imReceiveFileRsp.getTnsList().isEmpty()) {
                task.setRealFileKey(requestFileKey.substring(0, requestFileKey.lastIndexOf("#")) + imReceiveFileRsp.getTns(0));
            } else {
                //服务器没有缩略图时,返回了原图fileKey,检查任务队列有没有对应的缩略图任务,如果有就抛出异常,让业务层拿其他等级的图
                String thumFileKey = FileKeyHelper.getThumFileKey(fileKey, ThumbnailLevel.TN5);
                task = IMFileTaskQueueV5.getDownloadTask(thumFileKey);
                if (task == null) {
                    thumFileKey = FileKeyHelper.getThumFileKey(fileKey, ThumbnailLevel.TN1);
                    task = IMFileTaskQueueV5.getDownloadTask(thumFileKey);
                }
                if (task != null) {
                    log.error("缩略图不存在, requestId:{}, fileKey:{}, traceId:{}", task.getRequestId(), fileKey, traceId);
                    IMFileCore.onDownloadError(traceId, new RuntimeException("缩略图不存在, requestId:" + task.getRequestId() + ", fileKey:" + fileKey));
                }
                return;
            }
        }
        task.setFileSize(imReceiveFileRsp.getFileSize());
        task.setOffset(0);
        task.setMd5(imReceiveFileRsp.getFileMd5());

        try {
            IMFileSenderV5.filePullDataReq(task);

        } catch (Throwable e) {
            log.error("下载失败:", e);
            IMFileCore.onDownloadError(traceId, e);
        }
    }

    //3.2.6. 下载写入 文件数据请求的响应
    public static void handlePullDataRsp(int sq, IMFileV5.FilePullDataRsp imFilePullDataRsp) throws IOException {
        log.info(IMFileCore.LOG_PRE_DOWN_PULL_DATA_RSP + PBFormat.format(imFilePullDataRsp));
        String fileKey = imFilePullDataRsp.getFileKey();
        String traceId = imFilePullDataRsp.getTraceId();
        if (imFilePullDataRsp.getResultCode() != 0) {
            log.error("下载写入获取文件信息失败, requestFileKey:{}, traceId:{}", fileKey, traceId);
            IMFileCore.onDownloadError(traceId, new RuntimeException("下载写入获取文件信息失败, requestFileKey:" + fileKey + ",traceId"+ traceId));
            return;
        }

        final FileTaskInfoV5 fileTaskInfo = IMFileTaskQueueV5.getDownloadTask(traceId);

        if (fileTaskInfo == null) {
            log.error("下载写入 fileTaskInfo 为 null, requestFileKey:{}, traceId:{}", fileKey, traceId);
            IMFileCore.onDownloadError(traceId, new RuntimeException("下载写入 fileTaskInfo 为 null, requestFileKey:" + fileKey + ",traceId"+ traceId));
            return;
        }

        final String filePath = FileSDK.getDownloadTempPath(fileTaskInfo);
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "rw")) {
            raf.seek(imFilePullDataRsp.getDataOffset());
            raf.write(imFilePullDataRsp.getFileData().toByteArray());//写入
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            IMFileCore.onDownloadError(traceId, throwable);
        }

        fileTaskInfo.setOffset(imFilePullDataRsp.getDataOffset() + imFilePullDataRsp.getFileData().size());

        // 下载完成
        if (FileSDK.isDone(fileTaskInfo)) {
            // 无法覆盖已存在的文件
//            File file = new File(FileSDK.getDownloadTempPath(fileTaskInfo));
//            file.renameTo(new File(fileTaskInfo.getFilePath()));

            // 覆盖已存在的文件，多线程下载相同文件名文件（测试）
//            String orgPath = fileTaskInfo.getFilePath();
//            String dir = orgPath.substring(0, orgPath.lastIndexOf("/") + 1);
//            String fileName = orgPath.substring(orgPath.lastIndexOf("/") + 1);
//            Files.move(Paths.get(filePath), Paths.get(dir + traceId + "-" + fileName), StandardCopyOption.REPLACE_EXISTING);

            // 删除文件
            Files.delete(Paths.get(filePath));

            IMFileCore.onDownloadSuccess(traceId);
        } else {
            IMFileSenderV5.filePullDataReq(fileTaskInfo);
        }

    }

    //3.2.5. 上传 获取文件数据请求
    public static void handlePullDataReq(int sq, IMFileV5.FilePullDataReq imFilePullDataReq) {

        log.info(IMFileCore.LOG_PRE_UPLOAD_REC_PULL_DATA + PBFormat.format(imFilePullDataReq));

        String fileKey = imFilePullDataReq.getFileKey();
        FileTaskInfoV5 taskInfo = IMFileTaskQueueV5.getUploadTask(fileKey);
        if (taskInfo != null) {
            try {
                IMFileSenderV5.pullDataRsp(sq, imFilePullDataReq, taskInfo.getRequest().getFilePath());
            } catch (IOException e) {
                e.printStackTrace();
                IMFileCore.onUploadError(fileKey, e);
            }
            //cur_size 为当前已上传的大小
            if (imFilePullDataReq.getCurSize() != 0) {
                taskInfo.setOffset(imFilePullDataReq.getCurSize());
            }
        }
    }

    public static void handleDone(int sq, IMFileV5.FileUpdateStatusReq updateStatusReq) {
        if (updateStatusReq.getStatus() == IMFileV5.ClientFileState.CLIENT_FILE_DONE) {
            FileTaskInfoV5 taskInfoV5 = IMFileTaskQueueV5.getUploadTask(updateStatusReq.getFileKey());
            if (taskInfoV5 != null) {
                taskInfoV5.setExpiredDay(updateStatusReq.getExpiredDay());
            }

            log.info(IMFileCore.LOG_PRE_DONE + PBFormat.format(updateStatusReq), updateStatusReq.getFileKey());

            log.info("{} handleDone, method:{}", updateStatusReq.getFileKey(), TranMethod.UPLOAD);
            IMFileCore.onUploadSuccess(updateStatusReq.getFileKey());

        } else {
            log.error("{} handleDone error, number:{}", updateStatusReq.getFileKey(), updateStatusReq.getStatus().getNumber());
            IMFileCore.onUploadError(updateStatusReq.getFileKey(), new ImFileException(sq, FileCmdType.FILE_UPDATESTATUS_REQUEST, updateStatusReq.getStatus().getNumber()));
        }
    }
}
