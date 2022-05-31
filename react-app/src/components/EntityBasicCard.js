import { URL_PICTURE_PATH } from "../service/PictureService";
import * as React from 'react';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import ReadMoreIcon from '@mui/icons-material/ReadMore';
import { useHistory } from "react-router-dom";
import RatingEntity from "./Rating";
import { getCurrentUser } from "../service/AuthService";
import { logicalDeleteBookingEntityById } from "../service/BookingEntityService";

import DeleteIcon from '@mui/icons-material/Delete';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import Alert from '@mui/material/Alert';
import Snackbar from '@mui/material/Snackbar';
import TextField from '@mui/material/TextField';
import ImageSlider from "./image_slider/ImageSlider";

export default function EntityBasicCard(props) {

    const history = useHistory();

    ///////////////FOR DELETE///////////////////////////////////////////////
    const [open, setOpen] = React.useState(false);
    const [openDialog, setOpenDialog] = React.useState(false);
    const [password, setPassword] = React.useState("");
    const [message, setMessage] = React.useState("");
    const [typeAlert, setTypeAlert] = React.useState("");

    const handleClick = () => {
        setOpen(true);
    };

    const handleClose = (_event, reason) => {
        if (reason === 'clickaway') {
            return;
        }
        setOpen(false);
    };

    const handleClickOpen = (event) => {
        setOpenDialog(true);
    };

    const handleCloseDialog = () => {
        setOpenDialog(false);
    };

    const logicDeleteBookingEntity = (event) => {
        logicalDeleteBookingEntityById(props.bookingEntity.id, getCurrentUser().id, password).then(res => {
            setPassword("");
            handleClick();
            setTypeAlert("success");
            setMessage("Successfully delete entity " + props.bookingEntity.name);
            window.location.reload();
        }).catch(res => {
            setPassword("");
            setTypeAlert("error");
            console.log(res.response.data);
            setMessage(res.response.data);
            handleClick();
            return;
        })
        setOpenDialog(false);
    }
    ///////////////////////////////////////////////////////////////////////////////////


    const showBookingEntity = () => {
        if (props.bookingEntity.entityType === "COTTAGE") {
            history.push({
                pathname: "/showCottageProfile",
                state: { bookingEntityId: props.bookingEntity.id }
            })
        } else if (props.bookingEntity.entityType === "ADVENTURE") {
            history.push({
                pathname: "/showAdventureProfile",
                state: { bookingEntityId: props.bookingEntity.id }
            })
        } else if (props.bookingEntity.entityType === "SHIP")
            history.push({
                pathname: "/showShipProfile",
                state: { bookingEntityId: props.bookingEntity.id }
            })
    };

    const reserveBookingEntity = () => {
        console.log("Evo me");
        console.log(props.searchParams);
        history.push({
            pathname: "/newReservation",
            state: {
                bookingEntityId: props.bookingEntity.id,
                searchParams: props.searchParams
            }
        })
    }


    return (
        <Card style={{ margin: "2%" }} sx={{ maxWidth: 345 }}>
            <Dialog open={openDialog} onClose={handleCloseDialog}>
                <DialogTitle>Confirm Deleting</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        To delete this entity, please enter your password for security reasons.
                    </DialogContentText>
                    <TextField
                        autoFocus
                        margin="dense"
                        value={password}
                        onChange={e => {
                            setPassword(e.target.value);
                        }}
                        id="name"
                        label="Password"
                        type="password"
                        fullWidth
                        variant="standard"
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleCloseDialog}>Cancel</Button>
                    <Button onClick={logicDeleteBookingEntity}>Confirm</Button>
                </DialogActions>
            </Dialog>

            {props.bookingEntity.pictures.length === 0 ? (
                <CardMedia
                    component="img"
                    height="140"
                    alt="No Images"
                />
            ) : (
                <div>
                    {
                        props.bookingEntity.pictures.length > 1 ?
                            (
                                <CardMedia>
                                    <ImageSlider imageHeight="25vh" slides={props.bookingEntity.pictures.map((im) => ({ 'image': URL_PICTURE_PATH + im }))} />
                                </CardMedia>
                            ) :
                            (
                                <CardMedia
                                    component="img"
                                    style={{ height: "26vh" }}
                                    alt="No Images"
                                    image={URL_PICTURE_PATH + props.bookingEntity.pictures[0]}
                                >
                                </CardMedia>
                            )
                    }
                </div>
            )}
            <CardContent>
                <Typography style={{ textAlign: "left" }} gutterBottom variant="h5" component="div">
                    {props.bookingEntity.name}
                </Typography>
                <Typography style={{ textAlign: "left" }} gutterBottom variant="h7" component="div">
                    <text style={{ backgroundColor: "aliceblue" }}>Location:</text> {props.bookingEntity.address}, {props.bookingEntity.place.cityName} {props.bookingEntity.place.zipCode}, {props.bookingEntity.place.stateName}
                </Typography>

                <Typography style={{ textAlign: "left" }} gutterBottom variant="h7" component="div">
                    {props.bookingEntity.entityType === "COTTAGE" ? (
                        <div>
                            <text style={{ backgroundColor: "aliceblue" }}>Cost Per Night:</text> {props.bookingEntity.entityPricePerPerson} €
                        </div>

                    ) : (<div>
                        <text style={{ backgroundColor: "aliceblue" }}>Cost Per Person:</text> {props.bookingEntity.entityPricePerPerson} €

                    </div>)}
                </Typography>
                <Typography style={{ textAlign: "left" }} gutterBottom variant="h7" component="div">
                    <RatingEntity value={Math.floor(props.bookingEntity.averageRating * 2) / 2}></RatingEntity>
                </Typography>
                <Typography style={{ textAlign: "left" }} variant="body2" color="text.secondary">
                    {props.bookingEntity.promoDescription}
                </Typography>
            </CardContent>
            <CardActions>
                <Button size="small" onClick={showBookingEntity}><ReadMoreIcon fontSize="large" style={{ margin: "5px" }} /> Details</Button>
                {getCurrentUser() !== null &&
                    <span>
                        {getCurrentUser().userType.name === "ROLE_CLIENT"
                            ?
                            (
                                <Button size="small" onClick={reserveBookingEntity}><ReadMoreIcon fontSize="large" style={{ margin: "5px" }} /> Reserve</Button>
                            ) :
                            (
                                <span>
                                    {((props.onlyTypeForDeleteVisible === "ADVENTURES" && (getCurrentUser().userType.name === "ROLE_COTTAGE_OWNER" || getCurrentUser().userType.name === "ROLE_SHIP_OWNER")) ||
                                        (props.onlyTypeForDeleteVisible === "COTTAGES" && (getCurrentUser().userType.name === "ROLE_INSTRUCTOR" || getCurrentUser().userType.name === "ROLE_SHIP_OWNER")) ||
                                        (props.onlyTypeForDeleteVisible === "SHIPS" && (getCurrentUser().userType.name === "ROLE_INSTRUCTOR" || getCurrentUser().userType.name === "ROLE_COTTAGE_OWNER")))  
                                        ?
                                    (
                                    <span>
                                    </span>
                                    )
                                    :
                                    ( <span>
                                        <Button size="small" onClick={handleClickOpen}><DeleteIcon />Delete</Button>
                                        <Snackbar open={open} autoHideDuration={6000} onClose={handleClose}>
                                            <Alert onClose={handleClose} severity={typeAlert} sx={{ width: '100%' }}>
                                                {message}
                                            </Alert>
                                        </Snackbar>
                                    </span>)
                                    }
                                </span>
                            )
                        }
                    </span>
                }
            </CardActions>
        </Card>
    );

}