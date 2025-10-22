import axios from 'axios';

const USER_API_BASE_URL = "http://localhost:8080";

const  ApiService = {

  fetchTran(uri){
    return axios.get(USER_API_BASE_URL + uri);
  },

  deleteTran(uri){
    return axios.delete(USER_API_BASE_URL + uri);
  },
  
  postTran(uri, data, config){
    return axios.post(USER_API_BASE_URL + uri, data, config);
  }

}

export default ApiService;