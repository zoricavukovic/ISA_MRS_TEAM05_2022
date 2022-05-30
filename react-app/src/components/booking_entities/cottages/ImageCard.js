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
import { editCottageById, getCottageById } from '../../../service/CottageService';
import { getPricelistByEntityId } from '../../../service/PricelistService';
import Chip from '@mui/material/Chip';
import LocalFireDepartmentIcon from '@mui/icons-material/LocalFireDepartment';
import Home from "../../map/GoogleMap";
import { URL_PICTURE_PATH } from '../../../service/PictureService';
import { propsLocationStateFound } from '../../forbiddenNotFound/notFoundChecker';
import { checkIfCanEditEntityById } from '../../../service/BookingEntityService';
import RenderImageSlider from '../../image_slider/RenderImageSlider';
import EventAvailableIcon from '@mui/icons-material/EventAvailable';
import Avatar from '@mui/material/Avatar';
import Dialog from '@mui/material/Dialog';
import { Person } from '@mui/icons-material';
import { format } from "date-fns";
import { DateRange,Calendar } from 'react-date-range';
import Radio from '@mui/material/Radio';
import RadioGroup from '@mui/material/RadioGroup';
import { AddCircleOutlined, DateRangeOutlined, RemoveCircleOutlined } from '@mui/icons-material';
import { getBookingEntityById } from '../../../service/BookingEntityService';
import { addReservation } from '../../../service/ReservationService';
import { getCurrentUser } from '../../../service/AuthService';
import { Paper, TextField, Link, ListItemText, InputAdornment, FormControl, FormLabel, FormGroup, DialogTitle, DialogContent, DialogContentText, DialogActions, ListItem, Autocomplete } from '@mui/material';
import 'react-date-range/dist/styles.css'; // main style file
import 'react-date-range/dist/theme/default.css'; // theme css file
import CreateReservationForClient from '../../reservations/CreateReservationForClient';

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



    const editCottage = (event) => {
        event.preventDefault();
        
        checkIfCanEditEntityById(props.cottageId)
            .then(res => {
                history.push({
                    pathname: "/editCottage",
                    state: { cottageId: props.cottageId }
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
            state: { bookingEntityId: props.cottageId }
        })
    }


    const showFastReservations = (event) => {
        event.preventDefault();
        history.push({
            pathname: "./showFastReservations",
            state: { bookingEntityId: props.cottageId }
        })

    };

    function createReservationForClient() {
       setOpenDialogCreate(true);
    };

    function CottageAdditionalInfo(props) {
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

    //============================= DIALOG =====================================
    //----------------------------------------------------
    const [openDialogCreate, setOpenDialogCreate] = React.useState(false);

    //=======================================================================================

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
    function RenderRoom(props) {
        return (
            props.rooms.map((e) => (
                <Button style={{ borderRadius: '10px', backgroundColor: 'rgb(252, 234, 207)', color: 'black' }} key={e.equipmentName}>
                    <Typography textAlign="center">{"Room Num " + e.roomNum + "/Num of beds " + e.numOfBeds}</Typography>
                </Button>
            ))
        )
    }



    useEffect(() => {
        getCottageById(props.cottageId).then(res => {
            setCottageBasicData(res.data);
            setLoadingCottage(false);
        });
        getPricelistByEntityId(props.cottageId).then(res => {
            setPricelistData(res.data);
            setLoadingPricelist(false);
        });
    }, []);
    if (isLoadingCottage || isLoadingPricelist) { return <div className="App">Loading...</div> }
    return (
        <Card style={{ margin: "1% 9% 1% 9%" }} sx={{}}>
            <CreateReservationForClient bookingEntityId={props.cottageId} openDialog={openDialogCreate}/>
        
            <RenderImageSlider pictures={cottageBasicData.pictures}/>
            <CardHeader

                title={cottageBasicData.name}
                subheader={cottageBasicData.address + ", " + cottageBasicData.place.cityName + ", " + cottageBasicData.place.zipCode + ", " + cottageBasicData.place.stateName}

                action={
                    <ToggleButton value="module" aria-label="module onClick={handleMorePictures}">
                        <ViewModuleIcon />
                    </ToggleButton>

                }
            />

            <CardActions disableSpacing>
                <IconButton onClick={showCalendarForEntity} >
                    <Chip icon={<CalendarMonthIcon />} label="Calendar" />
                </IconButton>

                <IconButton value="module" aria-label="module" onClick={editCottage}>
                    <Chip icon={<EditIcon />} label="Edit Cottage" />
                </IconButton>
                <IconButton value="module" aria-label="module" onClick={showFastReservations}>
                    <Chip icon={<LocalFireDepartmentIcon />} label="Fast Reservations" />
                </IconButton>
                <IconButton value="module" aria-label="module" onClick={createReservationForClient}>
                    <Chip icon={<EventAvailableIcon />} label="Create Reservation For Client" />
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
                <Typography variant="body2" color="text.secondary" style={{ width: '20%', backgroundColor: 'rgb(252, 234, 207)', borderRadius: '10px', paddingLeft: '1%', paddingBottom: '0.2%', paddingTop: '0.2%', margin: '2%' }}>
                    <h4>Cost Per Night: {pricelistData.entityPricePerPerson} € </h4>
                    <RatingEntity value='3' />
                </Typography>
                <Typography variant="body2" style={{ width: '30%', minWidth: "200px", borderRadius: '10px', paddingLeft: '1%', paddingBottom: '0.2%', paddingTop: '0.2%', margin: '2%' }}>
                    <Home long={cottageBasicData.place.longitude} lat={cottageBasicData.place.lat}></Home>

                </Typography>
            </div >
            <Collapse in={expanded} timeout="auto" unmountOnExit>
                <div style={{ display: "flex", flexDirection: "row", flexWrap: 'wrap' }}>

                    <Grid item xs={12} sm={4}>
                        <CottageAdditionalInfo
                            header="Rules of conduct"
                            additionalData={<RenderRulesOfConduct rulesOfConduct={cottageBasicData.rulesOfConduct} />}
                        />
                    </Grid>
                    <Grid item xs={12} sm={4} minWidth="300px">
                        <CottageAdditionalInfo
                            header="Additional services"
                            additionalData={<RenderAdditionalServices additionalServices={pricelistData.additionalServices} />}
                        />
                    </Grid>
                    <Grid item xs={12} sm={4}>
                        <CottageAdditionalInfo
                            header="Rooms"
                            additionalData={<RenderRoom rooms={cottageBasicData.rooms} />}
                        />
                    </Grid>
                    <CardContent>

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