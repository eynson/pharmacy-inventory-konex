CREATE TABLE medicines (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    factory_laboratory VARCHAR(255) NOT NULL,
    manufacturing_date TIMESTAMP NOT NULL,
    expiration_date TIMESTAMP NOT NULL,
    quantity_in_stock INTEGER NOT NULL,
    unit_value DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT check_quantity CHECK (quantity_in_stock >= 0),
    CONSTRAINT check_unit_value CHECK (unit_value > 0)
);

CREATE INDEX idx_medicines_name ON medicines(name);
CREATE INDEX idx_medicines_factory ON medicines(factory_laboratory);
