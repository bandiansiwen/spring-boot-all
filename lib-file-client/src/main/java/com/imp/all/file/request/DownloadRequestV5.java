package com.imp.all.file.request;

import com.imp.all.file.core.FileSDK;
import com.imp.all.file.listenter.IDownloadListener;
import com.imp.all.file.model.Bucket;
import com.imp.all.file.utils.FileKeyHelper;
import com.imp.all.file.utils.McFileMD5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 文件下载请求 (V5)
 */
public class DownloadRequestV5 implements IMFileRequest, ITcpDownloadRequest {
    private From from;
    private String filekey;
    private String fileName;
    /**
     * 下载路径
     */
    private String filePath;
    /**
     * @see #getFilekey()
     */
    private int thum;
    private Object tag;

    private String traceId;
    private final List<IDownloadListener> listeners;

    private long timestamp = System.currentTimeMillis();

    /**
     * @param from      From.IM
     * @param filekey   fileKey
     * @param fileName  下载的文件名 (可为空)
     * @param filePath  文件的文件目录 (可为空)
     * @param thum      是否下载缩略图
     * @param tag       请求标签
     * @param listeners 下载监听器
     */
    public DownloadRequestV5(From from, String filekey, String fileName, String filePath,
                             int thum, Object tag,
                             List<IDownloadListener> listeners) {
        this.from = from;
        this.filekey = filekey;
        this.fileName = fileName;
        this.filePath = filePath;
        this.thum = thum;
        this.tag = tag;
        this.listeners = listeners;
    }

    public From getFrom() {
        return from;
    }

    /**
     * @param from From.IM
     */
    public void setFrom(From from) {
        this.from = from;
    }

    /**
     * @param filekey 下载的fileKey
     */
    public void setFilekey(String filekey) {
        this.filekey = filekey;
    }

    public boolean isThum() {
        return thum > 0;
    }

    /**
     * @param thum 是否下载缩略图
     */
    public void setThum(int thum) {
        this.thum = thum;
    }

    public String getFilePath() {
        return filePath;
    }

    /**
     * 设置下载目录路径
     *
     * @param filePath 下载目录路径
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public List<IDownloadListener> getListeners() {
        synchronized (listeners) {
            return Collections.unmodifiableList(new ArrayList<>(listeners));
        }
    }

    /**
     * 设置下载监听器
     *
     * @param listener 下载监听器
     */
    public void addListener(IDownloadListener listener) {
        synchronized (listeners) {
            this.listeners.add(listener);
        }
    }

    /**
     * 设置请求标签
     *
     * @param tag
     */
    public void setTag(Object tag) {
        this.tag = tag;
    }

    public boolean isThumRequest() {
        return thum > 0;
    }

    public String getFileName() {
        return fileName;
    }

    /**
     * 设置下载文件名
     *
     * @param fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilekey() {
        if (thum > 0) {
            return FileKeyHelper.getThumFileKey(filekey, thum);
        }
        return filekey;
    }

    @Override
    public String getId() {
        return getFilekey();
    }

    @Override
    public Object getTag() {
        return tag;
    }

    private Bucket bucket;

    /**
     * 设置下载空间
     *
     * @param bucket
     */
    public void setBucket(Bucket bucket) {
        this.bucket = bucket;
    }

    public Bucket getBucket() {
        if (bucket == null) {
            bucket = FileSDK.getBucket();
        }
        return bucket;
    }

    /**
     * 生成 AccessKey 作为检验
     *
     * @param accessTime
     * @return
     */
    public String getAccessKey(int accessTime) {
        return McFileMD5.getStrMd5(getBucket().getId() + bucket.getSalt() + getBucket().getRole() + getFilekey() + accessTime);
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

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
