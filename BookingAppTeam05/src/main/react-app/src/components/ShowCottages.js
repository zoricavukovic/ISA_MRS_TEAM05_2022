import * as React from 'react';
import Grid from '@mui/material/Grid';
import {CardActionArea} from "@mui/material";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import Typography from "@mui/material/Typography";
import CardMedia from "@mui/material/CardMedia";
import {useEffect, useState} from "react";
import axios from "axios";

function ShowCottages() {

    const [cottages,setCottages] = useState([]);

    useEffect(() => {
        axios.get("http://localhost:8092/bookingApp/cottages/").then(res => {
            setCottages(res.data);
            console.log(res.data);
        })
    }, []);

    return (
        <div className="App">
            {cottages.map(cottage=> {
                return <Grid container spacing={4} marginLeft={50} marginRight={50} marginTop={2} textAlign="left">
                        <Grid item xs={12} md={6}>
                            <CardActionArea component="a" href="#">
                                <Card sx={{display: 'flex'}}>
                                    <CardContent sx={{flex: 1}}>
                                        <Typography component="h2" variant="h5">
                                            {cottage.name}
                                        </Typography>
                                        <Typography variant="subtitle1" color="text.secondary">
                                            {cottage.address} {cottage.place.cityName} {cottage.place.stateName}
                                        </Typography>
                                        <Typography variant="subtitle1" paragraph>
                                            {cottage.promoDescription}
                                        </Typography>
                                        <Typography variant="subtitle1" color="primary">
                                            Continue reading...
                                        </Typography>
                                    </CardContent>
                                    <CardMedia
                                        component="img"
                                        sx={{width: 160, display: {xs: 'none', sm: 'block'}}}
                                        image="https://imagesvc.meredithcorp.io/v3/mm/image?url=https%3A%2F%2Fstatic.onecms.io%2Fwp-content%2Fuploads%2Fsites%2F24%2F2019%2F01%2F2270901_cainc0259_1-2000.jpg"
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

export default ShowCottages;
