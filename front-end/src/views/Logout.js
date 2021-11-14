import { React, useContext } from 'react';
import AuthService from '../service/AuthService';
import { useHistory } from "react-router-dom";
import { AuthedContext } from '../context/AuthedContext';


function Feed(){
    const history = useHistory();
    const {setIsAuthenticated} = useContext(AuthedContext);
	AuthService.logout();
    setIsAuthenticated(false);
    history.replace("/login");
    return (<span></span>)
}

export default Feed;