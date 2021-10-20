import React from 'react';
import { Route } from 'react-router-dom';
import Navigation from './components/Navigation'
import Home from './views/Home'
import Profile from './views/Profile'
import Login from './views/Login'
import Register from './views/Register'

function App() {
  return (
    <div>
      <Navigation />
      <div>
        <Route exact path="/">
          <Home />
        </Route>
        <Route exact path="/profile">
          <Profile />
        </Route>
        <Route exact path="/login">
          <Login />
        </Route>
        <Route exact path="/register">
          <Register />
        </Route>
      </div>
    </div>
  );
}

export default App;
