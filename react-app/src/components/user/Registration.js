
import React, { useEffect, useState, useRef } from "react";
import { useHistory } from "react-router-dom";
import { getAllPlaces } from "../../service/PlaceService";
import { checkIfEmailAlreadyExist, userLoggedInAsSuperAdmin } from "../../service/UserService";
import Icon from "../../icons/icon.png";
import ShipOwner from "../../icons/shipOwner.png";
import CottageOwner from "../../icons/cottageOwner.png";
import Instructor from "../../icons/instructor.png";
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import { Divider, Grid } from "@mui/material";
import { useForm } from "react-hook-form";
import Box from '@mui/material/Box';
import Autocomplete from '@mui/material/Autocomplete';
import Typography from '@mui/material/Typography';

import 'react-phone-input-2/lib/material.css'
import PhoneInput from 'react-phone-input-2'

import IconButton from '@mui/material/IconButton';
import Input from '@mui/material/Input';
import InputLabel from '@mui/material/InputLabel';
import InputAdornment from '@mui/material/InputAdornment';
import FormControl from '@mui/material/FormControl';
import Visibility from '@mui/icons-material/Visibility';
import VisibilityOff from '@mui/icons-material/VisibilityOff';
import addNewAdmin from "../../service/AdminService";
import Alert from '@mui/material/Alert';
import Snackbar from '@mui/material/Snackbar';
import ChooseAccType from "./ChooseAccType";

export default function Registration() {

    const history = useHistory();
    const { register, handleSubmit, watch, reset, formState: { errors } } = useForm();


    const [openAlert, setOpenAlert] = React.useState(false);
    const [alertMessage, setAlertMessage] = React.useState("");
    const [typeAlert, setTypeAlert] = React.useState("");


    const [hiddenEmailAlreadyExistError, setHiddenEmailAlreadyExistError] = useState("none");

    //----------------password-----------------------/
    const newPassword = useRef({});
    newPassword.current = watch("newPassword", "");

    const [showNewPassword, setShowNewPassword] = useState(false);

    const handleClickShowNewPassword = () => {
        setShowNewPassword(!showNewPassword);
    };

    //------------------------------------------

    const [phoneValue, setphoneValue] = useState();
    const [hiddenErrorPhone, setHiddenErrorPhone] = useState("none");

    //----------------------------------------------------
    const [open, setOpen] = React.useState(true);
    const handleClickOpen = () => {
        setOpen(true);
    };
    const handleClose = () => {
        setOpen(false);
        history.push('/showAdventures');
    };
    //----------------------------------------------------------------
    const [places, setPlaces] = React.useState([]);
    const [selectedPlaceId, setSelectedPlaceId] = useState('');
    const [isLoadingPlaces, setLoadingPlaces] = useState(true);
    const [hiddenErrorPlace, setHiddenErrorPlace] = useState("none");
    let allPlacesList;

    const placeOnChange = (event, newValue) => {
        if (newValue != null && newValue != undefined && newValue != '') {
            setSelectedPlaceId(newValue.id);
        } else {
            setSelectedPlaceId('');
        }
    }

    const getAllPlacesForTheList = () => {
        let newArray = [];
        for (let place of places) {
            newArray.push({ 'label': place.cityName + ',' + place.zipCode + ',' + place.stateName, 'id': place.id });
        }
        allPlacesList = newArray;
    }
    //-------------------------------------------------------------------



    const checkPlaceSelected = () => {
        if (selectedPlaceId !== null && selectedPlaceId !== undefined && selectedPlaceId !== '') {
            setHiddenErrorPlace("none");
            return true;
        } else {
            setHiddenErrorPlace("block");
            return false;
        }
    }

    const checkPhoneSelected = () => {
        if (phoneValue !== null && phoneValue !== undefined && phoneValue !== '') {
            setHiddenErrorPhone("none");
            return true;
        } else {
            setHiddenErrorPhone("block");
            return false;
        }
    }

    const emailAddressAlreadyExist = (email) => {
        checkIfEmailAlreadyExist(email)
            .then(res => {
                setHiddenEmailAlreadyExistError("none");
                return false;
            })
            .catch(err => {
                setHiddenEmailAlreadyExistError("block");
                return true;
            })
    }

    const onFormSubmit = data => {
        if (!checkPlaceSelected() || !checkPhoneSelected() || emailAddressAlreadyExist(data.email))
            return;

        let newAdmin = {
            email: data.email,
            name: data.name,
            surname: data.surname,
            phoneNumber: phoneValue,
            address: data.address,
            placeId: selectedPlaceId,
            password: data.newPassword,
            passwordChanged: false
        }
        addNewAdmin(newAdmin)
            .then(res => {
                setTypeAlert("success");
                setAlertMessage(res.data);
                setOpenAlert(true);
            })
            .catch(err => {
                setTypeAlert("error");
                setAlertMessage(err.response.data);
                setOpenAlert(true);
            });
    }


    useEffect(() => {
       
            getAllPlaces()
                .then(res => {
                    setPlaces(res.data);
                    setLoadingPlaces(false);
                })
    }, []);

    function redirectToChooseAcc(){
        history.push('/chooseAccType');
    }

    function registerForm(){
        history.push('/registrationForm');
    }

    if (isLoadingPlaces) {
        return <div className="App">Loading...</div>
    }
    else {
        { getAllPlacesForTheList() }
        return (
            <Dialog
                open={open}
                onClose={handleClose}
                style={{ margin: '1% auto 1% auto', padding: '1%', width: '50%', borderRadius: '10px' }}
                fullWidth
                maxWidth="sm"
            >
                <Grid style={{ padding:'5%', display: "flex", flexDirection: "row", flexWrap: 'wrap' }} container alignItems="left" display="flex" flexDirection="column" justifyContent="center">
                    <Avatar alt="Remy Sharp" src={Icon} />
                    <Typography button gutterBottom sx={{ marginLeft: '2.5%', mt: '1%'}} style={{fontWeight:"bold", fontSize:'20px'}} color='rgb(5, 30, 52)'>
                    Nature Booking
                    </Typography>
                    <Divider />
                    <br />
                    <Button onClick={registerForm}>I want to register as a client</Button>
                    <Button onClick={redirectToChooseAcc}>I want to register as an owner</Button>
                    
                </Grid>

            </Dialog >
        );
    }
}