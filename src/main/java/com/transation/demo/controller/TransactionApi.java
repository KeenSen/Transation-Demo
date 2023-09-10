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

    /**
     * 外部方法无事务，内部事务为NotSupported， 内部方法抛出异常
     */
    @PostMapping("/transfer/not-support1")
    public void notSupport1(@RequestBody TransferParam param) {
        transactionService.transferNotSupported1(param.getPayerId(), param.getPayeeId(), param.getAmount());
    }

    /**
     * 外部方法有事务，内部事务为NotSupported， 内部方法抛出异常
     */
    @PostMapping("/transfer/not-support2")
    public void notSupport2(@RequestBody TransferParam param) {
        transactionService.transferNotSupported2(param.getPayerId(), param.getPayeeId(), param.getAmount());
    }

    /**
     * 外部方法无事务，内部事务为Never
     */
    @PostMapping("/transfer/never1")
    public void never1(@RequestBody TransferParam param) {
        transactionService.transferNever1(param.getPayerId(), param.getPayeeId(), param.getAmount());
    }

    /**
     * 外部方法有事务，内部事务为Never
     */
    @PostMapping("/transfer/never2")
    public void never2(@RequestBody TransferParam param) {
        transactionService.transferNever2(param.getPayerId(), param.getPayeeId(), param.getAmount());
    }

    /**
     * 外部方法有事务，内部事务为回滚的Nested，外部方法提交事务
     */
    @PostMapping("/transfer/nested1")
    public void nested1(@RequestBody TransferParam param) {
        transactionService.transferNested1(param.getPayerId(), param.getPayeeId(), param.getAmount());
    }

    /**
     * 外部方法有事务，内部事务为回滚的Nested，外部方法回滚事务
     */
    @PostMapping("/transfer/nested2")
    public void nested2(@RequestBody TransferParam param) {
        transactionService.transferNested2(param.getPayerId(), param.getPayeeId(), param.getAmount());
    }

    /**
     * 外部方法有事务，内部事务为正常提交的Nested，外部方法回滚事务
     */
    @PostMapping("/transfer/nested3")
    public void nested3(@RequestBody TransferParam param) {
        transactionService.transferNested3(param.getPayerId(), param.getPayeeId(), param.getAmount());
    }
}
