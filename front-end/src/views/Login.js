import React from 'react';
import Grid from '@mui/material/Grid'
import Paper from '@mui/material/Paper'
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import AuthService from '../service/AuthService';

const paperStyle = {padding: '30px 20px', width: 300, margin: '20px auto'}

function Login(){
	function handleLogin(e){
		e.preventDefault();
		console.log("Handle input");
		var data = new FormData(e.target);
		AuthService.login(data.get("username"), data.get("password"));
	}
	return (
		<Grid>
			<Paper elevation={20} style={paperStyle}>
				<h2>Login</h2>
				<form onSubmit={handleLogin}>
					<Stack spacing={2}>
						<TextField fullWidth label="Username" name="username" variant="standard"/>
						<TextField fullWidth label="Password" name="password" variant="standard" type="password" />
						<Button type="submit" variant="outlined">Login</Button>
					</Stack>
				</form>
			</Paper>
		</Grid>
	);
}

export default Login;