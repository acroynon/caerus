import React from 'react';
import Grid from '@mui/material/Grid'
import Paper from '@mui/material/Paper'
import Typography from '@mui/material/Typography';
import AndroidIcon from '@mui/icons-material/Android';

const paperStyle = {padding: '30px 20px', width: 600, margin: '20px auto'}

function Home(){
    return (
            <Paper elevation={20} style={paperStyle} >
            <Grid container spacing={2} 
                    direction="row" 
                    justifyContent="center"
                    alignItems="center"> 
                <Grid item xs={6} align="center" >
                    <Typography variant="h1" component="h2">
                        404
                    </Typography>
                    <Typography variant="button" component="div" >
                        Page not found.
                    </Typography>
                </Grid>
                <Grid item xs={5} align="center">
                    <Typography variant="h6" gutterBottom component="div">
                        These are not the droids you're looking for
                    </Typography>
                    <AndroidIcon />
                </Grid>
        </Grid>
            </Paper>
    )
};

export default Home;