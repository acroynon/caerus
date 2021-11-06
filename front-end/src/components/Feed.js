import { React, useState, useEffect } from 'react';
import Grid from '@mui/material/Grid'
import Paper from '@mui/material/Paper'
import Stack from '@mui/material/Stack';
import IconButton from '@mui/material/IconButton';
import ThumbUpIcon from '@mui/icons-material/ThumbUp';
import { Box } from '@mui/system';
import StatusService from '../service/StatusService'

const paperStyle = {padding: '30px 20px', width: 600, margin: '20px auto'}

function Feed(){
    const [statusUpdates, setStatusUpdates] = useState([]);
    var pageNumber = 0;

    // Initial Status feed load
    useEffect(() => {
        StatusService.getStatusFeed()
            .then(response => {
                console.log(response);
                setStatusUpdates(response.data.content);
            })
    }, []);
    

	return (
        <Grid>
            {statusUpdates.map(update => {
                return (
                    <Paper elevation={20} style={paperStyle} key={update.guid}>
                        <Stack spacing={2}>
                            <div>{update.content}</div>
                            <Box style={{textAlign:'right'}}>
                                <IconButton aria-label="like">
                                    <ThumbUpIcon />
                                </IconButton>
                                <div>Posted by: {update.authorUsername} @ {update.creationDate}</div>
                            </Box>
                        </Stack>
                    </Paper>
                );
            })}
        </Grid>
	);
}

export default Feed;