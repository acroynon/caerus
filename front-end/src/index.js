import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter } from 'react-router-dom';
import './index.css';
import App from './App';
import axios from 'axios'

ReactDOM.render(
  <BrowserRouter>
    <App />
  </BrowserRouter>,
  document.getElementById('root')
);

axios.interceptors.request.use(function (config) {
  const token = sessionStorage.getItem("access_token");
  if(token != null){
    config.headers.Authorization = "Bearer " + token;
  }
  return config;
});

