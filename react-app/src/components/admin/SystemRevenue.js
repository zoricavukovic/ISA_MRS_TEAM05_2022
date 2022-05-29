import React, { useState, useEffect } from "react";
import Box from "@mui/material/Box";
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import { DialogActions } from "@material-ui/core";
import { useHistory } from "react-router-dom";
import { getCurrentSystemRevenuePercentage, saveNewSystemRevenuePercentage } from "../../service/SystemRevenuePercentagesService";
import { userLoggedInAsSuperAdminOrAdminWithResetedPassword } from "../../service/UserService";
import { Typography } from "@mui/material";
import { Divider, Grid } from "@mui/material";
import { useForm } from "react-hook-form";
import InputLabel from '@mui/material/InputLabel';
import Alert from '@mui/material/Alert';
import Snackbar from '@mui/material/Snackbar';
import { FormControl } from '@mui/material'
import OutlinedInput from '@mui/material/OutlinedInput';
import MilitaryTechIcon from '@mui/icons-material/MilitaryTech';
import EuroIcon from '@mui/icons-material/Euro';


export default function SystemRevenue() {

    const history = useHistory();
    const { register, handleSubmit, watch, reset, formState: { errors } } = useForm();
    const [open, setOpen] = React.useState(false);
    const [systemRevenuePercentage, setSystemRevenuePercentage] = useState();
    const [isLoadingSystemRevenuePercentage, setIsLoadingSystemRevenuePercentage] = useState(true);

    const [openAlert, setOpenAlert] = React.useState(false);
    const [alertMessage, setAlertMessage] = React.useState("");
    const [typeAlert, setTypeAlert] = React.useState("");

    const handleOpenRevenueDialog = () => {
        setOpen(true);
    }
    const handleCloseRevenueDialog = () => {
        loadData();
        setOpen(false);
        setOpenAlert(false);
    }

    const onFormSubmit = data => {
        let obj = {
            percentage: data.revenuePercentage,
        };
        saveNewSystemRevenuePercentage(obj)
            .then(res => {
                setTypeAlert("success");
                setAlertMessage("Successfully set new system revenue percentage");
                setOpenAlert(true);
            })
            .catch(err => {
                setTypeAlert("error");
                setAlertMessage("Error happened on server. Please try again.");
                setOpenAlert(true);
            });
    }

    const loadData = () => {
        setIsLoadingSystemRevenuePercentage(true);
        getCurrentSystemRevenuePercentage().then(res => {
            setSystemRevenuePercentage(res.data);
            setIsLoadingSystemRevenuePercentage(false);
        })
    }

    useEffect(() => {
        if (userLoggedInAsSuperAdminOrAdminWithResetedPassword(history)) {
            loadData();
        }
    }, []);

    if (isLoadingSystemRevenuePercentage) {
        return <div>Loading...</div>
    }
    return (
        <div style={{ margin: "1% 5% 1% 20%" }}>
            <div style={{ backgroundColor: 'aliceblue', borderRadius: '10px' }}>
                The percentage that the system takes
                <br />
                <Button variant="contained" onClick={handleOpenRevenueDialog} style={{ color: 'rgb(5, 30, 52)', fontSize: '15px', fontWeight: 'bold', textAlign: 'center', backgroundColor: 'rgb(244, 177, 77)', marginTop: '1%', padding: '1%', borderRadius: '10px', width: '15%' }}>
                    {systemRevenuePercentage.percentage} %
                </Button>
            </div>
            <Dialog open={open} onClose={handleCloseRevenueDialog} sm>
                <DialogTitle>
                    <Typography variant="h5" align="center">System revenue percentage</Typography>
                </DialogTitle>
                <Box
                    component="form"
                    noValidate
                    onSubmit={handleSubmit(onFormSubmit)}
                    style={{ width: '100%' }}
                >
                    <DialogContent>
                        <Divider />
                        <br />
                        <br />
                        <div style={{ display: "flex", flexDirection: "row", justifyContent: "center" }}>
                            <EuroIcon style={{ fontSize: "40px", color: "rgb(244, 177, 77)" }} />
                            <div>
                                <FormControl sx={{ m: 1 }}>
                                    <InputLabel>System revenue percentage</InputLabel>
                                    <OutlinedInput
                                        size="small"
                                        name="revenuePercentage"
                                        type="number"
                                        id="revenuePercentage"
                                        defaultValue={systemRevenuePercentage.percentage}
                                        placeholder="System revenue percentage"
                                        label="System revenue percentage"
                                        {...register("revenuePercentage", {
                                            required: "Specify system revenue percentage",
                                            validate: value =>
                                                value.toString() != systemRevenuePercentage.percentage.toString() ||
                                                    "Please select different value",
                                            min: {
                                                value: 1,
                                                message: "Min value of percentages is 1%"
                                            },
                                            max: {
                                                value: 90,
                                                message: "Max value of percentages is 90%"
                                            }
                                        })}
                                    />
                                </FormControl>
                                {errors.revenuePercentage && <p style={{ color: '#ED6663', fontSize: '12px' }}>{errors.revenuePercentage.message}</p>}
                            </div>
                        </div>
                        <br />
                        <br />
                        <Divider />
                    </DialogContent>
                    <DialogActions>
                        <Button type="submit" onSubmit={handleSubmit(onFormSubmit)} variant="contained">Save</Button>
                        <Button
                            variant="contained"
                            onClick={() => {
                                reset(
                                    {
                                        revenuePercentage: systemRevenuePercentage.percentage,
                                    }, {
                                    keepDefaultValues: false,
                                    keepErrors: false,
                                }
                                );
                            }}
                        >
                            Reset
                        </Button>
                        <Button variant="contained" onClick={handleCloseRevenueDialog}>Close</Button>
                    </DialogActions>
                </Box>
                <Snackbar open={openAlert} autoHideDuration={6000} onClose={handleCloseRevenueDialog}>
                    <Alert onClose={handleCloseRevenueDialog} severity={typeAlert} sx={{ width: '100%' }}>
                        {alertMessage}
                    </Alert>
                </Snackbar>
            </Dialog>

        </div>
    );
}