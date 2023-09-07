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
        transactionService.transferRequired1(param.getPayerId(), param.getPayeeId(), param.getAmount());
    }

    /**
     * 外部有事务，内部事务为required
     */
    @PostMapping("/transfer/required2")
    public void required2(@RequestBody TransferParam param) {
        transactionService.transferRequired2(param.getPayerId(), param.getPayeeId(), param.getAmount());
    }

    /**
     * 外部没有事务，内部事务为supports
     */
    @PostMapping("/transfer/supports1")
    public void supports1(@RequestBody TransferParam param) {
        transactionService.transferSupports1(param.getPayerId(), param.getPayeeId(), param.getAmount());
    }

    /**
     * 外部有事务，内部事务为supports
     */
    @PostMapping("/transfer/supports2")
    public void supports2(@RequestBody TransferParam param) {
        transactionService.transferSupports2(param.getPayerId(), param.getPayeeId(), param.getAmount());
    }

    /**
     * 外部没有事务，内部事务为mandatory
     */
    @PostMapping("/transfer/mandatory1")
    public void mandatory1(@RequestBody TransferParam param) {
        transactionService.transferMandatory1(param.getPayerId(), param.getPayeeId(), param.getAmount());
    }

    /**
     * 外部存在事务，内部事务为mandatory
     */
    @PostMapping("/transfer/mandatory2")
    public void mandatory2(@RequestBody TransferParam param) {
        transactionService.transferMandatory2(param.getPayerId(), param.getPayeeId(), param.getAmount());
    }

    /**
     * 外部方法有事务，内部事务为RequiredNew， 外部方法抛出异常
     */
    @PostMapping("/transfer/required-new1")
    public void requiredNew1(@RequestBody TransferParam param) {
        transactionService.transferRequiredNew1(param.getPayerId(), param.getPayeeId(), param.getAmount());
    }

    /**
     * 外部方法有事务，内部事务为RequiredNew， 内部方法抛出异常
     */
    @PostMapping("/transfer/required-new2")
    public void requiredNew2(@RequestBody TransferParam param) {
        transactionService.transferRequiredNew2(param.getPayerId(), param.getPayeeId(), param.getAmount());
    }
}
