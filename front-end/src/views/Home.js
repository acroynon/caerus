import { React, useState } from 'react';
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
    function statusSubmitHandler(e){
        StatusService.postStatus(statusUpdate);
    }

	return (
        <div>
            <Grid>
                <Paper elevation={20} style={paperStyle}>
                    <form>
                        <Grid container spacing={2} direction="row">
                            <Grid item xs={11}>
                                <TextField fullWidth label="Status" name="status" variant="outlined" multiline rows={2}
                                    onChange={event => setStatusUpdate(event.target.value)} />
                            </Grid>
                            <IconButton onClick={statusSubmitHandler}><SendIcon/></IconButton>
                        </Grid>
                    </form>
                </Paper>
            </Grid>
            <Feed/>
        </div>
	);
}

export default Home;