import React from 'react';
import { NavLink } from 'react-router-dom'
import ListItem from '@mui/material/ListItem';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import AuthService from '../service/AuthService';

function NavigationLink(props){
    var link = props.link;
    var isAuthenticated = AuthService.isLoggedIn();

    if((isAuthenticated && link.showOnAuthenticated)
        || (!isAuthenticated && link.showOnAnonymous)){
        return (
            <ListItem button
                component={NavLink} 
                to={link.link}
                onClick={props.onClick}
            >
                <ListItemIcon>
                    {link.icon}
                </ListItemIcon>
                <ListItemText primary={link.text} />
            </ListItem>
        )
    }
    return (
        <span></span>
    )
   
}

export default NavigationLink;