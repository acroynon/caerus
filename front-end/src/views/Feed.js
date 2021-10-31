import React from 'react';
import Grid from '@mui/material/Grid'
import Paper from '@mui/material/Paper'
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import IconButton from '@mui/material/IconButton';
import SendIcon from '@mui/icons-material/Send';
import ThumbUpIcon from '@mui/icons-material/ThumbUp';
import { Box } from '@mui/system';
import StatusService from '../service/StatusService'

const paperStyle = {padding: '30px 20px', width: 600, margin: '20px auto'}

function Feed(){
    function statusSubmitHandler(){
        StatusService.postStatus("Dummy Status");
    }

	return (
        <div>
            <Grid>
                <Paper elevation={20} style={paperStyle}>
                    <form>
                        <Grid container spacing={2} direction="row">
                            <Grid item xs={11}>
                                <TextField fullWidth label="Status" name="status" variant="outlined" multiline rows={2} />
                            </Grid>
                            <IconButton onClick={statusSubmitHandler}><SendIcon/></IconButton>
                        </Grid>
                    </form>
                </Paper>
            </Grid>
            <Grid>
                <Paper elevation={20} style={paperStyle}>
                        <Stack spacing={2}>
                            <div>Posted by: {'<someone>'}</div>
                            <div>Hello World</div>
                            <Box style={{textAlign:'right'}}>
                                <IconButton aria-label="like">
                                    <ThumbUpIcon />
                                </IconButton>
                            </Box>
                        </Stack>
                </Paper>
            </Grid>
        </div>
	);
}

export default Feed;