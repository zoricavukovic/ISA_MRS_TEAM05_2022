import React, {useEffect, useState, useRef} from 'react';
import {createTheme, styled, ThemeProvider} from "@mui/material/styles";
import {Alert, Autocomplete, CircularProgress, FormControl, Grid, InputAdornment, InputLabel, List, NativeSelect, Snackbar, TextField} from "@mui/material";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import Paper from "@mui/material/Paper";
import 'react-date-range/dist/styles.css'; // main style file
import 'react-date-range/dist/theme/default.css'; // theme css file
import { format } from "date-fns";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import Divider from "@mui/material/Divider";
import ListItem from "@mui/material/ListItem";
import FormatListNumberedIcon from "@mui/icons-material/FormatListNumbered";
import {useHistory} from "react-router-dom";
import Avatar from "@mui/material/Avatar";
import { editUserById, getUserById } from '../../service/UserService';
import { getAllPlaces } from '../../service/PlaceService';
import { userLoggedIn } from '../../service/UserService';
import { getCurrentUser } from '../../service/AuthService';
import CaptainIcon from '../../icons/captainOrange.png';
import Checkbox from '@mui/material/Checkbox';
import { DateRangeOutlined, Domain, Person, Phone, Place } from '@mui/icons-material';
import { Calendar } from 'react-date-range';
import Dialog from '@mui/material/Dialog';
import IconButton from '@mui/material/IconButton';
import Input from '@mui/material/Input';
import Visibility from '@mui/icons-material/Visibility';
import VisibilityOff from '@mui/icons-material/VisibilityOff';
import { useForm } from "react-hook-form";
import EmailIcon from '@mui/icons-material/Email';
import KeyIcon from '@mui/icons-material/Key';

function RegistrationForm(props) {


    const FireNav = styled(List)({
        '& .MuiListItemButton-root': {
            paddingLeft: 24,
            paddingRight: 24,
        },
        '& .MuiListItemIcon-root': {
            minWidth: 0,
            marginRight: 16,
        },
        '& .MuiSvgIcon-root': {
            fontSize: 20,
        },
    });


    const [userData, setUserData] = useState({});
    const [changedUserData, setChangedUserData] = useState({
        "firstName":"",
        "lastName": "",
        "dateOfBirth":null,
        "place":{
            "id":0
        },
        "password":"",
        "address":"",
        "email":""
    });
    const [places, setPlaces] = useState([]);
    const [selectedPlace, setSelectedPlace] = useState({});
    const [isLoading, setLoading] = useState(true);
    const [isLoading2, setLoading2] = useState(true);
    const [selectedPlaceId, setSelectedPlaceId] = useState(null);
    const [allPlacesList,setAllPlacesList] = useState([]);
    const [openDate,setOpenDate] = useState(false);
    const [dateOfBirth,setDateOfBirth] = useState(new Date());
    const history = useHistory();
    const [checked, setChecked] = React.useState();

    const handleChange = (event) => {
        setChecked(event.target.checked);
    };
    
    useEffect(() => {
       
        getAllPlaces().then(results =>{
            setPlaces(results.data);
            setSelectedPlaceId(results.data[0].id);
            setLoading2(false);
        })
        
    }, []);

            //setSelectedPlaceId(userData.place.id);
            //setSelectedPlace({ 'label': userData.place.cityName + ',' + userData.place.zipCode + ',' + userData.place.stateName, 'id': userData.place.id });
        

    const [open, setOpen] = React.useState(false);

    const showNotification = () => {
        setOpen(true);
    };

    const [openDialog, setOpenDialog] = React.useState(true);
 
    const handleCloseDialog = () => {
        setOpenDialog(false);
    };

    const handleClose = (event, reason) => {
        if (reason === 'clickaway') {
        return;
        }

        setOpen(false);
    };

    const saveChanges = (event) => {
        console.log(changedData);
        event.preventDefault();
        let changedData = changedUserData;
        changedData.dateOfBirth = dateOfBirth;
        changedData.place.id = selectedPlaceId;
        setChangedUserData(changedData);
        console.log("CHanged user data:",changedData);
        editUserById(getCurrentUser().id, changedData).then(res=>{
            console.log("Uspesno!!");
            console.log(res.data);
            showNotification();
        }).catch(res=>{
            console.log("Greska!!");
        })
    };

    const makeChange = (event)=>{
        var value = event.target.value;
        if(event.target.name === "dateOfBirth"){
            let year = parseInt(value.split(',')[0]);
            let month = parseInt(value.split(',')[1]);
            let day = parseInt(value.split(',')[2]);
            value = [year,month,day];
        }
        setChangedUserData(prevState => ({
            ...prevState,
            [event.target.name]: value
        }));
    }

    useEffect(() => {
        getAllPlacesForTheList();
        
    },[places])

    const handleDatePick = (date) =>{
        console.log("Usao u picker");
        date.setHours(12);
        setDateOfBirth(date); 
        setOpenDate(false);
      }

    const placeOnChange = (event, newValue) => {
        event.preventDefault();
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
        
        setAllPlacesList(newArray);
      }



    const { register, handleSubmit, watch, reset, formState: { errors } } = useForm();

    //----------------password-----------------------/
    const newPassword = useRef({});
    newPassword.current = watch("newPassword", "");

    const [showNewPassword, setShowNewPassword] = useState(false);

    const handleClickShowNewPassword = () => {
        setShowNewPassword(!showNewPassword);
    };

    //------------------------------------------

    const SubmitButton = <ListItemButton button type="submit" component="button"  style={{backgroundColor:"rgb(244,177,77)",color:"white",textAlign:"center", borderRadius: 7}}>
        <ListItemText
            sx={{ my: 0 }}
            primary="Register"
            primaryTypographyProps={{
                fontSize: 20,
                fontWeight: 'medium',
                letterSpacing: 0,
            }}
        />
    </ListItemButton>

    const [state,setState] = useState({
        key : Date.now()
    });


    if (isLoading2) { return <div className="App"><CircularProgress /></div> }
    return (
        <Dialog
            open={openDialog}
            onClose={handleCloseDialog}
            style={{ margin: '1% auto 1% auto', padding: '1%', width: '50%', borderRadius: '10px' }}
            fullWidth
            maxWidth="sm"
        >
            
            <div className="App" key={state.key} style={{backgroundColor: 'aliceblue'}}>
            <Grid style={{ padding:'5%', display: "flex", flexDirection: "row", flexWrap: 'wrap' }} container alignItems="left" display="flex" flexDirection="column" justifyContent="center">
              
                <Typography sx={{ marginLeft: '2.5%', mt: '1%'}} style={{fontWeight:"bold", fontSize:'20px'}} color='rgb(5, 30, 52)'>
                Registration Form
                </Typography>
                <Divider />
               
            </Grid>
                <div style={{margin:'0px auto', width:'80%'}}>
                <form onSubmit={saveChanges}>
                    <Grid container spacing={2} style={{backgroundColor: 'aliceblue', margin:'0px auto' , borderRadius: '10px' ,justifyContent:"center" ,alignItems:"center", paddingBottom:'30px'}} >
                    
                            <Grid item xs="auto">
                                <TextField
                                    id="outlined-read-only-input"
                                    label="First Name"
                                    placeholder="First Name"
                                    defaultValue={userData.firstName}
                                    name="firstName"
                                    onChange={makeChange}
                                    InputProps={{
                                        readOnly: false,
                                        startAdornment: (
                                            <InputAdornment position="start">
                                                <Person />
                                            </InputAdornment>
                                            )
                                    }}
                                    required
                                />
                            </Grid>
                            <Grid item xs="auto">
                                <TextField
                                    id="outlined-read-only-input"
                                    label="Last Name"
                                    defaultValue={userData.lastName}
                                    placeholder="Last Name"
                                    name="lastName"
                                    onChange={makeChange}
                                    InputProps={{
                                        readOnly: false,
                                        startAdornment: (
                                            <InputAdornment position="start">
                                                <Person />
                                            </InputAdornment>
                                            )
                                    }}
                                    required
                                />
                            </Grid>
                            <Grid item xs="auto">
                                <TextField
                                    id="outlined-read-only-input"
                                    label="Email"
                                    name="email"
                                    placeholder="Email"
                                    onChange={makeChange}
                                    InputProps={{
                                        readOnly: false,
                                        startAdornment: (
                                            <InputAdornment position="start">
                                                <EmailIcon />
                                            </InputAdornment>
                                            )
                                    }}
                                    required
                                />
                            </Grid>
                            <Grid item xs="auto">
                                <TextField
                                    id="outlined-read-only-input"
                                    label="Phone Number"
                                    defaultValue={userData.phoneNumber}
                                    name="phoneNumber"
                                    placeholder="Phone Number"
                                    onChange={makeChange}
                                    InputProps={{
                                        readOnly: false,
                                        startAdornment: (
                                            <InputAdornment position="start">
                                                <Phone />
                                            </InputAdornment>
                                            )
                                    }}
                                    required
                                />
                            </Grid>
                            <Grid item xs="auto">
                                <><TextField style={{/*margin:'10px 10px'*/ }} onClick={()=>setOpenDate(!openDate)} label='Date of birth' placeholder={`${format(dateOfBirth, "dd.MM.yyyy.")}`} 
                                    value={`${format(dateOfBirth, "dd.MM.yyyy.")}`}
                                    placeholder="Date Of Birth"
                                    InputProps={{
                                        startAdornment: (
                                            <InputAdornment position="start">
                                            <DateRangeOutlined />
                                            </InputAdornment>
                                        )
                                        }}
                                        />
                                        {openDate && <div style={{
                                                position:"absolute",
                                                zIndex:99999,
                                                backgroundColor:"white",
                                                border:"1px solid rgb(5, 30, 52)"
                                            }}
                                            
                                            >
                                            <Calendar
                                                editableDateInputs={true}
                                                date={dateOfBirth}
                                                onChange={handleDatePick}
                                            />
                                            </div>
                                        }
                                </>
                            </Grid>
                            <Grid item xs="auto">
                                <Autocomplete
                                    disablePortal
                                    id="place"
                                    options={allPlacesList}
                                    style={{width:'100%'}}
                                    onChange={placeOnChange}
                                    renderInput={(params) => <TextField {...params} label="Place" 
                                                                placeholder="Place"
                                                                InputProps={{
                                                                    ...params.InputProps,
                                                                    startAdornment: (
                                                                    <InputAdornment position="start">
                                                                        <Place />
                                                                    </InputAdornment>
                                                                    )
                                                                }}
                                                            />}
                                    required
                                    isOptionEqualToValue={(option, value) => option.label === value.label}
                                />
                            </Grid>
                            <Grid item xs="auto">
                                <TextField
                                    required
                                    id="outlined-required"
                                    label="Address"
                                    name="address"
                                    onChange={makeChange}
                                    style={{width:'100%'}}
                                    InputProps={{
                                        readOnly: false,
                                        startAdornment: (
                                            <InputAdornment position="start">
                                                <Domain />
                                            </InputAdornment>
                                            )
                                    }}
                                />
                            </Grid>
                        
                            <Grid item xs="auto">
                                <TextField
                                    required
                                    id="outlined-required"
                                    label="Type password"
                                    placeholder="Type password"
                                    type={showNewPassword ? 'text' : 'password'}
                                    name="newPassword"
                                    onChange={makeChange}
                                    style={{width:'100%'}}
                                    InputProps={{
                                        readOnly: false,
                                        startAdornment: (
                                            <InputAdornment position="start">
                                                <KeyIcon />
                                            </InputAdornment>
                                            ),
                                            endAdornment:(
                                                <InputAdornment position="end">
                                                    <IconButton
                                                        aria-label="toggle password visibility"
                                                        onClick={handleClickShowNewPassword}
                                                    >
                                                        {showNewPassword ? <VisibilityOff /> : <Visibility />}
                                                    </IconButton>
                                                </InputAdornment>
                                            )
                                    }}
                                    endAdornment={
                                        <InputAdornment position="end">
                                            <IconButton
                                                aria-label="toggle password visibility"
                                                onClick={handleClickShowNewPassword}
                                            >
                                                {showNewPassword ? <VisibilityOff /> : <Visibility />}
                                            </IconButton>
                                        </InputAdornment>
                                    }
                                    {...register("newPassword", {
                                        required: "You must specify a password",
                                        minLength: {
                                            value: 8,
                                            message: "Password must have at least 8 characters"
                                        }
                                    })}
                                />
                            </Grid>
                            {errors.newPassword && <p style={{ color: '#ED6663' }}>{errors.newPassword.message}</p>}
                            <Grid item xs="auto">
                                <TextField
                                    required
                                    id="outlined-required"
                                    label="Retype password"
                                    placeholder="Retype password"
                                    name="retypedPassword"
                                    onChange={makeChange}
                                    style={{width:'100%'}}
                                    InputProps={{
                                        readOnly: false,
                                        startAdornment: (
                                            <InputAdornment position="start">
                                                <KeyIcon />
                                            </InputAdornment>
                                            )
                                    }}
                                   
                                    {...register("retypedPassword", {
                                        validate: value =>
                                            value === newPassword.current || "The passwords do not match."
                                    })}
                                />
                                </Grid>
                            {errors.retypedPassword && <p style={{ color: '#ED6663' }}>{errors.retypedPassword.message}</p>}
                   
                            <Grid item xs="auto">
                                <Paper elevation={0} sx={{ maxWidth: 290 }}>
                                    <FireNav component="nav" disablePadding>
                                        {SubmitButton}
                                        <Divider />
                                        <Divider />
                                    </FireNav>
                                </Paper>
                            </Grid>
                        
                        
                        </Grid>

                    </form>
                </div>
                <Snackbar open={open} autoHideDuration={6000} onClose={handleClose}>
                    <Alert onClose={handleClose} severity="success" sx={{ width: '100%' }}>
                        Profile edited successfuly!
                    </Alert>
                </Snackbar>
            </div>
        </Dialog>

    );
}

export default RegistrationForm;
