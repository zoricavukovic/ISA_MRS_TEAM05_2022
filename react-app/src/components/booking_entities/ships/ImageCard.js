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
import ImageSlider from "../../image_slider/ImageSlider";
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
import RatingEntity from '../../Rating';
import Snackbar from '@mui/material/Snackbar';
import { editShipById, getShipById } from '../../../service/ShipService';
import { getPricelistByEntityId } from '../../../service/PricelistService';
import Chip from '@mui/material/Chip';
import LocalFireDepartmentIcon from '@mui/icons-material/LocalFireDepartment';
import Home from "../../map/GoogleMap";
import ShipSpecificationCard from "./ShipSpecificationCard";
import { checkIfCanEditEntityById } from '../../../service/BookingEntityService';
import { URL_PICTURE_PATH } from '../../../service/PictureService';
import RenderImageSlider from '../../image_slider/RenderImageSlider';

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
    const [shipBasicData, setShipBasicData] = useState({});
    const [pricelistData, setPricelistData] = useState({});
    const [isLoadingShip, setLoadingShip] = useState(true);
    const [isLoadingPricelist, setLoadingPricelist] = useState(true);
    const history = useHistory();

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

    const editShip = (event) => {
        event.preventDefault();

        checkIfCanEditEntityById(props.shipId)
            .then(res => {
                history.push({
                    pathname: "/editShip",
                    state: { shipId: props.shipId }
                });
            })
            .catch(res => {
                setMessage(res.response.data);
                handleClick();
                return;
            });

    };


    const showCalendarForEntity = (event) => {
        event.preventDefault();
        history.push({
            pathname: "/calendarForEntity",
            state: { bookingEntityId: props.shipId }
        })
    }


    const showFastReservations = (event) => {
        event.preventDefault();
        history.push({
            pathname: "./showFastReservations",
            state: { bookingEntityId: props.shipId }
        })

    };

    function ShipAdditionalInfo(props) {
        return (
            <CardContent>
                <Box sx={{ width: '100%', maxWidth: 350, bgcolor: 'background.paper' }}>
                    <Box sx={{ my: 2, mx: 3 }}>
                        <Grid container alignItems="center">
                            <Grid item xs>
                                <Typography gutterBottom variant="h5" component="div" style={{ color: 'rgb(5, 30, 52)', marginLeft: '2.5%' }}>
                                    {props.header}
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
                            gridTemplateRows: 'repeat(2, 1fr)',
                        }} direction="column" spacing={1}>
                            {props.additionalData}
                        </Stack>
                    </Box>
                </Box>
            </CardContent>
        )
    }


    function RenderRulesOfConduct(props) {
        return (
            props.rulesOfConduct.map((page) => (
                <Button style={{ borderRadius: '10px', backgroundColor: 'rgb(252, 234, 207)', color: 'black' }} key={page.ruleName}>
                    <FormControlLabel disabled control={<Checkbox size="small" checked={page.allowed} />} />
                    <Typography textAlign="center">{page.ruleName}</Typography>
                </Button>
            ))
        )
    }
    function RenderAdditionalServices(props) {
        return (
            props.additionalServices.map((service) => (
                <Button style={{ borderRadius: '10px', backgroundColor: 'rgb(252, 234, 207)', color: 'black' }} key={service.name}>
                    <Typography textAlign="center">{service.serviceName + "    " + service.price + " €"}</Typography>
                </Button>
            ))
        )
    }

    function RenderFishingEquipment(props) {
        return (
            props.fishingEquipment.map((e) => (
                <Button style={{ borderRadius: '10px', backgroundColor: 'rgb(252, 234, 207)', color: 'black' }} key={e.equipmentName}>
                    <Typography textAlign="center">{e.equipmentName}</Typography>
                </Button>
            ))
        )
    }

    function RenderNavigationalEquipment(props) {
        return (
            props.navigationalEquipment.map((e) => (
                <Button style={{ borderRadius: '10px', backgroundColor: 'rgb(252, 234, 207)', color: 'black' }} key={e.equipmentName}>
                    <Typography textAlign="center">{e.name}</Typography>
                </Button>
            ))
        )
    }

    useEffect(() => {
        getShipById(props.shipId).then(res => {
            setShipBasicData(res.data);
            setLoadingShip(false);
            console.log(res.data);
        });
        getPricelistByEntityId(props.shipId).then(res => {
            setPricelistData(res.data);
            setLoadingPricelist(false);
        });
    }, []);

    if (isLoadingShip || isLoadingPricelist) { return <div className="App">Loading...</div> }
    return (
        <Card style={{ margin: "1% 9% 1% 9%" }} sx={{}}>
            <RenderImageSlider pictures={shipBasicData.pictures}/>
            <CardHeader

                title={shipBasicData.name + ": " + shipBasicData.shipType}
                subheader={shipBasicData.address + ", " + shipBasicData.place.cityName + ", " + shipBasicData.place.zipCode + ", " + shipBasicData.place.stateName}
            />
            <CardActions disableSpacing>
                <IconButton onClick={showCalendarForEntity}>
                    <Chip icon={<CalendarMonthIcon />} label="Calendar" />
                </IconButton>

                <IconButton value="module" aria-label="module" onClick={editShip}>
                    <Chip icon={<EditIcon />} label="Edit Ship" />
                </IconButton>
                <IconButton value="module" aria-label="module" onClick={showFastReservations}>
                    <Chip icon={<LocalFireDepartmentIcon />} label="Fast Reservations" />
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
                <Typography variant="body2" style={{ width: '25%', minWidth: "200px", borderRadius: '10px', paddingLeft: '1%', paddingBottom: '0.2%', paddingTop: '0.2%', margin: '2%' }}>
                    <Typography variant="body2" color="text.secondary" style={{ width: '100%', backgroundColor: 'aliceblue', borderRadius: '10px', paddingLeft: '1%', paddingTop: '0.2%', paddingBottom: '0.1%', margin: '2%' }}>
                        <h4>Promo Description: </h4><h3>{shipBasicData.promoDescription} </h3>
                    </Typography>
                    <Typography variant="body2" color="text.secondary" style={{ width: '100%', backgroundColor: 'rgb(252, 234, 207)', borderRadius: '10px', paddingLeft: '1%', paddingBottom: '0.2%', paddingTop: '0.2%', margin: '2%' }}>
                        <h4>Cost Per Person: {pricelistData.entityPricePerPerson} € </h4>
                        <RatingEntity value='3' />
                    </Typography>
                </Typography>

                <Typography variant="body2" style={{ width: '30%', minWidth: "200px", borderRadius: '10px', paddingLeft: '1%', paddingBottom: '0.2%', paddingTop: '0.2%', margin: '2%' }}>
                    <ShipSpecificationCard ship={shipBasicData} />
                </Typography>
                <Typography variant="body2" style={{ width: '30%', minWidth: "200px", borderRadius: '10px', paddingLeft: '1%', paddingBottom: '0.2%', paddingTop: '0.2%', margin: '2%' }}>
                    <Home long={shipBasicData.place.longitude} lat={shipBasicData.place.lat}></Home>
                </Typography>

            </div >
            <Collapse in={expanded} timeout="auto" unmountOnExit>
                <div style={{ display: "flex", flexDirection: "row", flexWrap: 'wrap' }}>

                    <Grid item xs={12} sm={4} minWidth="300px">
                        <ShipAdditionalInfo
                            header="Rules of conduct"
                            additionalData={<RenderRulesOfConduct rulesOfConduct={shipBasicData.rulesOfConduct} />}
                        />
                    </Grid>
                    <Grid item xs={12} sm={4} minWidth="300px">
                        <ShipAdditionalInfo
                            header="Additional services"
                            additionalData={<RenderAdditionalServices additionalServices={pricelistData.additionalServices} />}
                        />
                    </Grid>
                    <Grid item xs={12} sm={4} minWidth="300px">
                        <ShipAdditionalInfo
                            header="Fishing equipment"
                            additionalData={<RenderFishingEquipment fishingEquipment={shipBasicData.fishingEquipment} />}
                        />
                    </Grid>
                    <Grid item xs={12} sm={4} minWidth="350px">
                        <ShipAdditionalInfo
                            header="Navigational equipment"
                            additionalData={<RenderNavigationalEquipment navigationalEquipment={shipBasicData.navigationalEquipment} />}
                        />
                    </Grid>
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