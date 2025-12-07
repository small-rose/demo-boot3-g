/*
bp_demo
bp_demo_user
small.rose@2025
*/

create table (
    id numeric not null ,
    name varchar(100)
    createtime date default sysdate,
    lastopdate date,
    primary key id
);