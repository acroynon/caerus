import { React, useState } from 'react';
import { AuthedContext } from './context/AuthedContext';
import { Route } from 'react-router-dom';
import PrivateRoute from './components/PrivateRoute';
import { Switch } from 'react-router';
import Navigation from './components/Navigation'
import Home from './views/Home'
import Profile from './views/Profile'
import Login from './views/Login'
import Register from './views/Register'
import Logout from './views/Logout'
import GenericNotFound from './views/GenericNotFound';

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  return (
    <AuthedContext.Provider value={{isAuthenticated, setIsAuthenticated}}>
      <Navigation />
      <Switch>
        <PrivateRoute exact path="/" component={Home} isAuthenticated={isAuthenticated} />
        <PrivateRoute exact path="/profile" component={Profile} isAuthenticated={isAuthenticated} />
        <PrivateRoute exact path="/discover" component={Home} isAuthenticated={isAuthenticated} />
        <Route exact path="/login" component={Login} isAuthenticated={isAuthenticated} />
        <Route exact path="/register" component={Register} isAuthenticated={isAuthenticated} />
        <Route exact path="/logout" component={Logout} isAuthenticated={isAuthenticated} />
        <Route path="*" component={GenericNotFound} /> 
      </Switch>
    </AuthedContext.Provider>
  );
}

export default App;
