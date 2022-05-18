import React, { useEffect, useState, useRef } from "react";
import Box from '@mui/material/Box';
import Container from '@mui/material/Container';
import InsightsIcon from '@mui/icons-material/Insights';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import TextField from '@mui/material/TextField';
import {Line} from 'react-chartjs-2';

export default function Reports() {
    const [value, setValue] = React.useState(new Date());
    const options = {
        responsive: true,
        plugins: {
          legend: {
            position: 'top'
          },
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
    return (
        <div>
            <Container >
                <Box style={{marginTop:'5%', padding:'1%', borderRadius: '10px'}} sx={{ bgcolor: 'aliceblue', width:'100%' }}>
                    <div style={{ color: 'rgb(5, 30, 52)', fontWeight: 'bold', textAlign: 'center', backgroundColor: 'rgb(244, 177, 77)', margin: '2%', marginLeft: '4%', padding: '1%', borderRadius: '10px', width: '15%', display: "flex", flexWrap: 'wrap', flexDirection: "row"}} >
                        <InsightsIcon/>
                        <div style={{marginTop:'3%', marginLeft:'5%'}}>Dashboard</div>
                    </div>
                </Box>
                <Box style={{padding:'1%', borderRadius: '10px'}} sx={{ bgcolor: 'aliceblue', height:'100%',float:'right' }}>
                <LocalizationProvider dateAdapter={AdapterDateFns}>
                    <DatePicker
                      label="Date Picker"
                      value={value}
                      onChange={(newValue) => {
                        setValue(newValue);
                      }}
                      minDate={Date.now()}
                      renderInput={(params) => <TextField {...params} />}
                    />
                </LocalizationProvider>

                </Box>
                <Line options={options} data={data} />

            </Container>
        </div>

    );
}