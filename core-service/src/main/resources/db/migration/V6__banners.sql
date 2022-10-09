create table if not exists banners
(
    id              bigserial primary key,
    title           varchar(255) not null,
    image_id        bigint  unique,
    created_at timestamp default current_timestamp
);

insert into banners (title)
VALUES
('1 Акция'),
('2 Акция'),
('3 Акция'),
('4 Акция'),
('5 Акция');