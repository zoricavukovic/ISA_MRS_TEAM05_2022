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
import CalendarMonthIcon from '@mui/icons-material/CalendarMonth';
import RatingEntity from '../../Rating';
import Snackbar from '@mui/material/Snackbar';
import { getShipById } from '../../../service/ShipService';
import { getPricelistByEntityId } from '../../../service/PricelistService';
import Chip from '@mui/material/Chip';
import LocalFireDepartmentIcon from '@mui/icons-material/LocalFireDepartment';
import Home from "../../map/GoogleMap";
import ShipSpecificationCard from "./ShipSpecificationCard";
import LocationOnIcon from '@mui/icons-material/LocationOn';
import PersonIcon from '@mui/icons-material/Person';
import { checkIfCanEditEntityById } from '../../../service/BookingEntityService';
import RenderImageSlider from '../../image_slider/RenderImageSlider';
import { getAvailableFastReservationsByBookingEntityId, reserveFastReservation } from '../../../service/ReservationService';
import { getRatingsByEntityId } from '../../../service/RatingService';
import { subscribeClientWithEntity, unsubscribeClientWithEntity } from '../../../service/UserService';
import { getCurrentUser } from '../../../service/AuthService';
import { Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, List, ListItem, Tooltip } from '@mui/material';
import { DateRangeOutlined, DirectionsBoat, Favorite, FavoriteBorder } from '@mui/icons-material';
import StyledAvatar from '../../StyledAvatar';
import EventAvailableIcon from '@mui/icons-material/EventAvailable';
import CreateReservationForClient from '../../reservations/CreateReservationForClient';
import { findAllClientsWithActiveReservations} from '../../../service/ReservationService';
import Approved from "../../../icons/approval.png";
import NotApproved from "../../../icons/notApprowed.png"
import Money from "../../../icons/money.png";

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

export default function ImageCardForShip(props) {

    const [expanded, setExpanded] = React.useState(false);
    const [open, setOpen] = React.useState(false);
    const [message, setMessage] = React.useState("");
    const [shipBasicData, setShipBasicData] = useState({});
    const [pricelistData, setPricelistData] = useState({});
    const [isLoadingShip, setLoadingShip] = useState(true);
    const [isLoadingPricelist, setLoadingPricelist] = useState(true);
    const [hasAuthority, setHasAuthority] = useState(false);
    const [fastReservations, setFastReservations] = useState([]);
    const [openDialog, setOpenDialog] = useState(false);
    const [selectedFastReservation, setSelectedFastReservation] = useState({});
    const [clientReviews, setClientReviews] = useState([]);
    const [isSubscribed, setIsSubscribed] = useState(false);
    const [typeAlert, setTypeAlert] = useState("success");
    const [userExists, setUserExists] = useState(false);
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
                setTypeAlert("error");
                handleClick();
                return;
            });

    };


    const showCalendarForEntity = (event) => {
        event.preventDefault();
        getShipById(props.shipId).then(res => {
            history.push({
                pathname: "/calendarForEntity",
                state: { bookingEntityId: props.shipId } 
            })
        }).catch(res => {
            setMessage(res.response.data);
            setTypeAlert("error");
            handleClick();
            return;
        });
    }


    const showFastReservations = (event) => {
        event.preventDefault();
        getShipById(props.shipId).then(res => {
            history.push({
                pathname: "/addFastReservation",
                state: { bookingEntityId: props.shipId } 
            })
        }).catch(res => {
            setMessage(res.response.data);
            setTypeAlert("error");
            handleClick();
            return;
        });

    };
    //============================= DIALOG =====================================
    //----------------------------------------------------
    const [openDialogCreate, setOpenDialogCreate] = React.useState(false);

    //=======================================================================================

    function createReservationForClient() {
        getShipById(props.shipId).then(res => {
            findAllClientsWithActiveReservations(props.shipId).then(res => {
                console.log(res.data);
                if (res.data.length !== 0){
                    setOpenDialogCreate(true);
                }
                else{
                    setMessage("Don't have clients with active reservations.");
                    setTypeAlert("error");
                    handleClick();
                }
            });
        }).catch(res => {
            setMessage(res.response.data);
            setTypeAlert("error");
            handleClick();
            return;
        });
        
       
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
                <Button disabled style={{ borderRadius: '10px', backgroundColor: 'rgb(252, 234, 207)', color: 'black' }} key={page.ruleName}>
                    <table>
                        <tr>
                        <th><Typography textAlign="left">{page.ruleName}</Typography></th>
                            <th>
                            
                                {page.allowed == true ? (
                                    <img style={{height:'70%', width:'70%'}} src={Approved}></img>):(
                                    <img style={{height:'70%', width:'70%'}} src={NotApproved}></img>  
                                )}
                            </th>
                            
                        </tr>
                    </table>
                </Button>
            ))
        )
    }
    function RenderAdditionalServices(props) {
        return (
            props.additionalServices.map((service) => (
                <Button style={{ borderRadius: '10px', backgroundColor: 'rgb(252, 234, 207)', color: 'black' }} key={service.name}>
                    <Typography textAlign="center">{service.serviceName + "    " + service.price + " ???"}</Typography>
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

    const FastReserve = (fastReservation)=> {
        console.log(fastReservation);
        setSelectedFastReservation(fastReservation);
        setOpenDialog(true);
    }

    const confirmReservation = ()=>{
        console.log(getCurrentUser());
        selectedFastReservation.client = getCurrentUser();
        reserveFastReservation(selectedFastReservation).then(res=>{
            console.log("Adding temp res success");
            console.log(res.data);
            setOpenDialog(false);
            let fRes = fastReservations;
            fRes = fRes.filter(elem => elem.id != selectedFastReservation.id);
            console.log(fRes);
            setFastReservations(fRes);
            setTypeAlert("success");
            handleClick();
            setMessage("Successfully reserved entity.");
        }).catch(res=>{
            console.log("Adding temp res failed");
        });
    }

    const reserveBookingEntity = () => {
        console.log("Evo me");
        console.log(props.searchParams);
        history.push({
            pathname: "/newReservation",
            state: {
                bookingEntityId: props.shipId,
                searchParams: {}
            }
        })
    }

    const subscribe =()=>{
        subscribeClientWithEntity(getCurrentUser().id, shipBasicData.id).then(res=>{
            console.log("Uspesno sub");
            console.log(res.data);
            if(typeof(props.setSubscribedEntities) !== "undefined" )
                props.setSubscribedEntities(res.data);
            else
                setIsSubscribed(true);
        });
    }
    const unsubscribe =()=>{
        unsubscribeClientWithEntity(getCurrentUser().id, shipBasicData.id).then(res=>{
            console.log("Uspesno unsub");
            console.log(res.data);
            if(typeof(props.setSubscribedEntities) !== "undefined" )
                props.setSubscribedEntities(res.data);
            else
            setIsSubscribed(false);
        });
    }

    useEffect(() => {
        setIsSubscribed(props.subscribed);

        if(getCurrentUser() != undefined && getCurrentUser() != null)
            setUserExists(true);

        getShipById(props.shipId).then(res => {
            setShipBasicData(res.data);
            console.log(res.data);
            if(getCurrentUser() != undefined && getCurrentUser() != null)
                if(getCurrentUser().id == res.data.shipOwner.id)
                    setHasAuthority(true);
            setLoadingShip(false);
        }).catch(res => {
            props.setMessage(res.response.data);
            console.log(res.response.data);
            setTypeAlert("error");
            props.handleClick();
            history.push('/ships');
            return;
        });
        getPricelistByEntityId(props.shipId).then(res => {
            setPricelistData(res.data);
            setLoadingPricelist(false);
        });

        if(getCurrentUser() != undefined && getCurrentUser() != null)
            getAvailableFastReservationsByBookingEntityId(props.shipId).then(res=>{
                console.log(res.data);
                setFastReservations(res.data);

            });

        getRatingsByEntityId(props.shipId).then(res=>{
            console.log("ratingd");
            console.log(res.data);
            setClientReviews(res.data);

        })
    }, []);

    if (isLoadingShip || isLoadingPricelist) { return <div className="App">Loading...</div> }
    return (
        <Card style={{ margin: "1% 9% 1% 9%" }} sx={{}}>
            <CreateReservationForClient bookingEntityId={props.shipId} openDialog={openDialogCreate}/>
        
            <RenderImageSlider pictures={shipBasicData.pictures}/>
            <CardHeader
                style={{marginTop:'20px'}}
                title={<div style={{display:'flex'}}><h2>{shipBasicData.name+": "}</h2>
                        <div style={{marginLeft:'30px', marginTop:'8px'}}>
                            <RatingEntity value={props.rating} text={false} size="large"/>
                        </div>
                        </div>}
                subheader={<div><h3><DirectionsBoat/>{" "+shipBasicData.shipType}</h3>
                            <h3><LocationOnIcon/>{shipBasicData.address + ", " + shipBasicData.place.cityName + ", " + shipBasicData.place.zipCode + ", " + shipBasicData.place.stateName}</h3>
                            </div>}

                action={
                    <>
                    {(userExists && getCurrentUser().userType.name == "ROLE_CLIENT")?(
                    <>
                    <Button onClick={reserveBookingEntity} disabled={getCurrentUser().penalties>2?true:false} variant='contained' size='large' /*style={{backgroundColor:'rgb(244, 177, 77)', color:'rgb(5, 30, 52)'}}*/>
                        Reserve
                    </Button>
                     {isSubscribed?
                                    <Button  size="large" style={{marginLeft:'5px', padding:'0px'}} onClick={unsubscribe}><Tooltip title="Unsubscribe"><Favorite fontSize="large" style={{ margin: "5px" }} /></Tooltip></Button>:
                                    <Button size="large" style={{ margin: "5px", padding:'0px' }} onClick={subscribe}><Tooltip title="Subscribe"><FavoriteBorder fontSize="large"  /></Tooltip></Button>
                     }
                    </>):(<></>)}
                    </>
                    
                }
            
            />
            {hasAuthority &&
            <CardActions disableSpacing>
                <IconButton onClick={showCalendarForEntity}>
                    <Chip icon={<CalendarMonthIcon />} label="Calendar" />
                </IconButton>

                <IconButton value="module" aria-label="module" onClick={editShip}>
                    <Chip icon={<EditIcon />} label="Edit Ship" />
                </IconButton>
                <IconButton value="module" aria-label="module" onClick={showFastReservations}>
                    <Chip icon={<LocalFireDepartmentIcon />} label="Create Action" />
                </IconButton>
                <IconButton value="module" aria-label="module" onClick={createReservationForClient}>
                    <Chip icon={<EventAvailableIcon />} label="Create Reservation For Client" />
                </IconButton>
            </CardActions>
            }

            <hr></hr>
            {fastReservations.length > 0 && 
                <>
                <div >
                    <h3 style={{marginLeft:'40px'}}>Available fast reservations:</h3>
                        <List direction={'row'} component={Stack} style={{marginLeft:'20px', overflow:'auto', maxHeight:'200px'}} >
                            {fastReservations.map((res,index) =>(
                                <ListItem style={{minWidth:'250px', maxWidth:'250px'}}>
                                    <Grid container style={{background:'rgb(5, 30, 52)', color:'white', borderRadius:'10px', margin:'5px', padding:'20px'}}>
                                        <Grid item xs>
                                            <Grid container>
                                                <Grid item>
                                                    <DateRangeOutlined size='medium'/>
                                                </Grid>
                                                <Grid item>
                                                    <Typography gutterBottom variant="subtitle1" component="div">
                                                        {res.startDate}
                                                    </Typography>
                                                </Grid>
                                        </Grid>
                                        <Grid item>
                                            <Grid container>
                                                <Grid item xs={6}> 
                                                    <Typography gutterBottom variant="subtitle1" component="div">
                                                        {res.numOfDays} Days
                                                    </Typography>
                                                </Grid>
                                                <Grid item xs={6}> 
                                                    <Grid container>
                                                        <Grid item>
                                                            <PersonIcon size='medium'/> 
                                                        </Grid>
                                                        <Grid item>
                                                        <Typography gutterBottom variant="subtitle1" component="div">
                                                            {res.numOfPersons} 
                                                        </Typography>
                                                        </Grid>
                                                    </Grid>
                                                </Grid> 
                                            </Grid>
                                        </Grid>
                                        <Grid item>
                                        <Typography variant="subtitle1" component="div">
                                            {res.cost*res.numOfDays*res.numOfPersons}???
                                        </Typography>
                                            {(userExists && getCurrentUser().userType.name == "ROLE_CLIENT")?(
                                                <Button onClick={() =>FastReserve(res)} disabled={getCurrentUser().penalties>2?true:false} variant='contained' style={{backgroundColor:'rgb(244, 177, 77)', color:'rgb(5, 30, 52)'}}>
                                                    Reserve Now
                                                </Button>
                                            ):(<></>)}
                                        </Grid>
                                        </Grid>
                                    </Grid>
                                </ListItem>
                            ))}
                        </List>
                </div>
                <hr></hr>
                </>
            }

            <Dialog open={openDialog} onClose={()=>setOpenDialog(false)}>
                <DialogTitle>Please Confirm Reservation</DialogTitle>
                <DialogContent>
                    {Object.keys(selectedFastReservation).length > 0 &&
                    <DialogContentText>
                        Reservation type:<b>{String(shipBasicData.entityType).toLowerCase()}</b> <br></br>
                        Name: <b>{shipBasicData.name} </b>             <br></br>
                        Place: <b>{shipBasicData.place.cityName+", "+shipBasicData.place.stateName}</b>    <br></br>
                        Start date and time: <b>{selectedFastReservation.startDate}</b><br></br>
                        Days: <b>{selectedFastReservation.numOfDays}</b><br></br>
                        Number of persons: <b>{selectedFastReservation.numOfPersons}</b><br></br>
                        Additional services selected:<b>{selectedFastReservation.additionalServices.length!=0? selectedFastReservation.additionalServices.map(service=>{
                            return service.serviceName + " "+service.price+"???, ";
                        }):<p>None</p>}</b><br></br>
                        Price: <b>{selectedFastReservation.cost+"???"}</b>
                    </DialogContentText>
                    }
                </DialogContent>
                <DialogActions>
                    <Button onClick={()=>setOpenDialog(false)}>Cancel</Button>
                    <Button onClick={confirmReservation}>Confirm</Button>
                </DialogActions>
            </Dialog>

            <div style={{ display: "flex", flexDirection: "row", flexWrap: 'wrap', marginTop:'10px' }}>
                <ShipSpecificationCard ship={shipBasicData} />
            
                <Grid item xs={12} sm={4} style={{ width: '30%'}} minWidth="200px">
                        <ShipAdditionalInfo
                            header="Rules of conduct"
                            additionalData={<RenderRulesOfConduct rulesOfConduct={shipBasicData.rulesOfConduct} />}
                        />
                    </Grid>
                    <Grid item xs={12} sm={4} style={{ width: '30%'}} minWidth="300px">
                        <ShipAdditionalInfo
                            header="Additional services"
                            additionalData={<RenderAdditionalServices additionalServices={pricelistData.additionalServices} />}
                        />
                    </Grid>
                    <Grid item xs={12} sm={4} style={{ width: '30%'}} minWidth="300px">
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
                    <Typography variant="body2" style={{ width: '25%', minWidth: "200px", borderRadius: '10px', paddingLeft: '1%', paddingBottom: '0.2%', paddingTop: '0.2%', margin: '2%' }}>
                        <Typography variant="body2" color="text.secondary" style={{ width: '100%', backgroundColor: 'aliceblue', borderRadius: '10px', paddingLeft: '1%', paddingTop: '0.2%', paddingBottom: '0.1%', margin: '2%' }}>
                            <h4>Promo Description: </h4><h3>{shipBasicData.promoDescription} </h3>
                        </Typography>
                        <Typography variant="body2" color="text.primary" style={{ width: '100%', backgroundColor: 'aliceblue', borderRadius: '10px', paddingLeft: '1%', paddingTop: '0.2%', paddingBottom: '0.1%', margin: '2%' }}>
                            <img src={Money} height='40px' width={"40px"}></img>
                            <div style={{marginTop:'10px', display:'flex'}}>
                                <h3 >Cancelation rate: </h3>{shipBasicData.entityCancelationRate > 0?<h3>{shipBasicData.entityCancelationRate+"%"} </h3>:<h3>Free</h3>}
                            </div>
                            <div style={{marginTop:'10px', display:'flex'}}>
                                <h3 >Price: {pricelistData.entityPricePerPerson+"???"} </h3>
                            </div>
                        </Typography>
                    </Typography>
            </div >
            <hr></hr>
                <Grid container spacing={2} style={{ marginTop:'10px', marginBottom:'20px', marginLeft:'40px'}}>
                    <Grid item xs={'auto'} md={4} lg={4} style={{ overflow:'auto'}}>
                        <h3>Location on map:</h3>
                        <Typography variant="body2" style={{ width: '50%', minWidth: "400px", borderRadius: '10px', paddingBottom: '0.2%', paddingTop: '0.2%', marginTop:'10px' }}>
                        <Home long={shipBasicData.place.longitude} lat={shipBasicData.place.lat}></Home>

                        </Typography>
                    </Grid >
                    <Grid item xs={6} style={{marginBottom:'50px', height:'100%', marginLeft:'40px'}}>
                        <h3 >Reviews from clients:</h3>
                        <Typography style={{ borderRadius: '10px', marginRight: '2%', marginTop:'10px', overflow:'auto', height:'100%' }}>
                            {clientReviews.length > 0 ? clientReviews.map((review, index)=>(
                                <Card variant="outlined" style={{width:'100%'}}>
                                    <React.Fragment>
                                        <CardContent>
                                        <Typography variant="h5" component="div">
                                            <StyledAvatar user= {review.owner}/>
                                            {review.owner.firstName+" "+review.owner.lastName}
                                        </Typography>
                                        <Typography sx={{ mb: 1.5 }}>
                                            <RatingEntity value={review.value} text={true} />
                                        </Typography>
                                        <Typography variant="body2">
                                           {review.comment}
                                        </Typography>
                                        </CardContent>
                                    </React.Fragment>
                                </Card>
                            )):<h4>Not yet reviewed</h4>}
                        </Typography>
                    </Grid>
                </Grid>
            <Snackbar open={open} autoHideDuration={6000} onClose={handleClose}>
                <Alert onClose={handleClose} severity={typeAlert} sx={{ width: '100%' }}>
                    {message}
                </Alert>
            </Snackbar>
        </Card>

    );
}