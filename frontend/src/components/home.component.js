import React, { Component } from "react";
import "../App.css";



import UserService from "../services/user.service";

let threadName = "movies";

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
      
        {
          this.state.posts
            .map(post =>
              <div key={post.id=1} className="test-div">
                <p><span className="username">{post.username} </span>| #{post.forumThreadName}</p>
                <h4 className="post-class">{post.name}</h4> 
                {post.description}
                
                <div className="bottom-bar">
                <div className="counter-div"><a className="button-plus">+ </a><span className="counter">{post.voteCount}</span><a className="button-minus"> -</a></div>
                <div className="comment-div">{post.commentCount} comments</div>
                </div>
                </div>
            )          
            
        }
        
      
        </header>
      
    );
  }
}