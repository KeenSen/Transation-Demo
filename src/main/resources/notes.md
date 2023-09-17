# 一、事务的基础概念
## 什么是事务
事务是一系列操作组成的工作单元，该工作单元内的操作是不可分割的，即要么所有操作都做，要么所有操作都不做，这就是事务。
## 事务的基本特性

1. 原子性

事务中的一系列操作要么全部成功，要么一个都不做

2. 一致性

事务执行的结果必须是使数据库从一个一致性状态变到另一个一致性状态。一致性与原子性是密切相关的。

3. 隔离性

一个事务的执行不能被其他事务干扰

4. 持久性

一个事务一旦提交，它对数据库中数据的改变就应该是永久性的
# 二、事务的传播行为
## 什么是事务的传播行为？
简单来说，就是两个支持事务的方法A,B，当方法A调用方法B时，方法B如何处理内部的事务。
## 提交/回滚对结果的影响
内外部方法在使用相同或不同的事务连接时，内外部方法的提交/回滚决定会有不一样的结果。

1. **内外部方法使用同一个事务**

内外部方法只要有一个回滚，那么内外部数据全都回滚。只有内外部方法全都提交时，内外部数据才会成功提交。
可以简单概括为：**“一荣俱荣，一损俱损”。**

2. **内外部使用不同的事务**

由于每个独立的事务使用一条数据库连接，所以此时内外部方法在两个数据库连接中，彼此互不影响。内外部数据可以独立提交/回滚**。**
可以简单概括为：**“各人自扫门前雪”。**
## Spring中@Transational的使用事项

- spring是通过处理exception决定是否回滚事务的
- 默认只针对 RuntimeException 和 Error进行回滚
- 如果需要增加 受检查异常（Checked Exception），需要在注解的rollBackForClassName中单独指出
- 对不回滚的异常，在注解的noRollBackForClassName单独指出
## 事务传播行为有哪些？
由于Spring中的事务管理使用较多，因此我们讨论在spring中，**TransactionDefinition **接口定义了七种事务传播行为，分别为：
### REQUIRED
如果当前没有事务，就创建一个新的事务，如果当前存在事务，就**加入该事务**。**是Spring默认的传播行为**。加入改事务意味着使用同一个数据库事务。
概括：“**有则加入，无则新建**”。
### SUPPORTS
如果当前存在事务，就加入该事务，如果当前不存在事务，就以非事务执行。
概括：“**有则加入，无则忽略**”
### MANDATORY
使用当前的事务，如果当前没有事务，就抛出异常。
概括：“**强制使用，否则报错**”
### REQUIRES_NEW
创建一个新的事务，如果当前存在事务，就把当前事务挂起。 这意味着内外部方法使用不同的数据库连接。
> **Tips: 两条不同的数据库连接，在某些情况下会存在锁竞争，因此该传播行为很容易使用不当引发Bug !!!**

### NOT_SUPPORTED
以非事务方式执行，如果当前存在事务，就把当前事务挂起。
### NEVER
以非事务方式执行，如果当前存在事务，就抛出异常。
概述：“**从不使用，有则报错**”
### NESTED
如果当前存在事务，就创建一个事务作为当前事务的嵌套事务来运行，如果当前没有事务，就按REQUIRED行为执行。
嵌套事务实际上就是父子事务，他们共用一个数据库连接，通过mysql的savepoint机制实现。
嵌套事务的特点

   1. 如果父事务回滚，则所有嵌套的子事务也会回滚。这是因为子事务是父事务的一部分，它们共享同一个事务上下文。当父事务回滚时，它会标记为回滚，这样所有嵌套的子事务都能感知到这个回滚，从而也会回滚。
   2. 如果父事务提交，则所有嵌套的子事务也会提交。这是因为子事务是父事务的一部分，它们共享同一个事务上下文。当父事务提交时，它会标记为提交，这样所有嵌套的子事务也能感知到这个提交，从而也会提交。
   3. 在进入子事务之前，父事务会建立一个回滚点，叫做savepoint。这是为了在需要时可以回滚到这个点而不影响父事务的其他部分。
   4. 当子事务执行结束时，父事务可以选择回滚到savepoint，然后尝试其他的事务或者其他的业务逻辑。父事务之前的操作不会受到影响，更不会自动回滚。
   5. 子事务的提交是依赖于父事务的。在父事务结束之前，子事务是不会提交的。这是为了保证父事务的所有嵌套子事务要么全部提交，要么全部回滚。
   6. 概括：父事务影响子事务，子事务不影响父事务。

savepoint的简单使用案例
```sql
-- 请在mysql client中逐条执行下列语句
USE transaction_demo;
SET autocommit = 0;
start transaction;
insert into save_point_demo(point)  VALUES('outer-point');
savepoint x;
INSERT into save_point_demo(point) VALUES('inner-point');
SELECT * FROM save_point_demo;
ROLLBACK TO x;
SELECT * FROM save_point_demo;
commit;
```
## 事务传播行为的作用
在实际业务开发中，针对不同模块、不同领域的数据进行CUD时，需要考虑异常情况下，不同数据的最终一致性。为了解决业务层方法之间互相调用的事务问题。
当一个事务方法被另一个事务方法调用时，必须制定事务应该如何传播。例如，在更新保单与财务数据时，会写入一条交易记录，成功时需要确保保单信息与财务信息同时写入成功，并且记录交易成功的日志，失败时保单数据与财务数据需要一起回滚至更新前的状态，同时记录交易失败的日志。
实际的业务需要会比该场景更复杂，因此需要考虑全面地考虑使用不同的事务传播行为。
# 三、事务传播行为Demo
> 定义说明
> - 外部数据：交易记录数据(accountTransaction)
> - 内部数据：账户余额信息(account)

## 针对Required传播行为
### 外部没有事务

- **接口**：/transfer/required1；
- **内部逻辑描述**： 先调用内部事务方法写入数据，然后无事务的外部方法抛出异常。
- **期望结果**：内部数据写入成功。因为外部抛出的异常，不会影响内部的事务。
### 外部存在事务

- **接口**：/transfer/required2
- **逻辑描述**： 先调用内部事务方法写入数据，然后有事务控制的外部方法抛出异常。
- **期望结果**：内部数据写入失败。因为内外部方法公用一个事务，外部抛出的异常，导致内部的数据也被回滚。
## 针对Supports传播行为
### 外部没有事务

- **接口：**/transfer/supports1
- **逻辑描述：**先写入外部数据，再写入内部数据，内部方法抛出异常。
- **期望结果：**由于外部方法没有事务，所以内部方法也没有事务，先行写入的数据不会因为异常而回滚。
### 外部存在事务

- **接口：**/transfer/supports2
- **逻辑描述：**先写入外部数据，再写入内部数据，内部方法抛出异常。
- **期望结果：**由于外部方法存在事务，所以内部方法会加入外部方法的事务，而内部抛出的异常，会将内外部方法写入的所有数据回滚。
## 针对MANDATORY 传播行为
### 外部没有事务

- **接口：**/transfer/mandatory1
- **逻辑描述：**先写入外部数据，再写入内部数据
- **期望结果：**由于外部方法没有事务，而内部方法要求调用时必须存在事务，所以会抛出异常，外部数据可以写入，内部数据无法写入
### 外部有事务

- **接口：**/transfer/mandatory2
- **逻辑描述：**先写入外部数据，再写入内部数据
- **期望结果：**由于外部方法存在事务，所以内部方法不会报错，最终，内外部数据都可以写入。
## 针对 REQUIRES_NEW 传播行为
> 内外部方法都有事务，需要分析的是内外部方法事务的独立性，内外部方法分别抛出异常时，对彼此的影响。

### 外部方法抛出异常

- **接口：**/transfer/required-new1
- **逻辑描述：**外部方法先写入数据，内部方法写入数据，然后在外部方法抛出异常
- **期望结果：**由于内外部方法的事务互相独立，所以外部方法抛出异常，并不会影响内部方法，最终内部方法的数据写入，外部方法的数据无法写入。
### 内部方法抛出异常

- **接口：**/transfer/required-new2
- **逻辑描述：**外部方法先写入数据，内部方法写入数据，然后在内部方法抛出异常
- **期望结果：**由于内部方法抛出异常，所以内部的事务会回滚，而外部事务因为异常也会回滚，最终，没有任务数据写入。在spring中存在一个情况，如果外部方法捕获并不抛出内部的异常，那么互不影响。但是，如果内部方法的异常被外部方法向上抛出，此时视为外部方法回滚。
## 针对 NOT_SUPPORTED 传播行为
> 内部方法始终不会开启事务，需要测试当内部方法抛出异常时，对外部的影响。因此，差异变量是外部方法是否有事务

### 外部方法无事务

- **接口：**not-support1
- **逻辑描述：**外部无事务方法先写入数据，内部方法写入数据，并抛出异常
- **期望结果：**由于外部方法无事务，内部方法始终以无事务的方式执行。数据无法回滚，最终内外部方法的数据均会写入。
### 外部方法有事务

- **接口：**not-support2
- **逻辑描述：**外部开启事务的方法先写入数据，内部方法写入数据，并抛出异常
- **期望结果：**外部数据回滚，内部数据写入。
##  针对 NEVER 的传播行为
> 差异变量为外部方法事务开启事务

### 外部方法无事务

- **接口：**never1
- **逻辑描述：**外部无事务方法先写入数据，内部方法写入数据
- **期望结果：**由于外部方法无事务，内部方法不会抛出异常，因此所有数据成功写入
### 外部方法有事务

- **接口：**never2
- **逻辑描述：**外部开启事务的方法先写入数据，内部方法写入数据
- **期望结果：**由于外部方法开启事务，内部方法在写入数据之前会报错，内部方法无法写入数据，而外部方法因为异常回滚，最终无任何数据写入。
## 针对NESTED 传播行为
> NESTED 在外部无事务时，变为REQUIRED的情况就不再测试，仅考虑内外部事务提交/回滚时，最终数据的提交情况。

### 内部报错，外部事务提交

- **接口： **nested1
- **逻辑描述： **外部方法先写入数据，内部方法写入数据，然后内部方法回滚报错，外部方法捕获异常，并不向上继续抛出，尝试提交事务。
- **期望结果：**内部数据回滚，外部数据提交
### 内部报错，外部事务回滚

- **接口:  **nested2
- **逻辑描述：**外部方法先写入数据，内部方法写入数据，内部方法回滚报错，外部方法不捕获异常，向上继续抛出，导致事务回滚
- **期望结果：**内部数据回滚，外部数据回滚
### 内部正常提交，外部事务报错并回滚

- **接口:  **nested3
- **逻辑描述：**外部方法先写入数据，内部方法写入数据，外部方法抛出异常，事务回滚
- **期望结果：**内部数据回滚，外部数据回滚
# 四、@Transaction 的常见错误
## Exception被吃掉了
> 只有单个使用事务的方法：这种情况下，由于spring的transaction manager无法拦截到异常，因此事务无法回滚

```java
@Override
    @Transactional
    public Result firstFunctionAboutException() {
        try{
            goodsStockMapper.updateStock();
            if(1 == 1) throw new RuntimeException();
            return Result.ok();
        }catch (Exception e){
            log.info("减库存失败！" + e.getMessage());
            return Result.server_error().Message("减库存失败！" + e.getMessage());
        }
    }
```
##   嵌套调用多个事务方法，这些事务方法使用一个数据库连接，在同一个事务中。
> 由于内部事务方法抛出异常,TransactionManager已经将当前事务标记为roll back only，而外部方法没有将异常继续向上抛出，spring会报错，因为当前事务已经被标记为只能roll back，但是无法获取到异常。

```java
class OuterClass {
    @Autowired
    private InnerClass innerClass

    @Transactional
    public void transferRequired2(Long payerId, Long payeeId, BigDecimal amount) {
        // write DB only
        this.appendTransactionRecord(payerId, payeeId, amount);
        // a transactional method
        try {
            innerClass.reduceBalance(payerId, amount);
        } catch (Exception e) {
            // do nothing
        }
    }
}

class InnerClass {
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void reduceBalance(Long accountId, BigDecimal amount) {
        // write into DB only
        reduceBalance(accountId, amount);
        throw new RuntimeException();
    }
}
```
##   Required_New的使用误区
### 两个事务中更新相同数据造成的锁竞争
外部方法在更新id为101的user后，事务没有提交，它只会在内部方法执行完成后提交，因此会一直持有这条数据的行锁，而innerMethod尝试更新id为101的user时，也会尝试获取这条数据的行锁，而锁被另一个连接一直占有，内部方法就会一直等待，最终陷入死锁，直到等待到达超时时间后，回滚内部方法的事务。
问题：外部方法的事务会回滚吗？
流程图：

| #
 | 

Transaction(Connection)  Out | 

Transaction(Connection)  In |
| --- | --- | --- |
| 

T0 | 

out获取到user:101的排它锁 | 
 |
| 

T1 | 
 | 

尝试获取user:101的排它锁，需等待连接OUT释放锁，陷入死锁 |
| 

T2 | 
 | 

等待锁超时，事务回滚 |
| 

T3 | 

事务回滚（由于异常传播到了外部方法） | 
 |


```java
class OuterClass {
    @Autowired
    private InnerClass innerClass;

    @Transactional(propagation = Propagation.REQUIRED)
    public void outerMethod() {
        String sql = "update user set name = 'out' where id = 101";
        // execute sql
        innerClass.innerMethod();
    }
}

class InnerClass {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void innerMethod() {
        String sql = "update user set name = 'out' where id = 101";
        // execute sql
    }
}
```
### 两个事务更新不同表的数据，但两个表存在外键关系造成的锁竞争
这种情况与更新相同数据造成的锁竞争类似。比如有两个表student, lesson， lesson中有column将student表的主键作为外键。在更新lesson表时，需要获取该数据行关联的student某行数据的共享锁，如果Student改行数据的排它锁正在被持有，那么也会陷入锁竞争，形成死锁。
