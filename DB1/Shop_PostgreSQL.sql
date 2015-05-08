CREATE DATABASE shop;
CREATE USER shop;
GRANT ALL PRIVILEGES ON DATABASE shop TO shop;
CREATE USER viewer;
GRANT CONNECT ON DATABASE shop TO viewer;
CREATE TABLE "category" (
 category_id serial,
 category_name varchar(50) NOT NULL,
 PRIMARY KEY (category_id)
);
INSERT INTO "category" (category_name) VALUES
  ('clothing'),
  ('shoes'),
  ('clocks'),
  ('furniture');
CREATE TABLE "good" (
 good_id serial,
 category_id integer NOT NULL,
 good_name varchar(50) NOT NULL,
 good_price double precision NOT NULL,
 PRIMARY KEY (good_id)
);
ALTER TABLE "good" ADD CONSTRAINT fk_good_category FOREIGN KEY (category_id)
 REFERENCES "category" (category_id);
INSERT INTO "good" (good_name, good_price, category_id) VALUES
  ('Fellini' , '1.00', '2'),
  ('Dino Richi', '1.00', '2'),
  ('Marc O`Polo', '1.00', '1'),
  ('Mexx', '1.00', '3'),
  ('Mexx', '1.00', '4'),
  ('Adidas', '1.00', '2'),
  ('Puma', '1.00', '1'),
  ('ECCO', '1.00', '2'),
  ('Colins', '1.00', '1'),
  ('Zenith', '1.00', '3'),
  ('Orient', '1.00', '3'),
  ('Patek Phillip', '1.00', '3'),
  ('Zarina', '1.00', '4');
UPDATE "good" SET good_price=3.50
  WHERE good_id=1;
UPDATE "good" SET good_price=price*1.1;
DELETE FROM "good"
  WHERE good_id='2';
SELECT * FROM "good" ORDER BY good_name ASC;
SELECT * FROM "good" ORDER BY good_price DESC;
SELECT good_name, good_price FROM "good" ORDER BY good_price DESC LIMIT 3;
SELECT good_name, good_price FROM "good" ORDER BY good_price LIMIT 3;
SELECT good_name, good_price FROM "good" ORDER BY good_price LIMIT 3 OFFSET 3;
SELECT good_name FROM "good" WHERE good_price IN
(SELECT MAX(good_price) FROM "good");
SELECT good_name FROM "good" WHERE good_price IN
(SELECT MIN(good_price) FROM "good");
SELECT COUNT(*) AS quantity FROM "good";
SELECT AVG(good_price) FROM "good";
CREATE VIEW top_three AS
SELECT * FROM "good" ORDER BY good_price DESC LIMIT 3;