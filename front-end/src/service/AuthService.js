import axios from 'axios'

const options = {
    headers: {
        'Access-Control-Allow-Origin' : '*',
        'Access-Control-Allow-Methods':'*',
        "Content-Type": "application/json"
    }
  };

class AuthService  {

    login(username, password){
        console.log("attempt login:", username, password)
        return new Promise((resolve, reject) => {
            axios.post("/api/auth-service/authenticate", {
                username: username,
                password: password
            }, options)
            .then(response => {
                console.log(response);
                var data = response.data;
                sessionStorage.setItem('access_token', data.access_token);
                sessionStorage.setItem('refresh_token', data.refresh_token);
                resolve();
            })
        });
    }

    logout(){
        sessionStorage.removeItem('access_token');
        sessionStorage.removeItem('refresh_token');
    }
}

export default new AuthService();