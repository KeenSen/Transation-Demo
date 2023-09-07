-- 初始化账户表数据
insert into account (id, name, balance) values(101, 'jayden', 1000);
insert into account (id, name, balance) values(201, 'lawrence', 1000);

-- 重置账户表数据
update account set balance = 1000 where id = 101;
update account set balance = 1000 where id = 201;

-- 清空转账记录表
delete from account_transaction;