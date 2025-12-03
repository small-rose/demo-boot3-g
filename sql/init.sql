create table t_test_td (
  id number not null,
  name varchar2(100),
  balance number(16, 2),
  createtime date default sysdate,
  lastopdate date default sysdate,
  modifydesc varchar2(100)
);

