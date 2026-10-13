CREATE TABLE IF NOT EXISTS stock_item (id BIGINT PRIMARY KEY, name VARCHAR(120) NOT NULL, available INTEGER NOT NULL CHECK (available >= 0));
CREATE TABLE IF NOT EXISTS reservation (request_key VARCHAR(80) PRIMARY KEY, item_id BIGINT NOT NULL REFERENCES stock_item(id), quantity INTEGER NOT NULL CHECK (quantity > 0));
