insert into categories (title, pathname, description) values ('ЧАСЫ', 'pathname', 'description');

insert into products (title, price, pathname, description)
values
('Watch made of steel', 8900, 'img/products/img1.jpg', 'Удобные спортивные часы с браслетом из стали'),
('Silver watch', 14900, 'img/products/img2.jpg', 'Серебряные часы с кожанным ремешком коричневого цвета'),
('Red watch', 11890, 'img/products/img3.jpg', 'Спортивные часы с красным ремешком'),
('Leather strap watch', 2790, 'img/products/img4.jpg', 'Часы в золотом корпусе с кожанным ремешком'),
('Silver watch', 3790, 'img/products/img5.jpg', 'Серебряные часы с ремешком'),
('Sports watches', 25900, 'img/products/img6.jpg', 'Спортивные часы с силиконовым ремешком'),
('Luxury watches', 68900, 'img/products/img7.jpg', 'Уникальные бизнес часы с кожанным ремешком'),
('Watch silicone strap', 1560, 'img/products/img8.jpg', 'Спортивные часы с силиконовым ремешком'),
('Sports Watch', 1690, 'img/products/img9.jpg', 'Спортивные часы с силиконовым ремешком в металическом корпусе'),
('Gold watch', 4990, 'img/products/img10.jpg', 'Часы для деловых людей с позолотой ремешка');

insert into products_categories (product_id, category_id)
values (1, 1),
       (2, 1),
       (3, 1),
       (4, 1),
       (5, 1),
       (6, 1),
       (7, 1),
       (8, 1),
       (9, 1),
       (10, 1);