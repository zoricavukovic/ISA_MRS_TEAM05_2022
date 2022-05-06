import React, { useEffect, useState } from "react";
import Home from "../map/GoogleMap";
import Box from "@mui/material/Box";
import { useHistory } from "react-router-dom";
import Card from "@mui/material/Card";
import CardHeader from "@mui/material/CardHeader";
import CardActions from "@mui/material/CardActions";
import IconButton from "@mui/material/IconButton";
import CalendarMonthIcon from "@mui/icons-material/CalendarMonth";
import EditIcon from "@mui/icons-material/Edit";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import CardContent from "@mui/material/CardContent";
import Typography from "@mui/material/Typography";
import RatingEntity from "../Rating";
import Collapse from "@mui/material/Collapse";
import Grid from "@mui/material/Grid";
import Divider from "@mui/material/Divider";
import Stack from "@mui/material/Stack";
import Button from "@mui/material/Button";
import FormControlLabel from "@mui/material/FormControlLabel";
import Checkbox from "@mui/material/Checkbox";
import { styled } from "@mui/material/styles";
import ImageSlider from "../image_slider/ImageSlider";
import { getAdventureById  } from "../../service/AdventureService";
import {checkIfCanEditEntityById} from "../../service/BookingEntityService";
import { getPricelistByEntityId } from "../../service/PricelistService";
import { URL_PICTURE_PATH } from "../../service/PictureService";
import Chip from '@mui/material/Chip';
import LocalFireDepartmentIcon from '@mui/icons-material/LocalFireDepartment';
import Snackbar from '@mui/material/Snackbar';
import Alert from '@mui/material/Alert';

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


function AdventureBasicInfo(props) {
    return (
        <div>
            <div style={{ display: "flex", flexDirection: "row", flexWrap: 'wrap' }}>

                <Typography variant="body2" color="text.secondary" style={{ width: '30%', backgroundColor: 'aliceblue', borderRadius: '10px', paddingLeft: '1%', paddingTop: '0.2%', paddingBottom: '0.1%', margin: '2%' }}>
                    <h4>Promo Description: </h4><h3>{props.adventureData.promoDescription} </h3>
                </Typography>
                <Typography variant="body2" color="text.secondary" style={{ width: '20%', backgroundColor: 'rgb(252, 234, 207)', borderRadius: '10px', paddingLeft: '1%', paddingBottom: '0.2%', paddingTop: '0.2%', margin: '2%' }}>
                    <h4>Cost Per Night: {props.pricelistData.entityPricePerPerson} € </h4>
                    <RatingEntity value='3' />
                </Typography>
                <Typography variant="body2" color="text.secondary" style={{ width: '20%', borderRadius: '10px', paddingLeft: '1%', paddingBottom: '0.2%', paddingTop: '0.2%', margin: '2%' }}>
                    <Home long={props.adventureData.place.longitude} lat={props.adventureData.place.lat}></Home>

                </Typography>
            </div>
            <Typography variant="body2" color="text.secondary" style={{ width: '30%', backgroundColor: 'rgb(252, 234, 207)', borderRadius: '10px', paddingLeft: '1%', paddingBottom: '0.2%', paddingTop: '0.2%', margin: '2%' }}>
                <h4>Short Bio: </h4><h3>{props.adventureData.shortBio} </h3>
            </Typography>
        </div>
    );
}

function AdventureAdditionalInfo(props) {
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

function AdventureActions(props) {

    const editAdventure = (event) => {
        event.preventDefault();
        checkIfCanEditEntityById(props.adventureId)
            .then(res => {
                props.history.push({
                    pathname: "/editAdventure",
                    state: { bookingEntityId: props.adventureId }
                });        
            })
            .catch(res => {
                props.setMessage(res.response.data);
                props.handleClick();
                return;
            });
    };

    const showCalendarForEntity = (event) => {
        event.preventDefault();
        props.history.push({
            pathname: "/calendarForEntity",
            state: { bookingEntityId: props.adventureId }
        })
    }
    const showFastReservations = (event) => {
        event.preventDefault();
        props.history.push({
            pathname: "/showFastReservations",
            state: { adventureId: props.adventureId }
        });

    };

    return (
        <CardActions disableSpacing>
            <IconButton onClick={showCalendarForEntity}>
                <Chip icon={<CalendarMonthIcon />} label="Calendar"/>
            </IconButton>

            <IconButton value="module" aria-label="module" onClick={editAdventure}>
                <Chip icon={<EditIcon />} label="Edit Adventure" />
            </IconButton>
            <IconButton value="module" aria-label="module" onClick={showFastReservations}>
                <Chip icon={<LocalFireDepartmentIcon />} label="Fast Reservations" />
            </IconButton>

            <ExpandMore
                expand={props.expanded}
                onClick={props.handleExpandClick}
                aria-expanded={props.expanded}
                label="Show more information"
                aria-label="show more"
            >
                <ExpandMoreIcon />
            </ExpandMore>
        </CardActions>
    )
}


export default function AdventureProfile(props) {

    const [expanded, setExpanded] = React.useState(false);

    const [pricelistData, setPricelistData] = useState({});
    const [adventureData, setAdventureData] = useState({});
    const [isLoading, setLoading] = useState(true);
    const [isLoadingPricelist, setLoadingPricelist] = useState(true);
    const history = useHistory();
    const [open, setOpen] = React.useState(false);
    const [message, setMessage] = React.useState("");
    let adventureId;

    const handleExpandClick = () => {
        setExpanded(!expanded);
    };
    const handleClick = () => {
        setOpen(true);
    };
    const handleClose = (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }
        setOpen(false);
    };

    useEffect(() => {
        if (props.location.state === undefined || props.location.state === null) {
            history.push("/notFoundPage");
        } else {
            getAdventureById(props.location.state.bookingEntityId).then(res => {
                setAdventureData(res.data);
                setLoading(false);
            })
            getPricelistByEntityId(props.location.state.bookingEntityId).then(result => {
                setPricelistData(result.data);
                setLoadingPricelist(false);
            })
        }
    }, [])

    if (isLoading || isLoadingPricelist) {
        return <div className="App">Loading...</div>
    }
    else {
        { adventureId = props.location.state.bookingEntityId }
        return (
            <Card style={{ margin: "1% 9% 1% 9%" }} sx={{}}>
                <ImageSlider slides={adventureData.pictures.map((im) => ({ 'image': URL_PICTURE_PATH + im.picturePath }))} />
                <br />
                <CardHeader
                    title={adventureData.name}
                    subheader={adventureData.address + ", " + adventureData.place.cityName + ", " + adventureData.place.zipCode + ", " + adventureData.place.stateName}
                />
                <AdventureActions
                    history={history}
                    expanded={expanded}
                    adventureId={adventureId}
                    setMessage={setMessage}
                    handleClick={handleClick}
                    handleExpandClick={() => handleExpandClick()}
                />

                <AdventureBasicInfo adventureData={adventureData} pricelistData={pricelistData} />

                <Collapse in={expanded} timeout="auto" unmountOnExit>
                    <Grid container spacing={2}>
                        <Grid item xs={12} sm={4}>
                            <AdventureAdditionalInfo
                                header="Rules of conduct"
                                additionalData={<RenderRulesOfConduct rulesOfConduct={adventureData.rulesOfConduct} />}
                            />
                        </Grid>
                        <Grid item xs={12} sm={4}>
                            <AdventureAdditionalInfo
                                header="Additional services"
                                additionalData={<RenderAdditionalServices additionalServices={pricelistData.additionalServices} />}
                            />
                        </Grid>
                        <Grid item xs={12} sm={4}>
                            <AdventureAdditionalInfo
                                header="Fishing equipment"
                                additionalData={<RenderFishingEquipment fishingEquipment={adventureData.fishingEquipment} />}
                            />
                        </Grid>
                    </Grid>
                </Collapse>
                <Snackbar open={open} autoHideDuration={6000} onClose={handleClose}>
                    <Alert onClose={handleClose} severity="error" sx={{ width: '100%' }}>
                        {message}
                    </Alert>
                </Snackbar>
            </Card>

        );
    }
}
