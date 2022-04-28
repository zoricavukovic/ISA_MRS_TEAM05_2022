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
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';
import { Divider } from "@mui/material";
import ImageUploader from "../image_uploader/ImageUploader.js";
import { CircularProgress, NativeSelect } from "@mui/material";
import { useForm } from "react-hook-form";
import { useHistory } from "react-router-dom";
import AddingAdditionalServiceAdventure from "../AddingAdditionalService.js";
import AddingRulesOfConductAdventure from "../AddingRulesOfConduct.js";
import Autocomplete from '@mui/material/Autocomplete';
import { addNewPriceListForEntityId} from '../../service/Pricelists.js';
import { getAllPlaces } from '../../service/PlaceService.js';
import {addNewCottage} from '../../service/CottageService.js';
import AddingRooms from '../AddingRooms.js';
import { getCurrentUser } from '../../service/AuthService.js';
import Alert from '@mui/material/Alert';
import Snackbar from '@mui/material/Snackbar';

const Input = styled(MuiInput)`
  width: 42px;
`;
export default function AddCottage(props) {

  const { register, handleSubmit, reset, formState: { errors } } = useForm();
  const [isLoadingPlaces, setLoadingPlaces] = useState(true);
  const [cottageBasicData, setCottageBasicData] = useState({});
  const [pricelistData, setPricelistData] = useState({});
  const [places, setPlaces] = useState([]);
  const [hiddenError, setHiddenError] = useState("none");
  const [message, setMessage] = React.useState("");
  const history = useHistory();
  let ownerId = null;
  const [newPricelist, setNewPricelist] = React.useState(
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

    ////////////////IMAGES//////////////////////////////////
    const [images, setImages] = React.useState([]);
    const maxNumber = 69;
    const onChange = (imageList, addUpdateIndex) => {
        console.log(imageList, addUpdateIndex);
        setImages(imageList);
    };
    const getImagesInJsonBase64 = () => {
        if (images.length === 0) {
            return [];
        }
        let retVal = [];
        
        for (let img of images) {
            retVal.push({
                imageName: img.file.name,
                dataBase64: getBase64String(img.data_url),
            });
        }
        return retVal;
    }

    const getBase64String = (data_url) => {
        return data_url.split(";")[1].split(',')[1];
    }
    /////////////////////////////////////


    //////////////////ADITIONAL SERVICES////////////////////////
    const [additionalServices, setAdditionalServices] = React.useState([
    ]);

    const handleDeleteAdditionalServiceChip = (chipToDelete) => {
        setAdditionalServices((chips) => chips.filter((chip) => chip.serviceName !== chipToDelete.serviceName));
        
    };

    const handleAddAdditionalServiceChip = (data) => {
        let sName = data.additionalService;
        let newKey = 1;
        if (additionalServices.length != 0) {
            for (let chip of additionalServices) {
                if (chip.serviceName.toLowerCase() === sName.toLowerCase())
                    return;
            }
            newKey = Math.max.apply(Math, additionalServices.map(chip => chip.key)) + 1;    
        }
        let newAmount = data.amount;
        let newObj = {
            "key": newKey,
            "serviceName": sName,
            "price": newAmount
        };
        let newChipData = [...additionalServices];
        newChipData.push(newObj);
        setAdditionalServices(newChipData);
    }

    const getAdditionalServicesJson = () => {
        if (additionalServices.length === 0) {
            return []
        }
        let retVal = [];
        for (let service of additionalServices) {
            retVal.push({
                serviceName : service.serviceName,
                price : service.price
            });
        }
        return retVal;
    }
    ////////////////////////////////////////////



    ////////////ROOM ///////////////////////////
    const [room, setRoom] = React.useState([
    ]);

    const handleDeleteRoomChip = (chipToDelete) => {
        console.log(chipToDelete);
        setRoom((chips) => chips.filter((chip) => chip.roomNum !== chipToDelete.roomNum));
    };

    const handleAddRoomChip = (data) => {
        let rNum = data.roomNum;
        let rNumOfBeds = data.numOfBeds;
        let newKey = 1;
        if (room.length != 0) {
            for (let chip of room) {
                if (chip.roomNum == rNum)
                    return;
            }
            newKey = Math.max.apply(Math, room.map(chip => chip.key)) + 1;    
        }
        
        let newObj = {
            "key": newKey,
            "roomNum": parseInt(rNum),
            "numOfBeds":parseInt(rNumOfBeds)
        };
        let newChipData = [...room];
        newChipData.push(newObj);
        
        setRoom(newChipData);
    }
    const getRoomsJson = () => {
      if (room.length === 0) {
        return []
      }
      let retVal = [];
      for (let r of room) {
          retVal.push({
              roomNum : r.roomNum,
              numOfBeds : r.numOfBeds, 
              deleted:false
          });
      }
      return retVal;
    }
    /////////////////////////////////////////////////////////////////////



    ////////////////////////RULES OF CONDUCT///////////////////////////////////////
    const [checked, setChecked] = React.useState(false);
    const [rulesOfConduct, setRulesOfConduct] = React.useState([
    ]);

    const handleRuleCheckedChange = (event) => {
        setChecked(event.target.checked);
      };
    
    const handleDeleteRuleChip = (chipToDelete) => {
        setRulesOfConduct((chips) => chips.filter((chip) => chip.ruleName !== chipToDelete.ruleName));
    };

    const handleAddRuleChip = (data) => {
        let rName = data.ruleName;
        let newKey = 1;
        if (rulesOfConduct.length != 0) {
            for (let chip of rulesOfConduct) {
                if (chip.ruleName.toLowerCase() === rName.toLowerCase())
                    return;
            }    
            newKey = Math.max.apply(Math, rulesOfConduct.map(chip => chip.key)) + 1;
        }
        let isAllowed = checked;

        let newObj = {
            "key": newKey,
            "ruleName": rName,
            "allowed": isAllowed
        };
        let newChipData = [...rulesOfConduct];
        newChipData.push(newObj);
        setRulesOfConduct(newChipData);
    }
    const getRuleNamesJson = () => {
        if (rulesOfConduct.length === 0) {
            return []
        }
        let retVal = [];
        for (let r of rulesOfConduct) {
            retVal.push({
                ruleName : r.ruleName,
                allowed : r.allowed,
            });
        }
        return retVal;
    }
    ///////////////////////////////////////////////////////////////////////////

    ///////////////// PLACES ///////////////////////////
    
    const [selectedPlaceZip, setSelectedPlaceZip] = useState('');
    let allPlacesList;

    const placeOnChange = (event, newValue) => {
        if (newValue != null && newValue != undefined && newValue != '') {
          setSelectedPlaceZip(newValue.zip);
        } else {
          setSelectedPlaceZip('');
        }
    }
    const getAllPlacesForTheList = () => {
      let newArray = []
      for (let plac of places) {
        newArray.push({ 'label': plac.cityName + ',' + plac.zipCode + ',' + plac.stateName, 'zip': plac.zipCode });
       
      }
      allPlacesList = newArray;
  }
  
    ///////////////////////////////////////////////////

    const onFormSubmit = data => {
      const cottageRate = cottageBasicData.entityCancelationRate;
      let zip = 0;
      if (selectedPlaceZip != null && selectedPlaceZip != undefined && selectedPlaceZip != '') {
          setHiddenError("none");
        zip = selectedPlaceZip;
      } else {
        setHiddenError("block");
        return;
      }
      const newCottage = {
        cottageId: 1,
        entityCancelationRate: cottageRate,
        name : data.name,
        address: data.address,
        place:{
          stateName:"",
          cityName:"",
          zipCode:selectedPlaceZip
        },
        promoDescription : data.promoDescription,
        entityCancelationRate: data.entityCancelationRate, 
        additionalServices: getAdditionalServicesJson(),
        rooms: getRoomsJson(),
        rulesOfConduct: getRuleNamesJson(),
        images: getImagesInJsonBase64(),
    } 
    newCottage.entityCancelationRate = cottageRate;
    let newPricel = newPricelist;
    newPricel.entityPricePerPerson =  parseInt(data.costPerNight);
    newPricel.additionalServices = getAdditionalServicesJson();
    setNewPricelist(newPricel);
    if (getCurrentUser() == null || getCurrentUser() == undefined || getCurrentUser().userType.name!=="ROLE_COTTAGE_OWNER") {
      history.push('/login');
    } else {
        ownerId = getCurrentUser().id;
    }
    addNewCottage(ownerId, newCottage).then(result => { 
      addNewPriceListForEntityId(result.data, newPricelist).then(res => {
        history.push({
          pathname: "/showCottageProfile",
          state: { bookingEntityId: result.data } 
        }) 
    }).catch(resError=>{
        console.log("Greska!!");
        setMessage(resError.response.data);
        handleClick();
        return;
    })    
  }).catch(resError=>{
    console.log("Greska!!");
    setMessage(resError.response.data);
    handleClick();
    return;
  })   
      
  }
  useEffect(() => {
    console.log(getCurrentUser());
    if (getCurrentUser() == null || getCurrentUser() == undefined || getCurrentUser().userType.name!=="ROLE_COTTAGE_OWNER") {
      history.push('/login');
    } else {
        ownerId = getCurrentUser().id;
    }
    getAllPlaces().then(results => {
      
      setPlaces(results.data);
        setLoadingPlaces(false);
      
    })
  }, []);
  

  //rate/////
  const [rate, setRate] = React.useState();
 
  function refreshPage() {
    window.location.reload(false);
  }

  if (isLoadingPlaces) { return <div className="App"><CircularProgress /></div> }

  return (
    <div style={{ backgroundColor: 'aliceblue', margin: '5%', padding: '1%', borderRadius: '10px', height: '100%' }} >
      {getAllPlacesForTheList()}
      <div style={{ color: 'rgb(5, 30, 52)', fontWeight: 'bold', textAlign: 'center', backgroundColor: 'rgb(244, 177, 77)', marginLeft: '15%', padding: '1%', borderRadius: '10px', width: '15%' }} >
        Add New Cottage
      </div>
      <br />
      <Divider />
      <br />
      <ImageUploader images={images} maxNumber={maxNumber} onChange={onChange} />
      <br />
      <h4 style={{ color: 'rgb(5, 30, 52)', fontWeight: 'bold', marginLeft:"15%" }}>Basic Information About Cottage</h4>
         
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
                    id="outlined-textarea"
                    label="Cottage Name"
                    placeholder="Cottage Name"
                    name="name"
                    onChange={e => {
                      let data = cottageBasicData;
                      data.name = e.target.value;
                      setCottageBasicData(data);
                    }}
                    multiline
                    size="small"
                    {...register("name", { required: true, maxLength: 50 })}
                    style={{ width: '200px' }}
                  />
                   
                 </td>      
              </tr>
              <tr><td>{errors.name && <p style={{ color: '#ED6663', fontSize:"10px" }}>Please check name: maxLength 50</p>}</td></tr>
              <tr>
                <td>
                    <TextField
                      id="outlined-textarea"
                      label="Address"
                      placeholder="Address"
                      multiline
                      name="address"
                      onChange={e => {
                        let dataAdd = cottageBasicData;
                        dataAdd.address = e.target.value;
                        setCottageBasicData(dataAdd);
                      }}
                      size="small"
                      {...register("address", { required: true, maxLength: 50 })}
                      style={{ width: '200px' }}
                    />
                  
                  </td>
              </tr>
              <tr><td> {errors.address && <p style={{ color: '#ED6663', fontSize:"10px" }}>Please check address: maxLength 50</p>}</td></tr>
              <tr>
              <td>
                <Grid item>
                        <Autocomplete
                            disablePortal
                            id="place"
                            size="small"
                            name="place"
                            options={allPlacesList}
                            onChange={placeOnChange}
                            renderInput={(params) => <TextField {...params} label="Place" />}
                            
                        />
                    </Grid>
                </td>
                </tr>
                <tr>
                <p style={{ color: '#ED6663', fontSize:"10px" , display:hiddenError}}>Please check place.</p>
                </tr>
            </table>
          </div>
          
          <div style={{ display: 'block', color: 'rgb(5, 30, 52)', fontWeight: 'bold', textAlign: 'left', backgroundColor: 'rgb(191, 230, 255)', marginLeft: '4%', marginBottom:"1%",padding: '1%', borderRadius: '10px', width: '40%', height: '100%', minWidth:"300px" }} >
            <table>
              <tr>
                <td>
                  <FormControl sx={{ m: 1 }}>
                    <InputLabel htmlFor="outlined-adornment-amount">Cost Per Night</InputLabel>
                    <OutlinedInput
                      id="outlined-adornment-amount"
                      size="small"
                      name="costPerNight"
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
                      placeholder="Cost Per Night"
                      startAdornment={<InputAdornment position="start">€</InputAdornment>}
                      label="Cost Per Night"
                      {...register("costPerNight", { required: true, min: 1, max: 100000 })}
                    />
                  </FormControl>
                  
                  </td>
                </tr>
                <tr>{errors.costPerNight && <p style={{ color: '#ED6663', fontSize:"10px" }}>Please check cost per night between 1-100000€</p>}</tr>
               
              <tr>
                <td>
                  <TextField
                    size="small"
                    id="outlined-multiline-static"
                    label="Promo Description"
                    name="promoDescription"
                    multiline
                    rows={2}
                    onChange={e => {
                      let data = cottageBasicData;
                      data.promoDescription = e.target.value;
                      setCottageBasicData(data);
                    }}
                    placeholder="Promo Description"
                    {...register("promoDescription", { maxLength: 250 })}
                    style={{ width: '200px' }}
                  />
                </td>
                </tr>
                <tr> {errors.promoDescription && <p style={{ color: '#ED6663', fontSize:"10px" }}>Max num of characters is 250.</p>}
                 </tr>
                <tr>
                <td>
                    <Typography id="input-slider" gutterBottom>
                      Cancelation Rate
                    </Typography>
                        <Input
                          size="small"
                          defaultValue = "0"
                          onChange={e => {
                            let data = cottageBasicData;
                            let cost = parseFloat(e.target.value);
                            if (cost === NaN) alert("Greska");
                            else {
                              data.entityCancelationRate = cost;
                            }
                            setCottageBasicData(data);
                          
                          }}
                          type="number"
                          name="entityCancelationRate"
                          inputProps={{
                            step: 1,
                            min: 0,
                            max: 50,
                            type: 'number',
                            'aria-labelledby': 'input-slider',
                          }}
                        />
                </td>
              </tr>
              
            </table>

          </div>
          
        </Box>
      </Box>
      <Box style={{ display: "flex", flexDirection: "row" }}>
          <AddingAdditionalServiceAdventure data={additionalServices} onDeleteChip={handleDeleteAdditionalServiceChip} onSubmit={handleAddAdditionalServiceChip} float="left" />
          <AddingRooms data={room} onDeleteChip={handleDeleteRoomChip} onSubmit={handleAddRoomChip} float="left" />
          <AddingRulesOfConductAdventure data={rulesOfConduct} onDeleteChip={handleDeleteRuleChip} onSubmit={handleAddRuleChip} ruleChecked={checked} handleRuleCheckedChange={handleRuleCheckedChange} float="left" />
      </Box>

      <Box style={{ display: "flex", flexDirection: "row" }}>
      <Button type="submit" onClick={handleSubmit(onFormSubmit)} variant="contained" style={{ color: 'rgb(5, 30, 52)', fontWeight: 'bold', textAlign: 'center', backgroundColor: 'rgb(244, 177, 77)', marginLeft: '33.5%', marginTop: '1%', padding: '1%', borderRadius: '10px', width: '15%' }}>
          Save
      </Button>
        <Button variant="contained" style={{ color: 'rgb(5, 30, 52)', fontWeight: 'bold', textAlign: 'center', backgroundColor: 'rgb(244, 177, 77)', marginLeft: '2%', marginTop: '1%', padding: '1%', borderRadius: '10px', width: '15%' }}
        onClick={() => {
          reset(
              {
                  name: "",
                  address: "",
                  entityCancelationRate: 0,
                  promoDescription: "",
                  costPerNight: 0,
                  rulesOfConduct:[],
                  rooms:[]
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