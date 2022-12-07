import React, { Component } from "react";
import { Navigate } from "react-router-dom";
import AuthService from "../services/auth.service";
import UserService from "../services/user.service";

export default class Profile extends Component {
  constructor(props) {
    super(props);

    this.state = {
      redirect: null,
      userReady: false,
      currentUser: { username: "" },
      posts: []
    };
  }

  componentDidMount() {
    const currentUser = AuthService.getCurrentUser();

    if (!currentUser) this.setState({ redirect: "/home" });
    this.setState({ currentUser: currentUser, userReady: true })

    UserService.getUserPosts(currentUser.username).then(
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
  
  render() {
    if (this.state.redirect) {
      return <Navigate to={this.state.redirect} />
    }

    const { currentUser } = this.state;

    return (
      <div className="container">
        {(this.state.userReady) ?
        <div>
        <header className="jumbotron">
          <h3>
          Cześć,<strong> {currentUser.username}</strong>!
          </h3>
          <br></br><br></br><br></br>
          <h4>Twoje posty:</h4>
          <br></br>
          {this.state.posts
            .map(post => 
              <div key={post.id} className="test-div">
                <p><span className="username">{post.username} </span>| #{post.forumThreadName}</p>
                <h4 className="post-class">{post.name}</h4> 
                {post.description}
                
                <div className="bottom-bar">
                <div className="counter-div">
                  <button type="button" class="btn btn-success">+</button>
                  <span className="counter">{post.voteCount}</span>
                  <button type="button" class="btn btn-danger">-</button></div>
                <div className="comment-div">{post.commentCount} comments</div>
                </div>
                </div>
            )  }
        </header>
        

      </div>: null}
      </div>
    );
  }
}