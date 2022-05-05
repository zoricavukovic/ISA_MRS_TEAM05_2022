import * as React from 'react';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import {useHistory} from "react-router-dom";
import {useEffect, useState} from "react";
import { CircularProgress} from "@mui/material";
import Grid from '@mui/material/Grid';
import {CardActionArea} from "@mui/material";
import { getCottageById } from '../../service/CottageService';
import Chip from '@mui/material/Chip';
import Stack from '@mui/material/Stack';

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
          history.push({
              pathname: "/showCottageProfile",
              state: { bookingEntityId: bookingEntity.id }
          })
    };
    
    useEffect(() => {
        if (props === null || props === undefined){
            history.push('/login');
        }
        getCottageById(props.reservation.bookingEntity.id).then(res => {
            setBookingEntity(res.data);
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
        
        
    }, [])
    if (isLoading) { return <div className="App"><CircularProgress /></div> }
  return (

    <Grid button onClick={showBookingEntity} textAlign="left" style={{margin:"2%"}} sx={{ width: "100%"}}>
    <Grid item xs={12} md={6}>
            <Card sx={{display: 'flex'}}>
            <CardMedia
                    component="img"
                    sx={{width: 160, display: {xs: 'none', sm: 'block'}}}
                    image="https://images.unsplash.com/photo-1605281317010-fe5ffe798166?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1144&q=80"
                    alt="slika"
                />
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