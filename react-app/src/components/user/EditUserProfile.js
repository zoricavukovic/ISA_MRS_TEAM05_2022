import React, {useEffect, useState} from 'react';
import {createTheme, styled, ThemeProvider} from "@mui/material/styles";
import {CircularProgress, FormControl, InputLabel, List, NativeSelect, TextField} from "@mui/material";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import Paper from "@mui/material/Paper";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import Divider from "@mui/material/Divider";
import ListItem from "@mui/material/ListItem";
import FormatListNumberedIcon from "@mui/icons-material/FormatListNumbered";
import {useHistory} from "react-router-dom";
import Avatar from "@mui/material/Avatar";
import { editUserById, getAllUsers, getUserById } from '../../service/UserService';
import { getAllPlaces, getPlaceById } from '../../service/PlaceService';
import { userLoggedIn } from '../../service/UserService';
import { getCurrentUser } from '../../service/AuthService';
import CaptainIcon from '../../icons/captainOrange.png';
import Checkbox from '@mui/material/Checkbox';

function EditUserProfile(props) {


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
    const [changedUserData, setChangedUserData] = useState({});
    const [places, setPlaces] = useState([]);
    const [allCountries, setAllCountries] = useState([])
    const [selectedPlace, setSelectedPlace] = useState({});
    const [isLoading, setLoading] = useState(true);
    const [isLoading2, setLoading2] = useState(true);
    const [country,setCountry] = useState("");
    const [city,setCity] = useState("");
    const history = useHistory();
    const [checked, setChecked] = React.useState();
    const handleChange = (event) => {
        setChecked(event.target.checked);
    };
    
    const avatar = <Avatar
        alt="Zorica Vukovic"
        src="./slika.jpeg"
        style={{ float: "left", marginLeft: '2%', marginRight: '2%' }}
        sx={{ flexGrow: 1, width: 150, height: 150 }}
        maxRows={4}
    />

    useEffect(() => {
        if (userLoggedIn(history)) {
            getUserById(getCurrentUser().id).then(res => {
                setUserData(res.data);
                setChangedUserData(res.data);
                setLoading(false);
                if (res.data.userType.name === "ROLE_SHIP_OWNER"){
                    console.log(res.data.captain);
                    setChecked(res.data.captain);
                }
                setCountry(res.data.place.stateName);
            })
    
            getAllPlaces().then(results =>{
                setPlaces(results.data);
                var countries = []
                for(var place of results.data)
                        if(!countries.some(e=>place.stateName === e))
                            countries.push(place.stateName);
                
                console.log(countries);
                setAllCountries(countries);
                setLoading2(false);
            })
        }
    }, []);


    const saveChanges = (event) => {
        
        console.log("CHanged user data:",changedUserData);
        editUserById(getCurrentUser().id, changedUserData).then(res=>{
            console.log("Uspesno!!");
            console.log(res.data);
            alert("Changes saved");
        }).catch(res=>{
            console.log("Greska!!");
        })
    };

    const reset = (event)=>{
        setState( { key: Date.now() } );
    }

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
        setChangedUserData(prevState => ({
            ...prevState,
            place: selectedPlace
        }));
    },[selectedPlace])

    useEffect(() => {
        console.log(city);
        let countryPlace = places.find(function (element) {
            let someCityInCountry = element.stateName.localeCompare(country) === 0;
            if(city)
                return someCityInCountry && element.cityName.localeCompare(city)===0;
            else
                return someCityInCountry;
        })
        console.log("Pronadjeno mesto:");
        console.log(countryPlace);

        setSelectedPlace(countryPlace);
    }, [country,city])

    useEffect(() => {
        setChangedUserData(prevState => ({
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

    const SubmitButton = <ListItemButton component="a" onClick={saveChanges} style={{backgroundColor:"rgb(244,177,77)",color:"white",textAlign:"center", borderRadius: 7}}>
        <ListItemText
            sx={{ my: 0 }}
            primary="Save Changes"
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


    if (isLoading || isLoading2) { return <div className="App"><CircularProgress /></div> }
    return (

        <div className="App" key={state.key}>

            <div style={{ backgroundColor: 'aliceblue', margin: '8%', padding: '2%', borderRadius: '10px', minHeight: '55px' }} >

                <div>
                    {avatar}
                    <div><Typography
                        variant="h5"
                        component="div"
                        style={{ minWidth: '200px', minHeight: '20px' }}
                        sx={{ mr: 2, display: { xs: 'none', color: 'black', fontWeight: "bold", md: 'flex' } }}

                    >
                        {userData.firstName} {userData.lastName} {places["Iran"]}
                    </Typography>
                    </div>
                    <Typography
                        variant="h6"
                        noWrap
                        component="div"
                        sx={{ mr: 2, display: { xs: 'none', color: 'black', md: 'flex' } }}

                    >
                        {userData.email}
                    </Typography>
                    <Typography
                        variant="h7"
                        noWrap
                        component="div"
                        style={{marginTop:'10px'}}
                        sx={{ mr: 2, display: { xs: 'none', color: 'black', md: 'flex'} }}

                    >
                        {userData.userType.name}
                    </Typography>

                </div>
                {userData.userType.name !== "ROLE_ADMIN" ? (
                    <Box sx={{ display: 'flex', float: 'right' }}>
                        <ThemeProvider
                            theme={createTheme({
                                components: {
                                    MuiListItemButton: {
                                        defaultProps: {
                                            disableTouchRipple: true,
                                        },
                                    },
                                },
                                palette: {
                                    mode: 'dark',
                                    primary: { main: 'rgb(102, 157, 246)' },
                                    background: { paper: 'rgb(5, 30, 52)' },
                                },
                            })}
                        >
                            <Paper elevation={0} sx={{ maxWidth: 290 }}>
                                <FireNav component="nav" disablePadding aria-disabled="true">
                                    <ListItemButton component="a">
                                        <ListItemIcon sx={{ fontSize: 20 }}>🔥 Loyalty Type</ListItemIcon>
                                        <ListItemText
                                            sx={{ my: 0 }}
                                            primary={userData.loyaltyProgram}
                                            primaryTypographyProps={{
                                                fontSize: 20,
                                                fontWeight: 'medium',
                                                letterSpacing: 0,
                                            }}
                                        />
                                    </ListItemButton>
                                    <Divider />
                                    <ListItem component="div" disablePadding>
                                        <ListItemButton sx={{ height: 56 }}>
                                            <ListItemIcon sx={{ fontSize: 20 }}>
                                                <FormatListNumberedIcon color="primary" />
                                                Loyalty Points
                                            </ListItemIcon>
                                            <ListItemText
                                                primary={userData.loyaltyPoints}
                                                primaryTypographyProps={{
                                                    color: 'primary',
                                                    fontSize: 20,
                                                    fontWeight: 'medium',
                                                    variant: 'body2',
                                                }}
                                            />
                                        </ListItemButton>

                                    </ListItem>
                                    <Divider />
                                    {userData.userType.name === "ROLE_CLIENT"?(
                                        <ListItem component="div" disablePadding>
                                            <ListItemButton sx={{ height: 56 }}>
                                                <ListItemIcon sx={{ fontSize: 20 }}>
                                                    <FormatListNumberedIcon color="primary" />
                                                    Penalties
                                                </ListItemIcon>
                                                <ListItemText
                                                    primary={userData.penalties}
                                                    primaryTypographyProps={{
                                                        color: 'primary',
                                                        fontSize: 20,
                                                        fontWeight: 'medium',
                                                        variant: 'body2',
                                                    }}
                                                />
                                            </ListItemButton>

                                        </ListItem>
                                    ):(
                                        <>
                                        {(userData.userType.name === "ROLE_SHIP_OWNER")? (
                                            <ListItem component="div" disablePadding>
                                               <img style={{marginLeft:'8%'}} src={CaptainIcon}></img>
                                                 
                                                <ListItemText
                                                    primary={" Captain"}
                                                    primaryTypographyProps={{
                                                        color: 'primary',
                                                        fontSize: 20,
                                                        fontWeight: 'medium',
                                                        variant: 'body2',
                                                    }}
                                                />
                                                 <Checkbox
                                                    checked={checked}
                                                    onChange={handleChange}
                                                    inputProps={{ 'aria-label': 'controlled' }}
                                                    
                                                />

                                        </ListItem>
                                       ):(
                                           <div></div>
                                       )}
                                       </>
                                    )}
                                </FireNav>
                            </Paper>
                        </ThemeProvider>
                    </Box>
                ) : (<div></div>)}

            </div>



            <Box style={{ backgroundColor: 'aliceblue', marginTop:'5%', marginLeft: '13%', marginRight: '13%', padding: '2%', borderRadius: '10px' }}
                 component="form"
                 sx={{
                     '& .MuiTextField-root': { m: 1, width: '25ch' },
                 }}
                 noValidate
                 autoComplete="off"
                 minRows="3"
            >
                <table align="center">
                    <tr>
                        <td>
                            <TextField
                                id="outlined-read-only-input"
                                label="First Name"
                                defaultValue={userData.firstName}
                                name="firstName"
                                onChange={makeChange}
                                InputProps={{
                                    readOnly: false,
                                }}
                            />
                        </td>
                        <td>
                            <TextField
                                id="outlined-read-only-input"
                                label="Last Name"
                                defaultValue={userData.lastName}
                                name="lastName"
                                onChange={makeChange}
                                InputProps={{
                                    readOnly: false,
                                }}
                            />

                        </td>
                    </tr>
                    <tr>
                        <td>
                            <TextField
                                id="outlined-read-only-input"
                                label="Phone Number"
                                defaultValue={userData.phoneNumber}
                                name="phoneNumber"
                                onChange={makeChange}
                                InputProps={{
                                    readOnly: false,
                                }}
                            />
                        </td>

                        <td>
                            <TextField
                                id="outlined-read-only-input"
                                label="Date Of Birth"
                                defaultValue={userData.dateOfBirth}
                                name="dateOfBirth"
                                onChange={makeChange}
                                InputProps={{
                                    readOnly: false,
                                }}
                            />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <FormControl style={{margin:'10px 10px'}}>
                                <InputLabel variant="standard" htmlFor="uncontrolled-native">
                                    Country
                                </InputLabel>
                                <NativeSelect
                                    defaultValue={userData.place.stateName}
                                    onChange = {countryChanged}
                                    inputProps={{
                                        name: 'Country',
                                        id: 'uncontrolled-native',
                                    }}
                                >
                                    {allCountries.map((state)=>(
                                        <option value={state}>{state}</option>
                                    ))}
                                </NativeSelect>
                            </FormControl>
                        </td>
                        <td>
                            <FormControl>
                                <InputLabel variant="standard" htmlFor="uncontrolled-native">
                                    City
                                </InputLabel>
                                <NativeSelect
                                    defaultValue={userData.place.cityName}
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
                            </FormControl>
                        </td>

                    </tr>
                    <tr>
                        <td colSpan="2">
                            <TextField
                                required
                                id="outlined-required"
                                label="Address"
                                defaultValue={userData.address}
                                name="address"
                                onChange={makeChange}
                                style={{width:'100%'}}
                            />
                        </td>
                    </tr>
                    <tr>
                        <td>

                            <Paper elevation={0} sx={{ maxWidth: 290 }}>
                                <FireNav component="nav" disablePadding>
                                    {SubmitButton}
                                    <Divider />


                                    <Divider />
                                </FireNav>
                            </Paper>
                        </td>

                        <td>
                            <Paper elevation={0} sx={{marginLeft:10, maxWidth: 290 }}>
                                <FireNav component="nav" disablePadding>
                                    <ListItemButton component="a" onClick={reset} style={{backgroundColor:"rgb(5, 30, 52)",color:"white",textAlign:"center", borderRadius: 7}}>
                                        <ListItemText
                                            sx={{ my: 0 }}
                                            primary="Reset" //STAVITI LOYALTY TIP
                                            primaryTypographyProps={{
                                                fontSize: 20,
                                                fontWeight: 'medium',
                                                letterSpacing: 0,
                                            }}
                                        />
                                    </ListItemButton>
                                    <Divider />


                                    <Divider />
                                </FireNav>
                            </Paper>

                        </td>
                    </tr>
                </table>
            </Box>



        </div>

    );
}

export default EditUserProfile;
