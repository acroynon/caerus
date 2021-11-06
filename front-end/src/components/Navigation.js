import {React, useState} from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import Drawer from '@mui/material/Drawer';
import List from '@mui/material/List';
import HomeIcon from '@mui/icons-material/Home';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import SearchIcon from '@mui/icons-material/Search';
import LoginIcon from '@mui/icons-material/Login';
import LogoutIcon from '@mui/icons-material/Logout';
import AppRegistrationIcon from '@mui/icons-material/AppRegistration';
import NavigationLink from './NavigationLink';

function Navigation(){
	const [isMenuOpen, setIsMenuOpen] = useState(false);
	const links = [
		{
			text: 'Home',
			icon: <HomeIcon />,
			link: '/',
			showOnAuthenticated: true,
			showOnAnonymous: false
		},
		{
			text: 'Profile',
			icon: <AccountCircleIcon />,
			link: '/profile',
			showOnAuthenticated: true,
			showOnAnonymous: false
		},
		{
			text: 'Discover',
			icon: <SearchIcon />,
			link: '/discover',
			showOnAuthenticated: true,
			showOnAnonymous: false
		},
		{
			text: 'Login',
			icon: <LoginIcon />,
			link: '/login',
			showOnAuthenticated: false,
			showOnAnonymous: true
		},
		{
			text: 'Register',
			icon: <AppRegistrationIcon />,
			link: '/register',
			showOnAuthenticated: false,
			showOnAnonymous: true
		},
		{
			text: 'Logout',
			icon: <LogoutIcon />,
			link: '/logout',
			showOnAuthenticated: true,
			showOnAnonymous: false
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
							  <NavigationLink 
							  	key={link.link}
							  	link={link}
								onClick = {() => setIsMenuOpen(false)}
							  />
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