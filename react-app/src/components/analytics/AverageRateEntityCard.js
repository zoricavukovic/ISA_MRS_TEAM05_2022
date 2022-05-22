import React, { useEffect, useState, useRef } from "react";
import Box from '@mui/material/Box';
import Container from '@mui/material/Container';
import InsightsIcon from '@mui/icons-material/Insights';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import TextField from '@mui/material/TextField';
import {Line, Bar} from 'react-chartjs-2';
import Chart from "react-apexcharts";
import Carousel from 'react-elastic-carousel';
import { CircularProgress } from "@mui/material";
import GradeIcon from '@mui/icons-material/Grade';


export default function AverageRateEntityCard(props) {
    
    const [loading, setLoading] = React.useState(true);
    const [info, setInfo] = React.useState();

    useEffect(() => {
        setInfo(props.info);
        console.log("pla");
        console.log(props);
        console.log(props.info);
        setLoading(false);
      }, [props.info]);
    
    if (loading) return <><CircularProgress /></> 
    return (
        <div>
            
        <div style={{   width:'auto',
                        backgroundColor: 'aliceblue',
                        color: 'rgb(5, 30, 52)',
                        padding:'1%',
                        marginTop: '5%',
                        marginRight:'82%',
                        border: '1px solid rgb(244, 177, 77)',
                        borderRadius: '10px'
                    }} 
        >
        <table>
            <tr>
                <th>{info.name}</th>
            </tr>
            <tr>
                <td style={{fontWeight: 'bold',  fontSize: '1.5em'}}><GradeIcon style={{marginTop:'2%'}}/> {Math.round(info.averageRating*100)/100}</td>
            </tr>
        </table>
            
    
        </div>
             
    </div>

    );
}