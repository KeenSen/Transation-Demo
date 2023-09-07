package com.transation.demo.model;

import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountTransactionRowMapper implements RowMapper<AccountTransaction> {
    @Override
    public AccountTransaction mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Long id = resultSet.getLong("id");
        Long accountId = resultSet.getLong("account");
        BigDecimal amount = resultSet.getBigDecimal("amount");
        String description = resultSet.getString("description");
        AccountTransaction result = new AccountTransaction();
        result.setId(id);
        result.setAccountId(accountId);
        result.setAmount(amount);
        result.setDescription(description);
        return result;
    }
}
