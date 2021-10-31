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
        console.log(username,password)
        return new Promise((resolve, reject) => {
            axios.post("/api/auth-service/authenticate", {
                username: username,
                password: password
            }, options)
            .then(response => {
                console.log(response);
                sessionStorage.setItem('access_token', response.access_token);
                sessionStorage.setItem('refresh_token', response.refresh_token);
                resolve();
            })
        });
    }

    logout(){
        sessionStorage.removeItem('access_token');
        sessionStorage.removeItem('refresh_token');
    }
    
    isLoggedIn(){
        return sessionStorage.getItem('access_token') != null;
    }
}

export default new AuthService();