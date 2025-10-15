package com.imp.all.file.policy;

/**
 * @author Longlin
 * @date 2024/2/17 8:56
 * @description
 */
public class RetryPolicy {

    public static final int DEFAULT_MAX_RETRY_COUNT = 3;
    public static final long DEFAULT_CALL_TIMEOUT = 20000L;
    public static final long DEFAULT_RETRY_DELAY = 2000L;

    public static final long SIZE_50M = 50L * 1024 * 1024;
    public static final long SIZE_4G = 4L * 1024 * 1024 * 1024;

    public static final long BLOCK_SIZE1 = 512L * 1024;
    public static final long BLOCK_SIZE2 = 64L * 1024;
    public static final long BLOCK_SIZE3 = 4L * 1024;

    public static long getCallTimeout() {
        return DEFAULT_CALL_TIMEOUT;
    }

    public static long getRetryDelay() {
        return DEFAULT_RETRY_DELAY;
    }

    public static int getMaxTryCount() {
        return DEFAULT_MAX_RETRY_COUNT;
    }

    public static long getPullDataBlockSize(long fileSize, int retryCount) {
        if (retryCount < 1) {
            return BLOCK_SIZE1;
        } else if (retryCount < 2) {
            return BLOCK_SIZE2;
        } else {
            return BLOCK_SIZE3;
        }
    }
}
