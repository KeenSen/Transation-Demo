package com.transation.demo.model;

import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountRowMapper implements RowMapper<Account> {

    @Override
    public Account mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        BigDecimal balance = resultSet.getBigDecimal("balance");
        Account account = new Account();
        account.setId(id);
        account.setName(name);
        account.setBalance(balance);
        return account;
    }

}
