const express = require('express'); 
const app = express(); 
app.get('/api/orders/stats', (req, res) => res.json({ message: 'Order stats' })); 
app.post('/api/orders', (req, res) => res.json({ message: 'Order created' })); 
app.listen(8080); 
