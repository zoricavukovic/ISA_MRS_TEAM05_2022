

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
import { DialogContent, DialogTitle, Divider } from "@mui/material";
import { Dialog } from "@mui/material";
import { DialogContentText } from "@mui/material";
import { DialogActions } from "@mui/material";
import { useForm } from "react-hook-form";
import Alert from '@mui/material/Alert';
import Snackbar from '@mui/material/Snackbar';
import { userLoggedInAsAdminWithResetedPassword, userLoggedInAsSuperAdmin } from "../../service/UserService";
import { getAllReportsForViewByType, PROCESSED, UNPROCESSED } from "../../service/ReportService";
import { DataGrid } from "@mui/x-data-grid";
import { fontWeight, height } from "@mui/system";
import { Card, Checkbox, TextareaAutosize } from "@mui/material";
import { SecurityUpdateSharp } from "@mui/icons-material";

function ReviewDialog(props) {

    let startDate = new Date(props.report.reservation.startDate);
    let endDate = new Date(props.report.reservation.startDate);

    const [adminResponse, setAdminResponse] = useState('');
    const [checkedPenalize, setCheckedPenalize] = useState(false);

    const handleAdminResponseToReport = () => {
        console.log(adminResponse);
        console.log(checkedPenalize);
    }

    const setDates = () => {
        endDate.setDate(endDate.getDate() + props.report.reservation.numOfDays);
    }

    return (

        <Dialog open={props.openReportDialog} onClose={props.handleCloseReportDialog} sm>
            {setDates()}
            <DialogTitle>Process Report</DialogTitle>
            <DialogContent>
                <Card style={{ color: 'rgb(5, 30, 52)', backgroundColor: "aliceblue", margin: "2%" }}>
                    <table style={{ whiteSpace: "nowrap" }}>
                        <tr>
                            <th><h4>{props.report.reservation.bookingEntity.name}</h4></th>
                            <th style={{ paddingLeft: "3%" }}>€ {props.report.reservation.cost} / {props.report.reservation.numOfDays} day(s)</th>
                        </tr>

                    </table>
                </Card>
                <table style={{ textAlign: "left" }}>

                    <tr>
                        <th style={{ color: 'rgb(5, 30, 52)', backgroundColor: "aliceblue", padding: "3%", fontWeight: "normal" }}>Check-in</th>
                        <th style={{ color: 'rgb(5, 30, 52)', backgroundColor: "aliceblue", padding: "3%", fontWeight: "normal" }}>Check-out</th>
                    </tr>
                    <tr>
                        <td variant="h6" style={{ color: 'rgb(5, 30, 52)', borderRight: "solid 2px aliceblue", paddingBottom: "2%", fontWeight: "bold" }}>
                            {new Intl.DateTimeFormat("en-GB", {
                                year: "numeric",
                                month: "long",
                                day: "2-digit"
                            }).format(startDate)
                            }
                        </td>
                        <td variant="h6" style={{ color: 'rgb(5, 30, 52)', paddingBottom: "2%", fontWeight: "bold" }}>
                            {new Intl.DateTimeFormat("en-GB", {
                                year: "numeric",
                                month: "long",
                                day: "2-digit"
                            }).format(endDate)
                            }
                        </td>
                    </tr>
                    <tr>
                        <td style={{ color: 'rgb(5, 30, 52)', borderRight: "solid 2px aliceblue", fontWeight: "lighter" }}>
                            {new Intl.DateTimeFormat('en-US', { hour: '2-digit', minute: '2-digit' }).format(startDate)}
                        </td>
                        <td style={{ color: 'rgb(5, 30, 52)', padding: "0%", fontWeight: "lighter" }}>
                            {new Intl.DateTimeFormat('en-US', { hour: '2-digit', minute: '2-digit' }).format(endDate)}
                        </td>
                    </tr>
                </table>

                <Divider style={{ margin: "2%" }}></Divider>


                <Typography variant="body2" style={{ backgroundColor: 'rgb(252, 234, 207)', borderRadius: '10px', paddingLeft: '1%', paddingBottom: '0.1%', paddingTop: '0.1%', margin: '1%', minWidth: '350px' }}>
                    <h3 style={{ color: 'rgb(5, 30, 52)' }}>Reservation report</h3>
                    <h4 style={{ color: 'rgb(5, 30, 52)' }}>Did the clients come?
                        <Checkbox
                            defaultChecked={props.report.clientCome}
                            readOnly
                            disabled
                        />
                    </h4>
                    <h4 style={{ color: 'rgb(5, 30, 52)' }}>Notes for administrator to see
                    </h4>
                    <TextareaAutosize
                        aria-label="minimum height"
                        minRows={3}
                        value={props.report.ownerComment}
                        name="reason"
                        style={{ width: 200 }}
                        disabled
                        readOnly
                    />
                    <h4 style={{ color: 'rgb(5, 30, 52)' }}>PenalizeClient
                        <Checkbox
                            checked={props.report.penalizeClient}
                            readOnly
                            disabled
                        />
                    </h4>
                </Typography>
                <br />
                <br />
                <br/>
                <br/>
                <Divider sx={{ borderBottomWidth: 5 }} />
                <Typography style={{ fontSize: '16px', fontWeight: 'bold' }}>Enter response here:<br /></Typography>
                <Typography style={{ fontSize: '10px' }} variant="caption">Note: This response will be send to owner: {props.report.owner.email} and client: {props.report.reservation.client.email}</Typography>
                <br />
                {props.report.processed ?
                    (
                        <div>
                            <TextareaAutosize
                                aria-label="minimum height"
                                minRows={4}
                                value={props.report.adminResponse}
                                style={{ width: 300 }}
                                disabled
                                readOnly
                            />
                            <Divider/>
                            Do you want to penalize this client?
                            <Checkbox
                                defaultChecked={props.report.adminPenalizeClient}
                                readOnly
                                disabled
                            />
                        </div>
                    )
                    :
                    (
                        <div>
                            <TextareaAutosize
                                aria-label="minimum height"
                                minRows={4}
                                value={adminResponse}
                                placeholder="Enter response..."
                                onChange={e => { setAdminResponse(e.target.value) }}
                                autoFocus
                                style={{ width: 300 }}
                            />
                            <Divider/>
                            <b>Do you want to penalize this client?</b>
                            <Checkbox
                                checked={checkedPenalize}
                                defaultChecked={checkedPenalize}
                                onChange={e => {setCheckedPenalize(e.target.checked)}}
                                inputProps={{ 'aria-label': 'controlled' }}
                            />
                        </div>
                    )}
            </DialogContent>

            <DialogActions>
                {!props.report.processed ?
                 (<Button onClick={handleAdminResponseToReport}>Send response</Button>) : (<div></div>)
                } 
                <Button onClick={props.handleCloseReportDialog}>Cancel</Button>
            </DialogActions>
        </Dialog>
    );
}


export default function ReviewReservationReport() {

    const [openReportDialog, setOpenReportDialog] = useState();
    const [isLoadingReports, setLoadingReports] = useState(true);
    const [reports, setReports] = useState();
    const [selectedReport, setSelectedReport] = useState(null);

    let rows = [];
    let columns = [
        { field: 'id', headerName: 'ID', width: 150 },
        { field: 'Owner', headerName: 'Owner', width: 200 },
        { field: 'Client', headerName: 'Client', width: 200 },
        { field: 'Comment', headerName: 'Comment', width: 200 },
        { field: 'Processed', headerName: 'Processed', type: 'boolean', width: 200 }

    ];

    const handleCloseReportDialog = () => {
        setOpenReportDialog(false);
        setSelectedReport(null);
    }
    const handleOpenReportDialog = () => {
        setOpenReportDialog(true);
    }

    const fillRowsWithData = () => {
        let newRows = [];
        for (let r of reports) {
            let comment = r.ownerComment;
            if (comment > 15)
                comment = comment.slice(0, 15) + '...';
            let rowToAdd = {
                'id': r.id,
                'Owner': r.owner.firstName + ' ' + r.owner.lastName,
                'Client': r.reservation.client.firstName + ' ' + r.reservation.client.lastName,
                'Comment': comment,
                'Processed': r.processed
            }
            newRows.push(rowToAdd);
        }
        rows = newRows;

    }


    const loadProcessed = () => {
        setLoadingReports(true);
        getAllReportsForViewByType(PROCESSED)
            .then(res => {
                setReports(res.data);
                setLoadingReports(false);
            })
    }
    const loadUnprocessed = () => {
        setLoadingReports(true);
        getAllReportsForViewByType(UNPROCESSED)
            .then(res => {
                setReports(res.data);
                setLoadingReports(false);
            })
    }

    const findReportByIndex = (index) => {
        for (let r of reports) {
            if (r.id === index) return r;
        }
    }

    const handleOnSelectRow = (index) => {
        let report = findReportByIndex(index);
        setSelectedReport(report);
        handleOpenReportDialog();
    }

    useEffect(() => {
        if (userLoggedInAsSuperAdmin || userLoggedInAsAdminWithResetedPassword) {
            loadUnprocessed();
        }
    }, [])

    if (isLoadingReports) {
        return <div className="App">Loading...</div>
    }
    else {

        { fillRowsWithData() }

        return (
            <Box
                alignItems="center"
                justifyContent="center"
                style={{ margin: '1% 11% 1% 20%' }}
            >

                <Box style={{ display: "flex", flexDirection: "row", margin: "1% auto 1% auto" }}>
                    <Button variant="contained" onClick={loadProcessed} style={{ color: 'white', backgroundColor: 'rgb(68, 255, 162)', fontSize: '10px', fontWeight: 'bold', textAlign: 'center', marginTop: '1%', padding: '1%', borderRadius: '10px', width: '15%' }}>
                        Processed
                    </Button>
                    <Button variant="contained" onClick={loadUnprocessed} style={{ color: 'white', backgroundColor: 'rgb(252,38, 38)', fontSize: '10px', fontWeight: 'bold', textAlign: 'center', marginTop: '1%', padding: '1%', borderRadius: '10px', width: '15%' }}>
                        Unprocessed
                    </Button>
                </Box>
                {(selectedReport !== null) ?
                    (<ReviewDialog
                        openReportDialog={openReportDialog}
                        handleCloseReportDialog={handleCloseReportDialog}
                        report={selectedReport}
                    />
                    ) : (<div></div>)
                }
                <div>
                    {reports.length === true ? (
                        <h5 style={{ color: 'rgb(5, 30, 52)', fontWeight: 'bold', marginLeft: "15%" }}>There are no reports.</h5>

                    ) : (
                        <DataGrid
                            sx={{
                                "& .MuiDataGrid-columnHeaders": {
                                    backgroundColor: "rgba(17,16,29,255)",
                                    color: "white",
                                },
                                "& .MuiDataGrid-virtualScrollerRenderZone": {
                                    "& .MuiDataGrid-row": {
                                        "&:nth-child(2n)": { backgroundColor: "rgb(208,208,208)" }
                                    }
                                },
                                '& .MuiDataGrid-menuIcon': {
                                    backgroundColor: 'white'
                                },
                                '& .MuiDataGrid-sortIcon': {
                                    color: 'white',

                                },
                            }}


                            style={{ height: '450px' }}
                            rows={rows}
                            columns={columns}
                            pageSize={5}
                            rowsPerPageOptions={[5]}
                            disableColumnSelector
                            onSelectionModelChange={item => { handleOnSelectRow(item["0"]) }}
                        />
                    )}
                </div>

            </Box>
        )
    }
}