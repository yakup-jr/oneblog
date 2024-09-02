DELETE FROM T_LABEL;

insert into T_LABEL(NAME, LABEL_ID) values ('Assembler', 10);
insert into T_LABEL(NAME, LABEL_ID) values ('Programming_languages', 11);
insert into T_LABEL(NAME, LABEL_ID) values ('Java', 12);
insert into T_LABEL(NAME, LABEL_ID) values ('Python', 13);
insert into T_LABEL(NAME, LABEL_ID) values ('CPlusPlus', 14);
insert into T_LABEL(NAME, LABEL_ID) values ('CHash', 15);
insert into T_LABEL(NAME, LABEL_ID) values ('C', 16);
insert into T_LABEL(NAME, LABEL_ID) values ('Go', 17);
insert into T_LABEL(NAME, LABEL_ID) values ('Kotlin', 18);

-- insert into T_USER(NICKNAME, NAME, EMAIL, PASSWORD)
-- values ('yakup_jr', 'Dima', 'fgrcnyxj@gmail.com', '12345678');
-- insert into T_USER(NICKNAME, NAME, EMAIL, PASSWORD)
-- values ('WHTOY', 'Dima', 'dimaakupovz@gmail.com', '12345678');
-- insert into T_USER(NICKNAME, NAME, EMAIL, PASSWORD)
-- values ('wylsa', 'Valentin', 'wylsa@apple.com', 'forAppleYe');
-- insert into T_USER(NICKNAME, NAME, EMAIL, PASSWORD)
-- values ('seeva', 'Stiv', 'stivvoznik@apple.com', 'betternow');
-- insert into T_USER(NICKNAME, NAME, EMAIL, PASSWORD)
-- values ('xahe12', 'Ilon', 'ilon_mask@spacex.com', 'teslaOneLove');
--
-- insert into T_ROLE(NAME)
-- values ('ROLE_ADMIN');
-- insert into T_ROLE(NAME)
-- values ('ROLE_USER');
--
-- insert into T_USER_ROLE(USER_ID, ROLE_ID)
-- values ((select ID from T_USER where NICKNAME = 'yakup_jr'),
--         (select ID from T_ROLE where NAME = 'ROLE_ADMIN'));
-- insert into T_USER_ROLE(USER_ID, ROLE_ID)
-- values ((select ID from T_USER where NICKNAME = 'yakup_jr'),
--         (select ID from T_ROLE where NAME = 'ROLE_USER'));
-- insert into T_USER_ROLE(USER_ID, ROLE_ID)
-- values ((select ID from T_USER where NICKNAME = 'WHTOY'),
--         (select ID from T_ROLE where NAME = 'ROLE_USER'));
-- insert into T_USER_ROLE(USER_ID, ROLE_ID)
-- values ((select ID from T_USER where NICKNAME = 'WHTOY'),
--         (select ID from T_ROLE where NAME = 'ROLE_ADMIN'));
-- insert into T_USER_ROLE(USER_ID, ROLE_ID)
-- values ((select ID from T_USER where NICKNAME = 'wylsa'),
--         (select ID from T_ROLE where NAME = 'ROLE_USER'));
-- insert into T_USER_ROLE(USER_ID, ROLE_ID)
-- values ((select ID from T_USER where NICKNAME = 'wylsa'),
--         (select ID from T_ROLE where NAME = 'ROLE_ADMIN'));
-- insert into T_USER_ROLE(USER_ID, ROLE_ID)
-- values ((select ID from T_USER where NICKNAME = 'seeva'),
--         (select ID from T_ROLE where NAME = 'ROLE_USER'));
-- insert into T_USER_ROLE(USER_ID, ROLE_ID)
-- values ((select ID from T_USER where NICKNAME = 'xahe12'),
--         (select ID from T_ROLE where NAME = 'ROLE_USER'));
