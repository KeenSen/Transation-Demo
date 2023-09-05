package com.transation.demo.controller;

import com.transation.demo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("transaction")
public class TransactionApi {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transfer")
    public void hello(@RequestParam("payerId") Long payerId, @RequestParam("payeeId") Long payeeId,
                      @RequestParam("amount") BigDecimal amount) {
        transactionService.transfer(payerId, payeeId, amount);
    }
}
