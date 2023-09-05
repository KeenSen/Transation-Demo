package com.transation.demo.service;

import java.math.BigDecimal;

public interface TransactionService {

    void transfer(Long payerId, Long payeeId, BigDecimal amount);
}
