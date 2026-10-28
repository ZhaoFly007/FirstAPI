create table test_db.user
(
    id       bigint auto_increment
        primary key,
    username varchar(50)                not null,
    password varchar(255)               not null,
    role     varchar(20) default 'USER' not null,
    constraint idx_username
        unique (username)
);

