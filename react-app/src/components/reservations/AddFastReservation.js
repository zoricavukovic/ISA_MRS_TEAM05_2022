import * as React from 'react';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import { useEffect, useState } from "react";
import { styled } from '@mui/material/styles';
import MuiInput from '@mui/material/Input';
import OutlinedInput from '@mui/material/OutlinedInput';
import InputLabel from '@mui/material/InputLabel';
import InputAdornment from '@mui/material/InputAdornment';
import FormControl from '@mui/material/FormControl';
import Button from '@mui/material/Button';
import { CircularProgress } from "@mui/material";
import { useForm } from "react-hook-form";
import { useHistory } from "react-router-dom";
import AddingAdditionalServiceWithoutAmount from "../AddingAdditionServicesWithoutAmount";
import {getBookingEntityByIdForCardView} from '../../service/BookingEntityService.js';
import { getCurrentUser } from '../../service/AuthService.js';
import Alert from '@mui/material/Alert';
import Snackbar from '@mui/material/Snackbar';
import { DateTimePicker } from '@mui/x-date-pickers/DateTimePicker';
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import Chip from '@mui/material/Chip';
import CalendarMonthIcon from '@mui/icons-material/CalendarMonth';
import IconButton from '@mui/material/IconButton';
import { getPricelistByEntityId } from '../../service/PricelistService';
import { addNewFastReservation } from '../../service/ReservationService';

const Input = styled(MuiInput)`
  width: 42px;
`;
export default function AddFastReservation(props) {

  const { register, handleSubmit, reset, formState: { errors } } = useForm();
  const [isLoading, setLoading] = useState(true);
  const [isLoadingPricelist, setLoadingPricelist] = useState(true);
  const [entityBasicData, setEntityBasicData] = useState({});
  const [pricelistData, setPricelistData] = useState({});
  const [message, setMessage] = React.useState("");
  const history = useHistory();
  let ownerId = null;
  const [fastResData, setFastResData] = React.useState(
    {
      "id":0,
      "entityPricePerPerson": 0,
      "startDate": new Date(new Date().toLocaleString()),
      "additionalServices": [], 
      "bookingEntity": null
    }
   );

   ////////////////ERROR MESSAGE/////////////////////////
   const [open, setOpen] = React.useState(false);
   const handleClose = (_event, reason) => {
    if (reason === 'clickaway') {
        return;
    }
    setOpen(false);
    };
    const handleClick = () => {
      setOpen(true);
    };

    //////////////////DATE TIME PICKER /////////////////////////
    const [value, setValue] = React.useState(Date.now());

    const handleChange = (newValue) => {
      setValue(newValue);
    };
    ////////////////////////////////////////////////////////////

    //////////////////ADITIONAL SERVICES////////////////////////
    const [additionalServices, setAdditionalServices] = React.useState([
    ]);
    const [addedAdditionalServices, setAddedAdditionalServices] = React.useState([
    ]);

    const handleDeleteAdditionalServiceChip = (chipToDelete) => {
        setAdditionalServices((chips) => chips.filter((chip) => chip.serviceName !== chipToDelete.serviceName));
       
        let newKey = 1;
        if (addedAdditionalServices.length != 0) {
            
            newKey = Math.max.apply(Math, addedAdditionalServices.map(chip => chip.key)) + 1;    
        }
        let newObj = {
          "key": newKey,
          "serviceName": chipToDelete.serviceName
        };
        let newChipData = [...addedAdditionalServices];
        newChipData.push(newObj);
        setAddedAdditionalServices(newChipData);
    };

    const handleDeleteAddedAdditionalServiceChip = (chipToDelete) => {
      setAddedAdditionalServices((chips) => chips.filter((chip) => chip.serviceName !== chipToDelete.serviceName));
      let addSer = additionalServices;
      addSer.push(chipToDelete);
      setAdditionalServices(addSer);
  };
    const getAddedAdditionalServicesJson = () => {
        if (addedAdditionalServices.length === 0) {
            return []
        }
        let retVal = [];
        for (let service of addedAdditionalServices) {
            retVal.push({
                serviceName : service.serviceName,
                price : 0.0
            });
        }
        return retVal;
    }
    ////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////


    const onFormSubmit = data => {
      
      if (getCurrentUser() == null || getCurrentUser() == undefined || getCurrentUser().userType.name!=="ROLE_COTTAGE_OWNER") {
        history.push('/login');
      } else {
          ownerId = getCurrentUser().id;
      }
      let newFastReservation={
        canceled:false,
        fastReservation: true,
        numOfDays: data.numNights,
        numOfPersons: data.maxNumPeople,
        startDate: data.dateTimeStart,
        additionalServices: getAddedAdditionalServicesJson()
      }
      console.log(newFastReservation);
      
  }
  useEffect(() => {
    let owner = getCurrentUser();
    if (owner === null || owner === undefined){
        history.push('/login');
    }
    else{
        ownerId = owner.id;
    }
    if (props.history.location.state === null || props.history.location.state === undefined){
        return;
    }
    getBookingEntityByIdForCardView(props.history.location.state.bookingEntityId).then(res => {
      console.log(res.data);
      setEntityBasicData(res.data);
      setLoading(false);
    }).catch(error => {
        setMessage(error.response.data);
        handleClick();
    });

    getPricelistByEntityId(props.history.location.state.bookingEntityId).then(res => {
      console.log(res.data);
      setPricelistData(res.data);
      setAdditionalServices(res.data.additionalServices);
      setLoadingPricelist(false);
    }).catch(error => {
        setMessage(error.response.data);
        handleClick();
    });

  }, []);
  

  if (isLoading || isLoadingPricelist) { return <div className="App"><CircularProgress /></div> }

  return (
    <div style={{ backgroundColor: 'aliceblue', margin: '5%', padding: '1%', borderRadius: '10px', height: '100%' }} >
      {console.log(entityBasicData)}
      <div style={{ color: 'rgb(5, 30, 52)', fontWeight: 'bold', textAlign: 'center', backgroundColor: 'rgb(244, 177, 77)', marginLeft: '15%', padding: '1%', borderRadius: '10px', width: '15%' }} >
        Add Fast Reservation
      </div>
      <Box sx={{ marginTop: '1%', marginLeft: '11%', marginRight: '5%', width: '90%' }}
      component="form"
      onSubmit={handleSubmit(onFormSubmit)}>
        <Box
          sx={{
            '& .MuiTextField-root': { m: 1, width: '28ch' },
          }}
          style={{ display: "flex", flexDirection: "row"}}
        >
          
          <div style={{  display: "flex", flexDirection: "row", color: 'rgb(5, 30, 52)', fontWeight: 'bold', textAlign: 'left', backgroundColor: 'rgb(191, 230, 255)', marginLeft: '4%', padding: '1%', borderRadius: '10px', width: '30%', height: '100%', minWidth:"320px" }} >
            <table>
              <tr>
                <td>
                  <TextField
                      id="outlined-read-only-input"
                      label="Entity Name"
                      multiline
                      defaultValue={entityBasicData.name}
                      InputProps={{
                          readOnly: true,
                      }}
                    />
                 </td>      
              </tr>
              
              <tr>
                <td>
                <TextField
                      id="outlined-read-only-input"
                      label="Place"
                      multiline
                      defaultValue={entityBasicData.address + ", " +  entityBasicData.place.zipCode + " " + entityBasicData.place.cityName + ", " + entityBasicData.place.stateName}
                      InputProps={{
                          readOnly: true,
                      }}
                    />
                  </td>
              </tr>
              <tr>
                <td>
                <TextField
                      id="outlined-read-only-input"
                      label="Cost Per Person"
                      multiline
                      defaultValue={pricelistData.entityPricePerPerson + "€"}
                      InputProps={{
                          readOnly: true,
                      }}
                    />
                  </td>
              </tr>
              <tr>
                <td>
                  <IconButton >
                    <Chip icon={<CalendarMonthIcon />} label="Calendar" />
                  </IconButton>
                </td>
              </tr>
            </table>
          </div>
          
          <div style={{ display: 'block', color: 'rgb(5, 30, 52)', fontWeight: 'bold', textAlign: 'left', backgroundColor: 'rgb(191, 230, 255)', marginLeft: '4%', marginBottom:"1%",padding: '1%', borderRadius: '10px', width: '40%', height: '100%', minWidth:"300px" }} >
            <table>
              <tr>
                <td>
                  <LocalizationProvider dateAdapter={AdapterDateFns}>
                    <DateTimePicker
                      name="dateTimeStart"
                      label="Date&Time Picker"
                      size="small"
                      value={value}
                      minDateTime = {value}
                      onChange={handleChange}
                      renderInput={(params) => <TextField {...params} />}
                      {...register("dateTimeStart", { required: true })}
                    />
                  </LocalizationProvider>
                </td>
              </tr>
              <tr>{errors.dateTimeStart && <p style={{ color: '#ED6663', fontSize:"10px" }}>Please check start date.</p>}</tr>
              
              <tr>
              <FormControl sx={{ m: 1 }}>
                  <InputLabel htmlFor="outlined-adornment-amount">Total Num Of Nights</InputLabel>
                    <OutlinedInput
                      id="outlined-adornment-amount"
                      size="small"
                      name="numNights"
                      type="number"
                      onChange={e => {
                        let data = fastResData;
                        let days = parseInt(e.target.value);
                        if (days === NaN) alert("Greska");
                        else {
                          data.totalNumLength = days;
                        }
                        setFastResData(data);
                      }}
                      placeholder="Total Num Of Nights"
                      startAdornment={<InputAdornment position="start">night</InputAdornment>}
                      label="Total Num Of Nights"
                      {...register("numNights", { required: true, min: 1, max: 100 })}
                    />
                  </FormControl>
              </tr>
              <tr>{errors.numNights && <p style={{ color: '#ED6663', fontSize:"10px" }}>Please check num of days between 1-100.</p>}</tr>
               
              <tr>
                <FormControl sx={{ m: 1 }}>
                  <InputLabel htmlFor="outlined-adornment-amount">Max Num Of People</InputLabel>
                    <OutlinedInput
                      id="outlined-adornment-amount"
                      size="small"
                      name="maxNumPeople"
                      type="number"
                      onChange={e => {
                        let data = fastResData;
                        let people = parseInt(e.target.value);
                        if (people === NaN) alert("Greska");
                        else {
                          data.numOfPeople = people;
                        }
                        setFastResData(data);
                      }}
                      placeholder="Max Num Of People"
                      label="Max Num Of People"
                      startAdornment={<InputAdornment position="start"></InputAdornment>}
                      {...register("maxNumPeople", { required: true, min: 1, max: 30 })}
                    />
                  </FormControl>
              </tr>
              <tr>{errors.maxNumPeople && <p style={{ color: '#ED6663', fontSize:"10px" }}>Please check num of people between 1-30.</p>}</tr>
               
              <tr>
                <td>
                  <FormControl sx={{ m: 1 }}>
                    <InputLabel htmlFor="outlined-adornment-amount">Cost Per Night</InputLabel>
                    <OutlinedInput
                      id="outlined-adornment-amount"
                      size="small"
                      name="cost"
                      type="number"
                      onChange={e => {
                        let data = pricelistData;
                        let cost = parseInt(e.target.value);
                        if (cost === NaN) alert("Greska");
                        else {
                          data.entityPricePerPerson = cost;
                        }
                        setPricelistData(data);
                      }}
                      placeholder="Cost"
                      startAdornment={<InputAdornment position="start">€</InputAdornment>}
                      label="Cost Of Fast Reservation"
                      {...register("cost", { required: true, min: 1, max: 100000 })}
                    />
                  </FormControl>
                  
                  </td>
                
                <tr>{errors.cost && <p style={{ color: '#ED6663', fontSize:"10px" }}>Please check cost per between 1-100000€.</p>}</tr>
              
              </tr>
              <tr>
                <td>
                <AddingAdditionalServiceWithoutAmount addServ={additionalServices} addedServ={addedAdditionalServices} onDeleteChip={handleDeleteAdditionalServiceChip} onDeleteAddedChip={handleDeleteAddedAdditionalServiceChip} float="left" />
              
                </td>
              </tr>
              
            </table>

          </div>
          
        </Box>
      </Box>
      <Box style={{ display: "flex", flexDirection: "row" }}>
      <Button type="submit" onClick={handleSubmit(onFormSubmit)} variant="contained" style={{ color: 'rgb(5, 30, 52)', fontWeight: 'bold', textAlign: 'center', backgroundColor: 'rgb(244, 177, 77)', marginLeft: '33.5%', marginTop: '1%', padding: '1%', borderRadius: '10px', width: '15%' }}>
          Save
      </Button>
        <Button variant="contained" style={{ color: 'rgb(5, 30, 52)', fontWeight: 'bold', textAlign: 'center', backgroundColor: 'rgb(244, 177, 77)', marginLeft: '2%', marginTop: '1%', padding: '1%', borderRadius: '10px', width: '15%' }}
        onClick={() => {
          setValue(Date.now());
          reset(
              {
                numNights: 1,
                cost: 1,
                maxNumPeople:1
                
              }, {
              keepDefaultValues: false,
              keepErrors: true,
          }
          
          );
      }}
        >
          Reset
        </Button>
        <Snackbar open={open} autoHideDuration={6000} onClose={handleClose}>
            <Alert onClose={handleClose} severity="error" sx={{ width: '100%' }}>
                {message}
            </Alert>
        </Snackbar>
      </Box>

    </div>
  );
}