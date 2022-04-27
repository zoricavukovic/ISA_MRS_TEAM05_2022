import * as React from 'react';
import { useEffect, useState } from "react";
import { getAllCottagesView } from '../../service/CottageService';
import EntityBasicCard from '../EntityBasicCard';

export default function ShowCottages() {

    const [cottages, setCottages] = useState([]);

    useEffect(() => {
        getAllCottagesView().then(res => {
            setCottages(res.data);
            console.log(res.data);
        });
    }, []);

    return (
        <div style={{ margin: '1% 9% 1% 9%' }} className="App">
            <div style={{ display: "flex", flexWrap: 'wrap', flexDirection: "row", justifyContent: "center" }}>
                {cottages.map((item, index) => (
                    <EntityBasicCard bookingEntity={item} key={index} />
                ))}
            </div>
        </div>
    );
}