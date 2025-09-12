-- Data for t_label
INSERT INTO t_label (name)
VALUES ('ASSEMBLER'),
       ('PROGRAMMING_LANGUAGES'),
       ('JAVA'),
       ('PYTHON'),
       ('C_PLUS_PLUS'),
       ('C_SHARP'),
       ('C'),
       ('GO'),
       ('KOTLIN');

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
INSERT INTO t_article (article_id, title, body, preview_body, created_at, user_id)
VALUES (1, 'Title 1',
        'Lorem ipsum dolor sit amet consectetur adipiscing elit quisque faucibus ex sapien vitae pellentesque sem placerat in id cursus mi pretium tellus duis convallis tempus leo eu aenean sed diam urna tempor pulvinar vivamus fringilla lacus nec metus bibendum egestas iaculis massa nisl malesuada lacinia integer nunc posuere ut hendrerit semper vel class aptent taciti sociosqu ad litora torquent per conubia nostra inceptos himenaeos orci varius natoque penatibus et magnis dis parturient montes nascetur ridiculus mus donec rhoncus eros lobortis nulla molestie mattis scelerisque maximus eget fermentum odio phasellus non purus est efficitur laoreet mauris pharetra vestibulum fusce dictum risus.',
        'Lorem ipsum dolor sit amet consectetur adipiscing elit quisque faucibus ex sapien vitae pellentesque sem placerat in id cursus mi pretium tellus duis convallis tempus leo eu aenean sed diam',
        '2014-05-03', 1),
       (2, 'Title 2',
        'Lorem ipsum dolor sit amet consectetur adipiscing elit quisque faucibus ex sapien vitae pellentesque sem placerat in id cursus mi pretium tellus duis convallis tempus leo eu aenean sed diam urna tempor pulvinar vivamus fringilla lacus nec metus bibendum egestas iaculis massa nisl malesuada lacinia integer nunc posuere ut hendrerit semper vel class aptent taciti sociosqu ad litora torquent per conubia nostra inceptos himenaeos orci varius natoque penatibus et magnis dis parturient montes nascetur ridiculus mus donec rhoncus eros lobortis.',
        'Lorem ipsum dolor sit amet consectetur adipiscing elit quisque faucibus ex sapien vitae pellentesque sem placerat in id cursus mi pretium tellus duis.',
        '2019-02-01', 2),
       (3, 'Title 3',
        'Lorem ipsum dolor sit amet consectetur adipiscing elit quisque faucibus ex sapien vitae pellentesque sem placerat in id cursus mi pretium tellus duis convallis tempus leo eu aenean sed diam urna tempor pulvinar vivamus fringilla lacus nec metus bibendum egestas iaculis massa nisl malesuada lacinia integer nunc posuere ut hendrerit semper vel class aptent taciti sociosqu ad litora torquent per conubia nostra inceptos himenaeos orci varius natoque penatibus et magnis dis parturient montes nascetur ridiculus mus donec rhoncus eros lobortis nulla molestie mattis scelerisque maximus eget fermentum odio phasellus non purus est efficitur laoreet mauris pharetra vestibulum fusce dictum risus blandit quis suspendisse aliquet nisi sodales consequat magna ante condimentum neque at luctus nibh finibus facilisis dapibus etiam interdum tortor ligula congue sollicitudin erat viverra ac tincidunt nam porta elementum a enim euismod quam justo lectus commodo augue arcu dignissim.',
        'Lorem ipsum dolor sit amet consectetur adipiscing elit quisque faucibus ex sapien vitae pellentesque sem placerat in id cursus mi pretium tellus duis convallis tempus leo eu aenean sed diam urna tempor pulvinar vivamus fringilla lacus nec metus bibendum egestas.',
        '2024-05-09 12:00:00', 3),
       (4, 'Title 4',
        'Lorem ipsum dolor sit amet consectetur adipiscing elit quisque faucibus ex sapien vitae pellentesque sem placerat in id cursus mi pretium tellus duis convallis tempus leo eu aenean sed diam urna tempor pulvinar vivamus fringilla lacus nec metus bibendum egestas iaculis massa nisl malesuada lacinia integer nunc posuere ut hendrerit semper vel class aptent taciti sociosqu ad litora torquent per conubia nostra inceptos himenaeos orci varius natoque penatibus et magnis dis parturient montes nascetur ridiculus mus donec rhoncus eros lobortis nulla molestie mattis scelerisque maximus eget fermentum odio phasellus non purus est efficitur laoreet mauris pharetra vestibulum fusce dictum risus blandit quis suspendisse aliquet nisi sodales consequat magna ante condimentum neque at luctus nibh finibus facilisis dapibus etiam interdum tortor ligula congue sollicitudin erat viverra ac tincidunt nam porta elementum a enim euismod quam justo lectus commodo augue arcu dignissim velit aliquam imperdiet mollis nullam volutpat porttitor ullamcorper rutrum gravida cras eleifend turpis fames primis vulputate ornare sagittis vehicula praesent dui felis venenatis ultrices proin libero feugiat tristique accumsan maecenas potenti ultricies habitant morbi senectus netus suscipit auctor curabitur facilisi cubilia curae hac habitasse platea dictumst lorem ipsum dolor sit amet consectetur adipiscing elit quisque faucibus ex sapien vitae pellentesque sem placerat in id cursus mi pretium tellus duis convallis tempus leo eu aenean sed diam urna tempor pulvinar vivamus fringilla lacus nec metus bibendum egestas iaculis massa nisl malesuada.',
        'Lorem ipsum dolor sit amet consectetur adipiscing elit quisque faucibus ex sapien vitae.',
        '2022-06-23 13:00:00', 5),
       (5, 'Title 5',
        'Lorem ipsum dolor sit amet consectetur adipiscing elit quisque faucibus ex sapien vitae pellentesque sem placerat in id cursus mi pretium tellus duis convallis tempus leo eu aenean sed diam urna tempor pulvinar vivamus fringilla lacus nec metus bibendum egestas iaculis massa nisl malesuada lacinia integer nunc posuere ut hendrerit semper vel class aptent taciti sociosqu ad litora torquent per conubia nostra inceptos himenaeos orci varius natoque penatibus et magnis.',
        'Lorem ipsum dolor sit amet.', '2024-09-02 10:34:25', 4);

-- Data for t_article_label
INSERT INTO t_article_label (article_id, label_id)
VALUES (1, 1),
       (2, 2),
       (2, 3),
       (3, 4),
       (4, 5),
       (5, 6),
       (5, 7);

-- Data for t_vote
INSERT INTO t_vote (vote_id, user_id, article_id, vote_type)
VALUES (1, 1, 1, 'LIKE'),
       (2, 2, 1, 'LIKE'),
       (3, 3, 1, 'DISLIKE'),
       (4, 1, 2, 'LIKE'),
       (5, 4, 2, 'LIKE'),
       (6, 5, 3, 'DISLIKE'),
       (7, 2, 4, 'LIKE'),
       (8, 3, 5, 'LIKE');

