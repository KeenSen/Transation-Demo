package com.transation.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

// 请分析并给出  practice2()在由一个外部方法调用后(该外部方法无任务事务), db中的数据写入情况，以及事务传播、commit、roll back的过程

/**
 * 写入数据到表 table_a
 */
@Component
public class RequiredNewPracticeService {

    @Autowired
    private RequiredNewOuterService requiredNewOuterService;

    public void practice2() {
        requiredNewOuterService.outerMethod();
    }
}

/**
 * 写入数据到表table_b
 */
@Service
class RequiredNewOuterService {
    @Autowired
    private RequiredNewInnerService requiredNewInnerService;

    @Transactional(noRollbackFor = IllegalStateException.class)
    public void outerMethod() {
        requiredNewInnerService.innerMethod();
        writeTableBData();
    }

    private void writeTableBData() {
        // 写入一条 table_b 的数据
    }

}

/**
 * 写入table_c数据
 */
@Service
class RequiredNewInnerService {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void innerMethod() {
        writeTableCData();
        throw new IllegalStateException("Exception in inner method");
    }

    private void writeTableCData() {
        // 写入一条 table_c 的数据
    }
}