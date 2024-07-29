insert into T_USER(NICKNAME, NAME, EMAIL, PASSWORD)
values ('WHTOY', 'Dima', 'dimaakupovz@gmail.com', '12345678');
insert into T_USER(NICKNAME, NAME, EMAIL, PASSWORD)
values ('yakup_jr', 'Dima', 'fgrcnyxj@gmail.com', '12345678');

insert into T_ROLE(NAME)
values ('ROLE_ADMIN');
insert into T_ROLE(NAME)
values ('ROLE_USER');

insert into T_USER_ROLE(USER_ID, ROLE_ID)
values ((select ID from T_USER where NICKNAME = 'WHTOY'),
        (select ID from T_ROLE where NAME = 'ROLE_USER'));
insert into T_USER_ROLE(USER_ID, ROLE_ID)
values ((select ID from T_USER where NICKNAME = 'WHTOY'),
        (select ID from T_ROLE where NAME = 'ROLE_ADMIN'));