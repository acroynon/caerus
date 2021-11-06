import axios from 'axios'

const options = {
    headers: {
        'Access-Control-Allow-Origin' : '*',
        'Access-Control-Allow-Methods':'*'
    }
  };

class StatusService  {
    postStatus(content){
        axios.post('/api/status-service/status', {
            content: content
        }, options)
        .then(response => {
            console.log("res", response);
        })
        .catch(err => {
            console.log("err", err)
        })
    }

    getStatusFeed(){
        return axios.get("/api/status-service/status");
    }

    likeStatus(statusId){

    }
}

export default new StatusService();