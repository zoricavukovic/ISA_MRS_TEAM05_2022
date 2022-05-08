
import React, { useEffect, useState } from "react";
import Box from '@mui/material/Box';
import IconButton from '@mui/material/IconButton';
import Input from '@mui/material/Input';
import FilledInput from '@mui/material/FilledInput';
import OutlinedInput from '@mui/material/OutlinedInput';
import InputLabel from '@mui/material/InputLabel';
import InputAdornment from '@mui/material/InputAdornment';
import FormHelperText from '@mui/material/FormHelperText';
import Button from '@mui/material/Button';
import FormControl from '@mui/material/FormControl';
import TextField from '@mui/material/TextField';
import { useHistory } from "react-router-dom";
import { useForm } from "react-hook-form";
import Visibility from '@mui/icons-material/Visibility';
import VisibilityOff from '@mui/icons-material/VisibilityOff';
import { getCurrentUser } from "../service/AuthService";


export default function ChangePassword() {

    const { register, handleSubmit, formState: { errors } } = useForm();
    const history = useHistory();

    const [show, setShow] = React.useState({
        showCurrentPassword: false,
        showNewPassword: '',
        showRetypedPassword: '',
    });

    const handleClickShowCurrentPassword = () => {
        setShow({
            ...show,
            showCurrentPassword: !show.showCurrentPassword,
        });
    };

    const handleClickShowNewPassword = () => {
        setShow({
            ...show,
            showNewPassword: !show.showNewPassword,
        });
    };

    const handleClickShowRetypedPassword = () => {
        setShow({
            ...show,
            showRetypedPassword: !show.showRetypedPassword,
        });
    };

    const onFormSubmit = data => {
        console.log(data.currentPassword);
        console.log(data.newPassword);
        console.log(data.retypedPassword);
    }

    useEffect(() => {
        if (getCurrentUser() === null || getCurrentUser() === undefined) {
            history.push('/login');
        }
    }, []);

    return (
        <div style={{ margin: '10% 40% 1% 40%', padding: '1%', borderRadius: '10px', height: '40%' }} >

            <Box
                component="form"
                noValidate
                onSubmit={handleSubmit(onFormSubmit)}
            >
                <FormControl sx={{ m: 1, width: '250px' }} variant="standard">
                    <InputLabel>Type current password</InputLabel>
                    <Input
                        id="currentPassword"
                        name="currentPassword"
                        type={show.showCurrentPassword ? 'text' : 'password'}
                        placeholder="Type current password"
                        endAdornment={
                            <InputAdornment position="end">
                                <IconButton
                                    aria-label="toggle password visibility"
                                    onClick={handleClickShowCurrentPassword}
                                >
                                    {show.showCurrentPassword ? <VisibilityOff /> : <Visibility />}
                                </IconButton>
                            </InputAdornment>
                        }
                        {...register("currentPassword", { required: true, maxLength: 50 })}
                    />
                </FormControl>
                {errors.currentPassword && <p style={{ color: '#ED6663' }}>Please check current password. Max 50 chars</p>}

                <FormControl sx={{ m: 1, width: '250px' }} variant="standard">
                    <InputLabel>Type new password</InputLabel>
                    <Input
                        id="newPassword"
                        name="newPassword"
                        type={show.showNewPassword ? 'text' : 'password'}
                        placeholder="Type new password"
                        endAdornment={
                            <InputAdornment position="end">
                                <IconButton
                                    aria-label="toggle password visibility"
                                    onClick={handleClickShowNewPassword}
                                >
                                    {show.showNewPassword ? <VisibilityOff /> : <Visibility />}
                                </IconButton>
                            </InputAdornment>
                        }
                        {...register("newPassword", { required: true, maxLength: 50 })}
                    />
                </FormControl>
                {errors.newPassword && <p style={{ color: '#ED6663' }}>Please check new password. Max 50 chars</p>}

                <FormControl sx={{ m: 1, width: '250px' }} variant="standard">
                    <InputLabel>Retype new password</InputLabel>
                    <Input
                        id="retypedPassword"
                        name="retypedPassword"
                        type={show.showRetypedPassword ? 'text' : 'password'}
                        placeholder="Retype password"
                        endAdornment={
                            <InputAdornment position="end">
                                <IconButton
                                    aria-label="toggle password visibility"
                                    onClick={handleClickShowRetypedPassword}
                                >
                                    {show.showRetypedPassword ? <VisibilityOff /> : <Visibility />}
                                </IconButton>
                            </InputAdornment>
                        }
                        {...register("retypedPassword", { required: true, maxLength: 50 })}
                    />
                </FormControl>
                {errors.retypedPassword && <p style={{ color: '#ED6663' }}>Please check retyped password. Max 50 chars</p>}
                <Button type="submit" onSubmit={handleSubmit(onFormSubmit)} variant="contained" style={{ color: 'rgb(5, 30, 52)', fontWeight: 'bold', textAlign: 'center', backgroundColor: 'rgb(244, 177, 77)', marginLeft: '33.5%', marginTop: '1%', padding: '1%', borderRadius: '10px', width: '15%' }}>
                    Save
                </Button>
            </Box>
        </div>
    );
}