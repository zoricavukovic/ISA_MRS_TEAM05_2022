import * as React from 'react';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import ReadMoreIcon from '@mui/icons-material/ReadMore';
import {useHistory} from "react-router-dom";
import {useEffect, useState} from "react";
import Alert from '@mui/material/Alert';
import Snackbar from '@mui/material/Snackbar';
import Divider from '@mui/material/Divider';
import { CircularProgress} from "@mui/material";
import { getPricelistByEntityId } from '../../service/Pricelists';
import { getAdditionalServicesByReservationId } from '../../service/AdditionalService';

export default function ImgReservation(props) {
    const history = useHistory();
    const [open, setOpen] = React.useState(false);
    const [isLoading, setLoading] = React.useState(true);
    const [isLoadingAddServices, setLoadingAddServices] = React.useState(true);
    const [reservationCost, setReservationCost] = React.useState(0);
    const [additionalServices, setAdditionalServices] = React.useState({});
    const [pricelist, setPricelist] = useState([]);
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

    function showReservation(){
        history.push({
            pathname: "/showReservationDetails",
            state: 
            { 
              reservation: props.reservation,
              additionalServices: additionalServices,
              pricelist: pricelist
            }
        })
    };
    
    useEffect(() => {
      console.log("Jedan ");
      console.log(props.reservation);
        let end = new Date(props.reservation.startDate[0], props.reservation.startDate[1]-1, props.reservation.startDate[2],props.reservation.startDate[3],props.reservation.startDate[4]);
        end.setDate(end.getDate() + props.reservation.numOfDays);
        setDates({
            startDate:new Date(props.reservation.startDate[0], props.reservation.startDate[1]-1, props.reservation.startDate[2],props.reservation.startDate[3],props.reservation.startDate[4]),
            endDate:end
        });
        getPricelistByEntityId(props.reservation.bookingEntity.id).then(res => {
            setPricelist(res.data);
            getAdditionalServicesByReservationId(props.reservation.id).then(addServices => {
            setAdditionalServices(addServices.data);
            let updatedResCost = res.data.entityPricePerPerson*props.reservation.numOfDays*props.reservation.numOfPersons;
            if (addServices.data.length>0){
              for (let addService of addServices.data){
                updatedResCost += addService.price;
              }
            }
            setReservationCost(updatedResCost);
            setLoadingAddServices(false);
            setLoading(false);
        });
            
        });
        
    }, [props.reservation])
    if (isLoading || isLoadingAddServices) { return <div className="App"><CircularProgress /></div> }
  return (
    <Card button onClick={showReservation} style={{margin:"2%"}} sx={{ maxWidth: 400, minWidth:250}}>
      <CardMedia
        component="img"
        alt="green iguana"
        height="10"
        image="/static/images/cards/contemplative-reptile.jpg"
      />
      <CardContent>
         
        <Typography style={{textAlign:"left", color: 'rgb(5, 30, 52)'}} gutterBottom variant="h6" component="div">
          Reservation Details
        </Typography>
        <table style={{textAlign:"left"}}>
            <tr>
                <th style={{color: 'rgb(5, 30, 52)', backgroundColor:"aliceblue", padding:"3%", fontWeight:"normal"}}>Check-in</th>
                <th style={{color: 'rgb(5, 30, 52)', backgroundColor:"aliceblue", padding:"3%", fontWeight:"normal"}}>Check-out</th>
            </tr>
            <tr>
                <td variant="h6" style={{color: 'rgb(5, 30, 52)', borderRight: "solid 2px aliceblue", paddingBottom:"2%", fontWeight:"bold"}}>{
                new Intl.DateTimeFormat("en-GB", {
                    year: "numeric",
                    month: "long",
                    day: "2-digit"
                  }).format(dates.startDate)
                  }
                </td>
                <td variant="h6" style={{color: 'rgb(5, 30, 52)', paddingBottom:"2%", fontWeight:"bold"}}>{
                new Intl.DateTimeFormat("en-GB", {
                    year: "numeric",
                    month: "long",
                    day: "2-digit"
                  }).format(dates.endDate)
                  }
                </td>
            </tr>
            <tr>
                <td style={{color: 'rgb(5, 30, 52)', borderRight: "solid 2px aliceblue", fontWeight:"lighter"}}>{
                    new Intl.DateTimeFormat('en-US', { hour: '2-digit', minute: '2-digit'}).format(dates.startDate)
                
                  }
                </td>
                <td style={{color: 'rgb(5, 30, 52)', padding:"0%", fontWeight:"lighter"}}>{
                new Intl.DateTimeFormat('en-US', { hour: '2-digit', minute: '2-digit'}).format(dates.endDate)
                
                  }
                </td>
            </tr>
        </table>
        <br/>
        <table style={{color: 'rgb(5, 30, 52)', textAlign:"left", whiteSpace: "nowrap"}}>
            <tr>
                <th style={{fontWeight:"normal"}}>Total length of stay:</th>
            </tr>
            <tr>
            <td style={{paddingBottom:"2%", fontWeight:"bold"}}>{props.reservation.numOfDays} nights</td>
            </tr>
        </table>
        <Divider style={{margin:"2%"}}></Divider>
       <table style={{color: 'rgb(5, 30, 52)', textAlign:"left", whiteSpace: "nowrap"}}>
            <tr>
                <th style={{fontWeight:"normal"}}>Total num of persons:</th>
            </tr>
            <tr>
            <td style={{paddingBottom:"2%", fontWeight:"bold"}}>{props.reservation.numOfPersons}</td>
            </tr>
        </table>
       <Divider style={{margin:"2%"}}></Divider>
       <table style={{color: 'rgb(5, 30, 52)', textAlign:"left", whiteSpace: "nowrap"}}>
            <tr>
                <th>Selected:</th>
            </tr>
            <tr>
            <td style={{paddingBottom:"2%", fontWeight:"normal"}}>{props.reservation.bookingEntity.name}</td>
            </tr>
        </table>
        
        <Card style={{color: 'rgb(5, 30, 52)', backgroundColor:"aliceblue", margin:"2%"}}>
            <table style={{whiteSpace: "nowrap"}}>
                <tr>
                    <th><h4>{props.reservation.bookingEntity.name}</h4></th>
                    <th style={{paddingLeft:"3%"}}>€ {reservationCost}</th>
                </tr>
                
            </table>
        </Card>
        
      </CardContent>
      <CardActions>
      {props.details === "true" ? (
          <div>
              <Button size="small" onClick={showReservation}><ReadMoreIcon fontSize="large" style={{margin:"5px"}}/> Details</Button>
          </div>
           
          ):(<div>
            {additionalServices.length === 0 ? (
                <Card style={{color: 'rgb(5, 30, 52)', minWidth:"200px", backgroundColor:"aliceblue", margin:"2%"}}>
                  No additional services
                </Card>
            ):(
            <Card style={{color: 'rgb(5, 30, 52)', backgroundColor:"aliceblue", margin:"2%"}}>
              <table style={{whiteSpace: "nowrap", minWidth:"200px", textAlign:"left"}}>
                 
                  <tr>
                      <th><h4>Additional services</h4></th>
                  </tr>
                  
                    {additionalServices.map(service=> {
                      if (additionalServices.length == 0){
                        return <tr style={{textAlign:"left", paddingLeft:"1%"}}>
                          <td>No additional services</td>
                          </tr>
                      }else{
                        return <tr style={{textAlign:"left", paddingLeft:"1%"}}>
                        <td>{service.serviceName} - € {service.price}</td>
                        </tr>
                      }
                        
                    })}
                  
              </table>
            </Card>)}
          </div>)}
       
        <Snackbar open={open} autoHideDuration={6000} onClose={handleClose}>
        <Alert onClose={handleClose} severity={typeAlert} sx={{ width: '100%' }}>
          {message}
        </Alert>
        
        
      </Snackbar>
      </CardActions>
    </Card>
  );
}