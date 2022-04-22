import React, { useEffect, useState } from "react";
import axios from "axios";
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
        <CardContent>
            <Typography variant="body2" color="text.secondary" style={{ backgroundColor: 'aliceblue', borderRadius: '5px', paddingLeft: '1%', paddingTop: '0.2%', paddingBottom: '0.1%' }}>
                <h4>Promo Description: </h4><h3>{props.adventureData.promoDescription} </h3>
            </Typography>
            <Typography variant="body2" color="text.secondary" style={{ backgroundColor: 'aliceblue', borderRadius: '5px', paddingLeft: '1%', paddingTop: '0.2%', paddingBottom: '0.1%' }}>
                <h4>Short Bio: </h4><h3>{props.adventureData.shortBio} </h3>
            </Typography>
            <Typography variant="body2" color="text.secondary" style={{ width: 'fit', backgroundColor: 'rgb(252, 234, 207)', borderRadius: '5px', paddingLeft: '1%', paddingBottom: '0.2%', paddingTop: '0.2%' }}>
                <h4>Cost Per Night: {props.adventureData.entityPricePerPerson} € </h4>
                <RatingEntity value='3' />
            </Typography>
        </CardContent>
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
    const history = useHistory();

    const editAdventure = (event) => {
        event.preventDefault();
        history.push("./editAdventure");
    };

    return (
        <CardActions disableSpacing>
            <IconButton >
                <CalendarMonthIcon />
            </IconButton>
            <IconButton value="module" aria-label="module" onClick={editAdventure}>
                <EditIcon />
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

    const adventureId = props.history.location.state.adventureId;
    const urlAdventurePath = "http://localhost:8092/bookingApp/adventures/" + adventureId;
    const urlPricelistPath = "http://localhost:8092/bookingApp/pricelists/" + adventureId;
    const urlPicturePath = "http://localhost:8092/bookingApp/pictures/";

    const handleExpandClick = () => {
        setExpanded(!expanded);
    };

    useEffect(() => {
        axios.get(urlAdventurePath).then(res => {
            setAdventureData(res.data);
            setLoading(false);
        })
        axios.get(urlPricelistPath).then(result => {
            setPricelistData(result.data);
            setLoadingPricelist(false);
        })
    }, [])

    if (isLoading || isLoadingPricelist) {
        return <div className="App">Loading...</div>
    }
    else {
        return (
            <Card sx={{}}>
                <ImageSlider slides={adventureData.pictures.map((im) => ({'image':  urlPicturePath + im.picturePath}))} />
                <br />
                <CardHeader
                    title={adventureData.name}
                    subheader={adventureData.address + ", " + adventureData.place.cityName + ", " + adventureData.place.zipCode + ", " + adventureData.place.stateName}
                />
                <AdventureActions
                    expanded={expanded}
                    handleExpandClick={() => handleExpandClick()}
                />

                <AdventureBasicInfo adventureData={adventureData} />

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
            </Card>

        );
    }
}
