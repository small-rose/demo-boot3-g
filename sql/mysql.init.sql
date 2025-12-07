/*
create schema bp_demo collate utf8_general_ci;
CREATE USER 'bp_demo_user'@'%' IDENTIFIED BY 'small.rose@2025';
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