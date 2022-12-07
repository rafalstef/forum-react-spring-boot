import axios from "axios";

const API_URL = "http://localhost:8080/api/";

const getPublicContent = () => {
  return axios.get(API_URL + "users/janek");
};

const getUserBoard = () => {
  return axios.get(API_URL + "users/janek");
};

const getAllThreads = () =>{
  return axios.get(API_URL + 'threads');
};
const getAllPosts = () =>{
  return axios.get(API_URL + 'posts/all');
};
const getThreadByName = (threadName) =>{
  return axios.get(API_URL + 'posts/thread/'+threadName);
};

const getComments = (postId) =>{
  return axios.get(API_URL + 'comments/post/'+postId);
};

const getSubscribedPosts = (username) =>{
  return axios.get(API_URL + 'posts/subscribed/'+username);
};

const getUserPosts = (username) =>{
  return axios.get(API_URL + 'posts/user/'+username);
};


const UserService = {
  getPublicContent,
  getUserBoard,
  getAllThreads,
  getAllPosts,
  getThreadByName,
  getComments,
  getSubscribedPosts,
  getUserPosts
}



export default UserService;
