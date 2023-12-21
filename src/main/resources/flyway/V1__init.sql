CREATE SEQUENCE transaction_seq;

CREATE TABLE IF NOT EXISTS transaction (
  id bigint NOT NULL DEFAULT NEXTVAL('transaction_seq') PRIMARY KEY,
  data VARCHAR(1000) NOT NULL,
  created_at TIMESTAMP NOT NULL default NOW()
);

--should execute after migration, to set the current sequence;
SELECT SETVAL('transaction_seq', (select MAX(id) from transaction));