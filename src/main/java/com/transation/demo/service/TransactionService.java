package com.transation.demo.service;

import com.transation.demo.model.AccountTransaction;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {

    void clearTransactionRecords();

    void transfer(Long payerId, Long payeeId, BigDecimal amount);

    /**
     * 外部没有事务，内部传播行为为required
     */
    void transferRequired1(Long payerId, Long payeeId, BigDecimal amount);

    /**
     * 外部有事务，内部传播行为为required
     */
    void transferRequired2(Long payerId, Long payeeId, BigDecimal amount);

    /**
     * 外部方法无事务，内部传播行为为supports
     */
    void transferSupports1(Long payerId, Long payeeId, BigDecimal amount);

    /**
     * 外部方法有事务，内部传播行为为supports
     */
    void transferSupports2(Long payerId, Long payeeId, BigDecimal amount);

    /**
     * 外部方法没有事务，内部传播行为为mandatory
     */
    void transferMandatory1(Long payerId, Long payeeId, BigDecimal amount);

    /**
     * 外部方法存在事务，内部传播行为为mandatory
     */
    void transferMandatory2(Long payerId, Long payeeId, BigDecimal amount);

    List<AccountTransaction> getAllTransactionRecords();
}
