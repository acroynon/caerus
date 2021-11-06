import { React, useState, useEffect } from 'react';
import Grid from '@mui/material/Grid'
import StatusService from '../service/StatusService'
import StatusUpdate from './StatusUpdate';

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
            {statusUpdates.map(status => {
                return (
                    <StatusUpdate  key={status.guid} status={status}/>
                );
            })}
        </Grid>
	);
}

export default Feed;