#!/bin/bash

# JSON payload for the request
payload='{
    "customerId": "CUST-001",
    "items": [
        {
            "productId": "PROD-001",
            "quantity": 10,
            "unitPrice": 1299.99,
            "productName": "Dell XPS 13 Laptop",
            "productCategory": "Electronics"
        },
        {
            "productId": "PROD-008",
            "quantity": 20,
            "unitPrice": 49.99,
            "productName": "Java Programming Guide",
            "productCategory": "Books"
        }
    ],
    "customerNote": "Please deliver during business hours",
    "promoCode": "WELCOME10"
}'

# Loop 100 times
for i in {1..100}; do
    echo "Sending request $i of 100..."
    curl -X POST 'http://172.18.0.2/orders/api' \
        -H 'Content-Type: application/json' \
        -d "$payload"
    
    # Add a small delay (0.5 seconds) between requests to prevent overwhelming the system
    sleep 0.5
    
    echo -e "\n-------------------\n"
done

echo "Load test complete!"