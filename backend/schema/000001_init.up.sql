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
    id             serial       not null unique,
    name           varchar(255) not null,
    surname        varchar(255) not null,
    patronymic     varchar(255) default '',
    password_hash  varchar(255) not null,
    email          varchar(255) not null unique,
    phone          varchar(255) not null unique,
    bio            varchar(511) default '',
    photo          varchar(511) default 'http://localhost:9000/photo/default/avatar.png',
    current_salary int          default 0
);

CREATE TABLE users_role
(
    id       serial                                      not null unique,
    users_id int references users (id) on delete cascade not null,
    role_id  int references role (id) on delete cascade  not null,
    CONSTRAINT unique_users_role UNIQUE (users_id, role_id)
);

CREATE TABLE client
(
    id         serial       not null unique,
    name       varchar(255) not null,
    surname    varchar(255) not null,
    patronymic varchar(255),
    email      varchar(255),
    phone      varchar(255) not null unique,
    comment    varchar(511),
    birthday   date
);

CREATE TABLE appointment
(
    id          serial       not null unique,
    name        varchar(255) not null unique,
    description varchar(255) not null,
    duration    VARCHAR(5)   not null,
    price       int          not null
);

CREATE TABLE users_appointment
(
    id             serial                                            not null unique,
    users_id       int references users (id) on delete cascade       not null,
    appointment_id int references appointment (id) on delete cascade not null,
    CONSTRAINT unique_users_appointment UNIQUE (users_id, appointment_id)
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
    start_time     timestamp                                         not null,
    day            date                                              not null
);

CREATE TABLE resource
(
    id          serial       not null unique,
    name        varchar(255) not null unique,
    description varchar(255) not null
);

CREATE TABLE users_resource
(
    id          serial                                         not null unique,
    users_id    int references users (id) on delete cascade    not null,
    resource_id int references resource (id) on delete cascade not null,
    CONSTRAINT unique_users_resource UNIQUE (users_id, resource_id)
);

CREATE TABLE feedback
(
    id        serial                                       not null unique,
    users_id  int references users (id) on delete cascade  not null,
    client_id int references client (id) on delete cascade not null,
    message   varchar(255)                                 not null,
    date      date
);
