# Db schema
 
# --- !Ups
 
CREATE TABLE Customer (
  id SERIAL PRIMARY KEY,
  email varchar(255) UNIQUE NOT NULL,
  username varchar(255) UNIQUE NOT NULL,
  password varchar(255) NOT NULL
);

CREATE TABLE Category (
  id SERIAL PRIMARY KEY,
  title varchar(255) UNIQUE NOT NULL
);

CREATE TABLE Item (
  id SERIAL PRIMARY KEY,
  customer_id INTEGER REFERENCES Customer(id) ON DELETE CASCADE,
  category_id INTEGER REFERENCES Category(id),
  title varchar(255) NOT NULL,
  description text,
  vaihdossa varchar(255),
  created_at timestamp,
  locked_at timestamp,
  accepted_offer_at timestamp,
  accepted_customer_id INTEGER REFERENCES Customer(id) ON DELETE SET NULL,
  locked_customer_id INTEGER REFERENCES Customer(id) ON DELETE SET NULL
);

CREATE TABLE CounterOffer (
  customer_id INTEGER REFERENCES Customer(id) ON DELETE CASCADE,
  item_id INTEGER REFERENCES Item(id) ON DELETE CASCADE,
  description text,
  PRIMARY KEY (customer_id, item_id)
);

# --- !Downs
 
DROP TABLE IF EXISTS Customer CASCADE;
DROP TABLE IF EXISTS Item CASCADE;
DROP TABLE IF EXISTS Category CASCADE;
DROP TABLE IF EXISTS CounterOffer CASCADE;