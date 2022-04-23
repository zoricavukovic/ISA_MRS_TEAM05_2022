import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8092/bookingApp/',
    headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      }
});


export default api;