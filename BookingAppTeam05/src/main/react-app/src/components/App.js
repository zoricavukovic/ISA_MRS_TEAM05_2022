import React, {Component} from 'react';
import '../App.css';
import Navbar from "./Navbar";
import HomePageCottageOwner from "./HomePageCottageOwner";
import UserProfile from "./UserProfile";
import EditUserProfile from "./EditUserProfile";

import {BrowserRouter as Router, Switch, Route, Redirect} from "react-router-dom";


function App() {
    return (
        <>
            <Router>
                <Navbar/>
                
                <Switch>
                    
                    <Route exact path="/homePageCottageOwner" component={HomePageCottageOwner} />
                    <Route path="/userProfile" component={UserProfile} />
                    <Route path="/editUserProfile" component={EditUserProfile} />
                    <Redirect to="/"></Redirect>
                </Switch>
            </Router>
        </>
    );
}

export default App;
