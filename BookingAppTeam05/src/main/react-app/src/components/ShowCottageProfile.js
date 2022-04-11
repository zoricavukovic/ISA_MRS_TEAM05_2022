import React, {Component} from 'react';
import ImageCard from "./ImageCard";
import { useEffect, useState } from "react";

import Box from '@mui/material/Box';

import CardHeader from '@mui/material/CardHeader';
import CardMedia from '@mui/material/CardMedia';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import CardActions from '@mui/material/CardActions';
import Collapse from '@mui/material/Collapse';
import Avatar from '@mui/material/Avatar';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';



function ShowCottageProfile() {

    return (
        <div>
            <ImageCard/>
            
    
        </div>
    );
}

export default ShowCottageProfile;