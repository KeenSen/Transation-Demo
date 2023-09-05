package com.transation.demo.service;

import com.transation.demo.model.Account;

import java.math.BigDecimal;

public interface AccountService {

    Account createBalance(String name);

    void reduceBalance(Long accountId, BigDecimal amount);

    void increaseBalance(Long accountId, BigDecimal amount);

}
