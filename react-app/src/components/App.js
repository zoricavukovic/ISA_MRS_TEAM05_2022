import React, { Component, useState, useEffect } from 'react';
import '../App.css';
import UserProfile from "./user/UserProfile";
import EditUserProfile from "./user/EditUserProfile";
import {BrowserRouter as Router, Switch, Route, Redirect} from "react-router-dom";

import ShowCottageProfile from "./cottage/ShowCottageProfile";
import AddCottage from "./cottage/AddCottage";
import EditCottage from "./cottage/EditCottage";

import ShowShipProfile from "./ships/ShowShipProfile";
import AddShip from "./ships/AddShip";
import EditShip from "./ships/EditShip";

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
import NotFoundPage404 from './forbiddenNotFound/NotFoundPage404';
import ForbiddenPage403 from './forbiddenNotFound/ForbiddenPage403';
import NewReservation from './reservations/NewReservationPage';
import ChangePassword from './ChangePassword';

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
                    
                    <Route path="/login" ><Login setCurrentUser={setNewUser} /></Route>

                    <Route path="/showShipProfile" component={ShowShipProfile} />
                    <Route path="/addShip" component={AddShip} />
                    <Route path="/editShip" component={EditShip} />

                    <Route path="/login" ><Login setCurrentUser={setCurrentUser}/></Route>
                    <Route path="/showReservationsOwner" component={ShowReservationsOwner} />
                    <Route path="/showReservationDetails" component={ShowReservationsDetails} />

                    <Route path="/showFastReservations" component={ShowFastReservations} />
                    <Route path="/addFastReservation" component={AddFastReservation} />

                    <Route path="/showAdventureProfile" component={AdventureProfile} />
                    <Route path="/addAdventure" component={AddAdventure} />
                    <Route path="/editAdventure" component={EditAdventure} />
                    <Route path="/newReservation" component={NewReservation} />



                    <Route path="/calendarForEntity" component={CalendarForEntity} />
                    <Route path="/notFoundPage" component={NotFoundPage404} />
                    <Route path="/forbiddenPage" component={ForbiddenPage403} />

                    <Route path="/changePassword" component={ChangePassword} />
                    <Redirect to="/"></Redirect>
                </Switch>
            </Router>
        </>
    );
}

export default App;
