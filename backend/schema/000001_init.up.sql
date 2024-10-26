CREATE TABLE role
(
    id   serial      not null unique,
    name varchar(20) not null unique
);

INSERT INTO role (name)
VALUES ('master'),
       ('admin'),
       ('director');

CREATE TABLE users
(
    id            serial                   not null unique,
    name          varchar(255)             not null,
    surname       varchar(255)             not null,
    patronymic    varchar(255) default '',
    password_hash varchar(255)             not null,
    email         varchar(255)             not null unique,
    phone         varchar(255)             not null unique,
    bio           varchar(511) default '',
    pfp_url       varchar(255) default '',
    role_id       int references role (id) not null,
    salary        int          default 0
);

CREATE TABLE client
(
    id         serial       not null unique,
    name       varchar(255) not null,
    surname    varchar(255) not null,
    patronymic varchar(255),
    email      varchar(255) not null unique,
    phone      varchar(255) not null unique,
    bio        varchar(255),
    birthday   date
);

CREATE TABLE appointment
(
    id          serial       not null unique,
    name        varchar(255) not null,
    description varchar(255) not null,
    duration    int          not null
);

CREATE TABLE users_appointment
(
    id             serial                                            not null unique,
    users_id       int references users (id) on delete cascade       not null,
    appointment_id int references appointment (id) on delete cascade not null,
    price          int                                               not null
);

CREATE TABLE status
(
    id   serial      not null unique,
    name varchar(20) not null unique
);

INSERT INTO status (name)
VALUES ('confirmed'), -- подтвержденный
       ('completed'), -- завершенный
       ('cancelled'); -- отмененный

CREATE TABLE schedule
(
    id             serial                                            not null unique,
    users_id       int references users (id) on delete cascade       not null,
    client_id      int references client (id) on delete cascade      not null,
    appointment_id int references appointment (id) on delete cascade not null,
    status_id      int references status (id)                        not null default 1,
    start_schedule timestamp                                         not null,
    end_schedule   timestamp                                         not null
);

CREATE TABLE resource
(
    id          serial       not null unique,
    name        varchar(255) not null unique,
    description varchar(255) not null
);

CREATE TABLE feedback
(
    id        serial                                       not null unique,
    users_id  int references users (id) on delete cascade  not null,
    client_id int references client (id) on delete cascade not null,
    message   varchar(255)                                 not null,
    date      date
);
