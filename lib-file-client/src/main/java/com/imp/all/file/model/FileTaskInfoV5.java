package com.imp.all.file.model;

import cn.hutool.core.util.StrUtil;
import com.imp.all.file.request.IMFileRequest;
import com.imp.all.file.request.UploadRequestV5;
import com.imp.all.file.utils.FileUtil;

public class FileTaskInfoV5 implements IMFileTask {

    private final IMFileRequest topRequest;

    private long fileSize;
    private long offset;
    private int dataSize;
    private String md5;
    private long expiredDay;
    private String realFileKey;

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public int getDataSize() {
        return dataSize;
    }

    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }

    public void setRealFileKey(String realFileKey) {
        this.realFileKey = realFileKey;
    }

    @Override
    public String getRequestId() {
        return topRequest.getId();
    }

    @Override
    public String getRealFileKey() {
        if (StrUtil.isEmpty(realFileKey)) {
            return getRequestId();
        }
        return realFileKey;
    }

    public FileTaskInfoV5(IMFileRequest topRequest) {
        this.topRequest = topRequest;
    }

    public static FileTaskInfoV5 parse(IMFileRequest iRequest) {
        FileTaskInfoV5 fileTaskInfoV5 = new FileTaskInfoV5(iRequest);
        if (iRequest instanceof UploadRequestV5) {
            fileTaskInfoV5.md5 = ((UploadRequestV5) iRequest).getMd5();
            fileTaskInfoV5.fileSize = FileUtil.getFileSize(iRequest.getFilePath());
        }
        return fileTaskInfoV5;
    }

    public IMFileRequest getRequest() {
        return topRequest;
    }

    public String getMd5() {
        if (md5 == null) {
            return null;
        }
        return md5.toUpperCase();
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    @Override
    public String getFilePath() {
        return topRequest.getFilePath();
    }

    public long getExpiredDay() {
        return expiredDay;
    }

    public void setExpiredDay(long expiredDay) {
        this.expiredDay = expiredDay;
    }

    @Override
    public float getProcess() {
        if (fileSize == 0) {
            return 0;
        }
        return offset * 1.0f / fileSize;
    }
}
