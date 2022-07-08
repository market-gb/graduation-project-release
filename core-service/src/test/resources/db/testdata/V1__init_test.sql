create table products
(
    id         bigserial primary key,
    title      varchar(255)  not null,
    price      numeric(8, 2) not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

insert into products (title, price)
values ('Milk', 50.00),
       ('Bread', 20.00);

create table categories
(
    id    bigserial primary key,
    title varchar(255) not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

insert into categories (title)
values ('Food');

create table products_categories
(
    product_id  bigint not null references products (id),
    category_id bigint not null references categories (id),
    created_at  timestamp default current_timestamp,
    updated_at  timestamp default current_timestamp,
    primary key (product_id, category_id)
);

insert into products_categories (product_id, category_id)
values (1, 1), (2, 1);

create table orders
(
    id          bigserial primary key,
    username    varchar(255)  not null,
    total_price numeric(8) not null,
    address     varchar(255),
    phone       varchar(255),
    order_status varchar(20) not null,
    created_at  timestamp default current_timestamp,
    updated_at  timestamp default current_timestamp
);

insert into orders (username, total_price, address, phone, order_status)
values ('test_user', 200.00, 'address', '123456', 'CREATED');

create table order_items
(
    id                bigserial primary key,
    product_id        bigint        not null references products (id),
    order_id          bigint        not null references orders (id),
    quantity          int           not null,
    price_per_product numeric(8, 2) not null,
    price             numeric(8, 2) not null,
    created_at        timestamp default current_timestamp,
    updated_at        timestamp default current_timestamp
);

insert into order_items (product_id, order_id, quantity, price_per_product, price)
values (1, 1, 4, 50.00, 200.00);