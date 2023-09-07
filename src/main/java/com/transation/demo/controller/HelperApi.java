package com.transation.demo.controller;

import com.transation.demo.model.Account;
import com.transation.demo.model.AccountTransaction;
import com.transation.demo.service.AccountService;
import com.transation.demo.service.TransactionService;
import com.transation.demo.vo.TransferParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("util")
public class HelperApi {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @PostMapping("/reset-data")
    @Transactional
    public void resetData(@RequestBody TransferParam param) {
        accountService.resetAccountBalance(param.getPayerId(), param.getAmount());
        accountService.resetAccountBalance(param.getPayeeId(), param.getAmount());
        transactionService.clearTransactionRecords();
    }

    @GetMapping("/data-summary")
    public Map<String, Object> getDataSummary() {
        Map<String, Object> summary = new HashMap<>();
        List<Long> accountIds = new ArrayList<>();
        accountIds.add(101L);
        accountIds.add(201L);
        for (Long accountId : accountIds) {
            Account account = accountService.queryById(accountId);
            summary.put(account.getName(), account);
        }

        List<AccountTransaction> transactionRecords = transactionService.getAllTransactionRecords();
        summary.put("transactionRecords", transactionRecords);
        return summary;
    }
}
