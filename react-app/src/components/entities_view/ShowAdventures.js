import * as React from 'react';
import { useEffect, useState } from "react";
import { getAllAdventuresView } from '../../service/AdventureService';
import EntityBasicCard from '../EntityBasicCard';

export default function ShowAdventures() {

    const [adventures, setAdventures] = useState([]);

    useEffect(() => {
        getAllAdventuresView().then(res => {
            setAdventures(res.data);
            console.log(res.data);
        })
    }, []);

    return (
        <div style={{ margin: '1% 9% 1% 9%' }} className="App">
            <div style={{ display: "flex", flexWrap: 'wrap', flexDirection: "row", justifyContent: "center" }}>
                {adventures.map((item, index) => (
                    <EntityBasicCard bookingEntity={item} key={index} />
                ))}
            </div>
        </div>
    );
}
