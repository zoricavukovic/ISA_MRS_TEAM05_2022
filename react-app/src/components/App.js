import React, {Component} from 'react';
import '../App.css';
import Navbar from "./Navbar";
import HomePageCottageOwner from "./HomePageCottageOwner";
import UserProfile from "./UserProfile";
import EditUserProfile from "./EditUserProfile";
import ShowCottageProfile from "./cottage/ShowCottageProfile";
import AddCottage from "./cottage/AddCottage";
import EditCottage from "./cottage/EditCottage";
import ShowCottagesCottageOwner from "./cottage/showCottagesCottageOwner.js";

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
                    
                    <Route path="/allBoats" component={AllBoats} />
                    <Route exact path="/homePageCottageOwner" component={HomePageCottageOwner} />
                    <Route path="/userProfile" component={UserProfile} />
                    <Route path="/editUserProfile" component={EditUserProfile} />
                    <Route path="/showCottageProfile" component={ShowCottageProfile} />
                    <Route path="/addCottage" component={AddCottage} />
                    <Route path="/editCottage" component={EditCottage} />
                    <Route path="/showCottages" component={showCottages} />
                    <Route path="/showCottagesOwner" component={ShowCottagesCottageOwner} />
                    <Route path="/showAdventureProfile" component={AdventureProfile} />


                    <Route path="/showAdventureProfile" component={AdventureProfile} />

                    <Redirect to="/"></Redirect>
                </Switch>
            </Router>
        </>
    );
}

export default App;
