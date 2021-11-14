import { React, useState, useEffect } from 'react';
import Paper from '@mui/material/Paper'
import Stack from '@mui/material/Stack';
import IconButton from '@mui/material/IconButton';
import ThumbUpIcon from '@mui/icons-material/ThumbUp';
import { Box } from '@mui/system';

const paperStyle = {padding: '30px 20px', width: 600, margin: '20px auto'}

function StatusUpdate(props){
    const status = props.status;
    const intervals = [
        { label: 'year', seconds: 31536000 },
        { label: 'month', seconds: 2592000 },
        { label: 'day', seconds: 86400 },
        { label: 'hour', seconds: 3600 },
        { label: 'minute', seconds: 60 },
        { label: 'second', seconds: 1 }
    ];
    
    function timeSince(date) {
        const seconds = Math.floor((Date.now() - date.getTime()) / 1000);
        const interval = intervals.find(i => i.seconds < seconds);
        if(interval == null){
            return "just now";
        }
        const count = Math.floor(seconds / interval.seconds);
        return `${count} ${interval.label}${count !== 1 ? 's' : ''} ago`;
    }


    return (
        <Paper elevation={20} style={paperStyle}>
            <Stack spacing={2}>
                <div>{status.content}</div>
                <Box style={{textAlign:'right'}}>
                    <IconButton aria-label="like">
                        <ThumbUpIcon />
                    </IconButton>
                    <div>@{status.authorUsername} | {timeSince(new Date(status.creationDate))}</div>
                </Box>
            </Stack>
        </Paper>
    );
}

export default StatusUpdate;