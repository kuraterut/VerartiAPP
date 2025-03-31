CREATE TABLE role
(
    id   serial      not null unique,
    name varchar(20) not null unique
);

INSERT INTO role (name)
VALUES ('MASTER'),
       ('ADMIN');

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
    CONSTRAINT unique_user_role UNIQUE (users_id, role_id)
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

CREATE TABLE option
(
    id          serial       not null unique,
    name        varchar(255) not null unique,
    description varchar(255) not null,
    duration    VARCHAR(5)   not null,
    price       int          not null
);

CREATE TABLE users_option
(
    id        serial                                       not null unique,
    users_id  int references users (id) on delete cascade  not null,
    option_id int references option (id) on delete cascade not null,
    CONSTRAINT unique_user_option UNIQUE (users_id, option_id)
);

CREATE TABLE status
(
    id   serial      not null unique,
    name varchar(20) not null unique
);

INSERT INTO status (name)
VALUES ('WAITING'),   -- ждет подтверждения
       ('CONFIRMED'), -- подтвержденный
       ('COMPLETED'); -- завершенный

CREATE TABLE master_appointment
(
    id         serial                                       not null unique,
    users_id   int references users (id) on delete cascade  not null,
    client_id  int references client (id) on delete cascade not null,
    status_id  int references status (id)                   not null default 1,
    start_time VARCHAR(5)                                   not null,
    date       date                                         not null,
    comment    VARCHAR(511)                                          default ''
);

CREATE TABLE master_appointment_option
(
    id                    serial                                                   not null unique,
    option_id             int references option (id) on delete cascade             not null,
    master_appointment_id int references master_appointment (id) on delete cascade not null,
    CONSTRAINT unique_option_master_appointment UNIQUE (option_id, master_appointment_id)
);

CREATE
OR REPLACE FUNCTION clean_orphaned_appointments()
RETURNS TRIGGER AS $$
BEGIN
DELETE
FROM master_appointment
WHERE id IN (SELECT OLD.master_appointment_id
WHERE NOT EXISTS (
    SELECT 1 FROM master_appointment_option mao
    WHERE mao.master_appointment_id = OLD.master_appointment_id
    )
    );
RETURN NULL;
END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER trigger_clean_orphaned_appointments
    AFTER DELETE
    ON master_appointment_option
    FOR EACH ROW EXECUTE FUNCTION clean_orphaned_appointments();

CREATE TABLE admin_shift
(
    id       serial                                      not null unique,
    users_id int references users (id) on delete cascade not null,
    date     date                                        not null unique
);

CREATE TABLE master_shift
(
    id       serial                                      not null unique,
    users_id int references users (id) on delete cascade not null,
    date     date                                        not null,
    CONSTRAINT unique_user_date UNIQUE (users_id, date)
);

CREATE TABLE product
(
    id    serial       not null unique,
    name  varchar(255) not null unique,
    price int          not null,
    count int default 0
);

CREATE TABLE feedback
(
    id        serial                                       not null unique,
    users_id  int references users (id) on delete cascade  not null,
    client_id int references client (id) on delete cascade not null,
    message   varchar(255)                                 not null,
    date      date
);
