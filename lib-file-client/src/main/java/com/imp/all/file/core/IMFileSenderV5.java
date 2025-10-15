package com.imp.all.file.core;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.imp.all.file.error.FileErrorMap;
import com.imp.all.file.error.ImFileException;
import com.imp.all.file.model.FileTaskInfoV5;
import com.imp.all.file.netty.ClientRequestWrapper;
import com.imp.all.file.netty.FileRequest;
import com.imp.all.file.pb.IMFileV5;
import com.imp.all.file.request.DownloadRequestV5;
import com.imp.all.file.request.UploadRequestV5;
import com.imp.all.file.type.FileCmdType;
import com.imp.all.file.utils.MsgUtil;
import com.imp.all.file.utils.PBFormat;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;

/**
 * @author Longlin
 * @date 2024/2/17 16:56
 * @description
 */
@Slf4j
public class IMFileSenderV5 {

    private static final DecimalFormat decimalFormat = new DecimalFormat("###,##0");

    public static void upload(UploadRequestV5 request) {
        final String fileKey = request.getFilekey();
        new ClientRequestWrapper(
                FileSDK.getFileClient(),
                MsgUtil.incrementAndGet7f(),
                FileCmdType.FILE_UPLOAD_REQUEST,
                fileKey,
                new ClientRequestWrapper.Assemble() {
                    @Override
                    public FileRequest run(int sq, int retryCount) {

                        boolean needThum = request.getNeedThum();

                        IMFileV5.FileUploadReq.Builder builder = IMFileV5.FileUploadReq.newBuilder()
                                .setBucketId(601901)
                                .setBucketRole(0)
                                .setAccessTime(0)
                                .setAccessKey("")
                                .setFileKey(fileKey)
                                .setFileName(request.getFileName())
                                .setFileMd5(request.getMd5())
                                .setFileSize(request.getFileSize())
                                .setFromUser(FileSDK.getUsername());

                        if (needThum) {
                            builder.setAction("tn1,tn5");
                        }

                        log.info(String.format("sq:%d, cmd:%s ,fileKey:%s ", sq, FileCmdType.FILE_UPLOAD_REQUEST, builder.getFileKey()));
                        return new FileRequest(sq, FileCmdType.FILE_UPLOAD_REQUEST, builder);
                    }

                },false
        ) {

            @Override
            public boolean checkIfCancel() {
                return false;
            }

            @Override
            public void onSuccess(
                    int sq,
                    FileCmdType cmd,
                    String requestFileKey,
                    byte[] data)
                    throws InvalidProtocolBufferException {
                final IMFileV5.FileUploadRsp rsp = IMFileV5.FileUploadRsp.parseFrom(data);
                log.info(String.format("upload request success sq:%d, cmd:%s, fileKey:%s", sq, cmd.name(), rsp.getFileKey()));
            }

            @Override
            public void onError(
                    int sq,
                    FileCmdType cmd,
                    String requestFileKey,
                    String traceId,
                    Throwable e) {
                log.error("upload request onError, requestFileKey:{},traceId:{}", requestFileKey, e);
                IMFileCore.onUploadError(requestFileKey, e);
            }

        }.request();

    }

    public static void pullDataRsp(int sq, IMFileV5.FilePullDataReq req, String filePath) throws IOException {
        new ClientRequestWrapper(
                FileSDK.getFileClient(),
                sq,
                FileCmdType.FILE_PULLDATA_RESPONSE,
                req.getFileKey(),
                new ClientRequestWrapper.Assemble() {
                    @Override
                    public FileRequest run(int sq, int retryCount) throws Throwable {
                        try (RandomAccessFile raf = new RandomAccessFile(filePath, "r")) {
                            raf.seek(req.getDataOffset());
                            if (req.getDataSize() > Integer.MAX_VALUE) {
                                throw new ImFileException(sq, FileCmdType.FILE_PULLDATA_RESPONSE, FileErrorMap.ERROR_CODE_UPLOAD_READ_FAILED);
                            }
                            byte[] temp = new byte[(int) req.getDataSize()];
                            raf.readFully(temp);
                            ByteString blockData = ByteString.copyFrom(temp);

                            IMFileV5.FilePullDataRsp.Builder builder = IMFileV5.FilePullDataRsp.newBuilder();
                            builder.setFileData(blockData);
                            builder.setDataOffset(req.getDataOffset());
                            builder.setFileMd5(req.getFileMd5());
                            builder.setFileKey(req.getFileKey());
                            builder.setResultCode(0);

                            log.info(IMFileCore.LOG_PRE_UPLOAD_SEND_FILE_DATA
                                            + "sq:" + sq
                                            + " dataSize:" + req.getDataSize()
                                            + " offset:" + decimalFormat.format(builder.getDataOffset()),
                                    builder.getFileKey());

                            return new FileRequest(sq, FileCmdType.FILE_PULLDATA_RESPONSE, builder);
                        }
                    }

                },
                false
        ) {

            @Override
            public boolean checkIfCancel() {
                return false;
            }

            @Override
            public void onSuccess(
                    int sq,
                    FileCmdType cmd,
                    String requestFileKey,
                    byte[] data)
                    throws InvalidProtocolBufferException {
                log.info("pullDataRsp onSuccess, requestFileKey:{}", requestFileKey);
            }

            @Override
            public void onError(
                    int sq,
                    FileCmdType cmd,
                    String requestFileKey,
                    String traceId,
                    Throwable e) {
                log.error("pullDataRsp onError, requestFileKey:{}, traceId:{}", requestFileKey, traceId);
                IMFileCore.onUploadError(requestFileKey, e);
            }

        }.request();
    }

    public static void download(DownloadRequestV5 request) {
        if (request.getId() == null) {
            return;
        }
        final String fileKey = request.getFilekey();

        final ClientRequestWrapper requestWrapper = new ClientRequestWrapper(
                FileSDK.getFileClientRec(),
                MsgUtil.incrementAndGet7f(),
                FileCmdType.FILE_GETINFO_REQUEST,
                fileKey,
                new ClientRequestWrapper.Assemble() {
                    @Override
                    public FileRequest run(int sq, int retryCount) {
                        String uid = FileSDK.getUsername();
                        int accessTime = IMFileCore.getAccessTime();

                        IMFileV5.FileGetInfoReq.Builder builder = IMFileV5.FileGetInfoReq.newBuilder();
                        builder.setBucketId(Integer.parseInt(request.getBucket().getId()));
                        builder.setBucketRole(Integer.parseInt(request.getBucket().getRole()));
                        builder.setAccessTime(accessTime);
                        builder.setAccessKey(request.getAccessKey(accessTime));
                        builder.setFileKey(fileKey);
                        builder.setFromUser(uid);
                        builder.setTraceId(request.getTraceId());

                        log.info(IMFileCore.LOG_PRE_UPLOAD_REC_FILE_REQ
                                + "sq:" + sq
                                + PBFormat.format(builder), request.getId());

                        log.info("sq:{}, cmd:{} ,fileKey:{} ", sq, FileCmdType.FILE_GETINFO_REQUEST, builder.getFileKey());
                        return new FileRequest(sq, FileCmdType.FILE_GETINFO_REQUEST, builder);
                    }

                },
                false
        ) {

            @Override
            public boolean checkIfCancel() {
                return false;
            }

            @Override
            public void onSuccess(
                    int sq,
                    FileCmdType cmd,
                    String requestFileKey,
                    byte[] data)
                    throws InvalidProtocolBufferException {
                final IMFileV5.FileGetInfoRsp rsp = IMFileV5.FileGetInfoRsp.parseFrom(data);
                log.info("get file info success sq:{}, cmd:{}, fileKey:{}, traceId:{}", sq, cmd.name(), rsp.getFileKey(), rsp.getTraceId());
                IMFileResponseHandlerV5.handleGetFileInfoRsp(sq, requestFileKey, rsp);
            }

            @Override
            public void onError(
                    int sq,
                    FileCmdType cmd,
                    String requestFileKey,
                    String traceId,
                    Throwable e) {
                log.error("get file info failed sq:{}, cmd:{}, fileKey:{}, traceId:{}", sq, cmd.name(), requestFileKey, traceId, e);
                IMFileCore.onDownloadError(traceId, e);
            }

        }.request();
        requestWrapper.setTraceId(request.getTraceId());
    }

    /**
     * 下载
     * 获取文件数据请求
     *
     * @param fileTaskInfo
     */
    public static void filePullDataReq(FileTaskInfoV5 fileTaskInfo) {

        final DownloadRequestV5 downloadRequest = (DownloadRequestV5) fileTaskInfo.getRequest();
        final String uid = FileSDK.getUsername();

        final ClientRequestWrapper requestWrapper = new ClientRequestWrapper(
                FileSDK.getFileClientRec(),
                MsgUtil.incrementAndGet7f(),
                FileCmdType.FILE_PULLDATA_REQUEST,
                downloadRequest.getFilekey(),
                new ClientRequestWrapper.Assemble() {
                    @Override
                    public FileRequest run(int sq, int retryCount) {

                        long lastLength = fileTaskInfo.getFileSize() - fileTaskInfo.getOffset();
                        if (lastLength <= 0) {
                            new File(fileTaskInfo.getFilePath()).delete();
                            throw new ImFileException(sq, FileCmdType.FILE_PULLDATA_REQUEST, FileErrorMap.ERROR_CODE_PULL_DATA_REQUEST_PARAM_ERROR);
                        }
                        final long blockSize = 512 * 1024L;
                        int dataSize = (int) Math.min(lastLength, blockSize);
                        fileTaskInfo.setDataSize(dataSize);

                        long offset = fileTaskInfo.getOffset();

                        int accessTime = IMFileCore.getAccessTime();
                        IMFileV5.FilePullDataReq.Builder builder = IMFileV5.FilePullDataReq.newBuilder();
                        builder.setBucketId(Integer.parseInt(downloadRequest.getBucket().getId()));
                        builder.setBucketRole(Integer.parseInt(downloadRequest.getBucket().getRole()));
                        builder.setAccessTime(accessTime);
                        builder.setAccessKey(downloadRequest.getAccessKey(accessTime));
                        builder.setFileKey(downloadRequest.getFilekey());
                        builder.setFileMd5(fileTaskInfo.getMd5());
                        builder.setDataOffset(offset);
                        builder.setDataSize(dataSize);
                        builder.setFromUser(uid);
                        builder.setCurSize(0);
                        builder.setTraceId(downloadRequest.getTraceId());

                        log.info(IMFileCore.LOG_PRE_DOWN_PULL_DATA_REQ
                                + "sq:" + sq
                                + ",offset/fileSize:"
                                + decimalFormat.format(offset)
                                + "/" + decimalFormat.format(fileTaskInfo.getFileSize())
                                + ",dataSize:" + decimalFormat.format(dataSize), fileTaskInfo.getRequestId()
                                + ",traceId:" + downloadRequest.getTraceId());

                        return new FileRequest(sq, FileCmdType.FILE_PULLDATA_REQUEST, builder);
                    }

                },
                 false
        ) {
            @Override
            public boolean checkIfCancel() {
                return false;
            }

            @Override
            @SneakyThrows
            public void onSuccess(
                    int sq,
                    FileCmdType cmd,
                    String requestFileKey,
                    byte[] data) {
                final IMFileV5.FilePullDataRsp rsp = IMFileV5.FilePullDataRsp.parseFrom(data);
                log.info(String.format("pull data sq:%d, cmd:%s, fileKey:%s, , traceId:%s", sq, cmd.name(), rsp.getFileKey(), rsp.getTraceId()));
                IMFileResponseHandlerV5.handlePullDataRsp(sq, rsp);
            }

            @Override
            public void onError(
                    int sq,
                    FileCmdType cmd,
                    String requestFileKey,
                    String traceId,
                    Throwable e) {
                log.error("filePullDataReq onError, requestFileKey:{}, traceId:{}", requestFileKey, traceId);
                IMFileCore.onDownloadError(traceId, e);
            }

        }.request();
        requestWrapper.setTraceId(downloadRequest.getTraceId());
    }

}
