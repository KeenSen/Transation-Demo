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