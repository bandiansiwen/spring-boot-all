package com.imp.all.demos.transaction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author Longlin
 * @date 2021/3/29 10:51
 * @description
 */
@Service
@Slf4j
public class TransactionService {

    @Autowired
    private TransactionTemplate transactionTemplate;

    public void sout() {

        log.info("======= A ========");

        TransactionCallback<Integer> transactionCallback = new TransactionCallback<Integer>() {

            @Override
            public Integer doInTransaction(TransactionStatus status) {
                // int xxx = jdbcTemplate.update
                int xxx = 0;
                if (xxx == 1) {
                    status.setRollbackOnly();
                    return -1;
                }
                return 1;
            }
        };
        // 业务中知道是否执行成功
        Integer result = transactionTemplate.execute(transactionCallback);
    }

    @Autowired
    private PlatformTransactionManager transactionManager;

    public void doSomething() {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            // ... 你的业务逻辑
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
    }
}
