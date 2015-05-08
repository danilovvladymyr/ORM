CREATE DATABASE shop;
use shop;
CREATE USER 'shop';
GRANT ALL ON shop.* TO 'shop';
CREATE USER 'viewer';
GRANT SELECT ON shop.* to 'viewer';
CREATE TABLE category (
  category_id int NOT NULL AUTO_INCREMENT,
  category_name char(50) NOT NULL,
  PRIMARY KEY (category_id)
);
INSERT INTO category (category_name) VALUES
  ('clothing'),
  ('shoes'),
  ('clocks'),
  ('furniture');
CREATE TABLE good (
  good_id int NOT NULL AUTO_INCREMENT,
  category_id int NOT NULL,
  name char(50) NOT NULL,
  price double NOT NULL,
  PRIMARY KEY (good_id),
  FOREIGN KEY (category_id)
);
-- ALTER TABLE good ADD CONSTRAINT fk_good_categories FOREIGN KEY (category_id) REFERENCES category (category_id);
INSERT INTO good (name, price, category_id) VALUES
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
UPDATE good SET price=3.50
  WHERE good_id=1;
UPDATE good SET price=price*1.1;
DELETE FROM good
  WHERE good_id='2';
SELECT * FROM good ORDER BY name;
SELECT * FROM good ORDER BY price DESC;
SELECT name, price FROM good ORDER BY price DESC LIMIT 3;
SELECT name, price FROM good ORDER BY price LIMIT 3;
SELECT name, price FROM good ORDER BY price LIMIT 3, 3;
SELECT name FROM good WHERE price IN
(SELECT MAX(price) FROM good);
SELECT name FROM good WHERE price IN
(SELECT MIN(price) FROM good);
SELECT COUNT(*) AS quantity FROM good;
SELECT AVG(price) FROM good;
CREATE VIEW top_three AS
SELECT * FROM good ORDER BY price DESC LIMIT 3;