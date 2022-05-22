import React, { useEffect, useState, useRef } from "react";
import Box from '@mui/material/Box';
import Container from '@mui/material/Container';
import InsightsIcon from '@mui/icons-material/Insights';
import TextField from '@mui/material/TextField';
import Chart from "react-apexcharts";
import { CircularProgress } from "@mui/material";
import { getAllBookingEntitiesByOwnerId } from '../../service/BookingEntityService';
import { getCurrentUser } from '../../service/AuthService.js';
import { getAnalysisByBookintEntityId } from '../../service/CalendarService';
import GradeIcon from '@mui/icons-material/Grade';
import Autocomplete from '@mui/material/Autocomplete';
import CalendarCard from '../calendar/CalendarCard'

export default function Reports() {
    const [loading, setLoading] = React.useState(true);
    const [loadingReservations, setLoadingReservations] = React.useState(true);
    const [value, setValue] = React.useState(new Date());
    const [entities, setEntities] = React.useState();
    const [state, setState] = React.useState(
      {
        options: {
          chart: {
            id: "basic-bar"
          },
          xaxis: {
            categories: [1991, 1992, 1993, 1994, 1995, 1996, 1997, 1998, 1999]
          }
        },
        series: [
          {
            name: "series-1",
            data: [30, 40, 45, 50, 49, 60, 70, 91]
          }
        ]
      }
    );
    const options = {
        responsive: true,
        chart: {
          type: 'area',
          stacked: false,
          height: 350,
          zoom: {
            type: 'x',
            enabled: true,
            autoScaleYaxis: true
          },
          toolbar: {
            autoSelected: 'zoom'
          },
        plugins: {
          legend: {
            position: 'top'
          }},
          title: {
            display: true,
            text: 'Chart.js Line Chart',
          },
        },
      };
      const labels = ['January', 'February', 'March', 'April', 'May', 'June', 'July'];

    const data = {
        labels,
        datasets: [
          {
            label: 'Dataset 1',
            data: labels,
            borderColor: 'rgb(255, 99, 132)',
            backgroundColor: 'rgba(255, 99, 132, 0.5)',
          },
          {
            label: 'Dataset 2',
            data: labels,
            borderColor: 'rgb(53, 162, 235)',
            backgroundColor: 'rgba(53, 162, 235, 0.5)',
          },
        ],
      };
      
    function DisplayAverageRate(props){
      return <div>
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
                <th>{props.entity.data.name}</th>
            </tr>
            <tr>
                <td style={{fontWeight: 'bold',  fontSize: '1.5em'}}><GradeIcon style={{marginTop:'2%'}}/> {Math.round(props.entity.data.averageRating*100)/100}</td>
            </tr>
        </table>
            
    
        </div>
             
    </div>
    }

    function DisplayChartReservations(props){
      console.log(props);
      getAnalysisByBookintEntityId(11).then( reservations => {
        console.log(reservations);
        // setReservationsBox(reservations);
        
      })
      return <div>OK</div>
      
    }


      const [selectedEntity, setSelectedEntity] = React.useState();
      const [optionsBox, setOptionsBox] = React.useState([]);
      const [reservationsBox, setReservationsBox] = React.useState([]);
  
    useEffect(() => {
      getAllBookingEntitiesByOwnerId(getCurrentUser().id)
        .then(res => {
            
            const sorted = [...res.data].sort((a, b) => b.averageRating - a.averageRating);
            
            let optBox = [];
            for (let opt of res.data){
              optBox.push({'label': opt.name, 'id': opt.name, 'data': opt});
              // getCalendarValuesByBookintEntityId(opt.id).then( reservations => {
              //   let resBox = reservationsBox;
                
              //   resBox.push({'label': opt.name, 'id': opt.name, 'data': reservations.data});
              //   setReservationsBox(resBox);
              // })
            }
            setEntities(sorted);
            setOptionsBox(optBox);
            setSelectedEntity(optBox[0]);
            setLoading(false);
            setLoadingReservations(false);
            
        });
    }, []);
  
    if (loading) return <><CircularProgress /></> 
    return (
        <div>
            <Container >
                <Box style={{marginTop:'2%', padding:'1%', borderRadius: '10px', display: "flex", flexWrap: 'wrap', flexDirection: "row"}} sx={{ bgcolor: 'aliceblue', width:'100%' }}>
                    <div style={{ color: 'rgb(5, 30, 52)', fontWeight: 'bold', textAlign: 'center', backgroundColor: 'rgb(244, 177, 77)', margin: '1%', marginLeft: '4%', padding: '1%', borderRadius: '10px', width: '15%', display: "flex", flexWrap: 'wrap', flexDirection: "row"}} >
                        <InsightsIcon/>
                        <div style={{marginTop:'1%', marginLeft:'5%'}}>Dashboard</div>
                        
                    </div>
                    <Autocomplete
                    disablePortal
                    id="selectedEntity"
                    size="small"
                    options={optionsBox}
                    sx={{ width: '250px' }}
                    defaultValue={selectedEntity}
                    style={{marginTop:'1%', marginLeft:'50%'}}
                    onChange={(event, newValue) => {
                      if (newValue != null && newValue != undefined && newValue != '') {
                        setSelectedEntity(newValue);

                    } else {
                      setSelectedEntity(null);
                    }
                    }}
                    renderInput={(params) => <TextField {...params} label="Select Entity" 
                                                  placeholder="Select entity"
                                                  
                                              />}
                    isOptionEqualToValue={(option, value) => option.label === value.label}
                    //{...register("place")}
                />
                </Box>
                <Box style={{ marginTop:'1%', marginLeft:'5%', padding:'1%', borderRadius: '10px'}}>
                    <div style={{ color: 'rgb(5, 30, 52)', fontWeight: 'bold', textAlign: 'center', backgroundColor: 'rgb(244, 177, 77)', float:'left', padding: '1%', borderRadius: '10px', width: '15%', display: "flex"}} >
                        <GradeIcon/>
                        <div style={{marginTop:'3%', marginLeft:'5%'}}>Avg. Rating</div>
                    </div>
                    
                      <DisplayAverageRate entity={selectedEntity}/>
                      <DisplayChartReservations entity = {11}/>
                      <Chart
                        options={state.options}
                        series={state.series}
                        type="area"
                        stacked= "false"
                        width="500"
                        zoom= {{
                          type: 'x',
                          enabled: true,
                          autoScaleYaxis: true
                        }}
                        toolbar={ {
                          autoSelected: 'zoom'
                        }}
                      />
                </Box>
                
            </Container>
            
        </div>

    );
}