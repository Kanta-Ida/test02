DROP TABLE IN_OUT_LOG if exists
CREATE TABLE IN_OUT_LOG(NO INTEGER IDENTITY PRIMARY KEY,
IN_OUT_TYPE VARCHAR(10) NOT NULL,
GOODS_CODE INTEGER,
QUANTITY INTEGER)