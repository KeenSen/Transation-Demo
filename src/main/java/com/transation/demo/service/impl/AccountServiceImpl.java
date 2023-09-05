package com.transation.demo.service.impl;

import com.transation.demo.model.Account;
import com.transation.demo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class AccountServiceImpl implements AccountService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Account createBalance(String name) {
        Account account = new Account();
        account.setName(name);
        account.setBalance(BigDecimal.ZERO);
        return saveAccount(account);
    }

    private Account saveAccount(Account account) {
        String sql = "INSERT INTO account (name, balance) VALUES (?, ?)";
        List<Object> params = Arrays.asList(account.getName(), account.getBalance());
        long pk = jdbcTemplate.update(sql, params.toArray());
        account.setId(pk);
        return account;
    }
}
