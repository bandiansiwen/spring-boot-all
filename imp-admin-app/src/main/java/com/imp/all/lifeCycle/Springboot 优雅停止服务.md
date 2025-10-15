# Spring Boot 优雅停止服务的四种方法

## 1. Spring Boot 提供的 actuator 的功能

第一种就是Springboot提供的actuator的功能，它可以执行shutdown, health, info等，默认情况下，actuator的shutdown是disable的，我们需要打开它。

首先引入acturator的maven依赖：
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```
然后将shutdown节点打开，也将/actuator/shutdown暴露web访问也设置上，除了shutdown之外还有health, info的web访问都打开的话将management.endpoints.web.exposure.include=*就可以。将如下配置设置到application.properties里边。设置一下服务的端口号为8888。
```yaml
server.port=8888
management.endpoint.shutdown.enabled=true
management.endpoints.web.exposure.include=shutdown
```
在启动类里边输出一个启动日志，当工程启动的时候，会看到启动的输出，接下来咱们执行停止命令。
```
curl -X POST http://localhost:8888/actuator/shutdown
```

## 2. 通过调用SpringApplication.exit(）方法

第二种方法，通过调用一个SpringApplication.exit(）方法也可以退出程序，同时将生成一个退出码，这个退出码可以传递给所有的context。这个就是一个JVM的钩子，通过调用这个方法的话会把所有PreDestroy的方法执行并停止，并且传递给具体的退出码给所有Context。通过调用System.exit(exitCode)可以将这个错误码也传给JVM。程序执行完后最后会输出：Process finished with exit code 0，给JVM一个SIGNAL。

```java
@SpringBootApplication
public class AdminApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContextHolder.applicationContext = SpringApplication.run(AdminApplication.class, args);
        exitApplication(ctx);
    }

    public static void exitApplication(ConfigurableApplicationContext context) {
        int exitCode = SpringApplication.exit(context, (ExitCodeGenerator) () -> 0);

        System.exit(exitCode);
    }
}
```

## 3. 通过 kill 进程号

第三种方法，在springboot启动的时候将进程号写入一个app.pid文件，生成的路径是可以指定的，可以通过命令  cat /Users/huangqingshi/app.id | xargs kill 命令直接停止服务，这个时候bean对象的PreDestroy方法也会调用的。这种方法大家使用的比较普遍。写一个start.sh用于启动springboot程序，然后写一个停止程序将服务停止。

```
SpringApplication application = new SpringApplication(ShutdowndemoApplication.class);
application.addListeners(new ApplicationPidFileWriter("/Users/huangqingshi/app.pid"));
application.run();
```

## 4. 利用 ConfigurableApplicationContext close 方法

**不建议使用** (因为直接调用 ConfigurableApplicationContext close 方法程序会报错)

第四种方法就是获取程序启动时候的context，然后关闭主程序启动时的context。这样程序在关闭的时候也会调用PreDestroy注解。如下方法在程序启动十秒后进行关闭。

```java
@SpringBootApplication
public class AdminApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContextHolder.applicationContext = SpringApplication.run(AdminApplication.class, args);
        try {
            TimeUnit.SECONDS.sleep(10);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ctx.close();
    }
}
```

或者自己写一个Controller，然后将自己写好的Controller获取到程序的context，然后调用自己配置的Controller方法退出程序。通过调用自己写的/shutDownContext方法关闭程序：curl -X POST http://localhost:8888/shutDownContext。

```java
@RestController
public class ShutDownController implements ApplicationContextAware {

    private ApplicationContext context;

    @PostMapping("/shutDownContext")
    public String shutDownContext() {
        ConfigurableApplicationContext ctx = (ConfigurableApplicationContext) context;
        ctx.close();
        return"context is shutdown";
    }

    @GetMapping("/")
    public String getIndex() {
        return"OK";
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
```

