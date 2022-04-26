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
import ReactPaginate from "react-paginate";
import "../../App.css"

function ShowReservationsOwner() {

    const [reservations, setReservations] = useState([]);
    const [isLoading, setLoading] = useState(true);
    const [currentPage, setCurrentPage] = useState(0);
    const entitiesPerPage = 6;
    const pagesVisited = currentPage*entitiesPerPage;
    const [valueFirst, setValueFirst] = React.useState();
    const [options, setOptions] = React.useState([]);
    let ownerId = 1; //IZMENNEEEEEEEEE!!!!!!!!!!!!!!!
    useEffect(() => {
        getReservationsByOwnerId(ownerId).then(res => {
            setReservations(res.data);
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
            setLoading(false);
        })
    }

    
    const displayReservations = reservations.slice(pagesVisited, pagesVisited + entitiesPerPage)
    .map(res=> {
        return <ImgReservation reservation={res} reservationId={res.id} details="true"></ImgReservation>
    })

    const pageCount = Math.ceil(reservations.length / entitiesPerPage);
    const changePage=({selected})=>{
        setCurrentPage(selected);
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
                {displayReservations}
                <ReactPaginate
                previousLabel="Previous"
                nextLabel="Next"
                pageCount={pageCount}
                onPageChange={changePage}
                containerClassName={"paginationBttns"}
                previousLinkClassName={"previosBttn"}
                nextLinkClassName={"nextBttn"}
                disabledClassName={"paginationDisabled"}
                activeClassName={"paginationActive"}
            />
            </div>
            
            
        </div>
    );
}

export default ShowReservationsOwner;
