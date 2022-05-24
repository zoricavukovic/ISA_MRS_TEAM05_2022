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
import { addNewUnavailableDate, checkOverlapForUnavailableDate, setUnavailablePeriodAsAvailable } from "../../service/UnavailablePeriodService";


const resources = [{
    fieldName: 'type',
    title: 'type',
    instances: [
        { id: 'fast reservation', text: 'fast reservation', color: indigo },
        { id: 'regular reservation', text: 'regular reservation', color: teal },
        { id: 'unavailable', text: 'unavailable', color: red },
    ]
}];


function ConfirmDialog(props) {
    return (
        <Dialog
            open={props.confirmDialog}
            onClose={props.handleCloseConfirmDialog}
            aria-labelledby="alert-dialog-title"
            aria-describedby="alert-dialog-description"
        >
            <DialogTitle id="alert-dialog-title">
                {"Confirm adding unavailable dates"}
            </DialogTitle>
            <DialogContent>
                <DialogContentText id="alert-dialog-description">
                    {console.log("IZ DIALOGAAAA")}
                    {console.log(props.data)}
                    {
                        (props.data !== undefined && props.data !== '' && props.data !== null) ?
                            (<div>
                                It looks like there are some unavailable dates that overlap with this dates.
                                Your entity now won't be available from {props.data.startDate} to {props.data.endDate}.
                                Do you want to proceed?
                            </div>) :
                            (<div></div>)
                    }
                </DialogContentText>
            </DialogContent>
            <DialogActions>
                <Button onClick={props.handleOpenConfirmDialog} autoFocus>
                    Yes
                </Button>
                <Button onClick={props.handleCloseConfirmDialog}>No</Button>
            </DialogActions>
        </Dialog>
    );
}



export default function CalendarCard() {


    const { register, handleSubmit, watch, reset, formState: { errors } } = useForm();

    const [data, setData] = useState();
    const [isLoadingData, setIsLoadingData] = useState(true);
    const [openDialog, setOpenDialog] = useState(false);
    const [confirmDialog, setConfirmDialog] = useState(false);
    const [startDatePicker, setStartDatePicker] = useState(new Date());
    const [endDatePicker, setEndDatePicker] = useState(new Date());
    const [radioBtnTimeValue, setRadioBtnTimeValue] = React.useState('09:00');

    const [hiddenStartDateError, setHiddenStartDateError] = useState("none");
    const [hiddenEndDateError, setHiddenEndDateError] = useState("none");
    const [hiddenErrorDateBefore, setHiddenErrorDateBefore] = useState("none");
    const [overlapPeriod, setoverlapPeriod] = useState(null);

    const [openAlert, setOpenAlert] = React.useState(false);
    const [alertMessage, setAlertMessage] = React.useState("");
    const [typeAlert, setTypeAlert] = React.useState("");


    const handleOpenDialog = () => {
        setOpenDialog(true);
    }
    const handleCloseDialog = () => {
        setOpenDialog(false);
    }

    const handleOpenConfirmDialog = () => {
        setConfirmDialog(true);
    }
    const handleCloseConfirmDialog = () => {
        setConfirmDialog(false);
    }
    const handleCloseAlert = () => {
        setOpenAlert(false);
    }

    const sendUnavailableDateToServer = () => {
        let newPeriod = {
            "entityId": 11,
            "startDate": createFormatedDateFromString(startDatePicker.toLocaleDateString()),
            "endDate": createFormatedDateFromString(endDatePicker.toLocaleDateString())
        };
        addNewUnavailableDate(newPeriod)
            .then(res => {
                setTypeAlert("success");
                setAlertMessage("Successfuly set new unavailable dates");
                setOpenAlert(true);
                handleCloseAfterAddingAndRefreshCalendar();
            })
            .catch(err => {
                setTypeAlert("error");
                setAlertMessage("Error happend on server. Check if there exist reservations on this period");
                setOpenAlert(true);
            }
            );

    }

    const handleCloseAfterAddingAndRefreshCalendar = () => {
        refreshPage();
        setConfirmDialog(false);
        setOpenDialog(false);
    }

    const checkStartDateSelected = () => {
        if (startDatePicker !== null && startDatePicker !== undefined && startDatePicker !== '') {
            setHiddenStartDateError("none");
            return true;
        } else {
            setHiddenStartDateError("block");
            return false;
        }
    }
    const checkEndDateSelected = () => {
        if (endDatePicker !== null && endDatePicker !== undefined && endDatePicker !== '') {
            setHiddenEndDateError("none");
            return true;
        } else {
            setHiddenEndDateError("block");
            return false;
        }
    }

    const createFormatedDateFromString = (string) => {
        let tokens = string.split("/");
        let month = tokens[0];
        let day = tokens[1];
        let year = tokens[2];
        if (month.length == 1)
            month = '0' + month;
        if (day.length == 1)
            day = '0' + day;
        return year + "-" + month + "-" + day + " " + radioBtnTimeValue;
    }


    const checkDateBefore = () => {
        let date1 = new Date(startDatePicker);
        let date2 = new Date(endDatePicker);
        if (date2 > date1) {
            setHiddenErrorDateBefore("none");
            return true;
        } else {
            setHiddenErrorDateBefore("block");
            return false;
        }

    }

    const onAddUnavailablePeriodSubmit = (data) => {
        if (!checkStartDateSelected() || !checkEndDateSelected())
            return;

        if (!checkDateBefore())
            return;


        let newPeriod = {
            "entityId": 11,
            "startDate": createFormatedDateFromString(startDatePicker.toLocaleDateString()),
            "endDate": createFormatedDateFromString(endDatePicker.toLocaleDateString())
        };

        checkOverlapForUnavailableDate(newPeriod)
            .then(res => {
                if (res.data !== null && res.data !== undefined && res.data !== '') {
                    if (JSON.stringify(res.data) === JSON.stringify(overlapPeriod)) {
                        handleOpenConfirmDialog();
                    } else {
                        setoverlapPeriod(res.data);
                    }
                } else {
                    sendUnavailableDateToServer();
                }
            })

    }

    // ===================================================== for adding =============================
    const [openModifyDialog, setOpenModifyDialog] = useState(false);
    const handleOnCloseModifyDialog = () => {
        setOpenModifyDialog(false);
    }
    const handleOnOpenModifyDialog = () => {
        setOpenModifyDialog(true);
    }



    useEffect(() => {
        if (overlapPeriod != null && overlapPeriod != undefined && overlapPeriod != '') {
            handleOpenConfirmDialog();
        }
    }, overlapPeriod);


    const onStartDateChangeDatePicker = (date) => {
        setStartDatePicker(date);
    }
    const onEndDateChangeDatePicker = (date) => {
        setEndDatePicker(date);
    }

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

    const handleSetAvailableDate = (item) => {
        let obj = {
            "entityId": 11,
            "startDate": item.startDate,
            "endDate": item.endDate
        };
        console.log("ovo slajeeeeemmmm")
        console.log(obj);
        setUnavailablePeriodAsAvailable(obj)
            .then(res => {
                setTypeAlert("success");
                setAlertMessage("Successfuly set period as available");
                setOpenAlert(true);
                refreshPage();
            })
            .catch(err => {
                setTypeAlert("error");
                setAlertMessage("Error happend on server. Can't set period as available");
                setOpenAlert(true);
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

                <Snackbar open={openAlert} autoHideDuration={5000} onClose={handleCloseAlert}>
                    <Alert onClose={handleCloseAlert} severity={typeAlert} sx={{ width: '100%' }}>
                        {alertMessage}
                    </Alert>
                </Snackbar>
            </Box>
        );
    }
}