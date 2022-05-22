import * as React from 'react';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Typography from '@mui/material/Typography';
import {useHistory} from "react-router-dom";
import {useEffect, useState} from "react";
import { CircularProgress} from "@mui/material";
import { URL_PICTURE_PATH } from "../../service/PictureService";
import Grid from '@mui/material/Grid';
import { getCottageByIdCanBeDeleted } from '../../service/CottageService';
import { getShipByIdCanBeDeleted } from '../../service/ShipService';
import { getAdventureByIdCanBeDeleted } from '../../service/AdventureService';
import Chip from '@mui/material/Chip';
import Stack from '@mui/material/Stack';
import { getCurrentUser } from '../../service/AuthService.js';

export default function ReservedBookingEntityDetail(props) {
    
    const history = useHistory();
    const [open, setOpen] = React.useState(false);
    const [isLoading, setLoading] = React.useState(true);
    const [rulesLabel, setRulesLabel] = React.useState([]);
    const [isLoadingAddServices, setLoadingAddServices] = React.useState(true);
    const [reservationCost, setReservationCost] = React.useState(0);
    const [additionalServices, setAdditionalServices] = React.useState({});
    const [pricelist, setPricelist] = useState([]);
    const [bookingEntity, setBookingEntity] = useState({});
    const [dates, setDates] = React.useState({
        startDate:null,
        endDate:null
    });
  
    const [message, setMessage] = React.useState("");
    const [typeAlert, setTypeAlert] = React.useState("");
    const handleClick = () => {
      setOpen(true);
    };
  
    const handleClose = (event, reason) => {
      if (reason === 'clickaway') {
        return;
      }
  
      setOpen(false);
    };


    function showBookingEntity(){
          if (getCurrentUser().userType.name === "ROLE_COTTAGE_OWNER"){
            history.push({
                pathname: "/showCottageProfile",
                state: { bookingEntityId: bookingEntity.id }
            })
          }
          else if (getCurrentUser().userType.name === "ROLE_SHIP_OWNER"){
            history.push({
                pathname: "/showShipProfile",
                state: { bookingEntityId: bookingEntity.id }
            })
          }
          else if (getCurrentUser().userType.name === "ROLE_INSTRUCTOR"){
            history.push({
                pathname: "/showAdventureProfile",
                state: { bookingEntityId: bookingEntity.id }
            })
          }
         
    };
    
    useEffect(() => {
        if (props === null || props === undefined){
            history.push('/forbiddenPage');
        }
        if (getCurrentUser().userType.name === "ROLE_COTTAGE_OWNER"){
            getCottageByIdCanBeDeleted(props.reservation.bookingEntity.id).then(res => {
                setBookingEntity(res.data);
                console.log(res.data);
                let labelRules = [];
                for (let rule of res.data.rulesOfConduct){
                    let label = "";
                    if (rule.allowed === true){
                        label += "Allowed " + rule.ruleName;
                    }
                    else{
                        label += "Unallowed " + rule.ruleName;
                    }
                    labelRules.push(label);
                }
                setRulesLabel(labelRules);
                setLoading(false);
            });
          }
          else if (getCurrentUser().userType.name === "ROLE_SHIP_OWNER"){
            getShipByIdCanBeDeleted(props.reservation.bookingEntity.id).then(res => {
                setBookingEntity(res.data);
                console.log(res.data);
                let labelRules = [];
                for (let rule of res.data.rulesOfConduct){
                    let label = "";
                    if (rule.allowed === true){
                        label += "Allowed " + rule.ruleName;
                    }
                    else{
                        label += "Unallowed " + rule.ruleName;
                    }
                    labelRules.push(label);
                }
                setRulesLabel(labelRules);
                setLoading(false);
            });
          }
          else if (getCurrentUser().userType.name === "ROLE_INSTRUCTOR"){
            getAdventureByIdCanBeDeleted(props.reservation.bookingEntity.id).then(res => {
                setBookingEntity(res.data);
                console.log(res.data);
                let labelRules = [];
                for (let rule of res.data.rulesOfConduct){
                    let label = "";
                    if (rule.allowed === true){
                        label += "Allowed " + rule.ruleName;
                    }
                    else{
                        label += "Unallowed " + rule.ruleName;
                    }
                    labelRules.push(label);
                }
                setRulesLabel(labelRules);
                setLoading(false);
            });
          }
        
    }, [])
    if (isLoading) { return <div className="App"><CircularProgress /></div> }
  return (

    <Grid button onClick={showBookingEntity} textAlign="left" sx={{ width: "100%"}}>
    <Grid item xs={12} md={6}>
            <Card sx={{display: 'flex'}}>
            {bookingEntity.pictures.length === 0 ? (
                <CardMedia
                    component="img"
                    height="140"
                    alt="No Images"
                />
            ) : (
                <CardMedia
                    component="img"
                    height="140"
                    alt="No Images"
                    image={URL_PICTURE_PATH + bookingEntity.pictures[0].picturePath}
                >
                </CardMedia>
            )}
            
                <CardContent sx={{flex: 1}}>
                    <Typography component="h2" variant="h4" style={{color: 'rgb(5, 30, 52)'}}>
                        {bookingEntity.name}
                    </Typography>
                    <Typography variant="subtitle1" color="text.secondary" style={{color: 'rgb(5, 30, 52)'}}>
                        {bookingEntity.address}, {bookingEntity.place.zipCode} {bookingEntity.place.cityName}, {bookingEntity.place.stateName}           
                    </Typography>
                    <Stack direction="row" spacing={0.2} sx={{gridTemplateColumns: 'repeat(3, 1fr)', mb: 2,
                                pb: 1,
                                display: 'grid',
                                gap: 1}}>
                    {rulesLabel.map(rule=> {
                        return <Chip style={{color: 'rgb(5, 30, 52)'}} size="small" label={rule}/>
                    })}
                    </Stack>
                    

                    <Typography button onClick={showBookingEntity} variant="subtitle1" style={{color: 'rgb(5, 30, 52)'}}>
                        More details...
                    </Typography>
                </CardContent>
                
            </Card>
    </Grid>
</Grid>

  );
}