CREATE TABLE "USERNAME"."retailer"(
	name VARCHAR(20) NOT NULL PRIMARY KEY, 
	address VARCHAR(80) NOT NULL, 
	creditLine DECIMAL(10, 2) NOT NULL, 
	phone VARCHAR(20) NOT NULL
);
CREATE TABLE "USERNAME"."product"(
	productName VARCHAR(20) NOT NULL PRIMARY KEY, 
	price DECIMAL(10, 2) NOT NULL, 
	stock INT NOT NULL
);
CREATE TABLE "USERNAME"."order"(
	orderId BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), 
	retailerName VARCHAR(20) NOT NULL REFERENCES "USERNAME"."retailer"(name), 
	orderDate DATE NOT NULL, 
	orderPrice DECIMAL(10, 2) NOT NULL, 
	status VARCHAR(20)
);
CREATE TABLE "USERNAME"."orderLine"(
	orderLineId BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), 
	orderId BIGINT NOT NULL REFERENCES "USERNAME"."order"(orderId), 
	price DECIMAL(10, 2) NOT NULL, 
	productName VARCHAR(20) NOT NULL REFERENCES "USERNAME"."product"(productName), 
	quantity INT NOT NULL
);
INSERT INTO "USERNAME"."retailer" (name, address, creditLine, phone) VALUES 
('KFT 1', 'Szeged, Dózsa György út 12.', 100000, '123-123-123'),
('KFT 2', 'Kecskemét, Petõfi út 2.', 150000, '111-1-1111'),
('RT 3', 'Budapest, Élmunkás tér', 10000, '333-153-673'),
('BT 4', 'Pécs, Ferencz Ferdinánd utca 33.', 2000000, '012024234');
INSERT INTO "USERNAME"."product" (productName, price, stock) VALUES
('tej', 12, 2000),
('kávé', 50, 200),
('ló', 135000, 1),
('kecske', 10000, 20),
('aszpirin', 30, 999);
INSERT INTO "USERNAME"."order" (retailerName, orderDate, orderPrice, status) VALUES
('KFT 1', '2018-01-01', 300, 'COMPLETED'),
('RT 3', '2018-05-16', 30000, 'UNDER_DELIVERY');
INSERT INTO "USERNAME"."orderLine" (orderId, price, productName, quantity) VALUES
(1, 150, 'tej', 100),
(1, 150, 'kávé', 20),
(2, 30000, 'kecske', 3);