create table if not exists images
(
    id         bigserial primary key,
    name   varchar(36) not null unique,
    content oid not null
);