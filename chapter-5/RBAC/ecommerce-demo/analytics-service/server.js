const express = require('express'); 
const app = express(); 
app.get('/api/analytics', (req, res) => res.json({ message: 'Analytics data' })); 
app.listen(8080); 
