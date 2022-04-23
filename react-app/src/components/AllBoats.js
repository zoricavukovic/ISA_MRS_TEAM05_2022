import * as React from 'react';
import Grid from '@mui/material/Grid';
import {CardActionArea} from "@mui/material";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import Typography from "@mui/material/Typography";
import CardMedia from "@mui/material/CardMedia";
import {useEffect, useState} from "react";
import axios from "axios";
import { getAllShips } from '../service/ShipService';

function AllBoats() {

    const [boats,setBoats] = useState([]);

    useEffect(() => {
        getAllShips().then(res => {
            setBoats(res.data);
            console.log(res.data);
        })
    }, []);

    return (
        <div className="App">
            {boats.map(boat=> {
                return <Grid container spacing={4} marginLeft={50} marginRight={50} marginTop={2} textAlign="left">
                    <Grid item xs={12} md={6}>
                        <CardActionArea component="a" href="#">
                            <Card sx={{display: 'flex'}}>
                                <CardContent sx={{flex: 1}}>
                                    <Typography component="h2" variant="h5">
                                        {boat.name}
                                    </Typography>
                                    <Typography variant="subtitle1" color="text.secondary">
                                        {boat.address} {boat.place.cityName} {boat.place.stateName}
                                    </Typography>
                                    <Typography variant="subtitle1" paragraph>
                                        {boat.promoDescription}
                                    </Typography>
                                    <Typography variant="subtitle1" color="primary">
                                        Continue reading...
                                    </Typography>
                                </CardContent>
                                <CardMedia
                                    component="img"
                                    sx={{width: 160, display: {xs: 'none', sm: 'block'}}}
                                    image="https://images.unsplash.com/photo-1605281317010-fe5ffe798166?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1144&q=80"
                                    alt="slika"
                                />
                            </Card>
                        </CardActionArea>
                    </Grid>
                </Grid>

            })}
        </div>

    );
}

export default AllBoats;
