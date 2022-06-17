import React, { useEffect, useState } from 'react'
import { Grid, Paper, Avatar, TextField, Button, Typography, Link, Checkbox, FormControlLabel, ListItemText, InputAdornment, FormControl, FormLabel, FormGroup, Dialog, DialogTitle, DialogContent, DialogContentText, DialogActions, ListItem } from '@mui/material'
import { useHistory } from 'react-router-dom';
import 'react-date-range/dist/styles.css'; // main style file
import 'react-date-range/dist/theme/default.css'; // theme css file
import { format } from "date-fns";
import { DateRange,Calendar } from 'react-date-range';
import Radio from '@mui/material/Radio';
import RadioGroup from '@mui/material/RadioGroup';
import { AddCircleOutlined, DateRangeOutlined, RemoveCircleOutlined } from '@mui/icons-material';
import { getBookingEntityById } from '../../service/BookingEntityService';
import { addReservation } from '../../service/ReservationService';
import { getCurrentUser } from '../../service/AuthService';
import Alert from '@mui/material/Alert';
import Snackbar from '@mui/material/Snackbar';
import { getCurrentLoyaltyProgram } from '../../service/LoyaltyProgramService';
import { getUserById, getUsersLoyaltyProgramById } from '../../service/UserService';

export default function NewReservationPage(props) {
    const [type, setType] = useState("");
    const [openDate,setOpenDate] = useState(false);
    const [personNumber,setPersonNumber] = useState(1);
    const [bookingEntity, setBookingEntity] = useState({});
    const [pricelistData, setPricelistData] = useState({});
    const [isLoaded, setLoaded] = useState(false);
    const [price, setPrice] = useState(0);
    const [additionalPrice, setAdditionalPrice] = useState(0);
    const [openDialog, setOpenDialog] = useState(false);
    const [unavailableDates, setUnavailableDates] = useState([]);
    const [times, setTimes] = useState([]);
    const [checkedTime, setCheckedTime] = useState({});
    const [additionalServices, setAdditionalServices] = useState([]);
    const [maxNumOfPersons, setMaxNumOfPersons] = useState(10);
    const [currentLoyaltyProgram, setCurrentLoyaltyProgram] = useState(null);
    const history = useHistory();
    const [reservationDTO,setReservationDTO] = useState({});
    const [message, setMessage] = React.useState("");
    const [typeAlert, setTypeAlert] = React.useState("");
    const [discount, setDiscount] = useState(0);
    const [open, setOpen] = React.useState(false);

    var availableTimes = [{
        text:"9 AM",
        value:"09:00:00",
        available:true
    },
    {
        text:"1 PM",
        value:"13:00:00",
        available:true
    },
    {
        text:"5 PM",
        value:"17:00:00",
        available:true
    },
    {
        text:"9 PM",
        value:"21:00:00",
        available:true
    }];
    const oneDay = 60 * 60 * 24 * 1000;

    const [selectionRange, setSelectionRange] = useState({
        startDate: new Date(),
        endDate: new Date(new Date().getTime() + oneDay),
        key: 'selection',
      });

    useEffect(() => {
        const entityId = props.history.location.state.bookingEntityId;
        getBookingEntityById(entityId).then(res => {
            console.log("+++++++++ ENTITY+++++++");
            console.log(res.data);
            res.data.pricelists.sort(function (a, b) {
                var key1 = new Date(a.startDate[0], a.startDate[1]-1, a.startDate[2], a.startDate[3],a.startDate[4]);
                var key2 = new Date(b.startDate[0], b.startDate[1]-1, b.startDate[2], b.startDate[3],b.startDate[4]);
            
                if (key1 < key2) {
                    return 1;
                } else if (key1 == key2) {
                    return 0;
                } else {
                    return -1;
                }
            });
            console.log("prosao");
            if(String(res.data.entityType) !== "COTTAGE")
                setMaxNumOfPersons(res.data.maxNumOfPersons);
            else    
                setMaxNumOfPersons(50);
            setBookingEntity(res.data);
            setPricelistData(res.data.pricelists[0]);
            let unaDates = []
            for(let unDate of res.data.allUnavailableDates)
                unaDates.push(new Date(unDate[0],unDate[1]-1,unDate[2],unDate[3],unDate[4]));
            
            console.log("prosao");
            let searchParams = null;
            if(props.history.location.state != null || props.history.location.state.searchParams != null)
                 searchParams = props.history.location.state.searchParams;
            
            if(searchParams == null || Object.keys(searchParams).length === 0){
                if(res.data.entityType === "ADVENTURE")
                    findNextAvailableDate(unaDates);
                else
                    findNextAvailableDateRange(unaDates);

                setUnavailableDates(unaDates);
            }
            else
                setFieldsWithSearchedParams(searchParams);
            console.log("posle ifa");
            setLoaded(true);
            setPrice(res.data.pricelists[0].entityPricePerPerson);
            setType(res.data.entityType);
            console.log("prosao");
            getUserById(getCurrentUser().id).then(resu =>{
                let usersLp = resu.data.loyaltyProgram;
                console.log(usersLp);
                getCurrentLoyaltyProgram().then(res=>{
                    setCurrentLoyaltyProgram(res.data);
                    console.log(res.data);
                    if(usersLp == "BRONZE")
                        setDiscount(res.data.clientBronzeDiscount);
                    if(usersLp == "SILVER")
                        setDiscount(res.data.clientSilverDiscount);
                    if(usersLp == "GOLD")
                        setDiscount(res.data.clientGoldDiscount);
                    
                })
            })


        });

    }, []);

    const setFieldsWithSearchedParams =(searchParams)=> {
        if (searchParams.endDate == null)
            setStartDate(searchParams.startDate);
        else
            setSelectionRange({
                startDate: searchParams.startDate,
                endDate: searchParams.endDate,
                key: 'selection'
            });
        if (!isNaN(searchParams.numOfPersons) && searchParams.numOfPersons != null)
            setPersonNumber(searchParams.numOfPersons);
    }

    const findUnavailableDates = (bookEntity)=>{
        var uDates = [];
        bookEntity.unavailableDates.forEach(e => {
            var startDateTime = new Date(e.startTime[0],e.startTime[1]-1,e.startTime[2],e.startTime[3],e.startTime[4]);
            console.log("UNA DATES");
            console.log(startDateTime);
            while(startDateTime.getTime() < new Date(e.endTime[0],e.endTime[1]-1,e.endTime[2],e.endTime[3],e.endTime[4]).getTime()){
                uDates.push(startDateTime);
                startDateTime = new Date(startDateTime.getTime()+ oneDay);
            }
        });
        console.log(uDates);
        for(var reservation of bookEntity.reservations)
        {
            console.log("===============NEW RESERVATION================");
            console.log(reservation);
            for(var i=0;i<reservation.numOfDays;i++){
                uDates.push(new Date(new Date(reservation.startDate).getTime()+i*oneDay));
                console.log("ADDED:");
                console.log(new Date(new Date(reservation.startDate).getTime()+i*oneDay));
            }
            if(new Date(reservation.startDate).getHours() >= 21){
                uDates.push(new Date(new Date(reservation.startDate).getTime()+reservation.numOfDays*oneDay));
                console.log("ADDED Extra:");
                console.log(new Date(new Date(reservation.startDate).getTime()+reservation.numOfDays*oneDay));
            }
            
        }
        setUnavailableDates(uDates);
    };

    const isDateUnavailable = (date, unavDates)=>{
        date.setHours(12);
        return unavDates.some(e =>{ 
            const date1WithoutTime = new Date(date.getTime());
            const date2WithoutTime = new Date(e.getTime());
            date1WithoutTime.setUTCHours(0, 0, 0, 0);
            date2WithoutTime.setUTCHours(0, 0, 0, 0);

            return date1WithoutTime.getTime() === date2WithoutTime.getTime();
        })
    };

    const isDateTimeUnavailable = (date, unavDates)=>{
        date.setHours(12);
        return unavDates.some(e =>{ 
            const date1WithoutTime = new Date(date.getTime());
            const date2WithoutTime = new Date(e.getTime());
            date1WithoutTime.setUTCHours(0, 0, 0, 0);
            date2WithoutTime.setUTCHours(0, 0, 0, 0);

            const date1 = new Date(date.getTime());
            const date2 = new Date(e.getTime());
            date1.setUTCHours(9,0,0,0);
            return date1WithoutTime.getTime() === date2WithoutTime.getTime() && date1 > date2;
        })
    };

    const findNextAvailableDateRange = (unavDates)=>{
        
        var nextAvailableDates = [new Date(), new Date(new Date().getTime() + oneDay)];
        var foundRange = false;
        console.log(unavDates);
        if(unavDates.length > 0)
            while(!foundRange){
                if(isDateTimeUnavailable(nextAvailableDates[0], unavDates)){
                    nextAvailableDates[0] = new Date(nextAvailableDates[0].getTime()+oneDay);
                }
                else{

                    nextAvailableDates[1] = new Date(nextAvailableDates[0].getTime()+oneDay);
                    
                    if(!isDateTimeUnavailable(nextAvailableDates[1], unavDates))
                        foundRange = true;
                    else{
                        unavDates.push(nextAvailableDates[0]);
                        nextAvailableDates[0] = new Date(nextAvailableDates[0].getTime()+oneDay);
                    }
                }
            }
        setSelectionRange({
            startDate: nextAvailableDates[0],
            endDate: nextAvailableDates[1],
            key: 'selection'
        });
    };

    const findNextAvailableDate=(unavDates)=>{
        var nextAvailableDate = new Date();
        var foundRange = false;
        console.log(unavDates);
        if(unavDates.length > 0)
            while(!foundRange){
                if(isDateTimeUnavailable(nextAvailableDate, unavDates)){
                    nextAvailableDate = new Date(nextAvailableDate.getTime()+oneDay);
                    foundRange = true;
                }
            }
        setStartDate(nextAvailableDate);
    }

    useEffect(() => {
        if(Object.keys(bookingEntity).length !== 0)
            for(var unavailableDate of bookingEntity.allUnavailableDates)
            {
                let unaDate = new Date(unavailableDate[0], unavailableDate[1]-1, unavailableDate[2], unavailableDate[3], unavailableDate[4]);

                if(isDateUnavailable(selectionRange.startDate, [unaDate]))
                    availableTimes.forEach(time => {
                        time.available = true;
                        if(parseInt(time.value.split(':')[0]) <= unaDate.getHours())
                            time.available = false;
                })
                if(isDateUnavailable(selectionRange.endDate, [unaDate])){
                    console.log("DATUM ZA PROVERU :"+unaDate);
                    availableTimes.forEach(function(time){
                        if(parseInt(time.value.split(':')[0]) >= unaDate.getHours())
                            time.available = false;
                    });
                    console.log("TIMES:");
                    console.log(availableTimes);
                }
            }
        for(var time of availableTimes){
            if(time.available == true){
                setCheckedTime(time);
                break;
            }
        }
        console.log(availableTimes);
        setTimes(availableTimes);
    }, [selectionRange]);

    // useEffect(() => {
    //     if(Object.keys(bookingEntity).length !== 0)
    //         for(var unavailableDate of bookingEntity.allUnavailableDates)
    //         {
    //             if(isDateUnavailable(startDate, [new Date(unavailableDate)]))
    //                 availableTimes.forEach(time => {
    //                     time.available = true;
    //                     if(parseInt(time.value.split(':')[0]) == new Date(unavailableDate).getHours())
    //                         time.available = false;
    //             })
    //         }
    //     for(var time of availableTimes){
    //         if(time.available == true){
    //             setCheckedTime(time);
    //             break;
    //         }
    //     }
    //     console.log(availableTimes);
    //     setTimes(availableTimes);
    // }, [selectionRange]);

    useEffect(() => {
        if(Object.keys(bookingEntity).length !== 0)
            changePrice();
    }, [personNumber]);

    const reserve = (event) => {
        event.preventDefault()
        var startDateTime = selectionRange.startDate;
        var endDateTime = selectionRange.endDate;
        startDateTime.setHours(0);
        startDateTime.setMinutes(0);
        endDateTime.setHours(0);
        endDateTime.setMinutes(0);
        console.log(startDateTime);
        console.log(endDateTime);
        
        var difference = endDateTime.getTime() - startDateTime.getTime();
        var days = Math.ceil(difference/(1000*3600*24)); 
        startDateTime.setHours(parseInt(checkedTime.value.split(':')[0]))
        startDateTime.setMinutes(0);
        var addServ = [];
        for(var serv of bookingEntity.pricelists[0].additionalServices){
            if(additionalServices.some(service=>{return service.id === serv.id}))
                addServ.push(serv);
        }
        var resDTO={
            startDate:`${format(startDateTime, "yyyy-MM-dd HH:mm")}`,
            numOfDays:days,
            numOfPersons:personNumber,
            additionalServices:addServ,
            fastReservation:false,
            bookingEntity:bookingEntity,
            canceled:false,
            cost:price,
            version:1,
            client:getCurrentUser()
        };
        setReservationDTO(resDTO);
        
        setOpenDialog(true);
    };

    const handleCloseDialog = () => {
        setOpenDialog(false);
    };

    const handleClick = () => {
        setOpen(true);
    };

    const handleClose = (_event, reason) => {
        if (reason === 'clickaway') {
            return;
        }
        setOpen(false);
    };

    const changePrice=()=>{
        var difference = selectionRange.endDate.getTime() - selectionRange.startDate.getTime();
        var daysBetweenDates = Math.ceil(difference/(1000*3600*24));
        console.log(daysBetweenDates);
        if(daysBetweenDates <= 0)
            daysBetweenDates = 1;
        setPrice(bookingEntity.pricelists[0].entityPricePerPerson*personNumber*daysBetweenDates+additionalPrice);

    };

    const goBack = () =>{
        history.goBack();
    };

    const confirmReservation = ()=>{
        console.log(reservationDTO);
        addReservation(reservationDTO).then(res=>{
            console.log("Adding temp res success");
            console.log(res.data);
            handleClick();
            setTypeAlert("success");
            setMessage("Successful reservation");
            history.goBack();
        }).catch(res => {
            setTypeAlert("error");
            setMessage(res.response.data);
            handleClick();
            setOpenDialog(false);
            return;
        })
    };

    const additionalServiceChecked =(event,service)=>{
        console.log(pricelistData);
        console.log("doslo je ovde");
        console.log(service);
        console.log(event.target.checked);
        if(event.target.checked == true){
            setPrice(price+service.price);
            setAdditionalPrice(additionalPrice+service.price);
            setAdditionalServices([...additionalServices, service]);
        }
        else{
            setPrice(price-service.price);
            setAdditionalPrice(additionalPrice-service.price);
            setAdditionalServices(additionalServices.filter(item => item.serviceName === service.serviceName));
        }
    };

    const radioButtonChanged=(event)=>{
        event.preventDefault();
        console.log(event.target.value);
        setCheckedTime(times.find(time => time.value === event.target.value));

    };

    const  handleSelect=(ranges)=>{
        if(ranges.selection.endDate.getDate() === ranges.selection.startDate.getDate())
            ranges.selection.endDate = new Date(ranges.selection.endDate.getTime() + oneDay);
        console.log("OPSEG JEE");
        console.log(ranges.selection);
        setSelectionRange(ranges.selection);
        var difference = ranges.selection.endDate.getTime() - ranges.selection.startDate.getTime();
        var daysBetweenDates = Math.ceil(difference/(1000*3600*24));
        console.log(daysBetweenDates);
        if(daysBetweenDates <= 0)
            daysBetweenDates = 1;
        setPrice(bookingEntity.pricelists[0].entityPricePerPerson*personNumber*daysBetweenDates+additionalPrice);
      };
    

    const paperStyle = { padding: 20, height: '70vh', width: 400, margin: "5% auto" }
    let btnstyle = { margin: '10px 30px', backgroundColor:'rgb(244, 177, 77)' }
    let btn2style = { margin: '10px 30px', backgroundColor:'rgb(5, 30, 52)' }

    const [startDate, setStartDate] = useState(new Date());

    const datePicker = <><TextField style={{margin:'10px 10px' }} onClick={()=>setOpenDate(!openDate)} label='Date picker' placeholder={`${format(startDate, "dd.MM.yyyy.")}`} 
                            value={`${format(startDate, "dd.MM.yyyy.")}`}
                            InputProps={{
                                startAdornment: (
                                <InputAdornment position="start">
                                    <DateRangeOutlined />
                                </InputAdornment>
                                )
                            }}
                                />
                                {openDate && <div style={{
                                        position:"absolute",
                                        zIndex:99999,
                                        backgroundColor:"white",
                                        border:"1px solid rgb(5, 30, 52)"
                                    }}
                                    >
                                    <Calendar
                                    date={startDate}
                                    onChange={(date)=>{setStartDate(date); setOpenDate(false);}}
                                    minDate={new Date()}
                                    disabledDates={unavailableDates}
                                    editableDateInputs={true}
                                    />
                                </div>
                                }
                        </>


    return (
        <div className='App'>
            <Grid >
                <Paper elevation={10} style={paperStyle}>
                    <Grid style={{margin:'10px 10px'}} align='left'>
                        {/* <Avatar style={avatarStyle}><LockOutlinedIcon /></Avatar> */}
                        <h2>Reservation</h2>
                    </Grid>
                    <Dialog open={openDialog} onClose={handleCloseDialog}>
                        <DialogTitle>Please Confirm Reservation</DialogTitle>
                        <DialogContent>
                            {isLoaded &&
                            <DialogContentText>
                                Reservation type:<b>{String(bookingEntity.entityType).toLowerCase()}</b> <br></br>
                                Name: <b>{bookingEntity.name} </b>             <br></br>
                                Place: <b>{bookingEntity.place.cityName+", "+bookingEntity.place.stateName}</b>    <br></br>
                                Date range: <b>{`${format(selectionRange.startDate, "dd.MM.yyyy.")}`} to {`${format(selectionRange.endDate, "dd.MM.yyyy.")}`}</b><br></br>
                                Number of persons: <b>{personNumber}</b><br></br>
                                Additional services selected:<b>{additionalServices.length!=0? additionalServices.map(service=>{
                                    return service.serviceName + " "+service.price+"€, ";
                                }):<p>None</p>}</b><br></br>
                                Time: <b>{checkedTime.text}</b> <br></br>
                                Price: <b>{price+"€"}</b>
                            </DialogContentText>
                            }
                        </DialogContent>
                        <DialogActions>
                            <Button onClick={handleCloseDialog}>Cancel</Button>
                            <Button onClick={confirmReservation}>Confirm</Button>
                        </DialogActions>
                    </Dialog>
                    <form onSubmit={reserve}>
                        {   type === "ADVENTURE"?datePicker: 
                        <><TextField style={{margin:'10px 10px'}} onClick={()=>setOpenDate(!openDate)} label='Date range' placeholder={`${format(selectionRange.startDate, "dd.MM.yyyy.")} to ${format(selectionRange.endDate, "dd.MM.yyyy.")}`} 
                                value={`${format(selectionRange.startDate, "dd.MM.yyyy.")} to ${format(selectionRange.endDate, "dd.MM.yyyy.")}`}
                                InputProps={{
                                    startAdornment: (
                                    <InputAdornment position="start">
                                        <DateRangeOutlined />
                                    </InputAdornment>
                                    ),
                                }}
                                />
                            {openDate && <div style={{
                                            position:"absolute",
                                            zIndex:99999,
                                            backgroundColor:"white",
                                            border:"1px solid rgb(5, 30, 52)"
                                        }}
                                        ><DateRange
                                            disabledDates={unavailableDates}
                                            onBlur={()=>setOpenDate(!openDate)}
                                            editableDateInputs={true}
                                            ranges={[selectionRange]}
                                            onChange={handleSelect}
                                            className="date"
                                            minDate={new Date()}
                                            />
                                     </div>}
                        </>
                        }
                        <div style={{zIndex:1}}>
                            <Grid>
                                <RemoveCircleOutlined style={{marginTop:'30px', width:32, height:32}} onClick={()=>personNumber > 1 && setPersonNumber(personNumber-1)}/>
                                <TextField style={{marginTop:'20px',textAlign:'center'}}   label='number of persons' value={personNumber}  />
                                <AddCircleOutlined style={{marginTop:'35px', width:32, height:32}} onClick={()=>personNumber < maxNumOfPersons && setPersonNumber(personNumber+1)}/>
                            </Grid>
                            <FormControl style={{marginTop:'20px', marginLeft:'10px', display:'flex'}}>
                                <FormLabel>Additional services:</FormLabel>
                                
                                <FormGroup>
                                    <Grid container style={{maxHeight:'85px', overflow:'auto'}}>
                                {(isLoaded && Object.keys(pricelistData.additionalServices).length !== 0) ? pricelistData.additionalServices.map((service,index)=>{
                                        return <Grid item xs={6}><FormControlLabel control={<Checkbox />} onChange={(event)=>additionalServiceChecked(event, service)} label={service.serviceName+" -"} /><b>{service.price}$</b></Grid>
                                    }):<h4>No additional services to choose</h4>}
                                    </Grid>
                                </FormGroup>
                            </FormControl>
                            <FormControl>
                                <FormLabel id="demo-row-radio-buttons-group-label" style={{margin:"10px 10px"}}>Select time:</FormLabel>
                                {
                                    isLoaded &&
                                    <RadioGroup
                                    style={{margin:"10px 20px"}}
                                    defaultValue={checkedTime.value}
                                    row
                                    onChange={radioButtonChanged}
                                    aria-labelledby="demo-row-radio-buttons-group-label"
                                    name="row-radio-buttons-group"
                                    required
                                >
                                    {
                                        isLoaded? times.map((time,index)=>{
                                            return <FormControlLabel value={time.value} control={<Radio />} label={time.text} disabled={!time.available}/>
                                        })
                                        :<></>
                                    }
                                </RadioGroup>

                                }
                                
                            </FormControl>
                            <hr></hr>
                            <div style={{margin:"10px 10px"}}>
                                <FormLabel>Price: </FormLabel><FormLabel style={{color:'rgb(5, 30, 52)', fontWeight:'bold'}}>{price+"€"}</FormLabel><br></br>
                                {discount > 0 && <><FormLabel>After discount: </FormLabel><FormLabel style={{color:'rgb(5, 30, 52)', fontWeight:'bold'}}>{price-price*discount+"€"}</FormLabel></>}
                            </div>
                            <br></br>
                            <div style={{justifyContent:'center', display:'flex', width:'100%'}}>
                                    <Button type='submit' color='primary' variant="contained" style={btnstyle} >Reserve</Button>
                                    <Button type='button' onClick={goBack} color='primary' variant="contained" style={btn2style} >Cancel</Button>
                            </div>
                        </div>
                    </form>
                    
                </Paper>
            </Grid>
            <Snackbar open={open} autoHideDuration={6000} onClose={handleClose}>
                <Alert onClose={handleClose} severity={typeAlert} sx={{ width: '100%' }}>
                    {message}
                </Alert>
            </Snackbar>
        </div>
    )
}