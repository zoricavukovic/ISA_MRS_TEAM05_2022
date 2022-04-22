import * as React from 'react';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Grid from '@mui/material/Grid';
import Stack from '@mui/material/Stack';
import Divider from '@mui/material/Divider';
import Typography from '@mui/material/Typography';
import TextField from '@mui/material/TextField';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import { styled } from '@mui/material/styles';
import Chip from '@mui/material/Chip';
import Paper from '@mui/material/Paper';
import TagFacesIcon from '@mui/icons-material/TagFaces';
import { useEffect } from "react";
import { useForm } from "react-hook-form";

const ListItem = styled('li')(({ theme }) => ({
    margin: theme.spacing(0.5),
}));

export default function AddingRulesOfConduct(props) {
    const [chipData, setChipData] = React.useState([]);

    const [conductData, setConductData] = React.useState({
        ruleName: '',
        allowed: true
    });
    useEffect(() => {
        let chipArray = [];
        let keyToInsert = 1;
        for (let obj of props.rules) {
            let chipString = obj.ruleName + ":" + obj.allowed;
            chipArray.push({key: keyToInsert, label:chipString});
            keyToInsert++;
        }
        setChipData(chipArray);
        props.onClick(chipArray);
      }, []);

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
        if (chipData.length !== 0) {
            keyToInsert = chipData[chipData.length - 1].key + 1
        }
        if (conductData.ruleName === "") return;
        let rules = conductData.ruleName + ":" + conductData.allowed;
        for (let obj of chipData) {
            let chipString = obj.label;
            if ((chipString === rules) || (conductData.ruleName === "")) return;
        }
        let data = { key: keyToInsert, label: rules }
        let newArray = chipData;
        newArray.push(data);
        setChipData(newArray);
        setChipData((chips) => chips.filter((chip) => chip.key !== null));
        props.onClick(chipData);

    };

    const makeChange = (event) => {
        if (event.target.name === "allowed"){
            setConductData(prevState => ({
                ...prevState,
                [event.target.name]: event.target.checked
            }));
        }else{
            setConductData(prevState => ({
                ...prevState,
                [event.target.name]: event.target.value
            }));
        }
        
    }
    return (
        <Box sx={{ ml: '6%', width: '100%', maxWidth: 350, bgcolor: 'background.paper' }}>
            <Box sx={{ my: 2, mx: 3 }}>
                <Grid container alignItems="center">
                    <Grid item xs>
                        <Typography gutterBottom variant="h5" component="div" style={{ color: 'rgb(5, 30, 52)', marginLeft: '2.5%' }}>
                            Rules Of Conduct
            </Typography>
                        <TextField
                            name="ruleName"
                            onChange={makeChange}
                            id="outlined-textarea"
                            label="Rule Name"
                            placeholder="Rule Name"
                            multiline
                            size="small"
                            style={{ width: '200px' }}
                        />
                        <FormControlLabel control={<Checkbox name="allowed"
                            onChange={makeChange} defaultChecked />} label="Rule Allowed" />
                    </Grid>

                </Grid>
                <Button onClick={handleInsertNewItem} label="Extra Soft" style={{ color: 'rgb(5, 30, 52)', fontWeight: 'bold', borderRadius: '10px', marginLeft: '2.5%', marginTop: '3%', backgroundColor: 'rgb(244, 177, 77)' }}>Add</Button>

            </Box>
            <Divider variant="middle" />
            <Box sx={{ m: 2, ml: 2.5 }}>
                <Typography gutterBottom variant="body1">
                    Added Rules Of Conduct
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