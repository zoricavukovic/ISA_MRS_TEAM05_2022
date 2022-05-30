import React, { useEffect, useState } from "react";
import Paper from '@mui/material/Paper';
import { indigo, teal, red } from '@mui/material/colors';
import Box from "@mui/material/Box";
import Button from '@mui/material/Button';
import "react-datepicker/dist/react-datepicker.css"
import { getCalendarValuesForAllOwnerEntitiesById } from "../../service/CalendarService";
import { useHistory } from "react-router-dom";
import { userLoggedInAsOwner } from "../../service/UserService";
import { getCurrentUser } from "../../service/AuthService";
import { makeStyles } from "@material-ui/core";


import { ViewState } from '@devexpress/dx-react-scheduler';
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
import CalendarForEntityChooser from "./CalendarForEntityChooser";


const resources = [{
    fieldName: 'type',
    title: 'type',
    instances: [
        { id: 'fast reservation', text: 'fast reservation', color: indigo },
        { id: 'regular reservation', text: 'regular reservation', color: teal },
        { id: 'unavailable', text: 'unavailable', color: red },
    ]
}];


const useStyles = makeStyles({
    timeTableCell: {
        height: "300px"
    }
});

const RowComponent = (props) => {
    const classes = useStyles();
    return (
        <AllDayPanel.Row {...props} className={classes.timeTableCell} />
    );
}



export default function Calendar() {

    const history = useHistory();
    const [data, setData] = useState();
    const [isLoadingData, setIsLoadingData] = useState(true);


    const [open, setOpen] = React.useState(false);
    const handleDrawerOpen = () => {
        setOpen(true);
    };
    const handleDrawerClose = () => {
        setOpen(false);
    };


    useEffect(() => {
        if (userLoggedInAsOwner(history)) {
            refreshPage();
        }
    }, []);


    const refreshPage = () => {
        getCalendarValuesForAllOwnerEntitiesById(getCurrentUser().id)
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
                <div style={{justifyContent:"right", display:"flex", flexDirection:"row"}}>
                    <Button onClick={handleDrawerOpen} edge="end" sx={{ ...(open && { display: 'none' }) }}  variant="contained" style={{ color: 'rgb(5, 30, 52)', fontSize: '10px', fontWeight: 'bold', textAlign: 'center', backgroundColor: 'rgb(244, 177, 77)', marginTop: '1%', padding: '1%', borderRadius: '10px', width: '10%' }}>
                        Entity calendar
                    </Button>
                </div>
                <br/>
                <br/>

                <CalendarForEntityChooser handleDrawerClose={handleDrawerClose} open={open} />

                <Paper>
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
                        <AllDayPanel
                            rowComponent={RowComponent}
                        />
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