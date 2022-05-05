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
import { useForm } from "react-hook-form";
import { useHistory } from "react-router-dom";
import AddingAdditionalServiceAdventure from "../AddingAdditionalService.js";
import AddingRulesOfConductAdventure from "../AddingRulesOfConduct.js";
import Autocomplete from '@mui/material/Autocomplete';
import { dataURLtoFile} from "../../service/PictureService.js";
import { addNewPriceListForEntityId, getPricelistByEntityId } from '../../service/PricelistService.js';
import { getAllPlaces } from '../../service/PlaceService.js';
import { editShipByIdReal, getShipById } from '../../service/ShipService.js';
import AddingRooms from '../AddingRooms.js';
import { getAllPictureBase64ForEntityId } from "../../service/PictureService.js";
import { getCurrentUser } from '../../service/AuthService.js';
import Alert from '@mui/material/Alert';
import Snackbar from '@mui/material/Snackbar';
import Home from "../map/GoogleMap";

const ListItem = styled('li')(({ theme }) => ({
  margin: theme.spacing(0.5),
}));
const Input = styled(MuiInput)`
  width: 42px;
`;
export default function EditShip(props) {

  const { register, handleSubmit, reset, formState: { errors } } = useForm();
  const [isLoadingShip, setLoadingShip] = useState(true);
  const [isLoadingPricelist, setLoadingPricelist] = useState(true);
  const [isLoadingPlaces, setLoadingPlaces] = useState(true);
  const [isLoadinBase64Images, setLoadingBase64Images] = useState(true);
  const [shipBasicData, setShipBasicData] = useState({});
  const [pricelistData, setPricelistData] = useState({});
  const [places, setPlaces] = useState([]);
  const [message, setMessage] = useState("");
  
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
    const [indexFirstPlace, setIndexPlace] = useState(-1);
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
  const getIndexPl = () => {
    
    let i = 0;
    for (let plac of places) {
      if (plac.zipCode==shipBasicData.place.zipCode){
        
        return allPlacesList[i];
      }
      i++;
    };
    return allPlacesList[i];
  }
    ///////////////////////////////////////////////////

    const onFormSubmit = data => {
      const shipRate = shipBasicData.entityCancelationRate;
      let zip = shipBasicData.place.zipCode;
      if (selectedPlaceZip != null && selectedPlaceZip != undefined && selectedPlaceZip != '') {
          zip = selectedPlaceZip;
      }
      let id = shipBasicData.id;
      const editedShip = {
        shipId: id,
        entityCancelationRate: shipRate,
        name : data.name,
        address: data.address,
        place:{
          stateName:"",
          cityName:"",
          zipCode:zip
        },
        promoDescription : data.promoDescription,
        entityCancelationRate: data.entityCancelationRate, 
        additionalServices: getAdditionalServicesJson(),
        rooms: getRoomsJson(),
        rulesOfConduct: getRuleNamesJson(),
        images: getImagesInJsonBase64(),
    } 
    editedShip.entityCancelationRate = shipRate;
    let newPricelistData = pricelistData;
    newPricelistData.entityPricePerPerson =  parseInt(data.costPerNight);
    newPricelistData.additionalServices = getAdditionalServicesJson();
    setPricelistData(newPricelistData);
    editShipByIdReal(shipBasicData.id, editedShip).then(result => {
      
      addNewPriceListForEntityId(shipBasicData.id, pricelistData).then(result => {
        history.push({
          pathname: "/showShipProfile",
          state: { bookingEntityId: shipBasicData.id } 
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
    console.log(props.history.location.state);
    if (props.history.location.state === undefined || props.history.location.state === null){
      return <div>Do not allowed to go to this page. Try again!</div>
    }
    if (getCurrentUser() == null || getCurrentUser() == undefined || getCurrentUser().userType.name!=="ROLE_COTTAGE_OWNER") {
        history.push('/login');
    } 
    getShipById(props.history.location.state.shipId).then(res => {
      setShipBasicData(res.data);
      setRoom(res.data.rooms);
      setRulesOfConduct(res.data.rulesOfConduct);
      setLoadingShip(false);
    })
    getAllPictureBase64ForEntityId(props.history.location.state.shipId).then(res => {
      setBase64Images(res.data);
      setLoadingBase64Images(false);
  });
    getPricelistByEntityId(props.history.location.state.shipId).then(result => {
      setPricelistData(result.data);
      setAdditionalServices(result.data.additionalServices);
      setLoadingPricelist(false);
    })
    
    getAllPlaces().then(results => {
      
      setPlaces(results.data);
        setLoadingPlaces(false);
      
    }) 
  }, []);

  useEffect(() => {
    if (isLoadinBase64Images) {
        return;
    }
    fillImageListFromBase64Images();
}, [isLoadinBase64Images]);

  //rate/////
  const [rate, setRate] = React.useState();
  const handleInputChange = (event) => {
    let newData = shipBasicData;
    newData.entityCancelationRate = event.target.value === '' ? '' : Number(event.target.value);
    setShipBasicData(newData);
    setRate(parseFloat(event.target.value));
    
  };
 
  function refreshPage() {
    window.location.reload(false);
  }

  if (isLoadingShip || isLoadingPricelist || isLoadingPlaces || isLoadinBase64Images) { return <div className="App"><CircularProgress /></div> }

  return (
    <div style={{ backgroundColor: 'aliceblue', margin: '5%', padding: '1%', borderRadius: '10px', height: '100%' }} >
      
    </div>
  );
}