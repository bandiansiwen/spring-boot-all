package com.imp.all.controller;

import com.imp.all.entity.Teacher;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Longlin
 * @date 2021/10/11 13:56
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/redisson")
public class RedissonController {

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 测试 redis 修改密码的时效性，即改即生效
     *
     * 查看redis密码：
     * config get requirepass
     *
     * 设置redis密码：
     * config set requirepass 123456
     */

    @GetMapping("/set")
    public String set() {redisTemplate.opsForValue().set("value", 123, 30, TimeUnit.MINUTES);
        return "set ok";
    }

    @GetMapping("/get")
    public String get() {
        Object value = redisTemplate.opsForValue().get("value");
        return "get ok：" + value;
    }

    @GetMapping("/hello")
    public String hello() {
        // 可重入锁
        // 拥有看门狗机制，锁自动续期
        // 1. 获取一把锁
        RLock lock = redissonClient.getLock("my-lock");
        // 2. 加锁
        lock.lock();
        // 1)、锁的自动续期，如果业务超长，运行期间自动给锁续上新的30s。不用担心业务时间长，锁自动过期被删掉
        // 2)、加锁的业务只要运行完成，就不会给当前锁续期，即使不手动解锁，锁默认在30s后自动删除

        // lock指定了时间就不会有自动续期

        // 尝试加锁，最多等待100秒，上锁以后10秒自动解锁
        // boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);

        try {
            System.out.println("加锁成功，执行业务" + Thread.currentThread().getId());
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 3. 解锁
            System.out.println("释放锁" + Thread.currentThread().getId());
            // 判断是否当前线程持有锁，再释放
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return "hello";
    }

    // 读写锁（ReadWriteLock）
    // 写锁是排它锁；读锁是一个共享锁
    // 写锁没释放读锁就必须等待
    // 读 + 读：相当于无锁,只会在redis中记录好所有的读锁，他们都会同时加锁成功
    // 写 + 读：等待写锁释放
    // 写 + 写：阻塞方式
    // 读 + 写：有读锁，写也需要等待
    // 只要有写的存在，都必须等待
    @GetMapping("/write")
    public String write() {
        RReadWriteLock lock = redissonClient.getReadWriteLock("rw-lock");
        String s = "";
        RLock rLock = lock.writeLock();
        // 1. 改数据加写锁，读数据加读锁
        rLock.lock();
        try {
            System.out.println("写锁加锁成功..." + Thread.currentThread().getId());
            s = UUID.randomUUID().toString();
            Thread.sleep(30000);
            redisTemplate.opsForValue().set("writeValue", s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (rLock.isLocked() && rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
            System.out.println("写锁释放" + Thread.currentThread().getId());
        }
        return s;
    }

    @GetMapping("/read")
    public String read() {
        RReadWriteLock lock = redissonClient.getReadWriteLock("rw-lock");
//        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
        String s = "";
        RLock rLock = lock.readLock();
        // 1. 改数据加写锁，读数据加读锁
        rLock.lock();
        try {
            System.out.println("读锁加锁成功..." + Thread.currentThread().getId());
            s = (String) redisTemplate.opsForValue().get("writeValue");
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (rLock.isLocked() && rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
            System.out.println("读锁释放" + Thread.currentThread().getId());
        }
        return s;
    }

    /**
     * 信号量（Semaphore）
     * 可以用作分布式限流
     * 车库停车：
     * 3车位
     */
    @GetMapping("/park")
    public String park() throws InterruptedException {
        RSemaphore park = redissonClient.getSemaphore("park");
        park.acquire(); // 阻塞式获取 获取一个信号，获取一个值（占一个车位）
//        boolean b = park.tryAcquire(); // 非阻塞式
        return  "park ok";
    }

    @GetMapping("/go")
    public String go() {
        RSemaphore park = redissonClient.getSemaphore("park");
        park.release(); // 释放一个信号
        return  "go ok";
    }

    /**
     * 闭锁（CountDownLatch）
     */
    @GetMapping("/lockDoor")
    public String lockDoor() throws InterruptedException {
        RCountDownLatch door = redissonClient.getCountDownLatch("door");
        door.trySetCount(5);
        door.await();   // 等待闭锁完成
        return  "放假了";
    }

    @GetMapping("/gogogo/{id}")
    public String gogogo(@PathVariable("id") Long id) {
        RCountDownLatch door = redissonClient.getCountDownLatch("door");
        door.countDown(); // 计数
        return id + "班的人都走了";
    }

    /**
     * 布隆过滤器
     * 布隆过滤器二进制数组，如何处理删除？
     * 初始化后的布隆过滤器，可以直接拿来使用了。但是如果原始数据删除了怎么办？布隆过滤器二进制数组如何维护？
     *
     * 直接删除不行吗？
     * 还真不行！因为这里面有Hash冲突的可能，会导致误删。
     *
     * 怎么办？
     * 方案1：开发定时任务，每隔几个小时，自动创建一个新的布隆过滤器数组，替换老的，有点CopyOnWriteArrayList的味道
     * 方案2：布隆过滤器增加一个等长的数组，存储计数器，主要解决冲突问题，每次删除时对应的计数器减一，如果结果为0，更新主数组的二进制值为0
     *
     * 应用场景
     * 1.解决缓存穿透
     * 2.网页爬虫对URL的去重，避免爬取相同的URL地址
     * 3.反垃圾邮件，从数十亿个垃圾邮件列表中判断某邮箱是否垃圾邮箱
     */
    @GetMapping("/bloomFilter")
    public String bloomFilter() {
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter("test5-bloom-filter");
        // 初始化布隆过滤器，数组长度100W，误判率 1%
        bloomFilter.tryInit(1000000L, 0.01);
        // 添加数据
        bloomFilter.add("Tom哥");
        // 判断是否存在
        log.info(String.valueOf(bloomFilter.contains("微观技术")));  // false   // 肯定不存在
        log.info(String.valueOf(bloomFilter.contains("Tom哥")));    // true    // 可能存在，有1%的误判率
        return "success";
    }

    /**
     * 分布式对象
     */
    @GetMapping("/bucket")
    public void bucket() {
        RBucket<AtomicInteger> bucket = redissonClient.getBucket("anyObject");
        bucket.set(new AtomicInteger(1));
        AtomicInteger obj = bucket.get();

        bucket.trySet(new AtomicInteger(3));
        bucket.compareAndSet(new AtomicInteger(4), new AtomicInteger(5));
        bucket.getAndSet(new AtomicInteger(6));

        RBuckets buckets = redissonClient.getBuckets();
        Map<String, AtomicInteger> loadedBuckets = buckets.get("myBucket1", "myBucket2", "myBucket3");

        Map<String, Object> map = new HashMap<>();
        map.put("myBucket1", new Teacher());
        map.put("myBucket2", new Teacher());

        // 利用Redis的事务特性，同时保存所有的通用对象桶，如果任意一个通用对象桶已经存在则放弃保存其他所有数据。
        buckets.trySet(map);
        // 同时保存全部通用对象桶。
        buckets.set(map);
    }
}
