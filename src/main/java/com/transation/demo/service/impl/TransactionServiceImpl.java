package com.transation.demo.service.impl;

import com.transation.demo.model.AccountRowMapper;
import com.transation.demo.model.AccountTransaction;
import com.transation.demo.model.AccountTransactionRowMapper;
import com.transation.demo.service.AccountService;
import com.transation.demo.service.TransactionService;
import com.transation.demo.utils.Panic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void clearTransactionRecords() {
        String sql = "delete from account_transaction";
        jdbcTemplate.update(sql);
    }

    @Override
    public void transfer(Long payerId, Long payeeId, BigDecimal amount) {
        accountService.reduceBalance(payerId, amount);
        accountService.increaseBalance(payeeId, amount);

        appendTransactionRecord(payerId, payeeId, amount);
    }

    private void appendTransactionRecord(Long payerId, Long payeeId, BigDecimal amount) {
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

    @Override
    public void transfer1(Long payerId, Long payeeId, BigDecimal amount) {
        accountService.reduceBalanceInRequiredPropagation(payerId, amount);
        accountService.increaseBalanceInRequiredPropagation(payeeId, amount);
        Panic.panicForRollBack();
        appendTransactionRecord(payerId, payeeId, amount);
    }

    @Override
    @Transactional
    public void transfer2(Long payerId, Long payeeId, BigDecimal amount) {
        accountService.reduceBalanceInRequiredPropagation(payerId, amount);
        accountService.increaseBalanceInRequiredPropagation(payeeId, amount);
        Panic.panicForRollBack();
        appendTransactionRecord(payerId, payeeId, amount);
    }

    @Override
    public List<AccountTransaction> getAllTransactionRecords() {
        List<String> allIds = jdbcTemplate.queryForList("select id from account_transaction", String.class);
        return allIds.stream().map(this::loadTransaction).collect(Collectors.toList());
    }

    private AccountTransaction loadTransaction(String id) {
        return jdbcTemplate.queryForObject("select * from account_transaction where id = ?", new AccountTransactionRowMapper(), id);
    }
}
