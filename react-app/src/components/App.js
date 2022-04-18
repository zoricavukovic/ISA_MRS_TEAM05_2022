import React, {Component} from 'react';
import '../App.css';
import Navbar from "./Navbar";
import HomePageCottageOwner from "./HomePageCottageOwner";
import UserProfile from "./UserProfile";
import EditUserProfile from "./EditUserProfile";
import ShowCottageProfile from "./ShowCottageProfile";
import AddCottage from "./AddCottage";
import EditCottage from "./EditCottage";

import {BrowserRouter as Router, Switch, Route, Redirect} from "react-router-dom";
import AdventureProfile from "./AdventureProfile";
import showCottages from "./ShowCottages";
import AllBoats from "./AllBoats";


function App() {
    return (
        <>
            <Router>
                <Navbar/>
                <Switch>
                    <Route exact path="/homePageCottageOwner" component={HomePageCottageOwner} />
                    <Route path="/userProfile" component={UserProfile} />
                    <Route path="/editUserProfile" component={EditUserProfile} />
                    <Route path="/showCottageProfile" component={ShowCottageProfile} />
                    <Route path="/addCottage" component={AddCottage} />
                    <Route path="/editCottage" component={EditCottage} />
                    <Route path="/showCottages" component={showCottages} />
                    <Route path="/allBoats" component={AllBoats} />

                    <Route path="/showAdventureProfile" component={AdventureProfile} />

                    <Redirect to="/"></Redirect>
                </Switch>
            </Router>
        </>
    );
}

export default App;
