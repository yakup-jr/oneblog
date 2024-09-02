drop table T_USER if exists;
drop table T_ROLE if exists;
drop table T_USER_ROLE if exists;

create table T_USER
(
    USER_ID       int identity primary key,
    NICKNAME varchar(50)  not null unique,
    NAME     varchar(50)  not null,
    EMAIL    varchar(100) not null unique,
    PASSWORD varchar(100) not null
);

create table T_ROLE
(
    ROLE_ID   int identity primary key,
    NAME varchar(50) not null
);

create table T_USER_ROLE
(
    USER_ID integer not null,
    ROLE_ID integer not null
);


alter table T_USER_ROLE
    add foreign key (ROLE_ID) references T_ROLE (ROLE_ID) on delete cascade;
alter table T_USER_ROLE
    add foreign key (USER_ID) references T_USER (USER_ID) on delete cascade;
alter table T_USER_ROLE
    add constraint PK_USER_ROLE primary key (USER_ID, ROLE_ID);


