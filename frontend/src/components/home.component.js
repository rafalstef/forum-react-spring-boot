import React, { Component } from "react";


import UserService from "../services/user.service";

export default class Home extends Component {
  constructor(props) {
    super(props);

    this.state = {
      threads: []
    };
  }

  componentDidMount() {
    UserService.getPublicContent().then(
      response => {
         this.setState({
          threads: response.data
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
    return (
      
        <header className="jumbotron">
        <ul>
        {
          this.state.threads
            .map(thread =>
              <li><div key={thread.id} className="container">{thread.name}, {thread.description}</div></li>
            )
        }
      </ul>
        </header>
      
    );
  }
}