import React, {Component, useState} from 'react';
import '../App.css';
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
import Sidebar from "./Sidebar";
import Login from './Login';

function App() {
    const [currentUser,setCurrentUser] = useState({});
    return (
        <>
            <Router>
                <Sidebar curUser={currentUser} setCurUser={setCurrentUser}/>
                <Switch>
                    <Route path="/allBoats" component={AllBoats} />
                    <Route path="/userProfile" component={UserProfile} />
                    <Route path="/editUserProfile" component={EditUserProfile} />
                    <Route path="/showCottageProfile" component={ShowCottageProfile} />
                    <Route path="/addCottage" component={AddCottage} />
                    <Route path="/editCottage" component={EditCottage} />
                    <Route path="/showCottages" component={showCottages} />
                    <Route path="/showCottagesOwner" component={ShowCottagesCottageOwner} />
                    <Route path="/showAdventureProfile" component={AdventureProfile} />
                    <Route path="/login" ><Login setCurrentUser={setCurrentUser}/></Route>


                    <Route path="/showAdventureProfile" component={AdventureProfile} />
                    <Route path="/addAdventure" component={AddAdventure} />

                    <Redirect to="/"></Redirect>
                </Switch>
            </Router>
        </>
    );
}

export default App;
