import axios from 'axios'

const options = {
    headers: {
        'Access-Control-Allow-Origin' : '*',
        'Access-Control-Allow-Methods':'*'
    }
  };

class StatusService  {
    postStatus(content){
        return axios.post('/api/status-service/status', {
            content: content
        }, options)
    }

    getStatusFeed(){
        return axios.get("/api/status-service/status");
    }

    likeStatus(statusId){

    }
}

export default new StatusService();