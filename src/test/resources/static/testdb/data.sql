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


insert into t_paragraph(text)
values ('Lorem ipsum dolor sit amet, consectetur adipiscing elit. ' ||
        'Ut id risus ultrices, rutrum nibh ' ||
        'sit amet, porta nisi. Integer in tortor cursus, hendrerit ex at, placerat nisl. Ut vel quam' ||
        ' volutpat, interdum nunc sit amet, eleifend nisi. Proin efficitur mauris sit amet sagittis sagittis.' ||
        ' Phasellus placerat mi in ullamcorper vestibulum. Donec consequat justo sed pharetra tempor.' ||
        ' Duis sagittis diam ipsum. Nullam vehicula massa id dolor ultricies elementum.' ||
        ' Sed hendrerit metus sed lorem viverra fermentum.');
insert into t_paragraph(text)
values ('Proin ac tempor nisl. Cras tincidunt posuere nisi vel ' ||
        'lacinia.' ||
        ' Sed ultrices neque tellus, quis tempor urna ullamcorper vitae. ' ||
        'Proin venenatis nunc ipsum, in accumsan sapien tincidunt sed. ' ||
        'Aenean ac tempor metus. Sed lobortis erat ut arcu mattis, ' ||
        'eget aliquam lectus blandit. Duis dictum leo vel dui ' ||
        'suscipit feugiat tincidunt.');
insert into t_paragraph(text)
values ('Nulla sollicitudin lacus eget urna aliquam, eget fermentum justo vehicula. ' ||
        'Praesent eleifend placerat justo quis dapibus.' ||
        ' Ut suscipit arcu quis condimentum efficitur. ' ||
        'Morbi bibendum rutrum est quis facilisis. Vivamus ac venenatis' ||
        ' nulla, aliquet venenatis purus. Morbi in tortor a nibh lobortis' ||
        ' ullamcorper. Integer ante diam, tincidunt hendrerit mi ' ||
        'sed, pulvinar.');
insert into t_paragraph(text)
values ('Maecenas tempus convallis enim, eget vulputate libero consequat eu. ' ||
        'Sed euismod euismod turpis et egestas. Nunc feugiat eros mi, ' ||
        'vel condimentum lectus condimentum a. Phasellus purus sapien, ' ||
        'porta non nisi eget, molestie egestas est. Mauris lacus nulla, ' ||
        'vulputate a dolor at, posuere lacinia leo. Fusce quam ' ||
        'ligula, posuere tincidunt lacus.');
insert into t_paragraph(text)
values ('Donec eget enim nulla. Donec dignissim quam vel sapien dignissim ' ||
        'consequat. Sed ac porta mauris. Nam nec turpis bibendum, euismod' ||
        ' mauris in, commodo neque. Sed pulvinar id lorem at viverra. ' ||
        'Suspendisse non suscipit mi, nec sollicitudin tellus.' ||
        ' Etiam ultricies ligula ac porta rhoncus. ' ||
        'Phasellus sollicitudin id sem ac commodo. Fusce.');


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


insert into t_article(title, created_at, user_id)
values ('The boys 5 season teaser', '2024-09-02 10:34:25', 1);
insert into t_article(title, created_at, user_id)
values ('The House of the Dragon 2 season', '2024-05-09 12:00:00', 2);
insert into t_article(title, created_at, user_id)
values ('The Peaky blinders film', '2022-06-23 13:00:00', 3);
insert into t_article(title, created_at, user_id)
values ('The Games of Thrones 8 season', '2019-02-01', 5);
insert into t_article(title, created_at, user_id)
values ('Squid game 2 season', '2014-05-03', 4);

insert into t_article_preview(article_id, paragraph_id)
values (1, 2);
insert into t_article_preview(article_id, paragraph_id)
values (3, 5);

update t_article
set article_preview_id = 1
where t_article.article_id = 1;
update t_article
set article_preview_id = 2
where t_article.article_id = 2;
update t_article
set article_preview_id = 2
where t_article.article_id = 3;
update t_article
set article_preview_id = 1
where t_article.article_id = 4;
update t_article
set article_preview_id = 1
where t_article.article_id = 5;


update t_paragraph
set article_id = 1
where t_paragraph.paragraph_id = 3;
update t_paragraph
set article_id = 1
where t_paragraph.paragraph_id = 4;



insert into t_article_label(article_id, label_id)
values (1, 1);
insert into t_article_label(article_id, label_id)
values (1, 2);
insert into t_article_label(article_id, label_id)
values (2, 3);
insert into t_article_label(article_id, label_id)
values (3, 4);
insert into t_article_label(article_id, label_id)
values (3, 5);
insert into t_article_label(article_id, label_id)
values (4, 6);
insert into t_article_label(article_id, label_id)
values (5, 7);

insert into t_article_paragraph(article_id, paragraph_id)
values (1, 1);
insert into t_article_paragraph(article_id, paragraph_id)
values (1, 3);











