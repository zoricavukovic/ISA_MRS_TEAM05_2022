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
import { Grid, TextField, Typography } from "@mui/material";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css"
import { getCalendarValuesByBookintEntityId } from "../../service/CalendarService";
import { DateTimePicker } from "@mui/x-date-pickers";
import { DialogContent, DialogTitle, Divider } from "@mui/material";
import { Dialog } from "@mui/material";
import { DialogContentText } from "@mui/material";
import { DialogActions } from "@mui/material";
import { useForm } from "react-hook-form";
import styles from './datePickerStyle.module.css';
import Alert from '@mui/material/Alert';
import Snackbar from '@mui/material/Snackbar';
import { useHistory } from "react-router-dom";
import {userLoggedInAsOnwer} from "../../service/UserService";


import { ViewState, EditingState, IntegratedEditing } from '@devexpress/dx-react-scheduler';
import {
    Scheduler,
    MonthView,
    WeekView,
    AllDayPanel,
    ViewSwitcher,
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

export default function Calendar() {


    const history = useHistory();
    const [data, setData] = useState();
    const [isLoadingData, setIsLoadingData] = useState(true);

    useEffect(() => {
        refreshPage();
    }, []);

    const refreshPage = () => {
        getCalendarValuesByBookintEntityId(11)
            .then(res => {
                setData(res.data);
                setIsLoadingData(false);
            });
    }

    if (isLoadingData) {
        return <div className="App">Loading...</div>
    }
    else {
        return (

            <Box
                style={{ margin: "1% 9% 1% 15%" }}
                alignItems="center"
                justifyContent="center"
            >

                <Paper>
                    {console.log(data)}
                    <Scheduler
                        data={data}
                    >
                        <ViewState
                            defaultCurrentDate={new Date()}
                        />
                        <WeekView
                            startDayHour={6}
                            endDayHour={21}
                        />
                        <MonthView />
                        <AllDayPanel />
                        <Toolbar />
                        <ViewSwitcher />
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