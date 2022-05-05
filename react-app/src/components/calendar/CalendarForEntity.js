import React, { useEffect, useState } from "react";
import Paper from '@mui/material/Paper';
import { indigo, teal, red } from '@mui/material/colors';
import Box from "@mui/material/Box";

import { ViewState } from '@devexpress/dx-react-scheduler';
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
    AppointmentForm,
} from '@devexpress/dx-react-scheduler-material-ui';
import { getCalendarValuesByBookintEntityId } from "../../service/CalendarService";
import { DateTimePicker } from "@mui/x-date-pickers";


const resources = [{
    fieldName: 'type',
    title: 'type',
    instances: [
        { id: 'fast reservation', text: 'fast reservation', color: indigo },
        { id: 'regular reservation', text: 'regular reservation', color: teal },
        { id: 'unavailable', text: 'unavailable', color: red },
    ]
}];


export default class CalendarForEntity extends React.PureComponent {

    constructor(props) {
        super(props);

        this.state = {
            isLoadingData: true,
            data: []
        };
        this.addNewAppointment = this.addNewAppointment.bind(this);
    }

    componentDidMount() {
        if (this.props.location.state === undefined)
            return;
        getCalendarValuesByBookintEntityId(11).then(res => {
            this.setState({ isLoadingData: false, data: res.data });
        });
    }

    addNewAppointment() {
        let newApt = { startDate: '2018-11-23T18:00', type: 'fast reservation', endDate: '2018-11-25T18:00', title: 'met5', allDay: 'false' };
        this.setState((state) => ({
            data: [...state.data, newApt]
        }));
    }

    render() {
        const { data } = this.state;

        if (this.state.isLoadingData)
            return <div className="App">Loading...</div>
        return (
            <Box style={{ margin: "1% 9% 1% 15%" }}>
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