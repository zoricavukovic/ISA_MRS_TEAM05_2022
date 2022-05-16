import React, { useEffect, useState } from "react";
import Paper from '@mui/material/Paper';
import { indigo, teal, red } from '@mui/material/colors';
import Box from "@mui/material/Box";
import Button from '@mui/material/Button';
import Radio from '@mui/material/Radio';
import RadioGroup from '@mui/material/RadioGroup';
import FormControlLabel from '@mui/material/FormControlLabel';
import FormControl from '@mui/material/FormControl';
import FormLabel from '@mui/material/FormLabel';
import { Grid, Typography } from "@mui/material";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css"
import { getCalendarValuesByBookintEntityId } from "../../service/CalendarService";
import { DateTimePicker } from "@mui/x-date-pickers";
import { DialogContent, DialogTitle, Divider } from "@mui/material";
import { Dialog } from "@mui/material";
import { useForm } from "react-hook-form";
import styles from './datePickerStyle.module.css';

import { ViewState, EditingState, IntegratedEditing } from '@devexpress/dx-react-scheduler';
import {
    Scheduler,
    MonthView,
    Appointments,
    Toolbar,
    DateNavigator,
    TodayButton,
    Resources,
    AppointmentTooltip,
    CurrentTimeIndicator,
} from '@devexpress/dx-react-scheduler-material-ui';

const resources = [{
    fieldName: 'type',
    title: 'type',
    instances: [
        { id: 'fast reservation', text: 'fast reservation', color: indigo },
        { id: 'regular reservation', text: 'regular reservation', color: teal },
        { id: 'unavailable', text: 'unavailable', color: red },
    ]
}];


export default function CalendarForEntity() {


    const { register, handleSubmit, watch, reset, formState: { errors } } = useForm();

    const [data, setData] = useState();
    const [isLoadingData, setIsLoadingData] = useState(true);
    const [openDialog, setOpenDialog] = useState(false);
    const [startDatePicker, setStartDatePicker] = useState(new Date());
    const [endDatePicker, setEndDatePicker] = useState(new Date());
    const [radioBtnTimeValue, setRadioBtnTimeValue] = React.useState('9:00:00');

    const [hiddenStartDateError, setHiddenStartDateError] = useState("none");
    const [hiddenEndDateError, setHiddenEndDateError] = useState("none");

    const handleOpenDialog = () => {
        setOpenDialog(true);
    }
    const handleCloseDialog = () => {
        setOpenDialog(false);
    }


    const checkStartDateSelected = () => {
        if (startDatePicker !== null &&  startDatePicker !== undefined && startDatePicker !== '') {
            setHiddenStartDateError("none");
            return true;
        } else {
            setHiddenStartDateError("block");
            return false;
        }
    }
    const checkEndDateSelected = () => {
        if (endDatePicker !== null &&  endDatePicker !== undefined && endDatePicker !== '') {
            setHiddenEndDateError("none");
            return true;
        } else {
            setHiddenEndDateError("block");
            return false;
        }
    }

    const onAddUnavailablePeriodSubmit = (data) => {
        if (!checkStartDateSelected() || !checkEndDateSelected())
            return;
        
        console.log(startDatePicker.toLocaleDateString());
        console.log(endDatePicker.toLocaleDateString());
        
    }

    const onStartDateChangeDatePicker = (date) => {
        setStartDatePicker(date);
    }
    const onEndDateChangeDatePicker = (date) => {
        setEndDatePicker(date);
    }

    useEffect(() => {
        getCalendarValuesByBookintEntityId(11)
            .then(res => {
                setData(res.data);
                setIsLoadingData(false);
            });
    }, []);




    if (isLoadingData) {
        return <div className="App">Loading...</div>
    }
    else {
        return (

            <Box style={{ margin: "1% 9% 1% 15%" }}>
                <Button variant="contained" onClick={handleOpenDialog} style={{ color: 'rgb(5, 30, 52)', fontSize: '10px', fontWeight: 'bold', textAlign: 'center', backgroundColor: 'rgb(244, 177, 77)', marginLeft: '33.5%', marginTop: '1%', padding: '1%', borderRadius: '10px', width: '15%' }}>
                    Add unavailable period
                </Button>
                <Dialog
                    open={openDialog}
                    onClose={handleCloseDialog}
                    style={{ margin: '1% auto 1% auto', padding: '1%', width: '100%', borderRadius: '10px' }}
                    fullWidth
                    maxWidth="xs"
                >
                    <DialogTitle>Add unavailable period</DialogTitle>
                    <Divider />
                    <br />
                    <DialogContent
                        style={{ height: '400px' }}
                    >
                        <Box
                            component="form"
                            noValidate
                            onSubmit={handleSubmit(onAddUnavailablePeriodSubmit)}
                            style={{ width: '100%' }}

                        >

                            <Grid
                                direction="column"
                                alignItems="center"
                                justifyContent="center"
                                container
                                spacing={2}
                            >
                                <label className={styles.labelNames} for="startDatePicker" >Select start date:</label>
                                <Grid item xs={12} sm={12}>
                                    <DatePicker 
                                        wrapperClassName={styles.datePicker} 
                                        id="startDatePicker"
                                        selected={startDatePicker}
                                        onChange={onStartDateChangeDatePicker}
                                        minDate={new Date()}
                                        dateFormat='yyyy-MM-dd'
                                    />
                                </Grid>
                                <p style={{ color: '#ED6663', fontSize: "11px", display: hiddenStartDateError }}>Please select start date.</p>
                                <br />
                                <Grid item xs={12} sm={12}>
                                    <FormControl>
                                        <FormLabel id="demo-controlled-radio-buttons-group"

                                        >
                                            <Typography variant="caption">Select the time from which the entity will not be available</Typography>
                                        </FormLabel>
                                        <RadioGroup
                                            row
                                            aria-labelledby="demo-controlled-radio-buttons-group"
                                            name="controlled-radio-buttons-group"
                                            value={radioBtnTimeValue}
                                            onChange={(event) => (setRadioBtnTimeValue(event.target.value))}
                                        >
                                            <FormControlLabel value="9:00:00" control={<Radio />} label="9 AM" />
                                            <FormControlLabel value="13:00:00" control={<Radio />} label="1 PM" />
                                            <FormControlLabel value="17:00:00" control={<Radio />} label="5 PM" />
                                            <FormControlLabel value="21:00:00" control={<Radio />} label="9 PM" />
                                        </RadioGroup>
                                    </FormControl>
                                </Grid>

                                <br />
                                <br />
                                <label className={styles.labelNames} for="endDatePicker">Select end date:</label>
                                <Grid item xs={12} sm={12}>
                                    <DatePicker 
                                        wrapperClassName={styles.datePicker}
                                        id="endDatePicker"
                                        selected={endDatePicker}
                                        minDate={(startDatePicker) ? (new Date().setDate(new Date(startDatePicker).getDate()+1)) : (new Date())}
                                        onChange={onEndDateChangeDatePicker}
                                        dateFormat='yyyy-MM-dd'
                                    />
                                        
                                </Grid>
                                <p style={{ color: '#ED6663', fontSize: "11px", display: hiddenEndDateError }}>Please select end date.</p>
                            </Grid>
                            <br/>
                            <Divider />
                            <br/>
                            <Box style={{ display: "flex", flexDirection: "row" }}>
                                <Button type="submit" onSubmit={handleSubmit(onAddUnavailablePeriodSubmit)} variant="contained" style={{ color: 'rgb(5, 30, 52)', fontWeight: 'bold', textAlign: 'center', backgroundColor: 'rgb(244, 177, 77)', marginLeft: '33.5%', marginTop: '1%', padding: '1%', borderRadius: '10px', width: '15%' }}>
                                    Save
                                </Button>
                                <Button
                                    variant="contained"
                                    style={{ color: 'rgb(5, 30, 52)', fontWeight: 'bold', textAlign: 'center', backgroundColor: 'rgb(244, 177, 77)', marginLeft: '2%', marginTop: '1%', padding: '1%', borderRadius: '10px', width: '15%' }}
                                    onClick={handleCloseDialog}
                                >
                                    Close
                                </Button>
                            </Box>
                        </Box>
                    </DialogContent>
                </Dialog>
                <Paper>
                    <Scheduler
                        data={data}
                    >
                        <ViewState
                            defaultCurrentDate={new Date()}
                        />
                        <MonthView />
                        <Toolbar />
                        <DateNavigator />
                        <TodayButton />
                        <Appointments
                        />
                        <AppointmentTooltip
                            showCloseButton
                        />
                        <Resources
                            data={resources}
                        />
                        <CurrentTimeIndicator
                            shadePreviousCells
                            shadePreviousAppointments
                        />
                    </Scheduler>
                </Paper>
            </Box>
        );
    }
}