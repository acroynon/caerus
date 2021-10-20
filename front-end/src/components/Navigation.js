import {React, useState} from 'react';
import { NavLink } from 'react-router-dom'
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import Drawer from '@mui/material/Drawer';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import HomeIcon from '@mui/icons-material/Home';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import LoginIcon from '@mui/icons-material/Login';
import AppRegistrationIcon from '@mui/icons-material/AppRegistration';

function Navigation(){
	const [isMenuOpen, setIsMenuOpen] = useState(false);
	const links = [
		{
			text: 'Home',
			icon: <HomeIcon />,
			link: '/'
		},
		{
			text: 'Profile',
			icon: <AccountCircleIcon />,
			link: '/profile'
		},
		{
			text: 'Login',
			icon: <LoginIcon />,
			link: '/login'
		},
		{
			text: 'Register',
			icon: <AppRegistrationIcon />,
			link: '/register'
		}
	]

	return (
		<Box sx={{ flexGrow: 1 }}>
	      <AppBar position="static">
	        <Toolbar>
	          <IconButton
	            size="large"
	            edge="start"
	            color="inherit"
	            aria-label="menu"
	            sx={{ mr: 2 }}
	            onClick={() => setIsMenuOpen(true)}
	          >
	            <MenuIcon />
	          </IconButton>
	          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
	            Caerus
	          </Typography>
	          <Drawer
	          	open={isMenuOpen}
	          	anchor='left'
	          >
      			<List>
		          	{links.map(link => {
		      			return (
			          		<ListItem button key={link.text} 
			          			component={NavLink} 
			          			to={link.link} 
			          			onClick={() => setIsMenuOpen(false)}
			          		>
					            <ListItemIcon>
					              {link.icon}
					            </ListItemIcon>
					            <ListItemText primary={link.text} />
				          </ListItem>
		      			)
		      		})}
          		</List>

	          </Drawer>
	        </Toolbar>
	      </AppBar>
	    </Box>
	);
}

export default Navigation;