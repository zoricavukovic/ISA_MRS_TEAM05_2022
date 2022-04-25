import * as React from 'react';
import Box from '@mui/material/Box';
import Chip from '@mui/material/Chip';
import Button from '@mui/material/Button';
import Grid from '@mui/material/Grid';
import Divider from '@mui/material/Divider';
import Typography from '@mui/material/Typography';
import TextField from '@mui/material/TextField';
import { styled } from '@mui/material/styles';
import Paper from '@mui/material/Paper';
import { useForm } from "react-hook-form";



const ListItem = styled('li')(({ theme }) => ({
  margin: theme.spacing(0.5),
}));


export default function AddingAdditionalServiceAdventure(props) {

  const { register, handleSubmit, formState: { errors } } = useForm();

  return (
    <Box
      sx={{ ml: '6%', width: '100%', maxWidth: 350, bgcolor: 'background.paper' }}
      component="form"
      noValidate
      onSubmit={handleSubmit(props.onSubmit)}
    >
      <Box sx={{ my: 2, mx: 3 }}>
        <Grid container alignItems="left" display="flex" flexDirection="column" justifyContent="left">
          <Grid item xs>
            <Typography gutterBottom variant="h5" component="div" style={{ color: 'rgb(5, 30, 52)', marginLeft: '2.5%' }}>
              Additional Services
            </Typography>
          </Grid>
          <Grid item>
            <TextField
              size="small"
              name="additionalService"
              style={{ width: '60%', marginLeft: '2.5%' }}
              id="additionalService"
              label="Add service"
              placeholder="Additional Service"
              variant="outlined"
              {...register("additionalService", { required: true, maxLength: 50 })}
            />
          </Grid>
          {errors.additionalService && <p style={{ color: '#ED6663' }}>Please check service name</p>}
          <br />
          <Grid item>
            <TextField
              size="small"
              type="number"
              name="amount"
              style={{ width: '60%', marginLeft: '2.5%' }}
              id="amount"
              label="Amount €"
              placeholder="Amount €"
              variant="outlined"
              {...register("amount", { required: true, min: 1, max: 100000 })}
            />
            {errors.amount && <p style={{ color: '#ED6663' }}>Please enter valid amount</p>}
          </Grid>
          <Grid item>
            <Button
              label="Extra Soft"
              style={{
                color: 'rgb(5, 30, 52)',
                fontWeight: 'bold',
                borderRadius: '10px',
                marginLeft: '2.5%',
                marginTop: '3%',
                backgroundColor: 'rgb(244, 177, 77)'
              }}
              onClick={handleSubmit(props.onSubmit)}
            >
              Add
            </Button>
          </Grid>
        </Grid>

      </Box>
      <Divider variant="middle" />

      <Box sx={{ m: 2, ml: 2.5 }}>
        <Typography gutterBottom variant="body1">
          Added Additional Services
        </Typography>
        <Paper
          sx={{
            display: 'flex',
            flexDirection: 'column',
            justifyContent: 'center',
            flexWrap: 'wrap',
            listStyle: 'none',
            p: 0.5,
            m: 0,
          }}
          component="ul"
        >
          {props.data.map((data) => {
            return (
              <ListItem key={data.key}>
                <Chip
                  label={"Service: " + data.serviceName + ',  Amount: ' + data.price + ' €'}
                  onDelete={() => props.onDeleteChip(data)}
                />
              </ListItem>
            );
          })}
        </Paper>

      </Box>

    </Box>
  );
}