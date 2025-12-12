CREATE TABLE sales (
    id VARCHAR(36) PRIMARY KEY,
    medicine_id VARCHAR(36) NOT NULL,
    medicine_name VARCHAR(255) NOT NULL,
    quantity_sold INTEGER NOT NULL,
    unit_value DECIMAL(10, 2) NOT NULL,
    total_value DECIMAL(10, 2) NOT NULL,
    sale_date_time TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_sales_medicine FOREIGN KEY (medicine_id) REFERENCES medicines(id),
    CONSTRAINT check_quantity_sold CHECK (quantity_sold > 0),
    CONSTRAINT check_total_value CHECK (total_value > 0)
);

CREATE INDEX idx_sales_medicine_id ON sales(medicine_id);
CREATE INDEX idx_sales_date_time ON sales(sale_date_time);
