package com.transation.demo.controller;

import com.transation.demo.service.TransactionService;
import com.transation.demo.vo.TransferParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("transaction")
public class TransactionApi {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transfer")
    public void normalTransfer(@RequestBody TransferParam param) {
        transactionService.transfer(param.getPayerId(), param.getPayeeId(), param.getAmount());
    }

    /**
     * 外部没有事务，内部事务为required
     */
    @PostMapping("/transfer/required1")
    public void required1(@RequestBody TransferParam param) {
        transactionService.transfer1(param.getPayerId(), param.getPayeeId(), param.getAmount());
    }

    /**
     * 外部有事务，内部事务为required
     */
    @PostMapping("/transfer/required2")
    public void required2(@RequestBody TransferParam param) {
        transactionService.transfer2(param.getPayerId(), param.getPayeeId(), param.getAmount());
    }
}
