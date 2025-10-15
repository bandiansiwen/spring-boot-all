package com.imp.all;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.imp.all.entity.Teacher;
import com.imp.all.framework.common.pojo.CommonResult;
import com.imp.all.framework.web.holder.ApplicationContextHolder;
import com.imp.all.framework.web.log.annotation.HiddenField;
import com.imp.all.framework.web.log.annotation.HiddenMode;
import com.imp.all.framework.web.util.ImpPathUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author Longlin
 * @date 2021/3/15 12:36
 * @description
 */
@RestController
@RequestMapping("/root")
public class RootController {

    @Value("${common.root:123}")
    private String rootStr;

    @GetMapping("/")
    public void main(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        request.getRequestDispatcher("/root/ok").forward(request,response);  // 转发
        response.sendRedirect("/root/ok");  // 重定向
    }

    @SentinelResource("RootOk")
    @GetMapping("/ok")
//    @RateLimit(permitsPerSecond = 5, timeout = 5000)
//    @ApiLimit(count = 3, period = 5)
    public CommonResult<?> ok() {
        return CommonResult.success("ok:" + rootStr);
    }

    @GetMapping("/shutdown")
    public void shutdown() {
        // 调用一个SpringApplication.exit(）方法也可以退出程序
        ApplicationContextHolder.shutDownContext();
    }


    @PostMapping("/insert")
    public String insert(@RequestBody Teacher teacher, @RequestParam String aa) {
        return "OK";
    }

    @GetMapping("/insert2")
    public String insert2(@RequestParam String username, @RequestParam @HiddenField(type = HiddenMode.PASSWORD) String password) {
        return "OK:" + username;
    }

    @PostMapping("/insert3") // form-data 形式传参
    public String insert3(@RequestParam String username, @RequestParam @HiddenField(type = HiddenMode.PASSWORD) String password) {
//        throw new RuntimeException("我屮艸芔茻");
        String path = ImpPathUtil.getPath();
        int i = 12/0;
        return "OK:" + username;
    }

    @PostMapping("/post")
    public String testPost() {
        return "post OK!";
    }

    @GetMapping("/copy")
    public CommonResult<?> copy() throws CloneNotSupportedException, IOException, ClassNotFoundException {
        // 测试深拷贝浅拷贝
        TestObj obj = new TestObj();
        obj.setAge(10);
        obj.setName("战三");
        obj.setId("admin");
//        obj.initUser();
        TestObj obj2 = new TestObj();
        BeanUtils.copyProperties(obj, obj2);
        TestObj obj3 = (TestObj)obj.clone();
        // 不实现 Serializable 接口会报错
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        //清空输出流
        objectOutputStream.flush();
        //释放资源
        objectOutputStream.close();
        //将 得到的序列化字节 丢进 缓冲区
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        //反序列化流 （输入流）--> 表示着从 一个 源头 读取 数据 ， （读取 缓冲区中 的字节）
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        //反序化成 一个对象
        TestObj obj4 = (TestObj)objectInputStream.readObject();

        return CommonResult.success(obj2);
    }
}
