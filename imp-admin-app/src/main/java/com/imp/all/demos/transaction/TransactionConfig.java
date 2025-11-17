package com.imp.all.demos.transaction;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Longlin
 * @date 2021/3/21 18:55
 * @description
 *
 * 声明式事务的实现机制
 *
 * 步骤一：启用事务管理和创建代理
 * 当你在配置类上使用 @EnableTransactionManagement 时，Spring 会自动注册必要的组件，其中最关键的是一个 AOP 顾问（Advisor），它包含一个 事务拦截器（TransactionInterceptor）。
 *
 * 步骤二：代理对象的创建
 * 1.当 Spring 容器启动时，它会扫描所有被 @Component、@Service 等注解的 Bean。
 * 2.如果发现某个 Bean 的类或方法上标有 @Transactional 注解，Spring 容器就会为该 Bean 创建一个代理对象。
 * 3.这个代理对象（可能是 JDK 动态代理，也可能是 CGLIB 代理）会包装原始的目标对象。
 *
 * 步骤三：方法调用与拦截流程
 * 这是整个机制的核心，我们通过一个流程图来清晰地展示整个过程：
 * 调用代理方法：当你的代码（调用者）调用一个 public 的、带有 @Transactional 注解的方法时，你实际上是在调用 Spring 创建的代理对象的方法，而不是原始目标对象的方法。
 * 拦截与事务决策：代理对象会将调用路由给 TransactionInterceptor。拦截器开始工作：
 * 它解析 @Transactional 注解的属性，创建一个 TransactionDefinition。
 * 根据定义中的传播行为，通过 PlatformTransactionManager 决定是创建一个新事务、加入现有事务，还是以非事务方式运行。
 * 在创建/加入事务时，PlatformTransactionManager 会与底层的数据源（如 DataSource）进行交互。对于 JDBC，这通常意味着从数据源获取一个 Connection，并设置其 autoCommit=false，然后将这个 Connection 绑定到当前线程（通过 ThreadLocal）。
 * 执行业务逻辑：如果事务准备就绪，拦截器会通过反射调用原始目标对象的业务方法。此时，业务方法中所有对数据库的操作（只要它们从同一数据源获取连接），都会使用这个绑定到线程的、开启了事务的连接。
 * 提交或回滚：业务方法执行完毕后，拦截器会根据执行结果决定最终行为：
 * 成功完成：调用 PlatformTransactionManager.commit()。管理器会提交数据库连接，并清理线程绑定。
 * 抛出异常：拦截器会捕获异常，检查该异常类型是否配置了需要回滚（默认对运行时异常和 Error 回滚），如果是，则调用 rollback() 进行回滚。
 */
@Configuration
@EnableTransactionManagement
public class TransactionConfig {
}
