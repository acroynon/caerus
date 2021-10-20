import React from 'react';
import Grid from '@mui/material/Grid'
import Paper from '@mui/material/Paper'
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';

const paperStyle = {padding: '30px 20px', width: 300, margin: '20px auto'}

function Register(){
	return (
		<Grid>
			<Paper elevation={20} style={paperStyle}>
				<h2>Register</h2>
				<form>
					<Stack spacing={2}>
						<TextField fullWidth label="Username" variant="standard"/>
						<TextField fullWidth label="Password" variant="standard" type="password" />
						<TextField fullWidth label="Confirm Password" variant="standard" type="password" />
						<Button variant="outlined">Register</Button>
					</Stack>
				</form>
			</Paper>
		</Grid>
	);
}

export default Register;