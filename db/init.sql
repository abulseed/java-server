
-- Create Product table
CREATE TABLE IF NOT EXISTS product (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(12,2) NOT NULL,
    quantity INT NOT NULL,
    photo_urls TEXT[]
);
