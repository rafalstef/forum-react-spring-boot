import axios from 'axios';
import authHeader from './auth-header';

const API_URL = 'http://localhost:8080/api/';

class UserService {
  getAllThreads() {
    return axios.get(API_URL + 'threads');
  }
  getAllPosts() {
    return axios.get(API_URL + 'posts/all');
  }
  getThreadByName(threadName) {
    return axios.get(API_URL + 'posts/thread/'+threadName);
  }

  getComments(postId) {
    return axios.get(API_URL + 'comments/post/'+postId);
  }

  getUserBoard() {
    return axios.get(API_URL + 'user', { headers: authHeader() });
  }
}

export default new UserService();