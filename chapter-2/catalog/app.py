from flask import Flask, jsonify
import json
import os

app = Flask(__name__)

# Load products from JSON file
def load_products():
    file_path = os.path.join(os.path.dirname(__file__), 'data', 'products.json')
    with open(file_path, 'r') as f:
        return json.load(f)

products = load_products()

# Endpoint to list all products
@app.route('/api/products', methods=['GET'])
def get_products():
    return jsonify(products)

# Endpoint to get a single product by ID
@app.route('/api/products/<int:product_id>', methods=['GET'])
def get_product(product_id):
    product = next((p for p in products if p['id'] == product_id), None)
    if product:
        return jsonify(product)
    return jsonify({'error': 'Product not found'}), 404

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8080)
