import React, { useEffect, useState } from 'react'
import { Grid, Paper, Avatar, TextField, Button, Typography, Link, Checkbox, FormControlLabel, ListItemText, InputAdornment, FormControl, FormLabel, FormGroup, Dialog, DialogTitle, DialogContent, DialogContentText, DialogActions, ListItem } from '@mui/material'
import { makeStyles } from '@mui/styles';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import { useHistory } from 'react-router-dom';
import 'react-date-range/dist/styles.css'; // main style file
import 'react-date-range/dist/theme/default.css'; // theme css file
import { format } from "date-fns";
import { DateRange  } from 'react-date-range';
import Radio from '@mui/material/Radio';
import RadioGroup from '@mui/material/RadioGroup';
import { Add, AddCircleOutlined, DateRangeOutlined, Label, Person, RemoveCircleOutlined } from '@mui/icons-material';
import { getBookingEntityById } from '../../service/BookingEntityService';
import { addReservation, addTemporaryReservation } from '../../service/ReservationService';
import { getCurrentUser } from '../../service/AuthService';

export default function NewReservationPage(props) {
    const [formData, setFormData] = useState({ email: '', password: '' });
    const [badInput, setBadInput] = useState(false);
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
    const history = useHistory();
    const [reservationDTO,setReservationDTO] = useState({});

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

    const makeChange = (event) => {
        setBadInput(false);
        setFormData(prevState => ({
            ...prevState,
            [event.target.name]: event.target.value
        }));
    }

    useEffect(() => {
        const entityId = props.history.location.state.bookingEntityId;
        console.log(entityId);
        getBookingEntityById(entityId).then(res => {
            console.log(res.data);
            res.data.pricelists.sort(function (a, b) {
                var key1 = a.startDate;
                var key2 = b.startDate;
            
                if (key1 < key2) {
                    return 1;
                } else if (key1 == key2) {
                    return 0;
                } else {
                    return -1;
                }
            });
            if(String(res.data.entityType) !== "COTTAGE")
                setMaxNumOfPersons(res.data.maxNumOfPersons);
            else    
                setMaxNumOfPersons(50);
            setBookingEntity(res.data);
            setPricelistData(res.data.pricelists[0]);
            findUnavailableDates(res.data);
            setLoaded(true);
            setPrice(res.data.pricelists[0].entityPricePerPerson);
        });
    }, []);

    const findUnavailableDates = (bookEntity)=>{
        var uDates = [];
        bookEntity.unavailableDates.forEach(e => {
            //----------DODATNO PROVERITI PO SATIM------------------------
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
            for(var i=0;i<reservation.numOfDays;i++){
                
                uDates.push(new Date(new Date(reservation.startDate).getTime()+i*oneDay));
            }
            if(new Date(reservation.startDate).getHours() >= 21)
                uDates.push(new Date(new Date(reservation.startDate).getTime()+reservation.numOfDays*oneDay));
        }
        setUnavailableDates(uDates);
        findNextAvailableDateRange(uDates);

    };

    const isDateUnavailable = (date, unavDates)=>{
        return unavDates.some(e =>{ 
            date.setHours(12);
            const date1WithoutTime = new Date(date.getTime());
            const date2WithoutTime = new Date(e.getTime());
            date1WithoutTime.setUTCHours(0, 0, 0, 0);
            date2WithoutTime.setUTCHours(0, 0, 0, 0);
            console.log(date1WithoutTime);
            console.log(date2WithoutTime);

            return date1WithoutTime.getTime() === date2WithoutTime.getTime();
        })
    };

    const findNextAvailableDateRange = (unavDates)=>{
        
        var nextAvailableDates = [new Date(), new Date(new Date().getTime() + oneDay)];
        var foundRange = false;
        while(!foundRange){
            if(isDateUnavailable(nextAvailableDates[0],unavDates))
            {
                nextAvailableDates[0] = new Date(nextAvailableDates[0].getTime()+oneDay);
            }
            else{
                nextAvailableDates[1] = new Date(nextAvailableDates[0].getTime()+oneDay);
                if(!isDateUnavailable(nextAvailableDates[1],unavDates))
                    foundRange = true;
            }
        }
        setSelectionRange({
            startDate: nextAvailableDates[0],
            endDate: nextAvailableDates[1],
            key: 'selection'
        });
    };

    useEffect(() => {
        if(Object.keys(bookingEntity).length !== 0)
            for(var reservation of bookingEntity.reservations)
            {
                if(isDateUnavailable(selectionRange.startDate, [new Date(new Date(reservation.startDate).getTime()+reservation.numOfDays*oneDay)]))
                    availableTimes.forEach(time => {
                        time.available = true;
                        if(parseInt(time.value.split(':')[0]) <= new Date(reservation.startDate).getHours())
                            time.available = false;
                })
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

        }).catch(res=>{
            console.log("Adding temp res failed");
        });
        history.goBack();
    };

    const additionalServiceChecked =(event,service)=>{
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
        setSelectionRange(ranges.selection);
        var difference = ranges.selection.endDate.getTime() - ranges.selection.startDate.getTime();
        var daysBetweenDates = Math.ceil(difference/(1000*3600*24));
        console.log(daysBetweenDates);
        if(daysBetweenDates == 0)
            daysBetweenDates = 1;
        setPrice(bookingEntity.pricelists[0].entityPricePerPerson*personNumber*daysBetweenDates+additionalPrice);
      };
    

    const paperStyle = { padding: 20, height: '70vh', width: 400, margin: "5% auto" }
    const avatarStyle = { backgroundColor: 'rgb(244, 177, 77)' }
    let btnstyle = { margin: '10px 30px', backgroundColor:'rgb(244, 177, 77)' }
    let btn2style = { margin: '10px 30px', backgroundColor:'rgb(5, 30, 52)' }


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
                        <TextField style={{margin:'10px 10px'}} onClick={()=>setOpenDate(!openDate)} label='Date range' placeholder={`${format(selectionRange.startDate, "dd.MM.yyyy.")} to ${format(selectionRange.endDate, "dd.MM.yyyy.")}`} 
                            value={`${format(selectionRange.startDate, "dd.MM.yyyy.")} to ${format(selectionRange.endDate, "dd.MM.yyyy.")}`}
                            InputProps={{
                                startAdornment: (
                                  <InputAdornment position="start">
                                    <DateRangeOutlined />
                                  </InputAdornment>
                                ),
                              }}
                               />
                        {openDate && <DateRange
                            style={{
                                position:"absolute",
                                zIndex:99999,
                                backgroundColor:"white",
                                border:"1px solid rgb(5, 30, 52)"
                            }}
                            disabledDates={unavailableDates}
                            onBlur={()=>setOpenDate(!openDate)}
                            editableDateInputs={true}
                            ranges={[selectionRange]}
                            onChange={handleSelect}
                            className="date"
                            minDate={new Date()}
                        />}
                        <div style={{zIndex:1}}>
                            <Grid>
                                <RemoveCircleOutlined style={{marginTop:'30px', width:32, height:32}} onClick={()=>personNumber > 1 && setPersonNumber(personNumber-1)}/>
                                <TextField style={{marginTop:'20px',textAlign:'center'}}   label='number of persons' value={personNumber}  />
                                <AddCircleOutlined style={{marginTop:'35px', width:32, height:32}} onClick={()=>personNumber < maxNumOfPersons && setPersonNumber(personNumber+1)}/>
                            </Grid>
                            <FormControl style={{marginTop:'20px', marginLeft:'10px', display:'flex'}}>
                                <FormLabel>Additional services:</FormLabel>
                                
                                <FormGroup>
                                {(isLoaded && Object.keys(pricelistData.additionalServices).length !== 0) ? pricelistData.additionalServices.map((service,index)=>{
                                        return <div display="inline-block"><FormControlLabel control={<Checkbox />} onChange={(event)=>additionalServiceChecked(event, service)} label={service.serviceName+" -"} /><b>{service.price}$</b></div>
                                    }):<h4>No additional services to choose</h4>}
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
                                <FormLabel>Price: </FormLabel><FormLabel style={{color:'rgb(5, 30, 52)', fontWeight:'bold'}}>{price+"€"}</FormLabel>
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
        </div>
    )
}