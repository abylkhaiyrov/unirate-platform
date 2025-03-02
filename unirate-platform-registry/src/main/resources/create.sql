create table _user
(
    id           bigint      not null
        primary key,
    created_by   varchar,
    created_date timestamp,
    active       boolean     not null,
    blocked      boolean,
    username        varchar(50) not null,
    password     text,
    email        varchar unique,
    first_name   varchar,
    last_name    varchar,
    deleted      boolean,
    telephone_number varchar,
    activation_code int4,
    activation_code_sent_at timestamp
);

create table role
(
    id           bigint       not null
        primary key,
    created_by   varchar,
    created_date timestamp,
    code         varchar(255) not null,
    ru           varchar(255) not null,
    deleted      boolean default false
);

create table _user_role
(
    id             bigint not null
        primary key,
    created_by     varchar,
    created_date   timestamp,
    begin_date     timestamp,
    end_date       timestamp,
    assign_user_id bigint
        references _user,
    deleted        boolean default false,
    code           varchar(255),
    role_id        bigint
        references role
);

CREATE SEQUENCE _user_role_id_seq
    START WITH 1
    INCREMENT BY 1;

CREATE SEQUENCE user_id_seq
    START WITH 1
    INCREMENT BY 1;

CREATE SEQUENCE role_id_seq
    START WITH 1
    INCREMENT BY 1;

INSERT INTO role(id, created_by, created_date, code, ru, deleted)
VALUES (nextval('role_id_seq'), 'system', now(), 'ADMIN', 'Админ', false);

INSERT INTO role(id, created_by, created_date, code, ru, deleted)
VALUES (nextval('role_id_seq'), 'system', now(), 'USER', 'Пользователь', false);