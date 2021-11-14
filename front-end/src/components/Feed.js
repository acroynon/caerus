import React from 'react';
import Grid from '@mui/material/Grid'
import StatusUpdate from './StatusUpdate';

function Feed(props){ 
    let feed = props.feed;
    return (
        <Grid>
            {feed.map((status, i) => {
                return (
                    <StatusUpdate key={i} status={status}/>
                );
            })}
        </Grid>
	);
}

export default Feed;