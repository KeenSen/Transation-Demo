package com.transation.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

// 请分析并给出  practice3()在由一个外部方法调用后(该外部方法无任务事务), db中的数据写入情况，以及事务传播、commit、roll back的过程

/**
 * 写入数据到表 table_a
 */
@Component
public class NestedPracticeService {
    @Autowired
    private NestedOuterService nestedOuterService;

    public void practice3() {
        writeTableAData();
        nestedOuterService.outerMethod();
    }

    private void writeTableAData() {
        // 写入一条 table_a 的数据
    }
}

/**
 * 写入数据到表table_b
 */
@Service
class NestedOuterService {
    @Autowired
    private NestedInnerService nestedInnerService;

    @Transactional
    public void outerMethod() {
        nestedInnerService.innerMethod();
        writeTableBData();
        throw new IllegalStateException("Exception from outer method");
    }

    private void writeTableBData() {
        // 写入一条 table_b 的数据
    }

}

/**
 * 写入table_c数据
 */
@Service
class NestedInnerService {

    @Transactional(propagation = Propagation.NESTED)
    public void innerMethod() {
        writeTableCData();
    }

    private void writeTableCData() {
        // 写入一条 table_c 的数据
    }
}