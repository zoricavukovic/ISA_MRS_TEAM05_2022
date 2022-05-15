import * as React from 'react';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import ReadMoreIcon from '@mui/icons-material/ReadMore';
import {useHistory} from "react-router-dom";
import {useEffect, useState} from "react";
import Alert from '@mui/material/Alert';
import Snackbar from '@mui/material/Snackbar';
import Divider from '@mui/material/Divider';
import { CircularProgress} from "@mui/material";
import { getPricelistByEntityId } from '../../service/PricelistService';
import { getAdditionalServicesByReservationId } from '../../service/AdditionalService';
import { addReport, isReportedResByReservationId } from '../../service/ReportService';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import TextareaAutosize from '@mui/material/TextareaAutosize';
import Checkbox from '@mui/material/Checkbox';

export default function ImgReservation(props) {
    const history = useHistory();
    const [open, setOpen] = React.useState(false);
    const [isLoading, setLoading] = React.useState(true);
    const [isLoadingAddServices, setLoadingAddServices] = React.useState(true);
    const [isLoadingReport, setLoadingReport] = React.useState(true);
    const [reservationCost, setReservationCost] = React.useState(0);
    const [additionalServices, setAdditionalServices] = React.useState({});
    const [pricelist, setPricelist] = useState([]);
    const [reported, setReported] = useState();

    ///////////////////////////////////////////////////////////////////
    const [checkedCome, setCheckedCome] = React.useState(false);
    const [checkedReward, setCheckedReward] = React.useState(false);
    const [reason, setReason] = React.useState("");
    ///////////////////////////////////////////////////////////////////

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

    const handleChangeCome = (event) => {
      setCheckedCome(event.target.checked);
    };
    const handleChangeReward = (event) => {
      setCheckedReward(event.target.checked);
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
    function createReport(){
      setOpen(true);
  };

    function handleCreateReport(){
        let report = {
          "clientCome": checkedCome,
          "comment": reason,
          "reward": checkedReward,
          "reservationId": props.reservation.id
        }
        addReport(report).then(result => {
          setTypeAlert("success");
          setMessage("Successfully send report");
          handleClick();
          setTimeout(() => {
            
          }, 2000);
          window.location.reload(false);
        }).catch(resError => {
          setTypeAlert("error");
          setMessage(resError.response.data);
          handleClick();
          return;
        })

    };
    
    useEffect(() => {
        console.log(props.reservation);
        let end = new Date(props.reservation.startDate);
        end.setDate(end.getDate() + props.reservation.numOfDays);
        setDates({
            startDate: new Date(props.reservation.startDate),
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
              isReportedResByReservationId(props.reservation.id).then(reported => {
                setReported(reported.data);
                setLoadingAddServices(false);
                setLoadingReport(false);
                setLoading(false);
              });
             
              
        });
            
        });
        
    }, [props.reservation])
    if (isLoading || isLoadingAddServices || isLoadingReport) { return <div className="App"><CircularProgress /></div> }
  return (
    <Card  style={{marginRight:"2%", marginLeft:"2%", marginBottom:"2%"}} sx={{ maxWidth: 400, minWidth:250}}>
      <CardContent>
      <Dialog open={open} onClose={handleClose} sx={{minWidth:500}}>
      <DialogTitle>Create Report</DialogTitle>
      <DialogContent>
      <Card style={{color: 'rgb(5, 30, 52)', backgroundColor:"aliceblue", margin:"2%"}}>
            <table style={{whiteSpace: "nowrap"}}>
                <tr>
                    <th><h4>{props.reservation.bookingEntity.name}</h4></th>
                    <th style={{paddingLeft:"3%"}}>€ {reservationCost} / {props.reservation.numOfDays} nights</th>
                </tr>
                
            </table>
      </Card>
      <table style={{textAlign:"left"}} style={{marginLeft:"18%"}}>

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
        
       <Divider style={{margin:"2%"}}></Divider>
    
        
        <Typography variant="body2" style={{backgroundColor: 'rgb(252, 234, 207)', borderRadius: '10px', paddingLeft: '1%', paddingBottom: '0.1%', paddingTop: '0.1%', margin: '1%', minWidth:'350px'}}>
            <h3 style={{color:'rgb(5, 30, 52)'}}>Please create reservation report</h3>
            <h4 style={{color:'rgb(5, 30, 52)'}}>Did the clients come?
            <Checkbox
              checked={checkedCome}
              onChange={handleChangeCome}
              inputProps={{ 'aria-label': 'controlled' }}
            />
            </h4>
            <h4 style={{color:'rgb(5, 30, 52)'}}>Notes for administrator to see
            </h4>
            <TextareaAutosize
              aria-label="minimum height"
              minRows={3}
              value = {reason}
              name="reason"
              onChange={e => {
                setReason(e.target.value);
              }}
              style={{ width: 200 }}
            />
            <h4 style={{color:'rgb(5, 30, 52)'}}>Reward for clients
            <Checkbox
              checked={checkedReward}
              onChange={handleChangeReward}
              inputProps={{ 'aria-label': 'controlled' }}
            />
            </h4>
            
        </Typography>


      </DialogContent>

      <DialogActions>
        <Button onClick={handleClose}>Cancel</Button>
        <Button onClick={handleCreateReport}>Save</Button>
      </DialogActions>
    </Dialog>
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
              {console.log((new Date(Date.now())).getFullYear())}
              {reported === true || dates.endDate.getFullYear() > new Date(Date.now()).getFullYear() || (dates.endDate.getFullYear() == new Date(Date.now()).getFullYear() && (dates.endDate.getMonth()+1) > (new Date(Date.now()).getMonth()+1) || (dates.endDate.getFullYear() == new Date(Date.now()).getFullYear() && (dates.endDate.getMonth()+1) == (new Date(Date.now()).getMonth()+1) && dates.endDate.getUTCDate() > new Date(Date.now()).getUTCDate())) ? (<div>

              </div>):(
                <Button size="small" onClick={createReport}><ReadMoreIcon fontSize="large" style={{margin:"5px"}}/> Report</Button>
              )}
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
                        <td>{service.serviceName} /<b>{service.price} €</b></td>
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