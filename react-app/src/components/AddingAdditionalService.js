import * as React from 'react';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import { styled } from '@mui/material/styles';
import Grid from '@mui/material/Grid';
import Stack from '@mui/material/Stack';
import Divider from '@mui/material/Divider';
import Typography from '@mui/material/Typography';
import TextField from '@mui/material/TextField';
import Chip from '@mui/material/Chip';
import Paper from '@mui/material/Paper';
import TagFacesIcon from '@mui/icons-material/TagFaces';
import { useEffect } from "react";

import OutlinedInput from '@mui/material/OutlinedInput';
import InputLabel from '@mui/material/InputLabel';
import InputAdornment from '@mui/material/InputAdornment';
import FormControl from '@mui/material/FormControl';
import { useForm } from "react-hook-form";

const ListItem = styled('li')(({ theme }) => ({
  margin: theme.spacing(0.5),
}));


export default function AddingAdditionalService(props) {

  const [chipData, setChipData] = React.useState([
  ]);
  const { register, handleInsertSubmit, formState: { errors } } = useForm();

  useEffect(() => {
    let chipArray = [];
    let keyToInsert = 1;
    for (let obj of props.services) {
        let chipString = obj.serviceName + ":" + obj.price;
        chipArray.push({key: keyToInsert, label:chipString});
        keyToInsert++;
    }
    setChipData(chipArray);
    props.onClick(chipArray);
  }, []);

  const [addSer, setAddSer] = React.useState({
    amount:"",
    service:""
  });

  const makeChange = (event) => {
    setAddSer(prevState => ({
        ...prevState,
        [event.target.name]: event.target.value
    }));
}

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
    let amountAndService = addSer.service + ":" + addSer.amount + " €";
    if ((addSer.service === "") || (addSer.amount==="")) return;
    for (let obj of chipData){
      let chipString = obj.label;
      if ((chipString === amountAndService) || (addSer.service === "") || (addSer.amount==="")) return;
    }
    let data = {key:keyToInsert, label:amountAndService}
    let newArray = chipData;
    newArray.push(data);
    setChipData(newArray);
    setChipData((chips) => chips.filter((chip) => chip.key !== null));
    props.onClick(chipData);
  };

  return (
    <Box sx={{ ml: '4%', width: '100%', maxWidth: 350, bgcolor: 'background.paper' }}
    component="form"
      //noValidate
      //onSubmit={handleInsertNewItem}
      >
      <Box sx={{ my: 2, mx: 3 }}>
        <Grid container alignItems="center">
          <Grid item xs>
            <Typography gutterBottom variant="h5" component="div" style={{ color: 'rgb(5, 30, 52)', marginLeft: '2.5%' }}>
              Additional Services
            </Typography>
          </Grid>
          <Grid item>
            <TextField name="service" size="small" onChange={makeChange} style={{ width: '60%', marginLeft: '2.5%' }} id="outlined-basic" label="Add service" variant="outlined"
            //{...register("service", { required: true, maxLength: 10 })} 
            />
            {errors.service && <p style={{ color: '#ED6663' }}>Please check service name</p>}
            <br />
    
            <FormControl fullWidth sx={{ m: 1 }}>
              <InputLabel htmlFor="outlined-adornment-amount">Amount</InputLabel>
              <OutlinedInput onChange={makeChange} name="amount" style={{ width: '60%' }} size="small"
                id="outlined-adornment-amount"
                placeholder="Amount"
                type="number"
                value={addSer.value}
                startAdornment={<InputAdornment position="start">€</InputAdornment>}
                label="Amount"
                //{...register("amount", { required: true, min: 1, max: 100000 })}
              />
            </FormControl>
            {errors.amount && <p style={{ color: '#ED6663' }}>Please enter valid amount</p>}
            <Button onClick={handleInsertNewItem} label="Extra Soft" style={{ color: 'rgb(5, 30, 52)', fontWeight: 'bold', borderRadius: '10px', marginLeft: '2.5%', marginTop: '3%', backgroundColor: 'rgb(244, 177, 77)' }}>Add</Button>
          </Grid>
        </Grid>

      </Box>
      <Divider variant="middle" />
      <Box sx={{ m: 2, ml: 2.5 }}>
        <Typography gutterBottom variant="body1">
          Added Additional Services
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