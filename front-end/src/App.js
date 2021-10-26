import React from 'react';
import { Route } from 'react-router-dom';
import Navigation from './components/Navigation'
import Home from './views/Home'
import Profile from './views/Profile'
import Feed from './views/Feed'
import Login from './views/Login'
import Register from './views/Register'
import Logout from './views/Logout'

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
        <Route exact path="/feed">
          <Feed />
        </Route>
        <Route exact path="/login">
          <Login />
        </Route>
        <Route exact path="/register">
          <Register />
        </Route>
        <Route exact path="/logout">
          <Logout />
        </Route>
      </div>
    </div>
  );
}

export default App;
