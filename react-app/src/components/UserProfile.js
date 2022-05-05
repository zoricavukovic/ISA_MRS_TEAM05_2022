import '../App.css';
import { useEffect, useState } from "react";
import { useHistory } from "react-router-dom";
import Avatar from '@mui/material/Avatar';
import "../style/showCottageOwnerProfile.css";
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import { List, TextField } from '@mui/material';
import React from 'react';



import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import Paper from '@mui/material/Paper';
import Divider from '@mui/material/Divider';
import FormatListNumberedIcon from '@mui/icons-material/FormatListNumbered';
import { styled, ThemeProvider, createTheme } from '@mui/material/styles';
import axios from "axios";
import { getUserById } from '../service/UserService';


function UserProfile(props) {
    

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
    const history = useHistory();
    const userId = props.history.location.state.userId;
   // const urlPath = "http://localhost:8092/bookingApp/users/" + userId;
    const avatar = <Avatar
        alt="Zorica Vukovic"
        src="./slika.jpeg"
        style={{ float: "left", marginLeft: '2%', marginRight: '2%' }}
        sx={{ flexGrow: 1, width: 150, height: 150 }}
        maxRows={4}
    />

    useEffect(() => {
        getUserById(props.history.location.state.userId).then(res => {
            setUserData(res.data);
            setLoading(false);
        })
    }, []);

    if (isLoading) { return <div className="App">Loading...</div> }
    return (
        
        <div className="App">
            <div style={{ backgroundColor: 'aliceblue', margin: '8%', padding: '2%', borderRadius: '10px', minHeight: '55px' }} >

                <div>
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
                                    ):(<div></div>)}
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
                <div>

                    <TextField
                        id="outlined-read-only-input"
                        label="First Name"
                        defaultValue={userData.firstName}
                        InputProps={{
                            readOnly: true,
                        }}
                    />
                    <TextField
                        id="outlined-read-only-input"
                        label="Last Name"
                        defaultValue={userData.lastName}
                        InputProps={{
                            readOnly: true,
                        }}
                    />
                    <TextField
                        id="outlined-read-only-input"
                        label="Email Address"
                        defaultValue={userData.email}
                        InputProps={{
                            readOnly: true,
                        }}
                    />
                    <TextField
                        id="outlined-read-only-input"
                        label="Phone Number"
                        defaultValue={userData.phoneNumber}
                        InputProps={{
                            readOnly: true,
                        }}
                    />
                    <TextField
                        id="outlined-read-only-input"
                        label="Date Of Birth"
                        defaultValue={userData.dateOfBirth}
                        InputProps={{
                            readOnly: true,
                        }}
                    />
                    <TextField
                        id="outlined-read-only-input"
                        label="Location"
                        defaultValue={userData.address + ", " + userData.place.cityName + ", " + userData.place.zipCode + ", " + userData.place.stateName}
                        InputProps={{
                            readOnly: true,
                        }}
                    />

                </div>

            </Box>



        </div>

    );
}

export default UserProfile;
