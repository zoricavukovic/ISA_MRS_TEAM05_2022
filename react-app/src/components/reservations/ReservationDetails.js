import * as React from 'react';
import { useEffect, useState } from "react";
import { CircularProgress } from "@mui/material";
import ImgReservation from "./ReservationBasicCard.js";
import ReservedBookingEntityDetail from './ReservedBookingEntityDetail';
import ClientDetails from './ClientDetails';

const options = ['AND', 'OR'];
function ShowReservationsDetails(props) {

    const [reservation, setReservation] = useState({});
    const [pricelist, setPricelist] = useState({});
    const [additionalServices, setAdditionalServices] = useState([]);
    const [isLoading, setLoading] = useState(true);
    const [pagination, setPagination] = useState(0);
    const [currentPage, setCurrentPage] = useState(1);
    const [numAddedCottages, setNumAddedCottages] = useState(0);
    const [searchElem, setSearchElem] = useState({
        firstOp:"OR",
        secondOp:"OR",
        thirdOp:"OR",
        cottageName:"",
        cityName:"",
        rate:0,
        cost:0
    });
    let ownerId = 1; //IZMENNEEEEEEEEE!!!!!!!!!!!!!!!
    useEffect(() => {
        console.log(props.history.location.state);
        setReservation(props.history.location.state.reservation);
        setPricelist(props.history.location.state.pricelist);
        setAdditionalServices(props.history.location.state.additionalServices);
        setLoading(false);
       
    }, []);
    const search = ()=>{
        
    }
    const makeChange = (event)=>{
        if (event.target.name==="rate" || event.target.name === "cost"){
            let num = parseFloat(event.target.value);
            if (num === NaN) {
                alert("Samo broj sme.");
                return;
            }
            setSearchElem(prevState => ({
                ...prevState,
                [event.target.name]: num
            }));

        }else{
            setSearchElem(prevState => ({
                ...prevState,
                [event.target.name]: event.target.value
            }));
        }
        
    }
    if (isLoading) { return <div><CircularProgress /></div> }
    return (
        <div>
            <div style={{ display: "flex", flexWrap: 'no-wrap', flexDirection: "row", justifyContent: "left", marginTop:"5%" }} className="App">
                <ImgReservation reservation={reservation} reservationId={reservation.id} details="false"></ImgReservation>
                <div style={{ display: "flex", flexWrap: 'no-wrap', flexDirection: "column", justifyContent: "left" }} className="App">
                    <ReservedBookingEntityDetail reservation={reservation} reservationId={reservation.id} details="false"></ReservedBookingEntityDetail>
                    <ClientDetails reservation={reservation}></ClientDetails>
                </div>  
            </div>
           
        </div>


    );
}

export default ShowReservationsDetails;
