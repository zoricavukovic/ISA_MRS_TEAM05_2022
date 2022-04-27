import * as React from 'react';
import { useEffect, useState } from "react";
import { getAllShipsView } from '../../service/ShipService';
import EntityBasicCard from '../EntityBasicCard';

export default function ShowBoats() {

    const [boats, setBoats] = useState([]);

    useEffect(() => {
        getAllShipsView().then(res => {
            setBoats(res.data);
            console.log(res.data);
        })
    }, []);

    return (
        <div style={{ margin: '1% 9% 1% 9%' }} className="App">
            <div style={{ display: "flex", flexWrap: 'wrap', flexDirection: "row", justifyContent: "center" }}>
                {boats.map((item, index) => (
                    <EntityBasicCard bookingEntity={item} key={index} />
                ))}
            </div>
        </div>

    );
}
