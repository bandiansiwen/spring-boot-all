package com.imp.all.demos.ThreadPoolTaskExecutor;

import com.imp.all.demos.ThreadPoolExecutor.CustomSupplier;
import com.imp.all.framework.common.pojo.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @author Longlin
 * @date 2021/3/29 11:01
 * @description
 */
@RestController
@Slf4j
public class AsyncController {

    @Resource
    AsyncService asyncService;

    @Resource
    ThreadPoolExecutor taskExecutor3;

    @GetMapping("/movies")
    public CommonResult<List<List<String>>> completableFutureTask() {
        // 开始时间
        long startTime = System.currentTimeMillis();
        // 开始执行大量异步任务
        List<String> words = Arrays.asList("F", "T", "S", "Z", "J", "C");
        List<CompletableFuture<List<String>>> completableFutureList = words.stream()
                .map(word -> asyncService.completableFutureTask(word))
                .collect(Collectors.toList());
        // CompletableFuture.join() 方法可以获取他们的结果并将结果连接起来
        List<List<String>> results = completableFutureList.stream().map(CompletableFuture::join).collect(Collectors.toList());
        // 打印结果以及运行花费时间
        log.info("Elapsed time: " + (System.currentTimeMillis() - startTime));
        return CommonResult.success(results);
    }

    //从上面的运行结果可以看出，当所有任务执行完成之后才返回结果。这种情况对应于我们需要返回结果给客户端请求的情况下，假如我们不需要返回任务执行结果给客户端的话呢？
    // 就比如我们上传一个大文件到系统，上传之后只要大文件格式符合要求我们就上传成功。普通情况下我们需要等待文件上传完毕再返回给用户消息，但是这样会很慢。
    // 采用异步的话，当用户上传之后就立马返回给用户消息，然后系统再默默去处理上传任务。
    // 这样也会增加一点麻烦，因为文件可能会上传失败，所以系统也需要一点机制来补偿这个问题，比如当上传遇到问题的时候，发消息通知用户。
    @GetMapping("/movies2")
    public CommonResult<?> completableTask() {
        // 开始时间
        long start = System.currentTimeMillis();
        // 开始执行大量异步任务
        List<String> words = Arrays.asList("F", "T", "S", "Z", "J", "C");
        words.forEach(word -> asyncService.completableFutureTaskWithoutResult(word));
        // 打印结果以及运行花费时间
        log.info("Elapsed time: " + (System.currentTimeMillis() - start));
        return CommonResult.success();
    }

    @GetMapping("/executor")
    public CommonResult<?> threadPoolExecutor() {
        long startTime = System.currentTimeMillis();
        List<Integer> integerList = Arrays.asList(0, 1);
        List<CompletableFuture<String>> collect = integerList.stream().map(
                i -> CompletableFuture.supplyAsync(new CustomSupplier(String.valueOf(i)), taskExecutor3)
//                i -> CompletableFuture.completedFuture(i.toString())
        ).collect(Collectors.toList());
        // WARNING: 当任务提交入线程池超过队列长度未进入时，以下代码不执行
        List<String> result = collect.stream().map(CompletableFuture::join).collect(Collectors.toList());
        log.info("Elapsed time: " + (System.currentTimeMillis() - startTime));
        return CommonResult.success(result);
    }
}
