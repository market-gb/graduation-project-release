create table products
(
    id         bigserial primary key,
    title      varchar(255)  not null unique ,
    description      varchar(255)  not null ,
    pathname      varchar(255)  not null unique ,
    price      numeric(8, 2) not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

create table categories
(
    id         bigserial primary key,
    title      varchar(255) not null unique,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp
);

create table products_categories
(
    product_id  bigint not null references products (id),
    category_id bigint not null references categories (id),
    created_at  timestamp default current_timestamp,
    updated_at  timestamp default current_timestamp,
    primary key (product_id, category_id)
);

create table orders
(
    id           bigserial primary key,
    username     varchar(255)  not null,
    total_price  numeric(8, 2) not null,
    address      varchar(255),
    phone        varchar(255),
    order_status varchar(20)   not null default 'CREATED',
    created_at   timestamp default current_timestamp,
    updated_at   timestamp default current_timestamp
);

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