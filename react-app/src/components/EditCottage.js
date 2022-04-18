import * as React from 'react';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import { useEffect, useState } from "react";
import { styled } from '@mui/material/styles';
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
import { CircularProgress, NativeSelect} from "@mui/material";
import { useForm } from "react-hook-form";

import axios from "axios";

const ListItem = styled('li')(({ theme }) => ({
  margin: theme.spacing(0.5),
}));
const Input = styled(MuiInput)`
  width: 42px;
`;
export default function EditCottage(props) {

  const { register, handleSubmit, reset, formState: { errors } } = useForm();
  const [isLoadingCottage, setLoadingCottage] = useState(true);
  const [isLoadingPricelist, setLoadingPricelist] = useState(true);
  const [isLoadingPlaces, setLoadingPlaces] = useState(true);
  const [cottageBasicData, setCottageBasicData] = useState({});
  const [pricelistData, setPricelistData] = useState({});
  const [ruleConData, setRuleCon] = useState([]);
  const [roomData, setRoom] = useState([]);
  const [addSerData, setAddSerData] = useState([]);
  const [places, setPlaces] = useState([]);
  const [selectedPlace, setSelectedPlace] = useState({});
  const [country,setCountry] = useState("");
  const [city,setCity] = useState("");
  const [error, setError] = useState({
      cottageName: ""
  });
  const urlCottagePath = "http://localhost:8092/bookingApp/cottages";
  const urlPricelistPath = "http://localhost:8092/bookingApp/pricelists";


  useEffect(() => {
    console.log(props.history.location.state);
    axios.get(urlCottagePath + "/" + props.history.location.state.cottageId).then(res => {
      setCottageBasicData(res.data);
      setCountry(res.data.place.stateName);
      setLoadingCottage(false);
    })
    axios.get(urlPricelistPath + "/" + props.history.location.state.cottageId).then(result => {
      setPricelistData(result.data);
      setLoadingPricelist(false);
    })
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
      if (isNaN(valueRoomNum)) {alert("Samo broj sme."); return;}
      let valueNumOfBeds= parseFloat(tokens[1]);
      if (isNaN(valueNumOfBeds)) {alert("Samo broj sme."); return;}
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
      if (valuePrice === NaN) {alert("Samo broj sme."); return;}
      let newAddS = {

        price:valuePrice,
        serviceName:tokens[0]
      }
      newServices.push(newAddS);
    }
    setAddSerData(newServices);
  };

  const handleInputChange = (event) => {
    let newData = cottageBasicData;
    newData.entityCancelationRate = event.target.value === '' ? '' : Number(event.target.value);
    setCottageBasicData(newData);
  };

  const handleBlur = () => {
    if (cottageBasicData.entityCancelationRate < 0) {
      let newData = cottageBasicData;
      newData.entityCancelationRate = 0;
      setCottageBasicData(newData);
    } else if (cottageBasicData.entityCancelationRate > 50) {
      let newData = cottageBasicData;
      newData.entityCancelationRate = 50;
      setCottageBasicData(newData);
    }
  };
  useEffect(() => {
    setCottageBasicData(prevState => ({
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
  setCottageBasicData(prevState => ({
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

  const saveChanges = () => {
    let newData = cottageBasicData;
    newData.rulesOfConduct = ruleConData;
    newData.rooms = roomData;
    newData.additionalServices = addSerData;
    let newPricelistData = pricelistData;
    newPricelistData.additionalServices = addSerData;
    if (cottageBasicData.entityCancelationRate < 0) {
      newData.entityCancelationRate = 0;
    } else if (cottageBasicData.entityCancelationRate > 50) {
      newData.entityCancelationRate = 50;
    }
    setCottageBasicData(newData);
    setPricelistData(newPricelistData);
    console.log(cottageBasicData.name.length);
    if (cottageBasicData.name.length > 255){
        let err = error;
        err.cottageName = "Max length is 255 characters!";
        setError(err);
        return;
    }
    console.log("Poslednja provera.");
    console.log(cottageBasicData);
    console.log(pricelistData);
    axios.put(urlCottagePath, cottageBasicData).then(result => {
      console.log("Uspesno!!");
            
        }).catch(res=>{
            console.log("Greska!!");})    
    axios.post(urlPricelistPath + "/" + cottageBasicData.id, pricelistData).then(result => {
      console.log("Uspesno!!");
            //alert("Changes saved");
        }).catch(res=>{
            console.log("Greska!!");})    
  };

  function refreshPage(){
    window.location.reload(false);
  }

  if (isLoadingCottage || isLoadingPricelist || isLoadingPlaces) { return <div className="App"><CircularProgress /></div> }

  return (
    <div style={{ backgroundColor: 'aliceblue', margin: '5%', padding: '1%', borderRadius: '10px', height: '100%' }} >
      <div style={{ color: 'rgb(5, 30, 52)', fontWeight: 'bold', textAlign: 'center', backgroundColor: 'rgb(244, 177, 77)', marginLeft: '42%', padding: '1%', borderRadius: '10px', width: '15%' }} >
        Edit Cottage
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
                    id="outlined-textarea"
                    label="Cottage Name"
                    placeholder="Cottage Name"
                    defaultValue={cottageBasicData.name}
                    onChange = {e => {
                      let data = cottageBasicData;
                      data.name = e.target.value;
                      setCottageBasicData(data);
                    }}
                    multiline
                    size="small"
                    style={{ width: '200px' }}
                  />
                  <div onChange={setError} style={{color:"red", fontSize:"10px"}}>{error.cottageName}</div>
                </td>
                <td>
                  <FormControl sx={{ m: 1 }}>
                    <InputLabel htmlFor="outlined-adornment-amount">Cost Per Night</InputLabel>
                    <OutlinedInput
                      id="outlined-adornment-amount"
                      size="small"
                      defaultValue={pricelistData.entityPricePerPerson}
                      onChange = {e => {
                        let data = pricelistData;
                        let cost = parseInt(e.target.value);
                        if (cost === NaN) alert("Greska");
                        else{
                          data.entityPricePerPerson = cost;
                        }
                        setPricelistData(data);
                      }}
                      placeholder="Cost Per Night"
                      startAdornment={<InputAdornment position="start">€</InputAdornment>}
                      label="Cost Per Night"
                    />
                  </FormControl>
                </td>
                <td>
                  <TextField
                    id="outlined-textarea"
                    label="Address"
                    placeholder="Address"
                    multiline
                    defaultValue={cottageBasicData.address}
                    onChange = {e => {
                      let dataAdd = cottageBasicData;
                      dataAdd.address = e.target.value;
                      setCottageBasicData(dataAdd);
                    }}
                    size="small"
                    style={{ width: '200px' }}
                  />
                </td>
                <td>
                <InputLabel variant="standard" htmlFor="uncontrolled-native">
                        Country
                    </InputLabel>
                    <NativeSelect
                        defaultValue={cottageBasicData.place.stateName}
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
                                    defaultValue={cottageBasicData.place.cityName}
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
                    size="small"
                    id="outlined-multiline-static"
                    label="Promo Description"
                    multiline
                    rows={2}
                    onChange = {e => {
                      let data = cottageBasicData;
                      data.promoDescription = e.target.value;
                      setCottageBasicData(data);
                    }}
                    defaultValue={cottageBasicData.promoDescription}
                    placeholder="Promo Description"
                    style={{ width: '200px' }}
                  />
                </td>
                <td>
                  <Box sx={{ width: 250 }}>
                    <Typography id="input-slider" gutterBottom>
                      Cancelation Rate
                                        </Typography>
                    <Grid container alignItems="center" style={{ width: '200px', marginLeft: '24%' }}>

                      <Grid item>
                        <Input
                          defaultValue={cottageBasicData.entityCancelationRate}
                          size="small"
                          onChange={handleInputChange}
                          onBlur={handleBlur}
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
        <AddingAdditionalService float="left" onClick={getAddSer} onDelete={getAddSer} services={pricelistData.additionalServices}/>
        
        <AddingRooms float="left" onClick={getRooms} onDelete={getRooms} rooms={cottageBasicData.rooms}/>
        <AddingRulesOfConduct float="left" onClick={getRuleCon} onDelete={getRuleCon} rules={cottageBasicData.rulesOfConduct}/>
      </Box>

      <Box style={{ display: "flex", flexDirection: "row" }}>
        <Button onClick={saveChanges} variant="contained" style={{ color: 'rgb(5, 30, 52)', fontWeight: 'bold', textAlign: 'center', backgroundColor: 'rgb(244, 177, 77)', marginLeft: '36%', marginTop: '1%', padding: '1%', borderRadius: '10px', width: '15%' }}>
          Save Changes
            </Button>
        <Button variant="contained" onClick={refreshPage} style={{ color: 'rgb(5, 30, 52)', fontWeight: 'bold', textAlign: 'center', backgroundColor: 'rgb(244, 177, 77)', marginLeft: '2%', marginTop: '1%', padding: '1%', borderRadius: '10px', width: '15%' }}>
          Reset
            </Button>
      </Box>

    </div>
  );
}