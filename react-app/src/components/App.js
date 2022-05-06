import React, { Component, useState, useEffect } from 'react';
import '../App.css';
import UserProfile from "./UserProfile";
import EditUserProfile from "./EditUserProfile";
import ShowCottageProfile from "./cottage/ShowCottageProfile";
import AddCottage from "./cottage/AddCottage";
import EditCottage from "./cottage/EditCottage";
import ShowCottagesCottageOwner from "./cottage/showCottagesCottageOwner.js";

import { BrowserRouter as Router, Switch, Route, Redirect } from "react-router-dom";
import AdventureProfile from './adventures/AdventureProfile';
import AddAdventure from './adventures/AddAdventure';
import EditAdventure from './adventures/EditAdventure';
import Sidebar from "./Sidebar";
import Login from './Login';

import ShowReservationsOwner from "./reservations/ShowReservationsOwner.js";
import ShowReservationsDetails from "./reservations/ReservationDetails";
import ShowFastReservations from "./reservations/ShowFastReservations";
import AddFastReservation from './reservations/AddFastReservation';

import ShowAllEntitiesForOwner from './ShowAllEntitiesForOwner';

import ShowBoats from "./entities_view/ShowBoats";
import ShowCottages from './entities_view/ShowCottages';
import ShowAdventures from './entities_view/ShowAdventures';

import CalendarForEntity from './calendar/CalendarForEntity';
import NotFoundPage404 from './NotFoundPage404';
import ForbiddenPage403 from './ForbiddenPage403';

function App() {
    const [currentUser, setCurrentUser] = useState({});

    useEffect(() => {
        if (window.localStorage.getItem('user') === null) {
            setCurrentUser({})
        } else {
            setCurrentUser(JSON.parse(window.localStorage.getItem('user')));
        }
    }, []);

    const setNewUser = (newUser) => {
        setCurrentUser(newUser);
    }

    return (
        <>
            <Router>
                <Sidebar curUser={currentUser} setCurUser={setNewUser} />
                <Switch>
                    <Route path="/showBoats" component={ShowBoats} />
                    <Route path="/showCottages" component={ShowCottages} />
                    <Route path="/showAdventures" component={ShowAdventures} />

                    <Route path="/adventures" component={ShowAllEntitiesForOwner} />
                    <Route path="/cottages" component={ShowAllEntitiesForOwner} />
                    <Route path="/ships" component={ShowAllEntitiesForOwner} />

                    <Route path="/userProfile" component={UserProfile} />
                    <Route path="/editUserProfile" component={EditUserProfile} />
                    <Route path="/showCottageProfile" component={ShowCottageProfile} />
                    <Route path="/addCottage" component={AddCottage} />
                    <Route path="/editCottage" component={EditCottage} />
                    <Route path="/showCottagesOwner" component={ShowCottagesCottageOwner} />
                    <Route path="/login" ><Login setCurrentUser={setNewUser} /></Route>
                    <Route path="/showReservationsOwner" component={ShowReservationsOwner} />
                    <Route path="/showReservationDetails" component={ShowReservationsDetails} />

                    <Route path="/showFastReservations" component={ShowFastReservations} />
                    <Route path="/addFastReservation" component={AddFastReservation} />

                    <Route path="/showAdventureProfile" component={AdventureProfile} />
                    <Route path="/addAdventure" component={AddAdventure} />
                    <Route path="/editAdventure" component={EditAdventure} />



                    <Route path="/calendarForEntity" component={CalendarForEntity} />
                    <Route path="/notFoundPage" component={NotFoundPage404} />
                    <Route path="/forbiddenPage" component={ForbiddenPage403} />
                    <Redirect to="/"></Redirect>
                </Switch>
            </Router>
        </>
    );
}

export default App;
