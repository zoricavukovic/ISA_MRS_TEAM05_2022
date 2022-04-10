import '../App.css';
import {useEffect, useState} from "react";
import axios from "axios";
import React, {Component} from 'react';

function Home() {
    const [clients, setClients] = useState({});

    const handleChange = (event) => {

    }

    useEffect(() => {
        // Update the document title using the browser API
        axios.get("http://localhost:8090/bookingApp/clients").then(data => {
            console.log(data);


        })
    });


    return (

        <div className="App">
           <h1>Neka nova stranica</h1>


        </div>
    );
}

export default Home;
