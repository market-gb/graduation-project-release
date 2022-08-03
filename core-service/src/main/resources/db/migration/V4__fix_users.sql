ALTER TABLE users ADD CONSTRAINT users_username_key UNIQUE (username);
ALTER TABLE users ADD CONSTRAINT users_password_key UNIQUE (password);
ALTER TABLE orders ADD COLUMN full_name varchar(80);
ALTER TABLE orders ALTER COLUMN full_name SET NOT NULL;