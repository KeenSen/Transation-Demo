package com.transation.demo.service.impl;

import com.transation.demo.model.AccountTransaction;
import com.transation.demo.service.AccountService;
import com.transation.demo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void transfer(Long payerId, Long payeeId, BigDecimal amount) {
        accountService.reduceBalance(payerId, amount);
        accountService.increaseBalance(payeeId, amount);

        AccountTransaction payerRecord = new AccountTransaction();
        payerRecord.setAccountId(payerId);
        payerRecord.setAmount(amount.negate());
        payerRecord.setDescription("付款方");
        createTransactionRecord(payerRecord);

        AccountTransaction payeeRecord = new AccountTransaction();
        payeeRecord.setAccountId(payeeId);
        payeeRecord.setAmount(amount);
        payeeRecord.setDescription("收款方");
        createTransactionRecord(payeeRecord);
    }

    public void createTransactionRecord(AccountTransaction transaction) {
        String sql = "insert into account_transaction  (account_id, amount, description) values (?,?,?)";
        List<Object> params = Arrays.asList(transaction.getAccountId(), transaction.getAmount(), transaction.getDescription());
        jdbcTemplate.update(sql, params.toArray());
    }

}
