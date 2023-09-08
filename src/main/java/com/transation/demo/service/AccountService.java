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

    void reduceBalanceInSupportsPropagation(Long accountId, BigDecimal amount);

    void increaseBalanceInSupportsPropagation(Long accountId, BigDecimal amount);

    void reduceBalanceInMandatoryPropagation(Long accountId, BigDecimal amount);

    void increaseBalanceInMandatoryPropagation(Long accountId, BigDecimal amount);

    void transferInRequiredNewPropagation(Long fromAccountId, Long toAccountId, BigDecimal amount, boolean throwException);

    void transferInNotSupportPropagation(Long fromAccountId, Long toAccountId, BigDecimal amount);

    void transferInNeverPropagation(Long fromAccountId, Long toAccountId, BigDecimal amount);
}
