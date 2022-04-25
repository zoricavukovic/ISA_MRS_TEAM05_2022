import * as React from 'react';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import ReadMoreIcon from '@mui/icons-material/ReadMore';
import DeleteIcon from '@mui/icons-material/Delete';
import {useHistory} from "react-router-dom";
import {useEffect, useState} from "react";
import RatingEntity from "../Rating.js";
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import axios from "axios";
import Alert from '@mui/material/Alert';
import Snackbar from '@mui/material/Snackbar';
import TextField from '@mui/material/TextField';
import { getPricelistByEntityId } from '../../service/Pricelists.js';
import { deleteCottageById } from '../../service/CottageService.js';

export default function ImgMediaCard(props) {
    const history = useHistory();
    const [open, setOpen] = React.useState(false);
    const [openDialog, setOpenDialog] = React.useState(false);
    const [password, setPassword] = React.useState("");
  
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

    const handleClickOpen = (event) => {
      setOpenDialog(true);
    };
  
    const handleCloseDialog = () => {
      setOpenDialog(false);
    };

    const [pricelist, setPricelist] = useState([]);
    function showBookingEntity(){
      console.log(props.bookingEntityId);
        history.push({
            pathname: "/showCottageProfile",
            state: { bookingEntityId: props.bookingEntityId }
        })
    };
    useEffect(() => {
      getPricelistByEntityId(props.bookingEntityId).then(res => {
            setPricelist(res.data);
        }); 

    }, [])
    const logicDeleteBookingEntity = (event) => {
      deleteCottageById(props.bookingEntityId, password).then(res => {
        setPassword("");
        handleClick();
        setTypeAlert("success");
        setMessage("Successfully delete entity " + props.bookingEntity.name);
        window.location.reload();
        }).catch(res=>{
          setPassword("");
          setTypeAlert("error");
          setMessage(res.response.data);
          handleClick();
          return;
        })    
        setOpenDialog(false);
  }


  return (
    <Card  style={{margin:"2%"}} sx={{ maxWidth: 345 }}>
      <Dialog open={openDialog} onClose={handleCloseDialog}>
        <DialogTitle>Confirm Deleting</DialogTitle>
        <DialogContent>
          <DialogContentText>
            To delete this entity, please enter your password for security reasons.
          </DialogContentText>
          <TextField
            autoFocus
            margin="dense"
            value={password}
            onChange={e => {
              setPassword(e.target.value);
            }}
            id="name"
            label="Password"
            type="password"
            fullWidth
            variant="standard"
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Cancel</Button>
          <Button onClick={logicDeleteBookingEntity}>Confirm</Button>
        </DialogActions>
      </Dialog>
      <CardMedia
        component="img"
        alt="green iguana"
        height="140"
        image="/static/images/cards/contemplative-reptile.jpg"
      />
      <CardContent>
        <Typography style={{textAlign:"left"}} gutterBottom variant="h5" component="div">
          {props.bookingEntity.name}
        </Typography>
        <Typography style={{textAlign:"left"}} gutterBottom variant="h7" component="div">
        <text style={{backgroundColor:"aliceblue"}}>Location:</text> {props.bookingEntity.address}, {props.bookingEntity.place.cityName} {props.bookingEntity.place.zipCode}, {props.bookingEntity.place.stateName}
        </Typography>
        
        <Typography style={{textAlign:"left"}} gutterBottom variant="h7" component="div">
        {props.bookingEntity.entityType === "COTTAGE" ? (
          <div>
              <text style={{backgroundColor:"aliceblue"}}>Cost Per Night:</text> {pricelist.entityPricePerPerson} € 
          </div>
           
          ):(<div>
            <text style={{backgroundColor:"aliceblue"}}>Cost Per Person:</text> {pricelist.entityPricePerPerson} € 
          
          </div>)}
         </Typography>
        <Typography style={{textAlign:"left"}} gutterBottom variant="h7" component="div">
          <RatingEntity value="2"></RatingEntity>
        </Typography>
        <Typography style={{textAlign:"left"}} variant="body2" color="text.secondary">
            {props.bookingEntity.promoDescription}
        </Typography>
      </CardContent>
      <CardActions>
        <Button size="small" onClick={showBookingEntity}><ReadMoreIcon fontSize="large" style={{margin:"5px"}}/> Details</Button>
        
        <Button size="small" onClick={handleClickOpen}><DeleteIcon/>Delete</Button>
        <Snackbar open={open} autoHideDuration={6000} onClose={handleClose}>
        <Alert onClose={handleClose} severity={typeAlert} sx={{ width: '100%' }}>
          {message}
        </Alert>
        
        
      </Snackbar>
      </CardActions>
    </Card>
  );
}