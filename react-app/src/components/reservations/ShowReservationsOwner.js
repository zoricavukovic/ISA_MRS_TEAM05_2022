import * as React from 'react';
import { useEffect, useState } from "react";
import { CircularProgress } from "@mui/material";
import ImgReservation from "./ReservationBasicCard.js";
import BasicPagination from "../Pagination.js";
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Autocomplete from '@mui/material/Autocomplete';
import axios from "axios";
import { getReservationsByOwnerId, getReservationsByOwnerIdAndFilter } from '../../service/ReservationService.js';

function ShowReservationsOwner() {

    const [reservations, setReservations] = useState([]);
    const [isLoading, setLoading] = useState(true);
    const [pagination, setPagination] = useState(0);
    const [currentPage, setCurrentPage] = useState(1);
    const [numAddedCottages, setNumAddedCottages] = useState(0);
    const [valueFirst, setValueFirst] = React.useState();
    const [options, setOptions] = React.useState([]);
    let ownerId = 1; //IZMENNEEEEEEEEE!!!!!!!!!!!!!!!
    useEffect(() => {
        getReservationsByOwnerId(ownerId).then(res => {
            setReservations(res.data);
            setPagination(Math.ceil(res.data.length / 6));
            let newOpts = [];
            for (let res of res.data){
                let found = false;
                for (let added of newOpts){
                    if (added === res.bookingEntity.name){
                        found = true;
                        break;
                    }
                }
                if (found === false) {newOpts.push(res.bookingEntity.name);}
            }
            setOptions(newOpts);
            setLoading(false);
        })
    }, []);
    const filter = ()=>{
        if (valueFirst === undefined){
            return;
        }
        
        setLoading(true);
        getReservationsByOwnerIdAndFilter(ownerId, valueFirst).then(res => {
            setReservations(res.data);
            setPagination(Math.ceil(res.data.length / 6));
            setLoading(false);
        })
    }
    
    if (isLoading) { return <div><CircularProgress /></div> }
    return (
        <div>
            <div style={{ display: "flex", flexDirection: "row", margin: "2%", width:"100%", alignItems: "stretch", backgroundColor: "aliceblue", borderRadius: "5px" }}>
                
                <Autocomplete
                    value={valueFirst}
                    onChange={(event, newValue) => {
                    setValueFirst(newValue);
                    }}
                    size="small"
                    name="firstOp"
                    id="controllable-states-demo"
                    options={options}
                    style={{ marginLeft: "60%", marginTop:"1%", minWidth:'200px', color: 'rgb(5, 30, 52)'}}
                    renderInput={(params) => <TextField {...params} label="Cottage name" />}
                />
            
                <Button onClick={filter} label="Extra Soft" style={{ color: 'rgb(5, 30, 52)', fontWeight: 'bold', borderRadius: '10px', margin: '1%', backgroundColor: 'rgb(244, 177, 77)' }}>
                    Filter
                </Button>
    


            </div>
            <div style={{ display: "flex", flexWrap: 'wrap', flexDirection: "row", justifyContent: "center" }} className="App">
                {reservations.map(reservation=> {
                    if (numAddedCottages !== 2){
                        let i = numAddedCottages + 1;
                        //setNumAddedCottages(i);
                        console.log({reservation});
                        return <ImgReservation key={i} reservation={reservation} reservationId={reservation.id} details="true"></ImgReservation>
                    }
                })}

            </div>
            <BasicPagination count={pagination} />
        </div>


    );
}

export default ShowReservationsOwner;
