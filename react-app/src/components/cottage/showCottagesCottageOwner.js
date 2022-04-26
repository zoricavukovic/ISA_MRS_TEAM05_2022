import * as React from 'react';
import { useEffect, useState } from "react";
import { CircularProgress } from "@mui/material";
import ImgMediaCard from "./CottageBasicCard.js";
import BasicPagination from "../Pagination.js";
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Autocomplete from '@mui/material/Autocomplete';
import axios from "axios";

const options = ['AND', 'OR'];
function ShowCottagesCottageOwner() {

    const [cottages, setCottages] = useState([]);
    const [isLoading, setLoading] = useState(true);
    const [pagination, setPagination] = useState(0);
    const [currentPage, setCurrentPage] = useState(1);
    const [numAddedCottages, setNumAddedCottages] = useState(0);
    const [valueFirst, setValueFirst] = React.useState(options[1]);
    const [valueSec, setValueSecond] = React.useState(options[1]);
    const [valueThird, setValueThird] = React.useState(options[1]);
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
    let bookingEntity = 1;
    
    
    useEffect(() => {
        axios.get("http://localhost:8092/bookingApp/cottages/owner/" + ownerId).then(res => {
            console.log("Barem se pokrece")
            console.log(res.data);
            setCottages(res.data);
            setPagination(Math.ceil(res.data.length / 6));
            setLoading(false);
        })
    }, []);
    const search = ()=>{
        let changesSearchElem = searchElem;
        changesSearchElem.firstOp = valueFirst;
        changesSearchElem.secondOp = valueSec;
        changesSearchElem.thirdOp = valueThird;
        setSearchElem(changesSearchElem);
        setLoading(true);
        axios.get("http://localhost:8092/bookingApp/cottages/" + ownerId + "/search/" + 
                        searchElem.cottageName + "/" + searchElem.cityName + "/" + searchElem.rate + "/" + searchElem.cost + "/" +
                        searchElem.firstOp + "/" + searchElem.secondOp + "/" + searchElem.thirdOp).then(res => {
            setCottages(res.data);
            setPagination(Math.ceil(res.data.length / 6));
            setLoading(false);
        })
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
            <div style={{ display: "flex", flexWrap: "wrap", flexDirection: "row", margin: "2%", width:"100%", alignItems: "stretch", backgroundColor: "aliceblue", borderRadius: "5px" }}>
                
                <TextField
                
                    id="outlined-number"
                    label="Cottage Name"
                    name="cottageName"
                    type="text"
                    onChange = {makeChange}
                    size="small"
                
                    placeholder="Cottage Name"
                    style={{ margin: "1%", maxWidth:"170px"}}
                    InputLabelProps={{
                        shrink: true,
                    }}
                />
                <Autocomplete
                    value={valueFirst}
                    onChange={(event, newValue) => {
                    setValueFirst(newValue);
                    }}
                    size="small"
                    name="firstOp"
                    id="controllable-states-demo"
                    options={options}
                    style={{ margin: "1%", minWidth:'120px', color: 'rgb(5, 30, 52)'}}
                    renderInput={(params) => <TextField {...params} label="Option" />}
                />
            
                <TextField style={{ margin: "1%", maxWidth:"170px" }}
                    id="outlined-number"
                    label="City"
                    type="text"
                    onChange = {makeChange}
                    size="small"
                    name="cityName"
                    placeholder="City"
                    InputLabelProps={{
                        shrink: true,
                    }}
                />
                <Autocomplete
                    value={valueSec}
                    onChange={(event, newValue) => {
                    setValueSecond(newValue);
                    }}
                    size="small"
                    name="firstOp"
                    id="controllable-states-demo"
                    options={options}
                    style={{ margin: "1%", minWidth:'120px', color: 'rgb(5, 30, 52)'}}
                    renderInput={(params) => <TextField {...params} label="Option" />}
                />
                <TextField style={{ margin: "1%",maxWidth:"100px" }}
                    id="outlined-number"
                    label="Rate"
                    type="number"
                    onChange = {makeChange}
                    size="small"
                    name="rate"
                    placeholder="Rate"
                    InputLabelProps={{
                        shrink: true,
                    }}
                />
                <Autocomplete
                    value={valueThird}
                    onChange={(event, newValue) => {
                    setValueThird(newValue);
                    }}
                    size="small"
                    name="firstOp"
                    id="controllable-states-demo"
                    options={options}
                    style={{ margin: "1%", minWidth:'120px', color: 'rgb(5, 30, 52)'}}
                    renderInput={(params) => <TextField {...params} label="Option" />}
                />
                <TextField style={{ margin: "1%", maxWidth:"160px"}}
                    id="outlined-number"
                    label="Min Cost Per Night"
                    type="number"
                    onChange = {makeChange}
                    size="small"
                    name="cost"
                    placeholder="Min Cost Per Night"
                    InputLabelProps={{
                        shrink: true,
                    }}
                />
                <Button onClick={search} label="Extra Soft" style={{ color: 'rgb(5, 30, 52)', fontWeight: 'bold', borderRadius: '10px', margin: '1%', backgroundColor: 'rgb(244, 177, 77)' }}>
                    Search
                </Button>
    


            </div>
            <div style={{ display: "flex", flexWrap: 'wrap', flexDirection: "row", justifyContent: "center" }} className="App">
                
                {cottages.map(cottage=> {
                    if (numAddedCottages !== 2){
                        let i = numAddedCottages + 1;
                        //setNumAddedCottages(i);
                        console.log({cottage});
                        return <ImgMediaCard key={i} bookingEntity={cottage} bookingEntityId={cottage.id}></ImgMediaCard>
                    }
                })}

            </div>
            <BasicPagination count={pagination} />
        </div>


    );
}

export default ShowCottagesCottageOwner;
