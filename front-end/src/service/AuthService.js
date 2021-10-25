import axios from 'axios'

const options = {
    headers: {
        'Access-Control-Allow-Origin' : '*',
        'Access-Control-Allow-Methods':'*',
        "Content-Type": "application/json"
    }
  };

class AuthService {
    login(username, password){
        console.log(username,password)
        return axios.post("/api/auth/authenticate", {
            username: username,
            password: password
        }, options)
        .then(response => {
            console.log(response);
        });
    }
}

export default new AuthService();