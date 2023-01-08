import React, { useState, useEffect, Component } from "react";
import "../App.css";
import UserService from "../services/user.service";
import AuthService from "../services/auth.service";
import { useHref } from "react-router-dom";

let threadName = "movies";
let namePost;
let threadPostName;
let descriptionPost;

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
          
          {(currentUser) ? (<><div className="row">
    <div className="col-25">
      <label for="fname">Post title</label>
    </div>
    <div className="col-75">
      <input type="text" id="fname" name="posttitle" placeholder="Your post title.."></input>
    </div>
  </div>
  <div className="row">
    <div className="col-25">
      <label for="lname">Thread Name</label>
    </div>
    <div className="col-75">
      <input type="text" id="lname" name="threadname" placeholder="Your thread name.."></input>
    </div>
  </div>
  
  <div className="row">
    <div className="col-25">
      <label for="subject">Description</label>
    </div>
    <div className="col-75">
      <textarea id="subject" name="description" placeholder="Write something.." style={{height:'100px'}}></textarea>
    </div>
  </div>
  <br/>
  
    <input type="submit" value="Add post"></input>
  <br></br><br></br><hr></hr><h4>Posts: </h4></>):(<h4>Posts: </h4>)}

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
                <button type="button" class="btn btn-success" onClick ={(e)=>{UserService.postUserVote("UPVOTE",post.id)}}>+</button>
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
    <hr></hr>
    <div className="bottom-bar">
    <div className="counter-div">
                
                <span className="counter">{post.voteCount} likes</span>
                </div>
              <div className="comment-div"><b>{post.commentCount} comments</b></div>
              </div>
    </div>)
        )}
          
        
        
      
        </header>
      
    );
    
    
  }
}