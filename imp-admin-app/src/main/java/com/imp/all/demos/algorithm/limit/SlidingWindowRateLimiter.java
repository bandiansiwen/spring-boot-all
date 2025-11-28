package com.imp.all.demos.algorithm.limit;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 滑动窗口限流器 - 支持非阻塞和阻塞获取
 */
public class SlidingWindowRateLimiter {

    // 时间窗口大小（毫秒）
    private final long windowSizeMs;
    // 窗口内允许的最大请求数
    private final int maxRequests;
    // 格子数量
    private final int slotCount;
    // 每个格子的时间长度（毫秒）
    private final long slotSizeMs;

    // 滑动窗口数组，每个元素代表一个时间格子的请求计数
    private final AtomicInteger[] slots;
    // 每个格子对应的开始时间（毫秒）
    private final long[] slotStartTimes;
    // 当前窗口的总请求数
    private final AtomicInteger totalRequests;

    // 锁和条件变量，用于阻塞等待
    private final ReentrantLock lock;
    private final Condition condition;

    /**
     * 构造函数
     * @param maxRequests 时间窗口内允许的最大请求数
     * @param windowSizeMs 时间窗口大小（毫秒）
     * @param slotCount 窗口分割的格子数量
     */
    public SlidingWindowRateLimiter(int maxRequests, long windowSizeMs, int slotCount) {
        if (maxRequests <= 0 || windowSizeMs <= 0 || slotCount <= 0) {
            throw new IllegalArgumentException("参数必须大于0");
        }

        this.maxRequests = maxRequests;
        this.windowSizeMs = windowSizeMs;
        this.slotCount = slotCount;
        this.slotSizeMs = windowSizeMs / slotCount;

        // 初始化滑动窗口
        this.slots = new AtomicInteger[slotCount];
        this.slotStartTimes = new long[slotCount];
        this.totalRequests = new AtomicInteger(0);

        long currentTime = System.currentTimeMillis();
        for (int i = 0; i < slotCount; i++) {
            slots[i] = new AtomicInteger(0);
            slotStartTimes[i] = currentTime - (slotCount - i) * slotSizeMs;
        }

        this.lock = new ReentrantLock();
        this.condition = lock.newCondition();
    }

    /**
     * 非阻塞方式尝试获取一个请求许可
     * @return 成功获取返回true，否则返回false
     */
    public boolean tryAcquire() {
        return tryAcquire(1);
    }

    /**
     * 非阻塞方式尝试获取指定数量的请求许可
     * @param permits 请求的许可数量
     * @return 成功获取返回true，否则返回false
     */
    public boolean tryAcquire(int permits) {
        if (permits <= 0) {
            throw new IllegalArgumentException("请求的许可数必须大于0");
        }
        if (permits > maxRequests) {
            return false; // 请求超过窗口容量，直接拒绝
        }

        lock.lock();
        try {
            // 滑动窗口，更新过期的格子
            slideWindow();

            // 检查当前窗口是否超过限制
            if (totalRequests.get() + permits <= maxRequests) {
                // 获取当前时间对应的格子索引
                long currentTime = System.currentTimeMillis();
                int currentSlotIndex = getCurrentSlotIndex(currentTime);

                // 更新当前格子的计数和总计数
                slots[currentSlotIndex].addAndGet(permits);
                totalRequests.addAndGet(permits);
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 阻塞方式获取一个请求许可（无限等待）
     * @throws InterruptedException 如果等待过程中线程被中断
     */
    public void acquire() throws InterruptedException {
        acquire(1);
    }

    /**
     * 阻塞方式获取指定数量的请求许可（无限等待）
     * @param permits 请求的许可数量
     * @throws InterruptedException 如果等待过程中线程被中断
     */
    public void acquire(int permits) throws InterruptedException {
        if (permits <= 0) {
            throw new IllegalArgumentException("请求的许可数必须大于0");
        }
        if (permits > maxRequests) {
            throw new IllegalArgumentException("请求的许可数超过窗口容量");
        }

        lock.lock();
        try {
            while (!tryAcquireInternal(permits)) {
                // 计算需要等待的时间（直到下一个格子过期）
                long waitTimeMs = calculateWaitTime(permits);
                if (waitTimeMs > 0) {
                    condition.await(waitTimeMs, TimeUnit.MILLISECONDS);
                } else {
                    condition.await();
                }
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 内部尝试获取方法（必须在锁内调用）
     */
    private boolean tryAcquireInternal(int permits) {
        slideWindow();

        if (totalRequests.get() + permits <= maxRequests) {
            long currentTime = System.currentTimeMillis();
            int currentSlotIndex = getCurrentSlotIndex(currentTime);

            slots[currentSlotIndex].addAndGet(permits);
            totalRequests.addAndGet(permits);
            return true;
        }
        return false;
    }

    /**
     * 滑动窗口，清理过期的格子
     */
    private void slideWindow() {
        long currentTime = System.currentTimeMillis();
        long windowStartTime = currentTime - windowSizeMs;

        for (int i = 0; i < slotCount; i++) {
            // 如果格子开始时间在窗口开始时间之前，说明这个格子已过期
            if (slotStartTimes[i] < windowStartTime) {
                int expiredCount = slots[i].getAndSet(0);
                if (expiredCount > 0) {
                    totalRequests.addAndGet(-expiredCount);
                    // 更新格子开始时间为当前周期开始时间
                    slotStartTimes[i] = currentTime - (slotCount - i - 1) * slotSizeMs;
                }
            }
        }

        // 如果有等待的线程，通知它们
        if (totalRequests.get() < maxRequests) {
            condition.signalAll();
        }
    }

    /**
     * 获取当前时间对应的格子索引
     */
    private int getCurrentSlotIndex(long currentTime) {
        return (int) ((currentTime / slotSizeMs) % slotCount);
    }

    /**
     * 计算需要等待的时间（毫秒）
     */
    private long calculateWaitTime(int permits) {
        long currentTime = System.currentTimeMillis();
        long windowStartTime = currentTime - windowSizeMs;

        // 找到最早的非空格子
        long earliestSlotTime = Long.MAX_VALUE;
        for (int i = 0; i < slotCount; i++) {
            if (slots[i].get() > 0 && slotStartTimes[i] < earliestSlotTime) {
                earliestSlotTime = slotStartTimes[i];
            }
        }

        if (earliestSlotTime == Long.MAX_VALUE) {
            return 0; // 没有找到非空格子，不需要等待
        }

        // 计算最早格子过期的时间
        long earliestExpireTime = earliestSlotTime + windowSizeMs;
        long waitTime = earliestExpireTime - currentTime;

        // 考虑请求数量，可能需要等待更多时间
        int availableSlots = maxRequests - totalRequests.get();
        if (availableSlots <= 0) {
            waitTime = Math.max(waitTime, slotSizeMs);
        }

        return Math.max(0, waitTime);
    }

    /**
     * 获取当前窗口内的总请求数
     * @return 当前请求数
     */
    public int getCurrentRequests() {
        lock.lock();
        try {
            slideWindow();
            return totalRequests.get();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        SlidingWindowRateLimiter rateLimiter = new SlidingWindowRateLimiter(100, 10, 5);
        System.out.println("1");
    }
}
