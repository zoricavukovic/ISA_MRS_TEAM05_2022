import * as React from 'react';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Grid from '@mui/material/Grid';
import Stack from '@mui/material/Stack';
import Divider from '@mui/material/Divider';
import Typography from '@mui/material/Typography';
import TextField from '@mui/material/TextField';
import Chip from '@mui/material/Chip';
import Paper from '@mui/material/Paper';
import TagFacesIcon from '@mui/icons-material/TagFaces';
import { styled } from '@mui/material/styles';
import { useEffect } from "react";

const ListItem = styled('li')(({ theme }) => ({
  margin: theme.spacing(0.5),
}));

export default function AddingRooms(props) {

  const [chipData, setChipData] = React.useState([
  ]);

  useEffect(() => {
    let chipArray = [];
    let keyToInsert = 1;
    for (let obj of props.rooms) {
        let chipString = obj.roomNum + ":" + obj.numOfBeds;
        chipArray.push({key: keyToInsert, label:chipString});
        keyToInsert++;
    }
    setChipData(chipArray);
    props.onClick(chipArray);
  }, []);


  const [roomData, setRoomData] = React.useState({
    roomName:"",
    numOfBeds:""
  });

  const handleDelete = (chipToDelete) => () => {
    setChipData((chips) => chips.filter((chip) => chip.key !== chipToDelete.key));
    let newArray = [];
        for (let obj of chipData) {
            let chipString = obj.label;
            if (chipString !== chipToDelete.label) newArray.push(obj);
        }
        setChipData(newArray);
        props.onDelete(newArray);
  };

  const handleInsertNewItem = (event) => {
    let keyToInsert = 1;
    if (chipData.length !== 0){
      keyToInsert = chipData[chipData.length-1].key + 1
    }
    if ((roomData.roomName === "") || (roomData.numOfBeds==="")) return;
    let roomBeds = roomData.roomName + ":" + roomData.numOfBeds;
    for (let obj of chipData){
      let chipString = obj.label;
      if ((chipString === roomBeds)|| ( roomData.roomName === "") || (roomData.numOfBeds==="")) return;
    }
    const data = {key:keyToInsert, label:roomBeds}
    const newArray = chipData;
    newArray.push(data);
    setChipData(newArray);
    setChipData((chips) => chips.filter((chip) => chip.key !== null));
    props.onClick(chipData);
  };
  const makeChange = (event)=>{
    setRoomData(prevState => ({
        ...prevState,
        [event.target.name]: event.target.value
    }));
}

  
  return (
  
    <Box sx={{ ml: '6%', width: '100%', maxWidth: 350, bgcolor: 'background.paper' }}>
      <Box sx={{ my: 2, mx: 3 }}>
        <Grid container alignItems="center">
          <Grid item xs>
            <Typography gutterBottom variant="h5" component="div"  style={{color: 'rgb(5, 30, 52)', marginLeft:'2.5%'}}>
              Rooms
            </Typography>
            <TextField
          id="outlined-number"
          label="Room Number"
          name="roomName"
          onChange={makeChange}
          type="number"
          size="small"
          placeholder="Room Number"
          value={roomData.roomName}
          InputLabelProps={{
            shrink: true,
          }}
        />
        <TextField style={{marginTop:"5%"}} onChange={makeChange}
          id="outlined-number"
          label="Num Of Beds"
          type="number"
          size="small"
          name="numOfBeds"
          placeholder="Num Of Beds"
          value={roomData.numOfBeds}
          InputLabelProps={{
            shrink: true,
          }}
        />
          </Grid>
          
        </Grid>
        <Button onClick={handleInsertNewItem} label="Extra Soft" style={{color: 'rgb(5, 30, 52)', fontWeight: 'bold',borderRadius:'10px', marginLeft:'2.5%', marginTop:'3%',backgroundColor: 'rgb(244, 177, 77)'}}>Add</Button>
      </Box>
      <Divider variant="middle" />
      <Box sx={{ m: 2,  ml:2.5}}>
        <Typography gutterBottom variant="body1">
          Added Rooms
        </Typography>
        <Stack sx={{
          mb: 3,
          pb: 1,
          display: 'grid',
          gap: 1,
          gridTemplateColumns: 'repeat(2, 1fr)',
        }} direction="row" spacing={1}>

          <Paper
            sx={{
              display: 'flex',
              justifyContent: 'center',
              flexWrap: 'wrap',
              listStyle: 'none',
              p: 0.5,
              m: 0,
            }}
            component="ul"
          >
            {chipData.map((data) => {
              let icon;

              if (data.label === 'React') {
                icon = <TagFacesIcon />;
              }

              return (
                <ListItem key={data.key}>
                  <Chip
                    icon={icon}
                    label={data.label}
                    onDelete={data.label === 'React' ? undefined : handleDelete(data)}
                  />
                </ListItem>
              );
            })}
          </Paper>


        </Stack>
      </Box>
      
    </Box>
  );
}