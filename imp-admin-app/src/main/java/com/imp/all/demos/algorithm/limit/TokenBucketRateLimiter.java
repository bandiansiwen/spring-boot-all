package com.imp.all.demos.algorithm.limit;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 令牌桶限流器 - 支持非阻塞和阻塞获取令牌
 */
public class TokenBucketRateLimiter {
    // 桶容量大小
    private final long capacity;
    // 令牌生成速率（每秒生成的令牌数）
    private final double refillRate;
    // 每个令牌的生成间隔（纳秒），等于 1秒对应的纳秒 除以 令牌生成速率
    private final long refillIntervalNanos;

    // 可用令牌大小
    private double tokens;
    // 最后刷新时间（纳秒）
    private long lastRefillTime;

    // 锁和条件变量，用于阻塞等待
    private final ReentrantLock lock;
    private final Condition condition;

    /**
     * 构造方法
     */
    public TokenBucketRateLimiter(long capacity, double refillRate) {
        if (capacity <= 0 || refillRate <= 0) {
            throw new IllegalArgumentException("容量和生成速率必须大于0");
        }

        this.capacity = capacity;
        this.refillRate = refillRate;
        this.refillIntervalNanos = (long) (1_000_000_000.0 / refillRate);
        this.tokens = capacity;
        this.lastRefillTime = System.nanoTime();
        this.lock = new ReentrantLock();
        this.condition = lock.newCondition();
    }

    /**
     * 非阻塞方式尝试获取一个令牌
     * @return 成功获取返回true，否则返回false
     */
    public boolean tryAcquire() {
        return tryAcquire(1);
    }

    /**
     * 非阻塞方式尝试获取指定数量的令牌
     * @param permits 请求的令牌数量
     * @return 成功获取返回true，否则返回false
     */
    public boolean tryAcquire(long permits) {
        if (permits <= 0) {
            throw new IllegalArgumentException("请求的令牌数必须大于0");
        }
        if (permits > capacity) {
            return false; // 请求超过桶容量，直接拒绝
        }

        lock.lock();
        try {
            // 重新补充令牌
            refillTokens();

            if (tokens >= permits) {
                tokens -= permits;
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 阻塞方式获取一个令牌
     * @throws InterruptedException 如果等待过程中线程被中断
     */
    public void acquire() throws InterruptedException {
        acquire(1);
    }

    /**
     * 阻塞方式获取指定数量的令牌（无限等待）
     * @param permits 请求的令牌数量
     * @throws InterruptedException 如果等待过程中线程被中断
     */
    public void acquire(long permits) throws InterruptedException {
        if (permits <= 0) {
            throw new IllegalArgumentException("请求的令牌数必须大于0");
        }
        if (permits > capacity) {
            throw new IllegalArgumentException("请求的令牌数超过桶容量");
        }

        lock.lock();
        try {
            while (tokens < permits) {
                // 计算需要等待的时间
                double neededTokens = permits - tokens;
                long waitNanos = (long) (neededTokens * refillIntervalNanos);

                // 等待直到有足够令牌或超时
                condition.awaitNanos(waitNanos);
                // 重新补充令牌
                refillTokens();
            }
            tokens -= permits;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 补充令牌（必须在锁内调用）
     */
    private void refillTokens() {
        long now = System.nanoTime();
        // 计算需要补充的令牌个数
        double tokensToAdd = (now - lastRefillTime) / (double) refillIntervalNanos;

        if (tokensToAdd > 0) {
            // 可用令牌大小，最大不能超过桶容量大小
            tokens = Math.min(capacity, tokens + tokensToAdd);
            lastRefillTime = now;

            // 如果有等待的线程，通知它们
            if (tokens > 0) {
                condition.signalAll();
            }
        }
    }
}
