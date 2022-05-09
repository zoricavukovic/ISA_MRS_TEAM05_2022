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
import { CircularProgress } from "@mui/material";
import { get, useForm } from "react-hook-form";
import { useHistory } from "react-router-dom";
import AddingAdditionalServiceAdventure from "../AddingAdditionalService.js";
import AddingRulesOfConductAdventure from "../AddingRulesOfConduct.js";
import AddingNavigationalEquipment from "../AddingNavigationalEquipment.js"
import Autocomplete from '@mui/material/Autocomplete';
import { dataURLtoFile} from "../../service/PictureService.js";
import { addNewPriceListForEntityId, getPricelistByEntityId } from '../../service/PricelistService.js';
import { getAllPlaces } from '../../service/PlaceService.js';
import { addNewShip } from '../../service/ShipService.js';
import AddingEquipment from '../AddingEquipment';
import { getCurrentUser } from '../../service/AuthService.js';
import Alert from '@mui/material/Alert';
import Snackbar from '@mui/material/Snackbar';
const ListItem = styled('li')(({ theme }) => ({
  margin: theme.spacing(0.5),
}));
const Input = styled(MuiInput)`
  width: 42px;
`;
export default function AddShip() {

  const { register, handleSubmit, reset, formState: { errors } } = useForm();
  const [isLoadingShip, setLoadingShip] = useState(true);
  const [isLoadingPricelist, setLoadingPricelist] = useState(true);
  const [isLoadingPlaces, setLoadingPlaces] = useState(true);
  const [isLoadinBase64Images, setLoadingBase64Images] = useState(true);
  const [shipBasicData, setShipBasicData] = useState({});
  const [pricelistData, setPricelistData] = useState({});
  const [places, setPlaces] = useState([]);
  const [message, setMessage] = useState("");
  const [hiddenError, setHiddenError] = useState("none");
  
  const history = useHistory();

  /////////////////////error message////////////////////
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
    const [base64Images, setBase64Images] = useState([]);
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

    const fillImageListFromBase64Images = () => {
        let imgArray = [];
        for (let img of base64Images) {
            let imgName = img.split(',')[0];
            let mimeType = imgName.split('.')[1];
            let base64Part = img.split(',')[1];
            if (mimeType === 'jpg') {
                mimeType = 'jpeg';
            }
            let dataUrl = "data:image/" + mimeType + ";base64," + base64Part;
            let newFile = dataURLtoFile(dataUrl, imgName);
            let newImgObj = {
                data_url : dataUrl,
                file: newFile
            };
            console.log(newImgObj);
            imgArray.push(newImgObj);
        }
        setImages(imgArray);
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


  ////////////FISHING EQUIPMENT ///////////////////////////////////////
  const [fishingEquipment, setFishingEquipment] = React.useState([
  ]);

  const handleDeleteFishingEquipmentChip = (chipToDelete) => {
      console.log(chipToDelete);
      setFishingEquipment((chips) => chips.filter((chip) => chip.key !== chipToDelete.key));
  };

  const handleAddFishingEquipmentChip = (data) => {
      let eName = data.equipmentName
      let newKey = 1;
      if (fishingEquipment.length != 0) {
          for (let chip of fishingEquipment) {
              if (chip.equipmentName.toLowerCase() === eName.toLowerCase())
                  return;
          }
          newKey = Math.max.apply(Math, fishingEquipment.map(chip => chip.key)) + 1;
      }
      let newObj = {
          "key": newKey,
          "equipmentName": eName,
      };
      let newChipData = [...fishingEquipment];
      newChipData.push(newObj);

      setFishingEquipment(newChipData);
  }

  const getFishingEquipmentNamesJson = () => {
      if (fishingEquipment.length === 0) {
          return []
      }
      let retVal = [];
      for (let equipment of fishingEquipment) {
          retVal.push({
              equipmentName: equipment.equipmentName,
          });
      }
      return retVal;
  }
  /////////////////////////////////////////////////////////////////////

  ////////////NAVIGATIONAL EQUIPMENT ///////////////////////////////////////
  const [navigationalEquipment, setNavigationalEquipment] = React.useState([
  ]);

  const handleDeleteNavigationalEquipmentChip = (chipToDelete) => {
      console.log(chipToDelete);
      setNavigationalEquipment((chips) => chips.filter((chip) => chip.key !== chipToDelete.key));
  };

  const handleAddNavigationalEquipmentChip = (data) => {
      console.log(navigationalEquipment);
      
      let eName = data.equipmentName
      let newKey = 1;
      if (navigationalEquipment.length != 0) {
          for (let chip of navigationalEquipment) {
              if (chip.name.toLowerCase() === eName.toLowerCase())
                  return;
          }
          newKey = Math.max.apply(Math, navigationalEquipment.map(chip => chip.key)) + 1;
      }
      let newObj = {
          "key": newKey,
          "name": eName,
      };
      let newChipData = [...navigationalEquipment];
      newChipData.push(newObj);

      setNavigationalEquipment(newChipData);
  }

  const getNavigationalEquipmentNamesJson = () => {
      if (navigationalEquipment.length === 0) {
          return []
      }
      let retVal = [];
      for (let equipment of navigationalEquipment) {
          retVal.push({
              name: equipment.name,
              ship_id: shipBasicData.id
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
      console.log("bla");
      const shipRate = shipBasicData.entityCancelationRate;
      let zip = 0;
      if (selectedPlaceZip != null && selectedPlaceZip != undefined && selectedPlaceZip != '') {
          setHiddenError("none");
        zip = selectedPlaceZip;
      } else {
        setHiddenError("block");
        return;
      }
      const costPerPerson = pricelistData.entityPricePerPerson;
      console.log(costPerPerson);
      
      let id = shipBasicData.id;
      const newShip = {
        shipId: 1,
        entityCancelationRate: shipRate,
        name : data.name,
        address: data.address,
        place:{
          stateName:"",
          cityName:"",
          zipCode:selectedPlaceZip
        },
        promoDescription : data.promoDescription,
        entityCancelationRate: data.entityCancelationRate, 
        engineNum : data.engineNum,
        length: data.length,
        shipType: data.shipType,
        maxSpeed: data.maxSpeed,
        enginePower: data.enginePower,
        maxNumOfPersons: data.maxNumOfPersons,
        additionalServices: getAdditionalServicesJson(),
        fishingEquipment: getFishingEquipmentNamesJson(),
        navigationalEquipment: getNavigationalEquipmentNamesJson(),
        rulesOfConduct: getRuleNamesJson(),
        images: getImagesInJsonBase64(),
    } 
    console.log(newShip);
    newShip.entityCancelationRate = shipRate;
    let newPricelistData = pricelistData;
    newPricelistData.entityPricePerPerson =  parseInt(data.costPerNight);
    newPricelistData.additionalServices = getAdditionalServicesJson();
    newPricelistData.bookingEntityId = shipBasicData.id;
    setPricelistData(newPricelistData);
    console.log(newShip);
    addNewShip(getCurrentUser().id, newShip).then(result => { 
      addNewPriceListForEntityId(result.data, newPricelistData).then(res => {
        history.push({
          pathname: "/showShipProfile",
          state: { bookingEntityId: result.data } 
        }) 
    }).catch(resError=>{
        console.log("Greska!!");
        setMessage(resError.response.data);
        handleClick();
        return;
    })    
  }).catch(resError=>{
    console.log("Greska u prvom!!");
    setMessage(resError.response.data);
    handleClick();
    return;
  })   
    
  }
  useEffect(() => {
    
    if (getCurrentUser() == null || getCurrentUser() == undefined || getCurrentUser().userType.name!=="ROLE_SHIP_OWNER") {
      history.push('/forbiddenPage');
    } 
    getAllPlaces().then(results => {
      
      setPlaces(results.data);
        setLoadingPlaces(false);
      
    })
  }, []);

  if (isLoadingPlaces) { return <div className="App"><CircularProgress /></div> }

  return (
    <div style={{ backgroundColor: 'aliceblue', margin: '5%', padding: '1%', borderRadius: '10px', height: '100%' }} >
      {getAllPlacesForTheList()}
      <div style={{ color: 'rgb(5, 30, 52)', fontWeight: 'bold', textAlign: 'center', backgroundColor: 'rgb(244, 177, 77)', marginLeft: '15%', padding: '1%', borderRadius: '10px', width: '15%' }} >
        Add Ship
      </div>
      <br />
      <Divider />
      <br />
      <ImageUploader images={images} maxNumber={maxNumber} onChange={onChange} />
      <br/>
      <h4 style={{ color: 'rgb(5, 30, 52)', fontWeight: 'bold', marginLeft:"15%" }}>Basic Information About Ship</h4>
         
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
                    label="Ship Name"
                    placeholder="Ship Name"
                    name="name"
                    onChange={e => {
                      let data = shipBasicData;
                      data.name = e.target.value;
                      setShipBasicData(data);
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
                        let dataAdd = shipBasicData;
                        dataAdd.address = e.target.value;
                        setShipBasicData(dataAdd);
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
              
                <tr>
                <td>
                    <TextField
                      id="outlined-textarea"
                      label="Ship Type"
                      placeholder="Ship Type"
                      multiline
                      name="shipType"
                      onChange={e => {
                        let dataAdd = shipBasicData;
                        dataAdd.shipType = e.target.value;
                        setShipBasicData(dataAdd);
                      }}
                      size="small"
                      {...register("shipType", { required: true, maxLength: 30 })}
                      style={{ width: '200px' }}
                    />
                  
                  </td>
              </tr>
              <tr><td> {errors.shipType && <p style={{ color: '#ED6663', fontSize:"10px" }}>Please check ship type: maxLength 30</p>}</td></tr>
              

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
                      let data = shipBasicData;
                      data.promoDescription = e.target.value;
                      setShipBasicData(data);
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
                <tr>
                <td>
                    <TextField
                      id="outlined-textarea"
                      label="Engine Name"
                      placeholder="Engine Name"
                      multiline
                      name="engineNum"
                      onChange={e => {
                        let dataAdd = shipBasicData;
                        dataAdd.engineNum = e.target.value;
                        setShipBasicData(dataAdd);
                      }}
                      size="small"
                      {...register("engineNum", { required: true, maxLength: 30 })}
                      style={{ width: '200px' }}
                    />
                  
                  </td>
              </tr>
              <tr><td> {errors.engineNum && <p style={{ color: '#ED6663', fontSize:"10px" }}>Please check engine name: maxLength 30</p>}</td></tr>
              
              </tr>
            </table>
          </div>
          
          <div style={{ display: 'block', color: 'rgb(5, 30, 52)', fontWeight: 'bold', textAlign: 'left', backgroundColor: 'rgb(191, 230, 255)', marginLeft: '4%', marginBottom:"1%",padding: '1%', borderRadius: '10px', width: '40%', height: '100%', minWidth:"300px" }} >
            <table>
            <tr>
                <td>
                  <Typography id="input-slider" gutterBottom sx={{ m: 1 }}> 
                    Ship Length (m)
                  </Typography>
                </td>
                <td>
                  <Input  sx={{ m: 1 }}
                    style={{minWidth:"50px"}}
                    onChange={e => {
                      let data = shipBasicData;
                      let cost = parseFloat(e.target.value);
                      if (cost === NaN) alert("Greska");
                      else {
                        data.length = cost;
                      }
                      setShipBasicData(data);
                    
                    }}
                    type="number"
                    name="length"
                    inputProps={{
                      step: 1,
                      min: 1,
                      max: 500,
                      type: 'number',
                      'aria-labelledby': 'input-slider',
                    }}
                    {...register("length", { required: true, minNumber:1, maxNumber:500 })}
                  />
                </td>
              </tr>
              <tr>{errors.length && <p style={{ color: '#ED6663', fontSize:"10px" }}>Please check ship length between 1-500m</p>}</tr>
              <tr>
                <td>
                  <Typography id="input-slider" gutterBottom sx={{ m: 1 }}> 
                    Max Speed (ft)
                  </Typography>
                </td>
                <td>
                  <Input  sx={{ m: 1 }}
                    style={{minWidth:"50px"}}
                    size="small"
                    onChange={e => {
                      let data = shipBasicData;
                      let cost = parseFloat(e.target.value);
                      if (cost === NaN) alert("Greska");
                      else {
                        data.maxSpeed = cost;
                      }
                      setShipBasicData(data);
                    
                    }}
                    type="number"
                    name="maxSpeed"
                    inputProps={{
                      step: 1,
                      min: 1,
                      max: 300,
                      type: 'number',
                      'aria-labelledby': 'input-slider',
                    }}
                    {...register("maxSpeed", { required: true, minNumber:1, maxNumber:300 })}
                  />
                </td>
              </tr>
              <tr>{errors.maxSpeed && <p style={{ color: '#ED6663', fontSize:"10px" }}>Please check max speed of ship between 1-300ft</p>}</tr>
              
              <tr>
                <td>
                  <Typography id="input-slider" gutterBottom sx={{ m: 1 }}> 
                    Engine Power (kW)
                  </Typography>
                </td>
                <td>
                  <Input  sx={{ m: 1 }}
                  style={{minWidth:"50px"}}
                    size="small"
                    onChange={e => {
                      let data = shipBasicData;
                      data.enginePower = e.target.value;
                      
                      setShipBasicData(data);
                    
                    }}
                    type="number"
                    inputProps={{
                      step: 1,
                      min: 1,
                      max: 10000,
                      type: 'number',
                      'aria-labelledby': 'input-slider',
                    }}
                    name="enginePower"
                    {...register("enginePower", { required: true, maxNumber:10000 })}
                  />
                </td>
                <tr>{errors.enginePower && <p style={{ color: '#ED6663', fontSize:"10px" }}>Please check engine power between 1-10000kW.</p>}</tr>
              
              </tr>
              <tr>
                <td>
                  <Typography id="input-slider" gutterBottom sx={{ m: 1 }}> 
                    Max Num Of Persons
                  </Typography>
                </td>
                <td>
                  <Input  sx={{ m: 1 }}
                    style={{minWidth:"50px"}}
                    size="small"
                    onChange={e => {
                      let data = shipBasicData;
                      data.maxNumOfPersons = e.target.value;
                      
                      setShipBasicData(data);
                    
                    }}
                    type="number"
                    inputProps={{
                      step: 1,
                      min: 1,
                      max: 100,
                      type: 'number',
                      'aria-labelledby': 'input-slider',
                    }}
                    name="maxNumOfPersons"
                    {...register("maxNumOfPersons", { required: true})}
                  />
                </td>
              </tr>
              <tr>{errors.maxNumOfPersons && <p style={{ color: '#ED6663', fontSize:"10px" }}>Please check max num of people on ship between 1-100.</p>}</tr>
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
                  <Typography id="input-slider" gutterBottom sx={{ m: 1 }}> 
                      Cancelation Rate
                  </Typography>
                </td>
                <td>
                    <Input sx={{ m: 1 }}
                      style={{minWidth:"50px"}}
                      size="small"
                      onChange={e => {
                        let data = shipBasicData;
                        let cost = parseFloat(e.target.value);
                        if (cost === NaN) alert("Greska");
                        else {
                          data.entityCancelationRate = cost;
                        }
                        setShipBasicData(data);
                      
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
                      {...register("entityCancelationRate", { required: true, minNumber:0, maxNumber:50 })}
                    />
                </td>
              </tr>
              <tr>{errors.entityCancelationRate && <p style={{ color: '#ED6663', fontSize:"10px" }}>Please check cancelation rate between 1-50%.</p>}</tr>
              
            </table>

          </div>
          
        </Box>
      </Box>
          
      <Box style={{ display: "flex", flexDirection: "row", color: 'rgb(5, 30, 52)', backgroundColor: 'rgb(191, 230, 255)', marginLeft: '15%', marginTop: "3%", marginBottom:"1%",padding: '1%', borderRadius: '10px', width: '70%', height: '100%', minWidth:"500px"}}>
        <AddingAdditionalServiceAdventure style={{marginLeft: '5%'}} data={additionalServices} onDeleteChip={handleDeleteAdditionalServiceChip} onSubmit={handleAddAdditionalServiceChip} float="left" />
        <AddingRulesOfConductAdventure data={rulesOfConduct} onDeleteChip={handleDeleteRuleChip} onSubmit={handleAddRuleChip} ruleChecked={checked} handleRuleCheckedChange={handleRuleCheckedChange} float="left" />
      </Box>
      <Box style={{ display: "flex", flexDirection: "row", color: 'rgb(5, 30, 52)', backgroundColor: 'rgb(191, 230, 255)', marginLeft: '15%', marginTop: "3%", marginBottom:"1%",padding: '1%', borderRadius: '10px', width: '70%', height: '100%', minWidth:"500px"}}>
        <AddingEquipment style={{marginLeft: '5%'}} data={fishingEquipment} onDeleteChip={handleDeleteFishingEquipmentChip} onSubmit={handleAddFishingEquipmentChip} float="left" />
        <AddingNavigationalEquipment data={navigationalEquipment} onDeleteChip={handleDeleteNavigationalEquipmentChip} onSubmit={handleAddNavigationalEquipmentChip} float="left" />
      </Box>

      <Box style={{ display: "flex", flexDirection: "row" }}>
      <Button type="submit" onClick={handleSubmit(onFormSubmit)} variant="contained" style={{ color: 'rgb(5, 30, 52)', fontWeight: 'bold', textAlign: 'center', backgroundColor: 'rgb(244, 177, 77)', marginLeft: '33.5%', marginTop: '1%', padding: '1%', borderRadius: '10px', width: '15%' }}>
          Save
      </Button>
        <Button variant="contained" style={{ color: 'rgb(5, 30, 52)', fontWeight: 'bold', textAlign: 'center', backgroundColor: 'rgb(244, 177, 77)', marginLeft: '2%', marginTop: '1%', padding: '1%', borderRadius: '10px', width: '15%' }}
          onClick={() => {
            reset(
                {
                    name: shipBasicData.name,
                    address: shipBasicData.address,
                    costPerNight: pricelistData.entityPricePerPerson,
                    entityCancelationRate: shipBasicData.entityCancelationRate,
                    promoDescription: shipBasicData.promoDescription,
                }, {
                keepDefaultValues: false,
                keepErrors: true,
            }
            );
            
            fillImageListFromBase64Images();
        }}>
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