import React, { useEffect, useState } from "react";
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Grid from '@mui/material/Grid';
import Autocomplete from '@mui/material/Autocomplete';
import AddingAdditionalServiceAdventure from "../AddingAdditionalService.js";
import AddingEquipmentAdventure from "../AddingEquipment.js";
import AddingRulesOfConductAdventure from "../AddingRulesOfConduct.js";
import { useForm } from "react-hook-form";
import { Divider } from "@mui/material";
import ImageUploader from "../image_uploader/ImageUploader.js";
import {useHistory} from "react-router-dom";
import { getAllPlaces } from "../../service/PlaceService.js";
import { editAdventureById, getAdventureById } from "../../service/AdventureService.js";
import { getPricelistByEntityId } from "../../service/Pricelists.js";
import { getAllPictureBase64ForEntityId } from "../../service/PictureService.js";
import { dataURLtoFile} from "../../service/PictureService.js";
import { getCurrentUser } from '../../service/AuthService.js';

export default function EditAdventure(props) {

    const { register, handleSubmit, reset, formState: { errors } } = useForm();
    const [currentAdventure, setCurrentAdventure] = useState({});
    let adventureId =  null;
    let ownerId = null;
    const [pricelist, setPricelist] = useState({});
    const [base64Images, setBase64Images] = useState([]);

    const [isLoadingAdventure, setLoadingAdventure] = useState(true);
    const [isLoadingPricelist, setLoadingPriceList] = useState(true);
    const [isLoadingPlaces, setLoadingPlaces] = useState(true);
    const [isLoadinBase64Images, setLoadingBase64Images] = useState(true);
    const history = useHistory();


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
        setAdditionalServices((chips) => chips.filter((chip) => chip.key !== chipToDelete.key));
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
            "amount": newAmount
        };
        let newChipData = [...additionalServices];
        newChipData.push(newObj);
        setAdditionalServices(newChipData);
    }

    const setInitialAdditionalServices = (data) => {
        let serviceData = []
        for (let s of data) {
            let newObj = {
                "key": s.id,
                "serviceName": s.serviceName,
                "amount": s.price
            };
            serviceData.push(newObj);
        }
        setAdditionalServices(serviceData);
    }

    const getAdditionalServicesJson = () => {
        if (additionalServices.length === 0) {
            return []
        }
        let retVal = [];
        for (let service of additionalServices) {
            retVal.push({
                serviceName : service.serviceName,
                price : service.amount
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

    const setInitialFishingEquipment = (data) => {
        let equipmentData = []
        for (let eq of data) {
            let newObj = {
                "key": eq.id,
                "equipmentName": eq.equipmentName,
            };
            equipmentData.push(newObj);
        }
        setFishingEquipment(equipmentData);
    }

    const getFishingEquipmentNamesJson = () => {
        if (fishingEquipment.length === 0) {
            return []
        }
        let retVal = [];
        for (let equipment of fishingEquipment) {
            retVal.push({
                equipmentName : equipment.equipmentName,
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
        setRulesOfConduct((chips) => chips.filter((chip) => chip.key !== chipToDelete.key));
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

    const setInitialRulesOfConduct = (data) => {
        let ruleData = []
        for (let rule of data) {
            let newObj = {
                "key": rule.id,
                "ruleName": rule.ruleName,
                "allowed": rule.allowed
            };
            ruleData.push(newObj);
        }
        setRulesOfConduct(ruleData);
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



    /////////////////////// PlACE /////////////////////////////////////////////////
    const [places, setPlaces] = React.useState([]);
    const [selectedPlaceId, setSelectedPlaceId] = useState('');
    let allPlacesList;

    const placeOnChange = (event, newValue) => {
        if (newValue != null && newValue != undefined && newValue != '') {
            setSelectedPlaceId(newValue.id);
        } else {
            setSelectedPlaceId('');
        }
    }
    /////////////////////////////////////////////////////////////////////////////


    const onFormSubmit = data => {
        if (selectedPlaceId === '') {
            alert("Please select place");
            return;
        }
        const editedAdventure = {
            instructorId: ownerId, // ovde promeniti posle u zavisnosti od ulogovanog korisnika
            name : data.name,
            address: data.address,
            placeId: selectedPlaceId,
            costPerPerson: data.costPerPerson,
            maxNumOfPersons: data.maxNumOfPersons,
            promoDescription : data.promoDescription,
            shortBio: data.shortBio,
            entityCancelationRate: data.entityCancelationRate, 
            additionalServices: getAdditionalServicesJson(),
            fishingEquipment: getFishingEquipmentNamesJson(),
            rulesOfConduct: getRuleNamesJson(),
            images: getImagesInJsonBase64(),
        }
        editAdventureById(adventureId, editedAdventure)
            .then(res => {
                console.log(res);
                alert("Adventure  successfully updated. Redirecting to updated adventure...");
                history.push({
                    pathname: "/showAdventureProfile",
                    state: { bookingEntityId: parseInt(adventureId) }
                  });
            })
            .catch(res => {
                console.log(res);
                alert("Error happened on server. Update not succesfull.");
            });
    }


    useEffect(() => {
        if (props.history.location.state === undefined || props.history.location.state === null){
            return <div>Do not allowed to go to this page. Try again!</div>
        }
        else{
            adventureId = props.history.location.state.bookingEntityId;
        }
        if (getCurrentUser() == null || getCurrentUser() == undefined || getCurrentUser().userType.name!=="ROLE_INSTRUCTOR") {
            history.push('/login');
        } 
        else{
            ownerId = getCurrentUser().id;
        }
        getAdventureById(adventureId).then(res => {
            setCurrentAdventure(res.data);
            setSelectedPlaceId(res.data.place.id);
            setInitialRulesOfConduct(res.data.rulesOfConduct);
            setInitialFishingEquipment(res.data.fishingEquipment);
            setLoadingAdventure(false);
        })
        getAllPictureBase64ForEntityId(adventureId).then(res => {
            setBase64Images(res.data);
            setLoadingBase64Images(false);
        });

        getAllPlaces()
            .then(res => {
                setPlaces(res.data);
                setLoadingPlaces(false);
        })
        getPricelistByEntityId(adventureId).then(res => {
            setPricelist(res.data);
            setInitialAdditionalServices(res.data.additionalServices);
            setLoadingPriceList(false);
        })
    }, [])

    useEffect(() => {
        if (isLoadinBase64Images) {
            return;
        }
        fillImageListFromBase64Images();
    }, [isLoadinBase64Images]);

    const getAllPlacesForTheList = () => {
        let newArray = []
        for (let place of places) {
            newArray.push({ 'label': place.cityName + ',' + place.zipCode + ',' + place.stateName, 'id': place.id });
        }
        allPlacesList = newArray;
    }

    if (isLoadingAdventure || isLoadingPlaces || isLoadingPricelist || isLoadinBase64Images) {
        return <div className="App">Loading...</div>
    }
    return (
        <div style={{ backgroundColor: 'aliceblue', margin: '1% 9% 1% 9%', padding: '1%', borderRadius: '10px', height: '100%' }} >

            {getAllPlacesForTheList()}

            <div style={{ color: 'rgb(5, 30, 52)', fontWeight: 'bold', textAlign: 'center', backgroundColor: 'rgb(244, 177, 77)', marginLeft: '42%', padding: '1%', borderRadius: '10px', width: '15%' }} >
                Edit adventure
            </div>
            <br />
            <Divider />
            <br />
            <ImageUploader images={images} maxNumber={maxNumber} onChange={onChange} />
            <br />


            <Box
                component="form"
                noValidate
                onSubmit={handleSubmit(onFormSubmit)}
            >
                <h4 style={{ color: 'rgb(5, 30, 52)', textAlign: 'center', fontWeight: 'bold' }}>Basic Information About Cottage</h4>

                <Grid
                    direction="column"
                    alignItems="center"
                    justifyContent="center"
                    container
                    spacing={2}
                >
                    <Grid item xs={12} sm={12}>
                        <TextField
                            name="name"
                            defaultValue={currentAdventure.name}
                            id="name"
                            label="Name"
                            placeholder="Name"
                            multiline
                            size="small"
                            style={{ width: '300px' }}
                            {...register("name", { required: true, maxLength: 50 })}
                        />
                    </Grid>
                    {errors.name && <p style={{ color: '#ED6663' }}>Please check the adventure name</p>}
                    <Grid item xs={12} sm={12}>
                        <TextField
                            name="address"
                            id="address"
                            defaultValue={currentAdventure.address}
                            label="Address"
                            placeholder="Address"
                            multiline
                            size="small"
                            style={{ width: '300px' }}
                            {...register("address", { required: true, maxLength: 50 })}
                        />
                    </Grid>
                    {errors.name && <p style={{ color: '#ED6663' }}>Please check the address name</p>}
                    <Grid item xs={12} sm={12}>
                        <Autocomplete
                            disablePortal
                            id="place"
                            defaultValue={currentAdventure.place.cityName + ',' + currentAdventure.place.zipCode + ',' + currentAdventure.place.stateName}
                            options={allPlacesList}
                            sx={{ width: '300px' }}
                            onChange={placeOnChange}
                            renderInput={(params) => <TextField {...params} label="Place" />}
                        />
                    </Grid>
                    <Grid item xs={12} sm={12}>
                        <TextField
                            name="costPerPerson"
                            id="costPerPerson"
                            type="number"
                            defaultValue={pricelist.entityPricePerPerson}
                            label="Cost Per Person €"
                            placeholder="Cost Per Person €"
                            style={{ width: '300px' }}
                            {...register("costPerPerson", { required: true, min: 1, max: 100000 })}
                        />
                    </Grid>
                    {errors.costPerNight && <p style={{ color: '#ED6663' }}>Please check cost per person</p>}
                    <Grid item xs={12} sm={12}>
                        <TextField
                            type="number"
                            name="maxNumOfPersons"
                            defaultValue={currentAdventure.maxNumOfPersons}
                            label="Max Num Of Persons"
                            placeholder="Max No. Of Persons"
                            style={{ width: '300px' }}
                            {...register(
                                "maxNumOfPersons",
                                { required: true, min: 1, max: 10000 },
                            )}
                        />
                    </Grid>
                    {errors.maxNumOfPersons && <p style={{ color: '#ED6663' }}>Enter num between 1 and 1000</p>}
                    <Grid item xs={12} sm={12}>
                        <TextField
                            name="promoDescription"
                            size="small"
                            defaultValue={currentAdventure.promoDescription}
                            id="promoDescription"
                            label="Promo Description"
                            multiline
                            rows={3}
                            {...register("promoDescription", { maxLength: 250 })}
                            placeholder="Promo Description"
                            style={{ width: '300px' }}
                        />
                    </Grid>
                    <Grid item xs={12} sm={12}>
                        <TextField
                            name="shortBio"
                            size="small"
                            defaultValue={currentAdventure.shortBio}
                            id="shortBio"
                            label="Short Bio"
                            multiline
                            rows={3}
                            {...register("shortBio", { maxLength: 250 })}
                            placeholder="Short Bio"
                            style={{ width: '300px' }}
                        />
                    </Grid>
                    <Grid item xs={12} sm={12}>
                        <TextField
                            type="number"
                            name="entityCancelationRate"
                            defaultValue={currentAdventure.entityCancelationRate}
                            label="Entity Cancelation Rate %"
                            placeholder="Entity Cancelation Rate %"
                            style={{ width: '300px' }}
                            {...register("entityCancelationRate", { required: true, min: 0, max: 100 })}
                        />
                    </Grid>
                    {errors.entityCancelationRate && <p style={{ color: '#ED6663' }}>Enter number between 0 and 100</p>}

                </Grid>
                <br />


                <Box style={{ display: "flex", flexDirection: "row" }}>
                    <AddingAdditionalServiceAdventure data={additionalServices} onDeleteChip={handleDeleteAdditionalServiceChip} onSubmit={handleAddAdditionalServiceChip} float="left" />
                    <AddingEquipmentAdventure data={fishingEquipment} onDeleteChip={handleDeleteFishingEquipmentChip} onSubmit={handleAddFishingEquipmentChip} float="left" />
                    <AddingRulesOfConductAdventure data={rulesOfConduct} onDeleteChip={handleDeleteRuleChip} onSubmit={handleAddRuleChip} ruleChecked={checked} handleRuleCheckedChange={handleRuleCheckedChange} float="left" />
                </Box>

                <Box style={{ display: "flex", flexDirection: "row" }}>
                    <Button type="submit" onSubmit={handleSubmit(onFormSubmit)} variant="contained" style={{ color: 'rgb(5, 30, 52)', fontWeight: 'bold', textAlign: 'center', backgroundColor: 'rgb(244, 177, 77)', marginLeft: '33.5%', marginTop: '1%', padding: '1%', borderRadius: '10px', width: '15%' }}>
                        Save
                    </Button>
                    <Button
                        variant="contained"
                        style={{ color: 'rgb(5, 30, 52)', fontWeight: 'bold', textAlign: 'center', backgroundColor: 'rgb(244, 177, 77)', marginLeft: '2%', marginTop: '1%', padding: '1%', borderRadius: '10px', width: '15%' }}
                        onClick={() => {
                            reset(
                                {
                                    name: currentAdventure.name,
                                    address: currentAdventure.address,
                                    costPerNight: pricelist.entityPricePerPerson,
                                    maxNumOfPersons: currentAdventure.maxNumOfPersons,
                                    entityCancelationRate: currentAdventure.entityCancelationRate,
                                    shortBio: currentAdventure.shortBio,
                                    promoDescription: currentAdventure.promoDescription,
                                }, {
                                keepDefaultValues: false,
                                keepErrors: true,
                            }
                            );
                            setInitialRulesOfConduct(currentAdventure.rulesOfConduct);
                            setInitialFishingEquipment(currentAdventure.fishingEquipment);
                            setInitialAdditionalServices(pricelist.additionalServices);
                            fillImageListFromBase64Images();
                        }}
                    >
                        Reset
                    </Button>
                </Box>


            </Box >
        </div >
    );
}