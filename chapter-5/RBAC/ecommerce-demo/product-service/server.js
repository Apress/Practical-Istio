const express = require('express'); 
const app = express(); 
app.get('/api/products/*', (req, res) => res.json({ message: 'Product data' })); 
app.get('/api/products/stats', (req, res) => res.json({ message: 'Product stats' })); 
app.listen(8080);