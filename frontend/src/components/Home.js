import React, { useState, useEffect, Component } from "react";
import "../App.css";
import UserService from "../services/user.service";
import AuthService from "../services/auth.service";

let threadName = "movies";

const currentUser = AuthService.getCurrentUser();
export default class Home extends Component {
  constructor(props) {
    super(props);

    this.state = {
      posts: []
    };
    
  }

  

  componentDidMount() {
    UserService.getAllPosts().then(
      response => {
         this.setState({
          
          posts: response.data
        });
      },
      
      error => {
        this.setState({
          content:
            (error.response && error.response.data) ||
            error.message ||
            error.toString()
        });
      }
    );
  }

 /* CommentMount(){
    let postId = 1;
    UserService.getComments(postId).then(
      response => {
         this.setState({
          
          comments: response.data
        });
      },
      
      error => {
        this.setState({
          content:
            (error.response && error.response.data) ||
            error.message ||
            error.toString()
        });
      }
    );
  }
*/
  render() {
    return (
      
        <header className="jumbotron">
      
      {(currentUser) ? (
          this.state.posts
          .map(post =>
            <div key={post.id} className="test-div">
              <p><span className="username">{post.username} </span>| #{post.forumThreadName}</p>
              <h4 className="post-class">{post.name}</h4> 
              {post.description}
              <hr></hr>
              <div className="bottom-bar">
              <div className="counter-div">
                <button type="button" class="btn btn-success">+</button>
                <span className="counter">{post.voteCount}</span>
                <button type="button" class="btn btn-danger">-</button></div>
              <div className="comment-div"><b>{post.commentCount} comments</b></div>
              </div>
              </div>)
        ) :(
        
        
this.state.posts
.map(post =>
  <div key={post.id} className="test-div">
    <p><span className="username">{post.username} </span>| #{post.forumThreadName}</p>
    <h4 className="post-class">{post.name}</h4> 
    {post.description}
    
    <div className="bottom-bar">
    <div className="counter-div">
      
      
      </div>
      <hr></hr>
    <div className="comment-div2"><b>{post.commentCount} comments</b></div>
    </div>
    </div>)
        )}
          
        
        
      
        </header>
      
    );
  }
}