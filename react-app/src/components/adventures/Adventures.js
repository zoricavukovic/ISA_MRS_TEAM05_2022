import React, { useEffect, useState } from "react";
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Grid from '@mui/material/Grid';
import axios from "axios";
import Autocomplete from '@mui/material/Autocomplete';
import { useForm } from "react-hook-form";
import { Divider } from "@mui/material";
import { useHistory } from "react-router-dom";
import { getAllPlaces } from "../../service/PlaceService.js";
import { addNewAdventure } from "../../service/AdventureService.js";
import Rating from '@mui/material/Rating';
import StarIcon from '@mui/icons-material/Star';
import { fontSize, height } from "@mui/system";
import { getAllBookingEntitiesByOwnerId, getAllSearchedAdventuresBySimpleCriteria } from "../../service/BookingEntityService.js";

const labels = {
    0.5: 'Useless',
    1: 'Useless+',
    1.5: 'Poor',
    2: 'Poor+',
    2.5: 'Ok',
    3: 'Ok+',
    3.5: 'Good',
    4: 'Good+',
    4.5: 'Excellent',
    5: 'Excellent+',
};

function getLabelText(value) {
    return `${value} Star${value !== 1 ? 's' : ''}, ${labels[value]}`;
}



export default function Adventures() {

    const { register, handleSubmit, reset, formState: { errors } } = useForm();

    const [places, setPlaces] = React.useState([]);
    const [isLoadingPlace, setLoadingPlace] = useState(true);
    const [isLoadingEntites, setLoadingEntities] = useState(false);
    const [selectedPlaceId, setSelectedPlaceId] = useState(null);
    const ownerId = 13; // ovde staviti ulogovanog korisnika
    const [entities, setEntities] = useState(null);

    const [value, setValue] = React.useState(2);
    const [hover, setHover] = React.useState(-1);
    let allPlacesList;

    const placeOnChange = (event, newValue) => {
        console.log(newValue);
        if (newValue != null && newValue != undefined && newValue != '') {
            setSelectedPlaceId(newValue.id);
        } else {
            setSelectedPlaceId(null);
        }
    }
    const getAllPlacesForTheList = () => {
        let newArray = [];
        for (let place of places) {
            newArray.push({ 'label': place.cityName + ',' + place.zipCode + ',' + place.stateName, 'id': place.id });
        }
        allPlacesList = newArray;
    }

    useEffect(() => {   
        getAllBookingEntitiesByOwnerId(ownerId)
        .then(res => {
            setEntities(res.data);
            setLoadingEntities(false);
        });

        getAllPlaces()
            .then(res => {
                setPlaces(res.data);
                setLoadingPlace(false);
            })
    }, [])

    const onSearch = data => {
        let minCost = data.minCostPerPerson;
        let maxCost = data.maxCostPerPerson; 
        if (minCost !== "" && maxCost != "") {
            minCost = parseInt(minCost);
            maxCost = parseInt(maxCost);
            if (minCost > maxCost)
                alert("Min cost should be less then max cost per person");
            return;
        }

        let searchCriteria = {
            "ownerId": ownerId,
            "name": data.name,
            "address": data.address,
            "placeId": data.placeId,
            "minCostPerPerson": data.minCostPerPerson,
            "maxCostPerPerson": data.maxCostPerPerson,
            "minRating": value,
        };
        console.log(searchCriteria);

        getAllSearchedAdventuresBySimpleCriteria(searchCriteria)
            .then(res => {
                setEntities(res.data);
            }).catch(res => {
                console.log(res);
                alert("Error happend on server while searching.");
            });
    }

    if (isLoadingPlace || isLoadingEntites) {
        return <div className="App">Loading...</div>
    }
    return (
        <div>
            <div style={{ display: "flex", gap:"10px", flexWrap: "wrap", padding:"3", flexDirection: "row", margin: "2%", width: "100%", alignItems: "stretch", backgroundColor: "aliceblue", borderRadius: "5px" }}>
                {getAllPlacesForTheList()}
                <TextField
                    name="name"
                    id="name"
                    label="Name"
                    placeholder="Name"
                    size="small"
                    style={{ width: '200px' }}
                    {...register("name", { maxLength: 50 })}
                />
                {errors.name && <p style={{ color: '#ED6663' ,fontSize: '11px'}}>Please check the name. Max 50 chars</p>}
                <TextField
                    name="address"
                    id="address"
                    label="Address"
                    placeholder="Address"
                    size="small"
                    style={{ width: '200px' }}
                    {...register("address", { maxLength: 50 })}
                />
                {errors.address && <p style={{ color: '#ED6663', fontSize: '11px' }}>Please check the address. Max 50 chars</p>}
                <Autocomplete
                    disablePortal
                    id="place"
                    size="small"
                    options={allPlacesList}
                    sx={{ width: '200px' }}
                    onChange={placeOnChange}
                    renderInput={(params) => <TextField {...params} label="Place" />}
                />
                <TextField
                    name="minCostPerPerson"
                    id="minCostPerPerson"
                    type="number"
                    size="small"
                    label="Min Cost Per Person €"
                    placeholder="Min Cost Per Person €"
                    style={{ width: '200px' }}
                    {...register("minCostPerPerson", { min: 1, max: 100000 })}
                />
                {errors.minCostPerPerson && <p style={{ fontSize: '11px', color: '#ED6663' }}>Min cost per person is num between 1 and 100000</p>}
                <TextField
                    name="maxCostPerPerson"
                    id="maxCostPerPerson"
                    type="number"
                    size="small"
                    label="Max Cost Per Person €"
                    placeholder="Max Cost Per Person €"
                    style={{ width: '200px' }}
                    {...register("maxCostPerPerson", { min: 1, max: 100000 })}
                />
                {errors.maxCostPerPerson && <p style={{ color: '#ED6663' ,fontSize: '11px' }}>Max cost per person is num between 1 and 100000</p>}
                    <Rating
                        name="hover-feedback"
                        value={value}
                        precision={0.5}
                        getLabelText={getLabelText}
                        onChange={(event, newValue) => {
                            setValue(newValue);
                        }}
                        onChangeActive={(event, newHover) => {
                            setHover(newHover);
                        }}
                        emptyIcon={<StarIcon style={{ opacity: 0.55 }} fontSize="inherit" />}
                    />
                    {value !== null && (
                        <Box sx={{ ml: 2 }}>{labels[hover !== -1 ? hover : value]}</Box>
                    )}


                <Button onClick={handleSubmit(onSearch)} label="Extra Soft" style={{ color: 'rgb(5, 30, 52)', fontWeight: 'bold', borderRadius: '10px', margin: '1%', backgroundColor: 'rgb(244, 177, 77)' }}>
                    Search
                </Button>
                <div>
                    <ul>
                {entities.map(e=> (
                    <li>{"Name:" + e.name + ", Adress:" + e.address + ", PlaceId:" + e.place.id + ", promo:" + e.promoDescription + ", entityPricePerPerson:" + e.entityPricePerPerson + ", Rating:" + e.averageRating}</li>
                    
                    // if (numAddedCottages !== 2){
                    //     let i = numAddedCottages + 1;
                    //     //setNumAddedCottages(i);
                    //     console.log({cottage});
                    //     return <ImgMediaCard key={i} cottage={cottage} bookingEntityId={cottage.id}></ImgMediaCard>
                    // }

                ))}
                </ul>
                </div>
            </div>

        </div>


    );
}