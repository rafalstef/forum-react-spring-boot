import React, { useState, useEffect } from "react";
import { Routes, Route, Link } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";

import AuthService from "./services/auth.service";

import Login from "./components/Login";
import Register from "./components/Register";
import Home from "./components/Home";
import Profile from "./components/Profile";
import BoardUser from "./components/BoardUser";



//import AuthVerify from "./common/AuthVerify";
import EventBus from "./common/EventBus";

const App = () => {
  
 
  const [currentUser, setCurrentUser] = useState(undefined);

  useEffect(() => {
    const user = AuthService.getCurrentUser();

    if (user) {
      setCurrentUser(user);

    }

    EventBus.on("logout", () => {
      logOut();
    });

    return () => {
      EventBus.remove("logout");
    };
  }, []);

  const logOut = () => {
    AuthService.logout();

    setCurrentUser(undefined);
  };

  return (
    <div>
       <nav className="navbar navbar-expand navbar-light bg-light">
       <a href={"/home"}><img className="logo"
                        src="https://i.postimg.cc/DmV2DpLb/bez-tla.png"
                        width="15%"
                        height="auto"
                        alt="logo" 
                        /></a>
        <div className="navbar-nav mr-auto">
        <form class="form-inline">
          <div class="form-group mx-sm-2 mb-2">
            <label for="searchbar" class="sr-only"></label>
            <input type="search" class="form-control" placeholder="#Example" />
          </div>
          <button type="submit" class="btn btn-primary mb-2">Search</button>
      </form>
      

        </div>

        {currentUser ? (
          <div className="navbar-nav ml-auto">
            <li className="nav-item">
              <Link to={"/profile"} className="nav-link">
                {currentUser.username}
              </Link>
            </li>
            <li className="nav-item">
              <a href="/login" className="nav-link" onClick={logOut}>
                LogOut
              </a>
            </li>
          </div>
        ) : (
          <div className="navbar-nav ml-auto">
            <li className="nav-item">
              <Link to={"/login"} className="nav-link">
              <button class="button-24" role="button">Login</button>
              </Link>
            </li>

            <li className="nav-item">
              <Link to={"/register"} className="nav-link">
              <button class="button-25" role="button">Sign Up</button>
              </Link>
            </li>
          </div>
        )}
      </nav>

      <div className="container mt-3">
        <Routes>
          <Route exact path={"/"} element={<Home />} />
          <Route exact path={"/home"} element={<Home />} />
          <Route exact path="/login" element={<Login />} />
          <Route exact path="/register" element={<Register />} />
          <Route exact path="/profile" element={<Profile />} />
          <Route path="/user" element={<BoardUser />} />

        </Routes>
      </div>

      {/* <AuthVerify logOut={logOut}/> */}
    </div>
  );
};

export default App;
