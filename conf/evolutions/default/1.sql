# Users schema
 
# --- !Ups
 
CREATE TABLE Users (
  id SERIAL PRIMARY KEY,
  name varchar(50) NOT NULL,
  password varchar(50) NOT NULL
)
# --- !Downs
 
DROP TABLE Users;
