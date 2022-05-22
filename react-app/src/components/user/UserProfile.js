import { useEffect, useState } from "react";
import Avatar from '@mui/material/Avatar';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import { Grid, InputAdornment, List, TextField } from '@mui/material';
import React from 'react';
import CaptainIcon from '../../icons/captainOrange.png';


import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import Paper from '@mui/material/Paper';
import Divider from '@mui/material/Divider';
import FormatListNumberedIcon from '@mui/icons-material/FormatListNumbered';
import { styled, ThemeProvider, createTheme } from '@mui/material/styles';
import { getUserById, userLoggedIn } from '../../service/UserService';
import { getCurrentUser } from '../../service/AuthService';
import { useHistory } from 'react-router-dom';
import { AlternateEmail, DateRangeOutlined, Domain, LocationOn, Person, Phone, Public } from "@mui/icons-material";

export default function UserProfile(props) {


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
    const [isLoading, setLoading] = useState(true);
    const [dateOfBirth, setDateOfBirth] = useState();
    const history = useHistory();
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
                if (res.data.dateOfBirth !== undefined && res.data.dateOfBirth !== null && res.data.dateOfBirth !== '')
                    setDateOfBirth(new Date(res.data.dateOfBirth[0], res.data.dateOfBirth[1] - 1, res.data.dateOfBirth[2]));
                
                setLoading(false);
            })
        }
    }, []);

    if (isLoading) { return <div className="App">Loading...</div> }
    else {
        return (

            <div className="App">
                <div style={{ backgroundColor: 'aliceblue',display:'flex',  margin: '10% auto', marginBottom:'20px', borderRadius: '10px',width:'55%', minHeight: '100px', padding:'2%' }} >
                    <Grid container style={{justifyContent:'center', alignItems:'center'}}>
                        <Grid item xs={12} md={10} lg={8}>
                            {avatar}
                            <div><Typography
                                variant="h5"
                                component="div"
                                style={{ minWidth: '200px', minHeight: '20px' }}
                                sx={{ mr: 2, display: { xs: 'none', color: 'black', fontWeight: "bold", md: 'flex' } }}

                            >
                                {userData.firstName} {userData.lastName}
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
                                style={{ marginTop: '10px' }}
                                sx={{ mr: 2, display: { xs: 'none', color: 'black', md: 'flex' } }}

                            >
                                {userData.userType.name.substring(userData.userType.name.indexOf('_')+1)}
                            </Typography>
                        </Grid>
                    {userData.userType.name !== "ROLE_ADMIN" ? (
                        <Grid item xs="auto">
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
                                        {userData.userType.name === "ROLE_CLIENT" ? (
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
                                        ) : (
                                        <>
                                            {(userData.userType.name === "ROLE_SHIP_OWNER" && userData.captain == true)? (
                                                 <ListItem component="div" disablePadding>
                                                 <ListItemButton sx={{ height: 56 }}>
                                                    <img src={CaptainIcon}></img>
                                                      
                                                     <ListItemText
                                                         primary={" Captain"}
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
                                                <div></div>
                                            )}
                                        </>)}
                                    </FireNav>
                                </Paper>
                            </ThemeProvider>
                        </Box>
                        </Grid>
                    ) : (<div></div>)}
                    </Grid>
                </div>
                <div style={{margin:'0px auto', width:'30%'}}>
                <Grid container spacing={2} style={{backgroundColor: 'aliceblue', margin:'0px auto' , borderRadius: '10px' ,justifyContent:"center" ,alignItems:"center", paddingBottom:'30px'}} >
                    
                    <Grid item xs={12} style={{margin:'0px 5%'}}>
                        <h3 style={{ margin:'0px'}}>Personal informations:</h3>
                    </Grid>
                    <Grid item xs="auto">
                        <TextField
                            id="outlined-read-only-input"
                            label="First Name"
                            defaultValue={userData.firstName}
                            InputProps={{
                                readOnly: true,
                                startAdornment: (
                                    <InputAdornment position="start">
                                        <Person />
                                    </InputAdornment>
                                    )
                            }}
                        />
                    </Grid>
                    <Grid item xs="auto">
                    <TextField
                        id="outlined-read-only-input"
                        label="Last Name"
                        defaultValue={userData.lastName}
                        InputProps={{
                            readOnly: true,
                            startAdornment: (
                                <InputAdornment position="start">
                                    <Person />
                                </InputAdornment>
                                )
                        }}
                    />
                    </Grid>
                    <Grid item xs="auto">
                    <TextField
                        id="outlined-read-only-input"
                        label="Email Address"
                        defaultValue={userData.email}
                        InputProps={{
                            readOnly: true,
                            startAdornment: (
                                <InputAdornment position="start">
                                    <AlternateEmail />
                                </InputAdornment>
                                )
                        }}
                    />
                    </Grid>
                    <Grid item xs="auto">
                    <TextField
                        id="outlined-read-only-input"
                        label="Phone Number"
                        defaultValue={userData.phoneNumber}
                        InputProps={{
                            readOnly: true,
                            startAdornment: (
                                <InputAdornment position="start">
                                    <Phone />
                                </InputAdornment>
                                )
                        }}
                    />
                    </Grid>
                    <Grid item xs="auto">
                    {
                        dateOfBirth === undefined || dateOfBirth === null || dateOfBirth === '' ?
                            (
                                <TextField
                                    id="outlined-read-only-input"
                                    label="Date Of Birth"
                                    defaultValue={""}
                                    InputProps={{
                                        readOnly: true,
                                        startAdornment: (
                                            <InputAdornment position="start">
                                                <DateRangeOutlined />
                                            </InputAdornment>
                                            )
                                    }}
                                />
                            ) :
                            (
                                <TextField
                                    id="outlined-read-only-input"
                                    label="Date Of Birth"

                                    defaultValue={new Intl.DateTimeFormat("en-GB", {
                                        year: "numeric",
                                        month: "long",
                                        day: "2-digit"
                                    }).format(dateOfBirth)}
                                    InputProps={{
                                        readOnly: true,
                                        startAdornment: (
                                            <InputAdornment position="start">
                                                <DateRangeOutlined />
                                            </InputAdornment>
                                            )
                                    }}
                                />
                            )
                    }
                    </Grid>
                    <Grid item xs="auto">
                        <TextField
                            id="outlined-read-only-input"
                            label="Address"
                            defaultValue={userData.address}
                            InputProps={{
                                readOnly: true,
                                startAdornment: (
                                    <InputAdornment position="start">
                                        <LocationOn />
                                    </InputAdornment>
                                    )
                            }}
                        />
                    </Grid>
                    <Grid item xs="auto">
                        <TextField
                            id="outlined-read-only-input"
                            label="Place"
                            defaultValue={userData.place.cityName + ", " + userData.place.zipCode}
                            InputProps={{
                                readOnly: true,
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
                            id="outlined-read-only-input"
                            label="State"
                            defaultValue={userData.place.stateName}
                            InputProps={{
                                readOnly: true,
                                startAdornment: (
                                    <InputAdornment position="start">
                                        <Public />
                                    </InputAdornment>
                                    )
                            }}
                        />
                    </Grid>
                </Grid>

                </div>

            </div>

        );
    }
}