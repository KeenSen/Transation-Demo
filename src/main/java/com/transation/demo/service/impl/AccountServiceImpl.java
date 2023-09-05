package com.transation.demo.service.impl;

import com.transation.demo.model.Account;
import com.transation.demo.model.AccountRowMapper;
import com.transation.demo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Account createBalance(String name) {
        Account account = new Account();
        account.setName(name);
        account.setBalance(BigDecimal.ZERO);
        return createAccount(account);
    }

    @Override
    public void reduceBalance(Long accountId, BigDecimal amount) {
        Account account = queryById(accountId);
        if (Objects.isNull(account)) {
            throw new IllegalArgumentException("Account [" + accountId + "] is not exist, can not transfer");
        }
        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Account [" + accountId + "] balance is [" + account.getBalance() + "], is not able to deduct");
        }
        account.setBalance(account.getBalance().subtract(amount));
        updateAccount(account);
    }

    @Override
    public void increaseBalance(Long accountId, BigDecimal amount) {
        Account account = queryById(accountId);
        if (Objects.isNull(account)) {
            throw new IllegalArgumentException("Account [" + accountId + "] is not exist, can not transfer");
        }

        account.setBalance(account.getBalance().add(amount));
        updateAccount(account);
    }

    private Account queryById(Long id) {
        return jdbcTemplate.queryForObject("select * from account where id = ?", new AccountRowMapper(), id);
    }

    @Deprecated
    private Account createAccount(Account account) {
//        String sql = "INSERT INTO account (name, balance) VALUES (?, ?)";
//        List<Object> params = Arrays.asList(account.getName(), account.getBalance());
//        long pk = jdbcTemplate.update(sql, params.toArray());
//        account.setId(pk);
        return account;
    }

    private void updateAccount(Account account) {
        String sql = "update account set name = ?, balance  = ? where id = ?";
        List<Object> params = Arrays.asList(account.getName(), account.getBalance(), account.getId());
        jdbcTemplate.update(sql, params.toArray());
    }
}
