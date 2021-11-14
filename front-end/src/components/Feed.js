import React from 'react';
import Grid from '@mui/material/Grid'
import StatusUpdate from './StatusUpdate';

function Feed(props){ 
    let feed = props.feed;
    return (
        <Grid>
            {feed.map(status => {
                return (
                    <StatusUpdate  key={status.guid} status={status}/>
                );
            })}
        </Grid>
	);
}

export default Feed;