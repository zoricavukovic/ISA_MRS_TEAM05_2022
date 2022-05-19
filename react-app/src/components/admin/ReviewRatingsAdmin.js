import React, { useEffect, useState } from "react";
import Box from "@mui/material/Box";
import Button from '@mui/material/Button';
import { Grid, TextField, Typography } from "@mui/material";
import { DialogContent, DialogTitle, Divider } from "@mui/material";
import { Dialog } from "@mui/material";
import { DialogActions } from "@mui/material";
import Alert from '@mui/material/Alert';
import { userLoggedInAsAdminWithResetedPassword, userLoggedInAsSuperAdmin } from "../../service/UserService";
import { PROCESSED, UNPROCESSED } from "../../service/ReportService";
import { DataGrid } from "@mui/x-data-grid";
import { Card, Checkbox, TextareaAutosize } from "@mui/material";
import { Snackbar } from "@mui/material";

import {getAllRatingsForViewByType} from "../../service/RatingService"


function ReviewDialog(props) {
    return (
        <div></div>
    )
}


export default function ReviewRatingsAdmin() {

    const [openReviewDialog, setOpenReviewDialog] = useState();
    const [isLoadingReviews, setLoadingReviews] = useState(true);
    const [reviews, setReviews] = useState();
    const [selectedReview, setSelectedReview] = useState(null);

    let rows = [];
    let columns = [
        { field: 'id', headerName: 'ID', width: 80 },
        { field: 'Client', headerName: 'Client', width: 180 },
        { field: 'Owner', headerName: 'Owner', width: 180 },
        { field: 'BookingEntity', headerName: 'BookingEntity', width: 180 },
        { field: 'ReviewDate', headerName: 'ReviewDate', width: 180 },
        { field: 'Rating', headerName: 'Rating', type:'number', width: 150 },
        { field: 'Processed', headerName: 'Processed', type: 'boolean', width: 150 }
    ];

    const handleCloseReviewDialog = () => {
        setOpenReviewDialog(false);
        setSelectedReview(null);
        loadUnprocessed();
    }
    const handleOpenReviewDialog = () => {
        setOpenReviewDialog(true);
    }

    const fillRowsWithData = () => {
        let newRows = [];
        for (let r of reviews) {
            let rowToAdd = {
                'id': r.id,
                'Client': r.reservation.client.firstName + ' ' + r.reservation.client.lastName,
                'Owner': r.owner.firstName + ' ' + r.owner.lastName,
                'BookingEntity': r.reservation.bookingEntity.name,
                'ReviewDate': r.reviewDate,
                'Rating': r.value,
                'Processed': r.processed
            }
            newRows.push(rowToAdd);
        }
        rows = newRows;
    }


    const loadProcessed = () => {
        setLoadingReviews(true);
        getAllRatingsForViewByType(PROCESSED)
            .then(res => {
                setReviews(res.data);
                setLoadingReviews(false);
            })
    }
    const loadUnprocessed = () => {
        setLoadingReviews(true);
        getAllRatingsForViewByType(UNPROCESSED)
            .then(res => {
                setReviews(res.data);
                setLoadingReviews(false);
            })
    }

    const findReviewByIndex = (index) => {
        for (let r of reviews) {
            if (r.id === index) return r;
        }
    }

    const handleOnSelectRow = (index) => {
        let review = findReviewByIndex(index);
        setSelectedReview(review);
        handleOpenReviewDialog();
    }

    useEffect(() => {
        if (userLoggedInAsSuperAdmin && userLoggedInAsAdminWithResetedPassword) {
            loadUnprocessed();
        }
    }, [])

    if (isLoadingReviews) {
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
                {(selectedReview !== null) ?
                    (<ReviewDialog
                        openReportDialog={openReviewDialog}
                        handleCloseReportDialog={handleCloseReviewDialog}
                        report={selectedReview}
                    />
                    ) : (<div></div>)
                }
                <div>
                    {reviews.length === true ? (
                        <h5 style={{ color: 'rgb(5, 30, 52)', fontWeight: 'bold', marginLeft: "15%" }}>There are no reviews.</h5>

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