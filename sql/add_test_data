-- Customer-taulun testidata
INSERT INTO Customer (email, username, password) VALUES ('erkki@esimerkki.com', 'erkki.esimerkki', 'salasana');
INSERT INTO Customer (email, username, password) VALUES ('vasta@tarjous.com', 'vastatarjous', 'salasana');

-- Category-taulun testidata
INSERT INTO Category (name) VALUES ('Pelit');
INSERT INTO Category (name) VALUES ('Taide');
INSERT INTO Category (name) VALUES ('Urheilu');

-- Item-taulun testidata
INSERT INTO Item (customer_id, category_id, title, description, vaihdossa, created_at, locked_at, accepted_offer_at, accepted_customer_id, locked_customer_id) VALUES (1, 1, 'sukset', 'laadukkaat sukset', 'tennismaila', CURRENT_TIMESTAMP, null, null, null, null);

-- CounterOffer-taulun testidata
INSERT INTO CounterOffer (customer_id, item_id, description) VALUES (2, 1, 'jääkiekkomaila');