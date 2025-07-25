-- Data for t_label
INSERT INTO t_label (name)
VALUES ('Assembler'),
       ('Programming_languages'),
       ('Java'),
       ('Python'),
       ('CPlusPlus'),
       ('CHash'),
       ('C'),
       ('Go'),
       ('Kotlin');

-- Data for t_user
INSERT INTO t_user (nickname, name, email)
VALUES ('yakup_jr', 'Dmitry', 'dimaakupovz@gmail.com'),
       ('shadow', 'Emily', 'shadow@mail.com'),
       ('falcon', 'John', 'falcon@mail.com'),
       ('sparrow', 'Robert', 'sparrow@mail.com'),
       ('finch', 'Sarah', 'finch@mail.com');

-- Data for t_auth
INSERT INTO t_auth (password, verificated, user_id)
VALUES ('defaultPasswordForYakupJr', TRUE, (SELECT user_id FROM t_user WHERE nickname = 'yakup_jr'));

INSERT INTO t_auth (password, verificated, user_id)
VALUES ('strongPass2', TRUE,
        (SELECT user_id FROM t_user WHERE nickname = 'shadow'));

INSERT INTO t_auth (password, verificated, user_id)
VALUES ('strongPass3', TRUE, (SELECT user_id FROM t_user WHERE nickname = 'falcon'));

INSERT INTO t_auth (password, verificated, user_id)
VALUES ('strongPass4', TRUE, (SELECT user_id FROM t_user WHERE nickname = 'sparrow'));

INSERT INTO t_auth (password, verificated, user_id)
VALUES ('strongPass5', TRUE, (SELECT user_id FROM t_user WHERE nickname = 'finch'));

-- Data for t_role
INSERT INTO t_role (name)
VALUES ('ROLE_ADMIN'),
       ('ROLE_USER');

-- Data for t_user_role
INSERT INTO t_user_role (auth_id, role_id)
VALUES ((SELECT auth_id FROM t_auth WHERE user_id = (SELECT user_id FROM t_user WHERE nickname = 'yakup_jr')),
        (SELECT role_id FROM t_role WHERE name = 'ROLE_USER'));

INSERT INTO t_user_role (auth_id, role_id)
VALUES ((SELECT auth_id FROM t_auth WHERE user_id = (SELECT user_id FROM t_user WHERE nickname = 'yakup_jr')),
        (SELECT role_id FROM t_role WHERE name = 'ROLE_ADMIN'));

INSERT INTO t_user_role (auth_id, role_id)
VALUES ((SELECT auth_id FROM t_auth WHERE user_id = (SELECT user_id FROM t_user WHERE nickname = 'shadow')),
        (SELECT role_id FROM t_role WHERE name = 'ROLE_USER'));

INSERT INTO t_user_role (auth_id, role_id)
VALUES ((SELECT auth_id FROM t_auth WHERE user_id = (SELECT user_id FROM t_user WHERE nickname = 'shadow')),
        (SELECT role_id FROM t_role WHERE name = 'ROLE_ADMIN'));

INSERT INTO t_user_role (auth_id, role_id)
VALUES ((SELECT auth_id FROM t_auth WHERE user_id = (SELECT user_id FROM t_user WHERE nickname = 'falcon')),
        (SELECT role_id FROM t_role WHERE name = 'ROLE_USER'));

INSERT INTO t_user_role (auth_id, role_id)
VALUES ((SELECT auth_id FROM t_auth WHERE user_id = (SELECT user_id FROM t_user WHERE nickname = 'falcon')),
        (SELECT role_id FROM t_role WHERE name = 'ROLE_ADMIN'));

INSERT INTO t_user_role (auth_id, role_id)
VALUES ((SELECT auth_id FROM t_auth WHERE user_id = (SELECT user_id FROM t_user WHERE nickname = 'sparrow')),
        (SELECT role_id FROM t_role WHERE name = 'ROLE_USER'));

INSERT INTO t_user_role (auth_id, role_id)
VALUES ((SELECT auth_id FROM t_auth WHERE user_id = (SELECT user_id FROM t_user WHERE nickname = 'finch')),
        (SELECT role_id FROM t_role WHERE name = 'ROLE_USER'));

-- Data for t_article
INSERT INTO t_article (article_id, title, body, created_at, user_id)
VALUES (1, 'Title 1', 'body 1 ...', '2014-05-03', 1),
       (2, 'Title 2', 'body 2 ...', '2019-02-01', 2),
       (3, 'Title 3', 'body 3 ...', '2024-05-09 12:00:00', 3),
       (4, 'Title 4', 'body 4 ...', '2022-06-23 13:00:00', 5),
       (5, 'Title 5', 'body 5 ...', '2024-09-02 10:34:25', 4);

-- Data for t_article_preview
INSERT INTO t_article_preview (article_preview_id, body)
VALUES (1, 'body 1'),
       (2, 'body 3'),
       (3, 'body 2'),
       (4, 'body 4'),
       (5, 'body 5');

-- Update t_article with article_preview_id
UPDATE t_article
SET article_preview_id = 1
WHERE article_id = 1;
UPDATE t_article
SET article_preview_id = 2
WHERE article_id = 2;
UPDATE t_article
SET article_preview_id = 3
WHERE article_id = 3;
UPDATE t_article
SET article_preview_id = 4
WHERE article_id = 4;
UPDATE t_article
SET article_preview_id = 5
WHERE article_id = 5;

-- Data for t_article_label
INSERT INTO t_article_label (article_id, label_id)
VALUES (1, 1),
       (2, 2),
       (2, 3),
       (3, 4),
       (4, 5),
       (5, 6),
       (5, 7);
