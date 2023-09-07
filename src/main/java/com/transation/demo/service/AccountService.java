package com.transation.demo.service;

import com.transation.demo.model.Account;

import java.math.BigDecimal;

public interface AccountService {

    Account queryById(Long id);

    void resetAccountBalance(Long accountId, BigDecimal amount);

    Account createBalance(String name);

    void reduceBalance(Long accountId, BigDecimal amount);

    void increaseBalance(Long accountId, BigDecimal amount);

    void reduceBalanceInRequiredPropagation(Long accountId, BigDecimal amount);

    void increaseBalanceInRequiredPropagation(Long accountId, BigDecimal amount);
}
