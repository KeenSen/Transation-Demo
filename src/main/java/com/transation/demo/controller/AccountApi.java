package com.transation.demo.controller;

import com.transation.demo.model.Account;
import com.transation.demo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountApi {

    @Autowired
    private AccountService accountService;

    @PutMapping("/account/new")
    public String hello(@RequestParam("accountName") String accountName) {
        Account account = accountService.createBalance(accountName);
        return account.toString();
    }
}
