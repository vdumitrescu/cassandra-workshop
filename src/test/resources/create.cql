CREATE TABLE metrics (
   name text,
   shipped text,
   ts timestamp,
   value double,
   primary key (name, shipped, ts))
WITH CLUSTERING ORDER BY (shipped ASC, ts DESC);