import { useEffect, useState } from "react";
import Avatar from '@mui/material/Avatar';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import { DialogContent, DialogTitle, Grid, InputAdornment, List, TextField } from '@mui/material';
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
import {getCurrentLoyaltyProgram} from '../../service/LoyaltyProgramService';
import { useHistory } from 'react-router-dom';
import { AlternateEmail, DateRangeOutlined, Domain, LocationOn, Person, Phone, Public } from "@mui/icons-material";
import UserInfoGrid from "./UserInfoGrid";
import Tooltip from "@mui/material/Tooltip";
import Button from "@mui/material/Button";
import MilitaryTechIcon from '@mui/icons-material/MilitaryTech';

import Dialog from '@mui/material/Dialog';
import HelpOutlineIcon from '@mui/icons-material/HelpOutline';
import LoyaltyProgramToolTip from "../loyaltyProgram/LoyaltyProgramsToolTip";
import { propsLocationStateFound } from "../forbiddenNotFound/notFoundChecker";


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


    const [loyaltyProgram, setLoyaltyProgram] = useState(null);
    const [isLoadingLoyaltyProgram, setIsLoadingLoyaltyProgram] = useState(true);
  

    const history = useHistory();
    const avatar = <Avatar
        alt="Zorica Vukovic"
        src="./slika.jpeg"
        style={{ float: "left", marginLeft: '2%', marginRight: '2%' }}
        sx={{ flexGrow: 1, width: 150, height: 150 }}
        maxRows={4}
    />

    useEffect(() => {
        if (propsLocationStateFound(props, history)) {
            getUserById(props.location.state.userId).then(res => {
                setUserData(res.data);
                setLoading(false);
            });
            getCurrentLoyaltyProgram().then(res => {
                setLoyaltyProgram(res.data);
                setIsLoadingLoyaltyProgram(false);
            });
        }
    }, []);

    if (isLoading || isLoadingLoyaltyProgram) { return <div className="App">Loading...</div> }
    else {
        return (

            <div className="App">
                <div style={{ backgroundColor: 'aliceblue', display: 'flex', margin: '10% auto', marginBottom: '20px', borderRadius: '10px', width: '55%', minHeight: '100px', padding: '2%' }} >
                    <Grid container style={{ justifyContent: 'center', alignItems: 'center' }}>
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
                                {userData.userType.name.substring(userData.userType.name.indexOf('_') + 1)}
                            </Typography>
                        </Grid>
                        {userData.userType.name !== "ROLE_ADMIN" && userData.userType.name !== "ROLE_SUPER_ADMIN" ? (
                            <Grid item xs="auto">
                                <LoyaltyProgramToolTip loyaltyProgram={loyaltyProgram}/>
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
                                                        {(userData.userType.name === "ROLE_SHIP_OWNER" && userData.captain == true) ? (
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
                                                        ) : (
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
                <div style={{ margin: '0px auto', width: '30%' }}>
                    <UserInfoGrid userData={userData} />
                </div>

            </div>

        );
    }
}