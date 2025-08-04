-- Sample data for inventory_item table based on InventoryItem.java entity
INSERT INTO inventory_item (
    product_id,
    quantity,
    product_name,
    category,
    last_updated,
    last_modified_by,
    trace_id,
    span_id
) VALUES 
-- Electronics category
('PROD-001', 100, 'Dell XPS 13 Laptop', 'Electronics', CURRENT_TIMESTAMP, 'system', '4f1c3c1d8b', 'a1b2c3d4'),
('PROD-002', 50, 'iPhone 14 Pro', 'Electronics', CURRENT_TIMESTAMP, 'system', '4f1c3c1d8c', 'b2c3d4e5'),
('PROD-003', 75, 'Samsung 4K TV', 'Electronics', CURRENT_TIMESTAMP, 'system', '4f1c3c1d8d', 'c3d4e5f6'),
('PROD-004', 30, 'MacBook Pro M2', 'Electronics', CURRENT_TIMESTAMP, 'system', '4f1c3c1d8e', 'd4e5f6g7'),

-- Clothing category
('PROD-005', 200, 'Cotton T-Shirt', 'Clothing', CURRENT_TIMESTAMP, 'system', '4f1c3c1d8f', 'e5f6g7h8'),
('PROD-006', 150, 'Denim Jeans', 'Clothing', CURRENT_TIMESTAMP, 'system', '4f1c3c1d8g', 'f6g7h8i9'),
('PROD-007', 80, 'Winter Jacket', 'Clothing', CURRENT_TIMESTAMP, 'system', '4f1c3c1d8h', 'g7h8i9j0'),

-- Books category
('PROD-008', 120, 'Java Programming Guide', 'Books', CURRENT_TIMESTAMP, 'system', '4f1c3c1d8i', 'h8i9j0k1'),
('PROD-009', 90, 'Spring Framework Cookbook', 'Books', CURRENT_TIMESTAMP, 'system', '4f1c3c1d8j', 'i9j0k1l2'),
('PROD-010', 60, 'Microservices Architecture', 'Books', CURRENT_TIMESTAMP, 'system', '4f1c3c1d8k', 'j0k1l2m3');