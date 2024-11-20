insert into t_label(name)
values ('Assembler');
insert into t_label(name)
values ('Programming_languages');
insert into t_label(name)
values ('Java');
insert into t_label(name)
values ('Python');
insert into t_label(name)
values ('CPlusPlus');
insert into t_label(name)
values ('CHash');
insert into t_label(name)
values ('C');
insert into t_label(name)
values ('Go');
insert into t_label(name)
values ('Kotlin');


insert into t_user(nickname, name, email, password)
values ('yakup_jr', 'Dima', 'fgrcnyxj@gmail.com', '12345678');
insert into t_user(nickname, name, email, password)
values ('WHTOY', 'Dima', 'dimaakupovz@gmail.com', '12345678');
insert into t_user(nickname, name, email, password)
values ('wylsa', 'Valentin', 'wylsa@apple.com', 'forAppleYe');
insert into t_user(nickname, name, email, password)
values ('seeva', 'Stiv', 'stivvoznik@apple.com', 'betternow');
insert into t_user(nickname, name, email, password)
values ('xahe12', 'Ilon', 'ilon_mask@spacex.com', 'teslaOneLove');


insert into t_role(name)
values ('ROLE_ADMIN');
insert into t_role(name)
values ('ROLE_USER');


insert into t_user_role(user_id, role_id)
values ((select user_id from T_USER where nickname = 'yakup_jr'),
        (select role_id from T_ROLE where name = 'ROLE_ADMIN'));
insert into t_user_role(user_id, role_id)
values ((select user_id from T_USER where nickname = 'yakup_jr'),
        (select role_id from T_ROLE where name = 'ROLE_USER'));
insert into t_user_role(user_id, role_id)
values ((select user_id from T_USER where nickname = 'WHTOY'),
        (select role_id from T_ROLE where name = 'ROLE_USER'));
insert into t_user_role(user_id, role_id)
values ((select user_id from T_USER where nickname = 'WHTOY'),
        (select role_id from T_ROLE where name = 'ROLE_ADMIN'));
insert into t_user_role(user_id, role_id)
values ((select user_id from T_USER where nickname = 'wylsa'),
        (select role_id from T_ROLE where name = 'ROLE_USER'));
insert into t_user_role(user_id, role_id)
values ((select user_id from T_USER where nickname = 'wylsa'),
        (select role_id from T_ROLE where name = 'ROLE_ADMIN'));
insert into t_user_role(user_id, role_id)
values ((select user_id from T_USER where nickname = 'seeva'),
        (select role_id from T_ROLE where name = 'ROLE_USER'));
insert into t_user_role(user_id, role_id)
values ((select user_id from T_USER where nickname = 'xahe12'),
        (select role_id from T_ROLE where name = 'ROLE_USER'));


insert into t_article(article_id, title, body, created_at, user_id)
values (1, 'The boys 5 season teaser', 'body 1', '2024-09-02 10:34:25', 1);
insert into t_article(article_id, title, body, created_at, user_id)
values (2, 'The House of the Dragon 2 season', 'body 2', '2024-05-09 12:00:00', 2);
insert into t_article(article_id, title, body, created_at, user_id)
values (3, 'The Peaky blinders film', 'body 3', '2022-06-23 13:00:00', 3);
insert into t_article(article_id, title, body, created_at, user_id)
values (4, 'The Games of Thrones 8 season', 'body 4', '2019-02-01', 5);
insert into t_article(article_id, title, body, created_at, user_id)
values (5, 'Squid game 2 season', 'body 5', '2014-05-03', 4);

insert into t_article_preview(article_preview_id, body)
values (1, 'body 1');
insert into t_article_preview(article_preview_id, body)
values (2, 'body 3');
insert into t_article_preview(article_preview_id, body)
values (3, 'body 2');

update t_article
set article_preview_id = 1
where article_id = 1;
update t_article
set article_preview_id = 2
where article_id = 2;
update t_article
set article_preview_id = 3
where article_id = 3;


insert into t_article_label(article_id, label_id)
values (1, 1);
insert into t_article_label(article_id, label_id)
values (2, 2);
insert into t_article_label(article_id, label_id)
values (2, 3);
insert into t_article_label(article_id, label_id)
values (3, 4);
insert into t_article_label(article_id, label_id)
values (4, 5);
insert into t_article_label(article_id, label_id)
values (5, 6);
insert into t_article_label(article_id, label_id)
values (5, 7);











