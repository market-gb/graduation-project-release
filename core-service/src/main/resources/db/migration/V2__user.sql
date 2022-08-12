create table users
(
    id         bigserial primary key,
    username   varchar(36) not null,
    password   varchar(80) not null,
    email      varchar(50) unique,
    enabled  BOOLEAN NOT NULL DEFAULT false,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

CREATE TABLE IF NOT EXISTS registration_tokens (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    expired_at TIMESTAMP NOT NULL,
    user_id BIGINT REFERENCES users(id)
);

create table roles
(
    id         bigserial primary key,
    name       varchar(50) not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

create table users_roles
(
    user_id    bigint not null references users (id),
    role_id    bigint not null references roles (id),
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp,
    primary key (user_id, role_id)
);

CREATE TABLE IF NOT EXISTS  message_text_elements(
    id        BIGSERIAL PRIMARY KEY,
    title     VARCHAR(255) NOT NULL UNIQUE,
    text      TEXT
);

INSERT INTO message_text_elements (title, text)
VALUES ('Приветствие1', 'Добрый день, '),
       ('Приветствие2', '!'),
       ('Подпись2', 'С уважением SpringBootMarket.'),
       ('Поступил новый заказ1', 'Поступил новый заказ №'),
       ('Поступил новый заказ2', '. Заказ ожидает обработки.'),
       ('Заказ успешно сформирован1', 'Заказ №'),
       ('Заказ успешно сформирован2', ' на сумму '),
       ('Заказ успешно сформирован3', ' успешно создан и ожидает обработки.'),
       ('Подтвердите ваш email1', 'Вам необходимо подтвердить введенный email.'),
       ('Подтвердите ваш email2', ' Для потверждения пройдите по ссылке.'),
       ('Подтвердите ваш email3', '<a href="http://localhost:5555/user/register?token='),
       ('Подтвердите ваш email4', '" target="_blank">Подтвердить</a>');
insert into roles (name)
values ('ROLE_ADMIN'),
       ('ROLE_MANAGER'),
       ('ROLE_USER');

insert into users (username, password, email, enabled)
values   ('admin', '$2a$12$fIxG7VKFdJw9HriHgNyuNu.DitJytiDsERb25YAvhUEicllt37m0O', 'admin@admin.ru', true),
         ('manager', '$2a$10$gW.USGSieFtRpJVxwN/zuuXumysWvFmbILacUPHXkmSO0e1R6BoyS', 'manager1@manager.ru', true),
         ('user', '$2a$12$.z4y.gN6zGcUMjU/USKMEedIinnVn.4xGonlD1.M2213psnAWqYW.', 'user@user.ru', true);

insert into users_roles (user_id, role_id)
values (1,1),
       (2,2),
       (3,3);