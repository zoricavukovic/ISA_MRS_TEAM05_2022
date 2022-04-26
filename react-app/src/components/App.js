import React, {Component} from 'react';
import '../App.css';
import Navbar from "./Navbar";
import UserProfile from "./UserProfile";
import EditUserProfile from "./EditUserProfile";
import ShowCottageProfile from "./cottage/ShowCottageProfile";
import AddCottage from "./cottage/AddCottage";
import EditCottage from "./cottage/EditCottage";
import ShowCottagesCottageOwner from "./cottage/showCottagesCottageOwner.js";

import {BrowserRouter as Router, Switch, Route, Redirect} from "react-router-dom";
import AdventureProfile from './adventures/AdventureProfile';
import showCottages from "./ShowCottages";
import AddAdventure from './adventures/AddAdventure';

import AllBoats from "./AllBoats";
import EditAdventure from './adventures/EditAdventure';
import Adventures from './adventures/Adventures';

function App() {
    return (
        <>
            <Router>
                <Navbar/>
                <Switch>
                    <Route path="/allBoats" component={AllBoats} />
                    <Route path="/userProfile" component={UserProfile} />
                    <Route path="/editUserProfile" component={EditUserProfile} />
                    <Route path="/showCottageProfile" component={ShowCottageProfile} />
                    <Route path="/addCottage" component={AddCottage} />
                    <Route path="/editCottage" component={EditCottage} />
                    <Route path="/showCottages" component={showCottages} />
                    <Route path="/showCottagesOwner" component={ShowCottagesCottageOwner} />
                    <Route path="/showAdventureProfile/:adventureId" component={AdventureProfile} />
                    <Route path="/addAdventure" component={AddAdventure} />
                    <Route path="/editAdventure/:adventureId" component={EditAdventure} />
                    <Route path="/adventures" component={Adventures} />
                    <Redirect to="/"></Redirect>
                </Switch>
            </Router>
        </>
    );
}

export default App;
