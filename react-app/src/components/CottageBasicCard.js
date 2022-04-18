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
import RatingEntity from "./Rating.js";
import axios from "axios";

export default function ImgMediaCard(props) {
    const history = useHistory();
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
  return (
    <Card style={{margin:"2%"}} sx={{ maxWidth: 345 }}>
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
        <Button size="small"><DeleteIcon/>Delete</Button>
      </CardActions>
    </Card>
  );
}