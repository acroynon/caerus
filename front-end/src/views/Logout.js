import AuthService from '../service/AuthService';
import { useHistory } from "react-router-dom";


function Feed(){
    const history = useHistory();
	AuthService.logout();
    history.replace("/login");
    return (<span></span>)
}

export default Feed;