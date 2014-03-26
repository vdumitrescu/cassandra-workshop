Cassandra Workshop


CREATE TABLE metrics (
  name text,
  shipped text,
  ts timestamp,
  value double,
  PRIMARY KEY (name, shipped, ts)
) WITH CLUSTERING ORDER BY (shipped ASC, ts DESC);

