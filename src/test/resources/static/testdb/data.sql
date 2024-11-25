-- Labels
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

-- Users
insert into t_user(nickname, name, email, password)
values ('hunter', 'James', 'hunter@mail.com', 'strongPass1');
insert into t_user(nickname, name, email, password)
values ('shadow', 'Emily', 'shadow@mail.com', 'strongPass2');
insert into t_user(nickname, name, email, password)
values ('falcon', 'John', 'falcon@mail.com', 'strongPass3');
insert into t_user(nickname, name, email, password)
values ('sparrow', 'Robert', 'sparrow@mail.com', 'strongPass4');
insert into t_user(nickname, name, email, password)
values ('finch', 'Sarah', 'finch@mail.com', 'strongPass5');

-- Roles (unchanged)
insert into t_role(name)
values ('ROLE_ADMIN');
insert into t_role(name)
values ('ROLE_USER');

-- User Roles
insert into t_user_role(user_id, role_id)
values ((select user_id from t_user where nickname = 'hunter'),
        (select role_id from t_role where name = 'ROLE_USER'));
insert into t_user_role(user_id, role_id)
values ((select user_id from t_user where nickname = 'hunter'),
        (select role_id from t_role where name = 'ROLE_ADMIN'));
insert into t_user_role(user_id, role_id)
values ((select user_id from t_user where nickname = 'shadow'),
        (select role_id from t_role where name = 'ROLE_USER'));
insert into t_user_role(user_id, role_id)
values ((select user_id from t_user where nickname = 'shadow'),
        (select role_id from t_role where name = 'ROLE_ADMIN'));
insert into t_user_role(user_id, role_id)
values ((select user_id from t_user where nickname = 'falcon'),
        (select role_id from t_role where name = 'ROLE_USER'));
insert into t_user_role(user_id, role_id)
values ((select user_id from t_user where nickname = 'falcon'),
        (select role_id from t_role where name = 'ROLE_ADMIN'));
insert into t_user_role(user_id, role_id)
values ((select user_id from t_user where nickname = 'sparrow'),
        (select role_id from t_role where name = 'ROLE_USER'));
insert into t_user_role(user_id, role_id)
values ((select user_id from t_user where nickname = 'finch'),
        (select role_id from t_role where name = 'ROLE_USER'));

-- Articles
insert into t_article(article_id, title, body, created_at, user_id)
values (1, 'Title 1', 'body 1 ...', '2014-05-03', 1);
insert into t_article(article_id, title, body, created_at, user_id)
values (2, 'Title 2', 'body 2 ...', '2019-02-01', 2);
insert into t_article(article_id, title, body, created_at, user_id)
values (3, 'Title 3', 'body 3 ...', '2024-05-09 12:00:00', 3);
insert into t_article(article_id, title, body, created_at, user_id)
values (4, 'Title 4', 'body 4 ...', '2022-06-23 13:00:00', 5);
insert into t_article(article_id, title, body, created_at, user_id)
values (5, 'Title 5', 'body 5 ...', '2024-09-02 10:34:25', 4);

-- Article Previews
insert into t_article_preview(article_preview_id, body)
values (1, 'body 1');
insert into t_article_preview(article_preview_id, body)
values (2, 'body 3');
insert into t_article_preview(article_preview_id, body)
values (3, 'body 2');
insert into t_article_preview (article_preview_id, body)
values (4, 'body 4');
insert into t_article_preview (article_preview_id, body)
values (5, 'body 5');


update t_article
set article_preview_id = 1
where article_id = 1;
update t_article
set article_preview_id = 2
where article_id = 2;
update t_article
set article_preview_id = 3
where article_id = 3;
update t_article
set article_preview_id = 4
where article_id = 4;
update t_article
set article_preview_id = 5
where article_id = 5;


-- Article Labels
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
