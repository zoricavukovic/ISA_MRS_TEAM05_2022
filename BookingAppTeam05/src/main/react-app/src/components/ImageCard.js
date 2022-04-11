import * as React from 'react';
import { styled } from '@mui/material/styles';
import Card from '@mui/material/Card';
import CardHeader from '@mui/material/CardHeader';
import CardMedia from '@mui/material/CardMedia';
import CardContent from '@mui/material/CardContent';
import CardActions from '@mui/material/CardActions';
import Collapse from '@mui/material/Collapse';
import Avatar from '@mui/material/Avatar';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import ToggleButton from '@mui/material/ToggleButton';
import ViewModuleIcon from '@mui/icons-material/ViewModule';
import cottageImg from './cottage.jpg';
import Box from '@mui/material/Box';
import { useEffect, useState } from "react";
import EditIcon from '@mui/icons-material/Edit';
import { useHistory } from "react-router-dom";
import Button from '@mui/material/Button';
import Grid from '@mui/material/Grid';
import Stack from '@mui/material/Stack';
import Divider from '@mui/material/Divider';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import CalendarMonthIcon from '@mui/icons-material/CalendarMonth';
import RatingEntity from './Rating.js';

import axios from "axios";


const ExpandMore = styled((props) => {
    const { expand, ...other } = props;
    return <IconButton {...other} />;
})(({ theme, expand }) => ({
    transform: !expand ? 'rotate(0deg)' : 'rotate(180deg)',
    marginLeft: 'auto',
    transition: theme.transitions.create('transform', {
        duration: theme.transitions.duration.shortest,
    }),
}));

export default function CardIm() {
    const [expanded, setExpanded] = React.useState(false);
    const [cottageBasicData, setCottageBasicData] = useState({});
    const [pricelistData, setPricelistData] = useState({});
    const [isLoadingCottage, setLoadingCottage] = useState(true);
    const [isLoadingPricelist, setLoadingPricelist] = useState(true);
    const history = useHistory();
    const urlCottagePath = "http://localhost:8092/bookingApp/cottages/" + 115;
    const urlPricelistPath = "http://localhost:8092/bookingApp/pricelists/" + 115;

    const handleExpandClick = () => {
        setExpanded(!expanded);
    };
    const handleMorePictures = (event) => {
        event.preventDefault();
        alert("LA");
    };

    const editCottage = (event) => {
        event.preventDefault();
        history.push("./editCottage");
    };

    useEffect(() => {
        
    }, []);
    if (isLoadingCottage || isLoadingPricelist) { return <div className="App">Loading...</div> }
    return (
        <Card sx={{}}>
            <CardMedia
                component="img"
                height="300"
                src={cottageImg}
                alt="Slika vikendice"
            />
            <CardHeader

                title={cottageBasicData.name}
                subheader={cottageBasicData.address + ", " + cottageBasicData.place.cityName + ", " + cottageBasicData.place.zipCode + ", " + cottageBasicData.place.stateName}

                action={
                    <ToggleButton value="module" aria-label="module onClick={handleMorePictures}">
                        <ViewModuleIcon onClick={handleMorePictures} />
                    </ToggleButton>

                }
            />

            <CardActions disableSpacing>
                <IconButton >
                    <CalendarMonthIcon />
                </IconButton>
                <IconButton value="module" aria-label="module" onClick={editCottage}>
                    <EditIcon />
                </IconButton>

                <ExpandMore
                    expand={expanded}
                    onClick={handleExpandClick}
                    aria-expanded={expanded}
                    label="Show more information"
                    aria-label="show more"
                >
                    <ExpandMoreIcon />
                </ExpandMore>
            </CardActions>

            <CardContent>
                <Typography variant="body2" color="text.secondary" style={{ backgroundColor: 'aliceblue', borderRadius: '5px', paddingLeft: '1%', paddingTop: '0.2%', paddingBottom: '0.1%' }}>
                    <h4>Promo Description: </h4><h3>{cottageBasicData.promoDescription} </h3>
                </Typography>
                <Typography variant="body2" color="text.secondary" style={{ width: 'fit', backgroundColor: 'rgb(252, 234, 207)', borderRadius: '5px', paddingLeft: '1%', paddingBottom: '0.2%', paddingTop: '0.2%' }}>
                    <h4>Cost Per Night: {pricelistData.entityPricePerPerson} € </h4>
                    <RatingEntity value='3' />
                </Typography>
            </CardContent>
            <Collapse in={expanded} timeout="auto" unmountOnExit>
                <CardContent>

                    <Box sx={{ width: '100%', maxWidth: 350, bgcolor: 'background.paper' }}>
                        <Box sx={{ my: 2, mx: 3 }}>
                            <Grid container alignItems="center">
                                <Grid item xs>
                                    <Typography gutterBottom variant="h5" component="div" style={{ color: 'rgb(5, 30, 52)', marginLeft: '2.5%' }}>
                                        Rules Of Conduct
            </Typography>
                                </Grid>
                            </Grid>

                        </Box>
                        <Divider variant="middle" />
                        <Box sx={{ m: 2, ml: 2.5 }}>

                            <Stack sx={{
                                mb: 2,
                                pb: 1,
                                display: 'grid',
                                gap: 1,
                                gridTemplateColumns: 'repeat(2, 1fr)',
                            }} direction="row" spacing={1}>


                                {cottageBasicData.rulesOfConduct.map((page) => (

                                    <Button style={{ borderRadius: '10px', backgroundColor: 'rgb(252, 234, 207)', color: 'black' }} key={page.ruleName}>
                                        <FormControlLabel disabled control={<Checkbox size="small" checked={page.allowed} />} />
                                        <Typography textAlign="center">{page.ruleName}</Typography>
                                    </Button>
                                ))}

                            </Stack>
                        </Box>

                    </Box>
                </CardContent>
                <CardContent>

                    <Box sx={{ width: '100%', maxWidth: 350, bgcolor: 'background.paper' }}>
                        <Box sx={{ my: 2, mx: 3 }}>
                            <Grid container alignItems="center">
                                <Grid item xs>
                                    <Typography gutterBottom variant="h5" component="div" style={{ color: 'rgb(5, 30, 52)', marginLeft: '2.5%' }}>
                                        Additional Services
            </Typography>
                                </Grid>
                            </Grid>

                        </Box>
                        <Divider variant="middle" />
                        <Box sx={{ m: 2, ml: 2.5 }}>

                            <Stack sx={{
                                mb: 2,
                                pb: 1,
                                display: 'grid',
                                gap: 1,
                                gridTemplateColumns: 'repeat(2, 1fr)',
                            }} direction="row" spacing={1}>


                                {pricelistData.additionalServices.map((service) => (

                                    <Button style={{ borderRadius: '10px', backgroundColor: 'rgb(252, 234, 207)', color: 'black' }} key={service.name}>

                                        <Typography textAlign="center">{service.serviceName + "    " + service.price + " €"}</Typography>
                                    </Button>
                                ))}
                            </Stack>
                        </Box>
                    </Box>
                </CardContent>
                <CardContent>

                    <Box sx={{ width: '100%', maxWidth: 350, bgcolor: 'background.paper' }}>
                        <Box sx={{ my: 2, mx: 3 }}>
                            <Grid container alignItems="center">
                                <Grid item xs>
                                    <Typography gutterBottom variant="h5" component="div" style={{ color: 'rgb(5, 30, 52)', marginLeft: '2.5%' }}>
                                        Rooms
            </Typography>
                                </Grid>
                            </Grid>

                        </Box>
                        <Divider variant="middle" />
                        <Box sx={{ m: 2, ml: 2.5 }}>

                            <Stack sx={{
                                mb: 2,
                                pb: 1,
                                display: 'grid',
                                gap: 1,
                                gridTemplateColumns: 'repeat(2, 1fr)',
                            }} direction="row" spacing={1}>
                                <table >
                                    <tr>
                                        <th>
                                        Room Name
                                        </th>
                                        <th>
                                        Num Of Beds
                                        </th>
                                    </tr>
                                
                                {cottageBasicData.rooms.map((room) => (

                                    <tr style={{textAlign:'center'}}>
                                        <td> {room.roomNum} </td>
                                   
                                        <td> {room.numOfBeds} </td>
                                    </tr>
                                  
                                ))}
                                </table>
              
            
        </Stack>
        </Box>

                        </Box>

        </CardContent>
      </Collapse>
    </Card>
    
  );
}