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
    void transfer1(Long payerId, Long payeeId, BigDecimal amount);

    /**
     * 外部有事务，内部传播行为为required
     */
    void transfer2(Long payerId, Long payeeId, BigDecimal amount);

    List<AccountTransaction> getAllTransactionRecords();
}
