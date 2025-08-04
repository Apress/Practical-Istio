-- Sample data for payments table
INSERT INTO payments (
    payment_id,
    order_id,
    amount,
    status,
    currency,
    payment_method,
    transaction_id,
    error_message,
    customer_id,
    gateway_reference,
    gateway_response,
    created_at,
    updated_at,
    created_by,
    last_modified_by,
    trace_id,
    span_id
) VALUES 
-- Successful payments
('PAY-001', 'ORDER-001', 1299.99, 'SUCCESSFUL', 'USD', 'CREDIT_CARD', 'TXN-001', NULL, 
    'CUST-001', 'GATE-001', '{"status": "approved", "code": "200"}', 
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system', '5e2d4c3b1a', 'k1l2m3n4'),
('PAY-002', 'ORDER-002', 799.50, 'SUCCESSFUL', 'USD', 'PAYPAL', 'TXN-002', NULL,
    'CUST-002', 'GATE-002', '{"status": "approved", "code": "200"}',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system', '5e2d4c3b1b', 'l2m3n4o5'),
-- Failed payment
('PAY-003', 'ORDER-003', 2499.99, 'FAILED', 'USD', 'CREDIT_CARD', 'TXN-003', 'Insufficient funds',
    'CUST-003', 'GATE-003', '{"status": "declined", "code": "402"}',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system', '5e2d4c3b1c', 'm3n4o5p6'),
-- Pending payment
('PAY-004', 'ORDER-004', 599.99, 'PENDING', 'USD', 'BANK_TRANSFER', 'TXN-004', NULL,
    'CUST-004', 'GATE-004', '{"status": "pending", "code": "100"}',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system', '5e2d4c3b1d', 'n4o5p6q7'),
-- Processing payment
('PAY-005', 'ORDER-005', 1899.99, 'PROCESSING', 'USD', 'CREDIT_CARD', 'TXN-005', NULL,
    'CUST-005', 'GATE-005', '{"status": "processing", "code": "102"}',
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'system', 'system', '5e2d4c3b1e', 'o5p6q7r8');

-- Sample data for payment_history table
INSERT INTO payment_history (
    payment_id,
    order_id,
    old_status,
    new_status,
    change_reason,
    error_message,
    timestamp,
    changed_by,
    trace_id,
    span_id
) VALUES 
-- History for PAY-001
('PAY-001', 'ORDER-001', 'PENDING', 'PROCESSING', 'Payment processing initiated', NULL,
    DATEADD('HOUR', -1, CURRENT_TIMESTAMP), 'system', '5e2d4c3b1a', 'p6q7r8s9'),
('PAY-001', 'ORDER-001', 'PROCESSING', 'SUCCESSFUL', 'Payment successfully processed', NULL,
    DATEADD('MINUTE', -45, CURRENT_TIMESTAMP), 'system', '5e2d4c3b1a', 'q7r8s9t0'),
-- History for PAY-003 (Failed payment)
('PAY-003', 'ORDER-003', 'PENDING', 'PROCESSING', 'Payment processing initiated', NULL,
    DATEADD('HOUR', -2, CURRENT_TIMESTAMP), 'system', '5e2d4c3b1c', 'r8s9t0u1'),
('PAY-003', 'ORDER-003', 'PROCESSING', 'FAILED', 'Payment processing failed', 'Insufficient funds',
    DATEADD('MINUTE', -110, CURRENT_TIMESTAMP), 'system', '5e2d4c3b1c', 's9t0u1v2'),
-- History for PAY-004 (Pending payment)
('PAY-004', 'ORDER-004', 'PENDING', 'PENDING', 'Payment initiated', NULL,
    DATEADD('MINUTE', -30, CURRENT_TIMESTAMP), 'system', '5e2d4c3b1d', 't0u1v2w3'),
-- History for PAY-005 (Processing payment)
('PAY-005', 'ORDER-005', 'PENDING', 'PROCESSING', 'Payment processing started', NULL,
    DATEADD('MINUTE', -15, CURRENT_TIMESTAMP), 'system', '5e2d4c3b1e', 'u1v2w3x4');