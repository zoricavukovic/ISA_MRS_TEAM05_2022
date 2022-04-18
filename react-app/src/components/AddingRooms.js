import * as React from 'react';
import Box from '@mui/material/Box';
import Chip from '@mui/material/Chip';
import Button from '@mui/material/Button';
import Grid from '@mui/material/Grid';
import Stack from '@mui/material/Stack';
import Divider from '@mui/material/Divider';
import Typography from '@mui/material/Typography';
import TextField from '@mui/material/TextField';
import RemoveCircleIcon from '@mui/icons-material/RemoveCircle';

import Input from '@mui/material/Input';
import FilledInput from '@mui/material/FilledInput';
import OutlinedInput from '@mui/material/OutlinedInput';
import InputLabel from '@mui/material/InputLabel';
import InputAdornment from '@mui/material/InputAdornment';
import FormHelperText from '@mui/material/FormHelperText';
import FormControl from '@mui/material/FormControl';
import Visibility from '@mui/icons-material/Visibility';
import VisibilityOff from '@mui/icons-material/VisibilityOff';

export default function AddingRooms() {
  const [values, setValues] = React.useState({
    amount: '',
    password: '',
    weight: '',
    weightRange: '',
    showPassword: false,
  });
  const handleChange = (prop) => (event) => {
    setValues({ ...values, [prop]: event.target.value });
  };
  return (
    <Box sx={{ ml: '6%', width: '100%', maxWidth: 350, bgcolor: 'background.paper' }}>
      <Box sx={{ my: 2, mx: 3 }}>
        <Grid container alignItems="center">
          <Grid item xs>
            <Typography gutterBottom variant="h5" component="div"  style={{color: 'rgb(5, 30, 52)', marginLeft:'2.5%'}}>
              Rooms
            </Typography>
          </Grid>
          <Grid item>
          <TextField size="small" style={{width:'60%', marginLeft:'2.5%'}} id="outlined-basic" label="Add service" variant="outlined" />
          <FormControl fullWidth sx={{ m: 1 }}>
          <InputLabel htmlFor="outlined-adornment-amount">Amount</InputLabel>
          <OutlinedInput style={{width:'60%'}} size="small" 
            id="outlined-adornment-amount"
            value={values.amount}
            startAdornment={<InputAdornment position="start">€</InputAdornment>}
            label="Amount"
          />
        </FormControl>
          <Button label="Extra Soft" style={{color: 'rgb(5, 30, 52)', fontWeight: 'bold',borderRadius:'10px', marginLeft:'2.5%', marginTop:'3%',backgroundColor: 'rgb(244, 177, 77)'}}>Add</Button>
          </Grid>
        </Grid>
        
      </Box>
      <Divider variant="middle" />
      <Box sx={{ m: 2,  ml:2.5}}>
        <Typography gutterBottom variant="body1">
          Added Additional Services
        </Typography>
        <Stack  sx={{
            mb: 3,
            pb: 1,
            display: 'grid',
            gap: 1,
            gridTemplateColumns: 'repeat(2, 1fr)',
          }}direction="row" spacing={1}>
          <Button style={{borderRadius:'10px'}} label="Extra Soft" >Delete<RemoveCircleIcon/></Button>
          
        </Stack>
      </Box>
      
    </Box>
  );
}