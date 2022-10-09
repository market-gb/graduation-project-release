create table users
(
    id         bigserial primary key,
    username   varchar(36) not null,
    password   varchar(80) not null,
    email      varchar(50) unique,
    enabled    boolean not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
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

insert into roles (id, name)
values (1, 'FIRST_ROLE'),
       (2, 'NEW_ROLE');

insert into users (id, username, password, email, enabled)
values (1, 'User_1', 'Password_1', 'email_1', true),
       (2, 'User_2', 'Password_2', 'email_2', true);

insert into users_roles (user_id, role_id)
values (1, 1),
       (2, 2);
