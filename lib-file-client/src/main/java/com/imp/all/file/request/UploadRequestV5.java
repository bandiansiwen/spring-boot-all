package com.imp.all.file.request;

import cn.hutool.core.util.StrUtil;
import com.imp.all.file.core.FileSDK;
import com.imp.all.file.listenter.IUploadListener;
import com.imp.all.file.model.Bucket;
import com.imp.all.file.utils.AccessKeyHelper;
import com.imp.all.file.utils.FileKeyHelper;
import com.imp.all.file.utils.McFileMD5;

import java.util.List;
import java.util.Map;

public class UploadRequestV5 implements IMFileRequest {
    private From from;
    private String filePath;
    private String md5;
    private Object tag;
    private Map<String, Object> tags;

    private List<IUploadListener> listeners;

    private boolean needThum;
    private String fileKey;

    private String fileName;

    private long fileSize;

    private long timestamp = System.currentTimeMillis();

    /**
     * @param from      From.IM
     * @param filePath  上传文件路径
     * @param listeners 上传监听器
     */
    public UploadRequestV5(From from, String filePath, List<IUploadListener> listeners) {
        this.from = from;
        this.filePath = filePath;
    }

    /**
     * @param from      From.IM
     * @param filePath  上传文件路径
     * @param listeners 上传监听器
     */
    public UploadRequestV5(From from, String filePath, Object tag, List<IUploadListener> listeners) {
        this.from = from;
        this.filePath = filePath;
        this.tag = tag;
        this.listeners = listeners;
    }

    /**
     * @param from      From.IM
     * @param filePath  上传文件路径
     * @param tags      设置tag
     * @param listeners 上传监听器
     */
    public UploadRequestV5(From from, String filePath, Object tag, Map<String, Object> tags, List<IUploadListener> listeners) {
        this.from = from;
        this.filePath = filePath;
        this.tag = tag;
        this.tags = tags;
        this.listeners = listeners;
    }

    public String getFilePath() {
        return filePath;
    }

    /**
     * 设置上传文件路径
     *
     * @param filePath
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public From getFrom() {
        return from;
    }

    /**
     * 设置 From
     *
     * @param from From.IM
     */
    public void setFrom(From from) {
        this.from = from;
    }

    @Override
    public String getId() {
        return getFilekey();
    }

    public Object getTag() {
        return tag;
    }

    /**
     * 设置标签
     *
     * @param tag
     */
    public void setTag(Object tag) {
        this.tag = tag;
    }

    public List<IUploadListener> getListeners() {
        return listeners;
    }

    /**
     * 设置上传监听器
     *
     * @param listeners
     */
    public void setListeners(List<IUploadListener> listeners) {
        this.listeners = listeners;
    }

    /**
     * 设置是否需要生成缩略图
     *
     * @param needThum
     */
    public void setNeedThum(boolean needThum) {
        this.needThum = needThum;
    }

    public boolean getNeedThum() {
        return needThum;
    }

    public void setMd5(String md51) {
        md5 = md51;
    }

    public String getMd5() {
        if (md5 == null) {
            md5 = McFileMD5.fileMd5(filePath);
        }
        return md5;
    }

    public void setFileKey(String fk) {
        fileKey = fk;
    }

    public String getFilekey() {
        if (StrUtil.isEmpty(fileKey)) {
            fileKey = FileKeyHelper.getFileKey(filePath, timestamp, getMd5());
        }
        return fileKey;
    }

    private Bucket bucket;

    /**
     * 设置上传空间
     *
     * @param bucket
     */
    public void setBucket(Bucket bucket) {
        this.bucket = bucket;
    }

    public Bucket getBucket() {
        if (bucket == null) {
            bucket = FileSDK.DEFAULT_IM_BUCKET;
        }
        return bucket;
    }

    /**
     * 设置 AccessKey , 用于后台校验
     *
     * @param accessTime
     * @return
     */
    public String getAccessKey(int accessTime) {
        return AccessKeyHelper.getAccessKey(bucket, getFilekey(), accessTime);
    }

    private long doneTimestamp;

    @Override
    public long getDoneTimestamp() {
        return doneTimestamp;
    }

    @Override
    public void setDoneTimestamp(long doneTimestamp) {
        this.doneTimestamp = doneTimestamp;
    }

    public Object getTag(String key) {
        if (tags != null) {
            return tags.get(key);
        }
        return null;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
