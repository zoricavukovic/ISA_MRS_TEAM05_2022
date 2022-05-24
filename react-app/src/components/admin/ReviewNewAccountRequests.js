import React, { useEffect, useState } from "react";
import Box from "@mui/material/Box";
import Button from '@mui/material/Button';
import { DialogContent, DialogTitle, Divider } from "@mui/material";
import { Dialog } from "@mui/material";
import { DialogActions } from "@mui/material";
import Alert from '@mui/material/Alert';
import { getAllNewAccountRequests, userLoggedInAsSuperAdminOrAdminWithResetedPassword } from "../../service/UserService";
import { DataGrid } from "@mui/x-data-grid";
import { Checkbox, TextareaAutosize } from "@mui/material";
import { Snackbar } from "@mui/material";
import Avatar from '@mui/material/Avatar';
import { deepOrange } from '@mui/material/colors';
import { useHistory } from "react-router-dom";
import { Typography } from "@mui/material";
import PersonAddAlt1Icon from '@mui/icons-material/PersonAddAlt1';
import ShipOwner from "../../icons/shipOwner.png";
import CottageOwner from "../../icons/cottageOwner.png";
import Instructor from "../../icons/instructor.png";


export default function ReviewNewAccountRequests() {

    const [openReviewDialog, setOpenReviewDialog] = useState();
    const [isLoadingNewAccountRequests, setLoadingNewAccountRequests] = useState(true);
    const [newAccountRequests, setNewAccountRequests] = useState();
    const [selectedNewAccountRequest, setNewAccountRequest] = useState(null);
    const history = useHistory();

    let rows = [];
    let columns = [
        { field: 'id', headerName: 'User ID', width: 120 },
        { field: 'User', headerName: 'User', width: 200 },
        { field: 'Email', headerName: 'Email', width: 400 },
        {
            field: 'Type',
            headerName: 'Type',
            width: 120,
            renderCell: (params) => {
                const IconByType = (props) => {
                    if (params.value === "ROLE_COTTAGE_OWNER") {
                        return (
                            <img style={{width:"45px"}} src={CottageOwner}></img>
                        );
                    }
                    else if (params.value === "ROLE_INSTRUCTOR") {
                        return (
                            <img style={{width:"45px"}} src={Instructor}></img>
                        );
                    }
                    else if (params.value === "ROLE_SHIP_OWNER") {
                        return (
                            <img style={{width:"45px"}} src={ShipOwner}></img>
                        );
                    }
                }
                return (
                    <div>
                        <IconByType/>
                    </div>
                );
            }
        },
        { field: 'Processed', headerName: 'Processed', type: 'boolean', width: 120 }
    ];

    const handleCloseReviewDialog = () => {
        setOpenReviewDialog(false);
        setNewAccountRequest(null);
        loadUnprocessed();
    }
    const handleOpenReviewDialog = () => {
        setOpenReviewDialog(true);
    }

    const fillRowsWithData = () => {
        let newRows = [];
        for (let r of newAccountRequests) {
            let rowToAdd = {
                'id': r.user.id,
                'User': r.user.firstName + ' ' + r.user.lastName,
                'Email': r.user.email,
                'Type': r.user.userType.name,
                'Processed': r.processed
            }
            newRows.push(rowToAdd);
        }
        rows = newRows;
    }

    const loadUnprocessed = () => {
        setLoadingNewAccountRequests(true);
        getAllNewAccountRequests()
            .then(res => {
                setNewAccountRequests(res.data);
                setLoadingNewAccountRequests(false);
            })
    }

    const findDeleteRequestByIndex = (index) => {
        for (let r of newAccountRequests) {
            if (r.id === index) return r;
        }
    }

    const handleOnSelectRow = (index) => {
        let deleteRequest = findDeleteRequestByIndex(index);
        setNewAccountRequest(deleteRequest);
        handleOpenReviewDialog();
    }

    useEffect(() => {
        if (userLoggedInAsSuperAdminOrAdminWithResetedPassword(history)) {
            loadUnprocessed();
        }
    }, [])

    if (isLoadingNewAccountRequests) {
        return <div className="App">Loading...</div>
    }
    else {

        { fillRowsWithData() }

        return (
            <Box
                alignItems="center"
                justifyContent="center"
                style={{ margin: '1% 11% 1% 25%' }}
            >
                <h3 color="rgba(17,16,29,255)">New account requests</h3>
                <br />

                <div >
                    <Divider sx={{ borderBottomWidth: 5, color: "rgba(17,16,29,255)" }} />
                    {newAccountRequests.length === true ? (
                        <h5 style={{ color: 'rgb(5, 30, 52)', fontWeight: 'bold', marginLeft: "15%" }}>There are no new account requests.</h5>
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