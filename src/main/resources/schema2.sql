CREATE SEQUENCE transaction_seq;
ALTER sequence transaction_seq restart WITH (select MAX(id) from transaction) +1;