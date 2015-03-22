# Db schema
 
# --- !Ups
 
CREATE TABLE Customer (
  id SERIAL PRIMARY KEY,
  email varchar(255) NOT NULL,
  username varchar(255) NOT NULL,
  password varchar(255) NOT NULL
);

CREATE TABLE Category (
  id SERIAL PRIMARY KEY,
  title varchar(255) NOT NULL
);

CREATE TABLE Item (
  id SERIAL PRIMARY KEY,
  customer_id INTEGER REFERENCES Customer(id),
  category_id INTEGER REFERENCES Category(id),
  title varchar(255) NOT NULL,
  description text,
  vaihdossa varchar(255),
  created_at timestamp,
  locked_at timestamp,
  accepted_offer_at timestamp,
  accepted_customer_id INTEGER REFERENCES Customer(id),
  locked_customer_id INTEGER REFERENCES Customer(id)
);

CREATE TABLE CounterOffer (
  id SERIAL PRIMARY KEY,
  customer_id INTEGER REFERENCES Customer(id),
  item_id INTEGER REFERENCES Item(id),
  description text
);

# --- !Downs
 
DROP TABLE IF EXISTS Customer CASCADE;
DROP TABLE IF EXISTS Item CASCADE;
DROP TABLE IF EXISTS Category CASCADE;
DROP TABLE IF EXISTS CounterOffer CASCADE;