import * as React from 'react';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import { styled } from '@mui/material/styles';
import { useEffect, useState } from "react";
import MuiInput from '@mui/material/Input';
import AddingAdditionalService from './AddingAdditionalService.js';
import AddingRooms from './AddingRooms.js';
import OutlinedInput from '@mui/material/OutlinedInput';
import InputLabel from '@mui/material/InputLabel';
import InputAdornment from '@mui/material/InputAdornment';
import FormControl from '@mui/material/FormControl';
import AddingRulesOfConduct from './AddingRulesOfConduct.js';
import Button from '@mui/material/Button';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';
import Slider from '@mui/material/Slider';
import { CircularProgress, NativeSelect} from "@mui/material";
import { useForm } from "react-hook-form";
import axios from "axios";

const Input = styled(MuiInput)`
  width: 42px;
`;
export default function AddCottage() {
    //PROMENITI ID COTTAGE OWNERA
    const [value, setValue] = React.useState(0);
    const { register, handleSubmit, formState: { errors } } = useForm();
    const [ruleConData, setRuleCon] = useState([]);
    const [roomData, setRoom] = useState([]);
    const [addSerData, setAddSerData] = useState([]);
    const [places, setPlaces] = useState([]);
    const [selectedPlace, setSelectedPlace] = useState({});
    const [country,setCountry] = useState("");
    const [city,setCity] = useState("");
    const [isLoadingPlaces, setLoadingPlaces] = useState(true);
    const urlPricelistPath = "http://localhost:8092/bookingApp/pricelists";
    const [newCottage, setNewCottage] = React.useState(
      {
      "id": 1,
      "rulesOfConduct": [],
      "place": {},
      "pricelists": [],
      "entityType": "COTTAGE",
      "entityCancelationRate": 0,
      "address": "",
      "name": "",
      "promoDescription": "",
      "rooms": []
      }
    );

    const [newPlace, setNewPlace] = React.useState(
      {
        "id": 1,
        "zipCode": 1,
        "cityName": "",
        "stateName": ""
      }
    );

    useEffect(() => {
        axios.get("http://localhost:8092/bookingApp/places/").then(results =>{
            setPlaces(results.data);
            setLoadingPlaces(false);
        })
      }, []);

    const getRuleCon = (ruleCon) =>{
        let newRulesCond = [];
        for (let obj of ruleCon) {
          let tokens = obj.label.split(":");
          let newRulCon = {
            ruleName:tokens[0],
            allowed:tokens[1]
          }
          newRulesCond.push(newRulCon);
        }
        setRuleCon(newRulesCond);
      };
    
      const getRooms = (rooms) =>{
        let newRooms = [];
        for (let obj of rooms) {
          let tokens = obj.label.split(":");
          let valueRoomNum = parseFloat(tokens[0]);
          if (valueRoomNum === NaN) {alert("Samo broj sme."); return;}
          let valueNumOfBeds= parseFloat(tokens[1]);
          if (valueNumOfBeds === NaN) {alert("Samo broj sme."); return;}
          let newRoom = {
            roomNum:valueRoomNum,
            numOfBeds:valueNumOfBeds
          }
          newRooms.push(newRoom);
        }
        setRoom(newRooms);
      };
    
      const getAddSer = (services) =>{
        let newServices = [];
        for (let obj of services) {
          let tokens = obj.label.split(":");
          let valuePrice = parseFloat(tokens[1]);
          console.log(valuePrice);
          if (valuePrice === NaN) {alert("Samo broj sme."); return;}
          let newAddS = {
    
            price:valuePrice,
            serviceName:tokens[0]
          }
          newServices.push(newAddS);
        }
        setAddSerData(newServices);
      };

      useEffect(() => {
        setNewCottage(prevState => ({
            ...prevState,
            place: selectedPlace
        }));
    },[selectedPlace])
    
      useEffect(() => {
        console.log(places);
        let countryPlace = places.find(function (element) {
            let someCityInCountry = element.stateName.localeCompare(country) === 0;
            if(city)
                return someCityInCountry && element.cityName.localeCompare(city)===0;
            else
                return someCityInCountry;
        })
    
        setSelectedPlace(countryPlace);
    }, [country,city])
    
    useEffect(() => {
        setNewCottage(prevState => ({
          ...prevState,
          place: selectedPlace
      }));
    },[city])
    
    
    const countryChanged = (event) =>{
      setCountry(event.target.value);
    }
    
    const cityChanged = (event) =>{
      setCity(event.target.value);
    };

    const [newPricelist, setNewPricelist] = React.useState(
      {
        "id":0,
        "entityPricePerPerson": 0,
        "startDate": new Date(new Date().toLocaleString()),
        "additionalServices": [], 
        "bookingEntity": null
      }
    );
    const makeChangePricelist = (event)=>{
      setNewPricelist(prevState => ({
        ...prevState,
        [event.target.name]: event.target.value
      }));
      setNewCottage(prevState => ({
          ...prevState,
          ["pricelists"]: newPricelist
      }));
    
    }

    const handleSliderChange = (event, newValue) => {
        setValue(newValue);
    };

    const handleInputChange = (event) => {
        console.log(newCottage);
        setValue(event.target.value === '' ? '' : Number(event.target.value));
    };

    const handleBlur = () => {
        if (value < 0) {
            setValue(0);
        } else if (value > 50) {
            setValue(0);
        }
    };

    const makeChange = (event)=>{
      console.log(newCottage)
      setNewCottage(prevState => ({
          ...prevState,
          [event.target.name]: event.target.value
      }));
  }
  const addCottage = ()=>{
    let data = newCottage;
    data.pricelists.entityPricePerPerson= parseFloat(newCottage.pricelists.entityPricePerPerson);
    data.entityCancelationRate = value;
    let pricelistData = newPricelist;
    pricelistData.additionalServices = addSerData;
    setNewPricelist(pricelistData);
    data.pricelists = null;
    data.rooms = roomData;
    data.rulesOfConduct = ruleConData;
    let bla = {
        "zipCode": newCottage.place.zipCode,
        "cityName": newCottage.place.cityName,
        "stateName": newCottage.place.stateName
    } 
    data.place = bla;
    setNewCottage(data);
    if (newCottage.place === undefined) {
        console.log("Nije uneto mesto"); 
        //countryName.backgroundColor = "red";
        return;
    }
    console.log(newCottage);
    //PROMENITIIITITITIITITITITIT
    axios.post("http://localhost:8092/bookingApp/cottages" + "/1", newCottage).then(res=>{
        axios.post(urlPricelistPath + "/" + res.data.id, newPricelist).then(result => {
            console.log("Uspesno!!");
               alert("Changes saved");
        }).catch(res=>{
            console.log("Greska!!");})
    }).catch(res=>{
        console.log("Greska!!");
    })
        
    
  }
  function refreshPage(){
    window.location.reload(false);
  }

  if (isLoadingPlaces) { return <div className="App"><CircularProgress /></div> }
    return (
        <div style={{ backgroundColor: 'aliceblue', margin: '5%', padding: '1%', borderRadius: '10px', height: '100%' }} >
            <div style={{ color: 'rgb(5, 30, 52)', fontWeight: 'bold', textAlign: 'center', backgroundColor: 'rgb(244, 177, 77)', marginLeft: '42%', padding: '1%', borderRadius: '10px', width: '15%' }} >
                Add New Cottage
        </div>
            <Box sx={{ marginTop: '1%', marginLeft: '5%', marginRight: '5%', width: '90%' }}>
                <Box
                    component="form"
                    sx={{
                        '& .MuiTextField-root': { m: 1, width: '28ch' },
                    }}
                    noValidate
                    autoComplete="off"
                >

                    <h4 style={{ color: 'rgb(5, 30, 52)', fontWeight: 'bold' }}>Basic Information About Cottage</h4>
                    <div style={{ display: 'block', color: 'rgb(5, 30, 52)', fontWeight: 'bold', textAlign: 'center', backgroundColor: 'rgb(191, 230, 255)', margin: '1%', padding: '1%', borderRadius: '10px', width: '100%', height: '100%' }} >
                        <table>
                            <tr>
                                <td>
                                    <TextField
                                        name="name"
                                        onChange={makeChange}
                                        id="outlined-textarea"
                                        label="Cottage Name"
                                        placeholder="Cottage Name"
                                        multiline
                                        size="small"
                                        style={{ width: '200px' }}
                                    />
                                </td>
                                <td>
                                <FormControl sx={{ m: 1 }}>
                                    <InputLabel htmlFor="outlined-adornment-amount">Cost Per Night</InputLabel>
                                    <OutlinedInput
                                        type="number"
                                        name="entityPricePerPerson"
                                        onChange={makeChangePricelist}
                                        id="outlined-adornment-amount"
                                        size="small"
                                        placeholder="Cost Per Night"
                                        startAdornment={<InputAdornment position="start">€</InputAdornment>}
                                        label="Cost Per Night"
                                    />
                                </FormControl>
                                </td>
                                <td>
                                    <TextField
                                        name="address"
                                        onChange={makeChange}
                                        id="outlined-textarea"
                                        label="Address"
                                        placeholder="Address"
                                        multiline
                                        size="small"
                                        style={{ width: '200px' }}
                                    />
                                </td>
                                <td>
                                    <InputLabel variant="standard" htmlFor="uncontrolled-native">
                                        Country
                                    </InputLabel>
                                    <NativeSelect
                                        className = "countryName"
                                        defaultValue = {places[0]}
                                        onChange = {countryChanged}
                                        inputProps={{
                                            name: 'Country',
                                            id: 'uncontrolled-native',
                                        }}
                                    >
                                        {places.map((place)=>(
                                            <option value={place.stateName}>{place.stateName}</option>
                                        ))}
                                    </NativeSelect>
                                </td>

                            </tr>
                            <tr>
                                
                                <td>
                                <InputLabel variant="standard" htmlFor="uncontrolled-native">
                                    City
                                </InputLabel>
                                <NativeSelect
                                    
                                    onChange = {cityChanged}
                                    inputProps={{
                                        name: 'city',
                                        id: 'uncontrolled-native',
                                    }}
                                >
                                    {places.map(place=> {
                                            if (place.stateName === country)
                                                return <option value={place.cityName}>{place.cityName}</option>
                                            return <></>
                                        }
                                    )}
                                </NativeSelect>
                                </td>
                                <td>
                                    <TextField
                                        name="promoDescription"
                                        onChange={makeChange}
                                        size="small"
                                        id="outlined-multiline-static"
                                        label="Promo Description"
                                        multiline
                                        rows={2}
                                        placeholder="Promo Description"
                                        style={{ width: '200px' }}
                                    />
                                </td>
                                <td>
                                    <Box sx={{ width: 250 }}>
                                        <Typography id="input-slider" gutterBottom>
                                            Cancelation Rate
                                        </Typography>
                                        <Grid container alignItems="center" style={{ width: '200px', marginLeft:'20%'}}>
                                        
                                            <Grid item xs>
                                                <Slider
                                                max="50"
                                                    value={typeof value === 'number' ? value : 0}
                                                    onChange={handleSliderChange}
                                                    aria-labelledby="input-slider"
                                                />
                                            </Grid>
                                            <Grid item>
                                                <Input
                                                    value={value}
                                                    size="small"
                                                    onChange={handleInputChange}
                                                    onBlur={handleBlur}
                                                    style={{marginLeft:'50%'}}
                                                    inputProps={{
                                                        step: 1,
                                                        min: 0,
                                                        max: 50,
                                                        type: 'number',
                                                        'aria-labelledby': 'input-slider',
                                                    }}
                                                />
                                            </Grid>
                                        </Grid>
                                    </Box>
                                </td>
                            </tr>
                        </table>
                        
                    </div>
                </Box>
            </Box>

            <Box style={{ display: "flex", flexDirection: "row" }}>
                <AddingAdditionalService float="left" onClick={getAddSer} onDelete={getAddSer} services={[]}/>
                <AddingRooms float="left" onClick={getRooms} onDelete={getRooms} rooms={[]}/>
                <AddingRulesOfConduct float="left" onClick={getRuleCon} onDelete={getRuleCon} rules={[]}/>
      
            </Box>

            <Box style={{ display: "flex", flexDirection: "row" }}>
                <Button onClick={addCottage} variant="contained" style={{ color: 'rgb(5, 30, 52)', fontWeight: 'bold', textAlign: 'center', backgroundColor: 'rgb(244, 177, 77)', marginLeft: '33.5%', marginTop: '1%', padding: '1%', borderRadius: '10px', width: '15%' }}>
                    Save
            </Button>
                <Button variant="contained" onClick={refreshPage} style={{ color: 'rgb(5, 30, 52)', fontWeight: 'bold', textAlign: 'center', backgroundColor: 'rgb(244, 177, 77)', marginLeft: '2%', marginTop: '1%', padding: '1%', borderRadius: '10px', width: '15%' }}>
                    Reset
            </Button>
            </Box>

        </div>
    );
}