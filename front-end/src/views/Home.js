import { React, useState, useEffect } from 'react';
import Grid from '@mui/material/Grid'
import Paper from '@mui/material/Paper'
import TextField from '@mui/material/TextField';
import IconButton from '@mui/material/IconButton';
import SendIcon from '@mui/icons-material/Send';
import Feed from '../components/Feed';
import StatusService from '../service/StatusService'

const paperStyle = {padding: '30px 20px', width: 600, margin: '20px auto'}

function Home(){

    const [statusUpdate, setStatusUpdate] = useState();
    const [statusFeed, setStatusFeed] = useState([]);

    // Initial Status feed load
    useEffect(() => {
        refreshFeed();
    }, []);

    function statusSubmitHandler(e){
        StatusService.postStatus(statusUpdate)
            .then(res => {
                setStatusUpdate("");
                refreshFeed();
            });
    }
    
    function refreshFeed(){
        StatusService.getStatusFeed()
            .then(response => {
                setStatusFeed(response.data.content);
            })
    }

	return (
        <div>
            <Grid>
                <Paper elevation={20} style={paperStyle}>
                    <form>
                        <Grid container spacing={2} direction="row">
                            <Grid item xs={11}>
                                <TextField fullWidth label="What are you thinking..." name="status" variant="outlined" multiline rows={2}
                                    onChange={event => setStatusUpdate(event.target.value)} 
                                    value={statusUpdate} />
                            </Grid>
                            <IconButton onClick={statusSubmitHandler}><SendIcon/></IconButton>
                        </Grid>
                    </form>
                </Paper>
            </Grid>
            <Feed feed={statusFeed}/>
        </div>
	);
}

export default Home;