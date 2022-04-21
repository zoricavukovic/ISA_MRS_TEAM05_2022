import * as React from 'react';
import { styled } from '@mui/material/styles';
import Card from '@mui/material/Card';
import CardHeader from '@mui/material/CardHeader';
import CardMedia from '@mui/material/CardMedia';
import CardContent from '@mui/material/CardContent';
import CardActions from '@mui/material/CardActions';
import Collapse from '@mui/material/Collapse';
import Alert from '@mui/material/Alert';
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
import Snackbar from '@mui/material/Snackbar';
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

export default function CardIm(props) {
    const [expanded, setExpanded] = React.useState(false);
    const [open, setOpen] = React.useState(false);
    const [message, setMessage] = React.useState("");
    const [cottageBasicData, setCottageBasicData] = useState({});
    const [pricelistData, setPricelistData] = useState({});
    const [isLoadingCottage, setLoadingCottage] = useState(true);
    const [isLoadingPricelist, setLoadingPricelist] = useState(true);
    const history = useHistory();

    const urlCottagePath = "http://localhost:8092/bookingApp/cottages/";
    const urlPricelistPath = "http://localhost:8092/bookingApp/pricelists/";

    const handleClick = () => {
        setOpen(true);
      };
    const handleClose = (event, reason) => {
        if (reason === 'clickaway') {
          return;
        }
    
        setOpen(false);
      };
  

    const handleExpandClick = () => {
        setExpanded(!expanded);
    };
    const handleMorePictures = (event) => {
        event.preventDefault();
        alert("LA");
    };

    const editCottage = (event) => {
        event.preventDefault();
        axios.get("http://localhost:8092/bookingApp/cottages/editQue/" + props.cottageId).then(res => {
            history.push({
                pathname: "./editCottage",
                state: { cottageId: props.cottageId }
            })
        
        }).catch(res=>{
          setMessage(res.response.data);
          handleClick();
          return;
        })         
    };

    useEffect(() => {
        axios.get(urlCottagePath + props.cottageId).then(res => {
            setCottageBasicData(res.data);
            setLoadingCottage(false);
        })
        axios.get(urlPricelistPath + props.cottageId).then(result => {
            setPricelistData(result.data);
            setLoadingPricelist(false);
        })
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

            <div style={{ display: "flex", flexDirection: "row", flexWrap: 'wrap' }}>
                <Typography variant="body2" color="text.secondary" style={{ width: '30%', backgroundColor: 'aliceblue', borderRadius: '10px', paddingLeft: '1%', paddingTop: '0.2%', paddingBottom: '0.1%', margin: '2%' }}>
                    <h4>Promo Description: </h4><h3>{cottageBasicData.promoDescription} </h3>
                </Typography>
                <Typography variant="body2" color="text.secondary" style={{ width: '30%', backgroundColor: 'rgb(252, 234, 207)', borderRadius: '10px', paddingLeft: '1%', paddingBottom: '0.2%', paddingTop: '0.2%', margin: '2%' }}>
                    <h4>Cost Per Night: {pricelistData.entityPricePerPerson} € </h4>
                    <RatingEntity value='3' />
                </Typography>
            </div >
            <Collapse in={expanded} timeout="auto" unmountOnExit>
                <div style={{ display: "flex", flexDirection: "row", flexWrap: 'wrap' }}>
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

                                            <tr style={{ textAlign: 'center' }}>
                                                <td> {room.roomNum} </td>

                                                <td> {room.numOfBeds} </td>
                                            </tr>

                                        ))}
                                    </table>


                                </Stack>
                            </Box>

                        </Box>

                    </CardContent>
                </div>
            </Collapse>
            <Snackbar open={open} autoHideDuration={6000} onClose={handleClose}>
                <Alert onClose={handleClose} severity="error" sx={{ width: '100%' }}>
                    {message}
                </Alert>
                
                
            </Snackbar>
        </Card>

    );
}