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

    const handleClickOpen = () => {
      setOpenDialog(true);
    };
  
    const handleCloseDialog = () => {
      setOpenDialog(false);
    };

    const [pricelist, setPricelist] = useState([]);
    function showCottage(){
        history.push({
            pathname: "/showCottageProfile",
            state: { cottageId: props.cottage.id } //OVDE SE MENJA ID
        })
    };
    useEffect(() => {
        axios.get("http://localhost:8092/bookingApp/pricelists/" + props.bookingEntityId).then(res => {
            setPricelist(res.data);
        });
    }, [])
    const logicDeleteCottage = (event) => {
      
      
          
    }
  return (
    <Card style={{margin:"2%"}} sx={{ maxWidth: 345 }}>
      <Dialog open={openDialog} onClose={handleCloseDialog}>
        <DialogTitle>Confirm Deleting</DialogTitle>
        <DialogContent>
          <DialogContentText>
            To delete this cottage, please enter your password for security reasons.
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
          <Button onClick={logicDeleteCottage}>Confirm</Button>
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
          {props.cottage.name}
        </Typography>
        <Typography style={{textAlign:"left"}} gutterBottom variant="h7" component="div">
        <text style={{backgroundColor:"aliceblue"}}>Location:</text> {props.cottage.address}, {props.cottage.place.cityName} {props.cottage.place.zipCode}, {props.cottage.place.stateName}
        </Typography>
        <Typography style={{textAlign:"left"}} gutterBottom variant="h7" component="div">
          <text style={{backgroundColor:"aliceblue"}}>Cost Per Night:</text> {pricelist.entityPricePerPerson} €
        </Typography>
        <Typography style={{textAlign:"left"}} gutterBottom variant="h7" component="div">
          <RatingEntity value="2"></RatingEntity>
        </Typography>
        <Typography style={{textAlign:"left"}} variant="body2" color="text.secondary">
            {props.cottage.promoDescription}
        </Typography>
      </CardContent>
      <CardActions>
        <Button size="small" onClick={showCottage}><ReadMoreIcon fontSize="large" style={{margin:"5px"}}/> Details</Button>
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