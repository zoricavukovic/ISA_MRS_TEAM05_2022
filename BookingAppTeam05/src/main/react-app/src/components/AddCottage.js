import * as React from 'react';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import { styled } from '@mui/material/styles';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';
import Slider from '@mui/material/Slider';
import MuiInput from '@mui/material/Input';
import VolumeUp from '@mui/icons-material/VolumeUp';
import AddingAdditionalService from './AddingAdditionalService.js';
import AddingRooms from './AddingRooms.js';
import OutlinedInput from '@mui/material/OutlinedInput';
import InputLabel from '@mui/material/InputLabel';
import InputAdornment from '@mui/material/InputAdornment';
import FormHelperText from '@mui/material/FormHelperText';
import FormControl from '@mui/material/FormControl';

const Input = styled(MuiInput)`
  width: 42px;
`;
export default function AddCottage() {

  const [value, setValue] = React.useState(0);

  const handleSliderChange = (event, newValue) => {
    setValue(newValue);
  };

  const handleInputChange = (event) => {
    setValue(event.target.value === '' ? '' : Number(event.target.value));
  };

  const handleBlur = () => {
    if (value < 0) {
      setValue(0);
    } else if (value > 1) {
      setValue(1);
    }
  };

  return (
    <div style={{ backgroundColor: 'aliceblue', margin: '5%', padding: '1%', borderRadius: '10px', height: '100%' }} >
        <div style={{ color: 'rgb(5, 30, 52)', fontWeight: 'bold', textAlign:'center', backgroundColor: 'rgb(244, 177, 77)', marginLeft:'42%', padding: '1%', borderRadius: '10px', width: '15%' }} >
            Add New Cottage
        </div>
        <Box sx={{ marginTop:'1%', marginLeft:'5%', marginRight:'5%', width: '90%'}}>
        <Box
        component="form"
        sx={{
            '& .MuiTextField-root': { m: 1, width: '28ch' },
        }}
        noValidate
        autoComplete="off"
        >

            <h4 style={{ color: 'rgb(5, 30, 52)', fontWeight: 'bold'}}>Basic Information About Cottage</h4>
        <div style={{ display:'block', color: 'rgb(5, 30, 52)', fontWeight: 'bold', textAlign:'center', backgroundColor: 'rgb(191, 230, 255)', margin: '1%', padding: '1%', borderRadius: '10px', width:'100%', height: '100%' }} >
           
            <TextField
            id="outlined-textarea"
            label="Cottage Name"
            placeholder="Cottage Name"
            multiline
            size="small"
            style={{width:'200px'}}
            />
            <FormControl sx={{ m: 1 }}>
          <InputLabel htmlFor="outlined-adornment-amount">Cost Per Night</InputLabel>
          <OutlinedInput
            id="outlined-adornment-amount"
            size="small"
            placeholder="Cost Per Night"
            startAdornment={<InputAdornment position="start">€</InputAdornment>}
            label="Cost Per Night"
          />
        </FormControl>
            <TextField
            id="outlined-textarea"
            label="Address"
            placeholder="Address"
            multiline
            size="small"
            style={{width:'200px'}}
            />
            <TextField
            id="outlined-textarea"
            label="City Name"
            placeholder="City"
            multiline
            size="small"
            style={{width:'200px'}}
            />
            <TextField
            id="outlined-textarea"
            label="Zip Code"
            placeholder="Zip Code"
            multiline
            size="small"
            style={{width:'200px'}}
            />
            <TextField
            id="outlined-textarea"
            label="State Name"
            placeholder="State"
            multiline
            size="small"
            style={{width:'200px'}}
            />
            
            <TextField
            size="small"
            id="outlined-multiline-static"
            label="Promo Description"
            multiline
            rows={2}
            placeholder="Promo Description"
            style={{width:'200px'}}
            />
        </div>
        
        </Box>
        </Box>
        <AddingAdditionalService float="left"/>
        <AddingRooms float="left"/>
        </div>
  );
}