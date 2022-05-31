import React, { useEffect, useState } from "react";
import Box from "@mui/material/Box";
import { Divider } from "@mui/material";
import { Dialog } from "@mui/material";
import { DialogActions } from "@mui/material";
import Alert from '@mui/material/Alert';
import { getAllNewAccountRequests, getAllUsers, giveResponseForNewAccountRequest, logicalDeleteUserById, userLoggedInAsSuperAdminOrAdminWithResetedPassword } from "../../../service/UserService";
import { DataGrid } from "@mui/x-data-grid";
import { Checkbox, TextareaAutosize } from "@mui/material";
import { Snackbar } from "@mui/material";
import Avatar from '@mui/material/Avatar';
import { deepOrange } from '@mui/material/colors';
import { useHistory } from "react-router-dom";
import { Typography } from "@mui/material";
import PersonAddAlt1Icon from '@mui/icons-material/PersonAddAlt1';

import ShipOwner from "../../../icons/shipOwner.png";
import CottageOwner from "../../../icons/cottageOwner.png";
import Instructor from "../../../icons/instructor.png";
import UserInfoGrid from "../../user/UserInfoGrid";

import PersonIcon from '@mui/icons-material/Person';
import BackspaceIcon from '@mui/icons-material/Backspace';
import { TextField } from "@mui/material";
import { DialogContentText } from "@mui/material";
import { getCurrentUser } from "../../../service/AuthService";
import UsersDataGrid from "./UsersDataGrid";
import ConfirmPassDialog from "./ConfirmPassDialog";


let rows = [];


export default function ReviewAllUsers() {

    const history = useHistory();
    const [isLoadingAllUsers, setLoadingAllUsers] = useState(true);
    const [allUsers, setAllUsers] = useState();
    const [selectedId, setSelectedId] = useState(null);

    //------------------------CONFIRM PASS----------------------
    const [openedConfirmPassDialog, setOpenedConfirmPassDialog] = React.useState(false);
    const handleClickOpenConfirmPassDialog = () => {
        setOpenedConfirmPassDialog(true);
    };
    const handleCloseConfirmPassDialog = () => {
        setSelectedId(null);
        setOpenedConfirmPassDialog(false);
        loadUsers();
    };
    //-----------------------------------------------------------------------------

    const deleteUserById = (id) => {
        console.log("postavaljam novi id na: " + id);
        setSelectedId(id);
        handleClickOpenConfirmPassDialog();
    }

    const handleOnCellClick = (params) => {
        if (params.field === "Profile") {
            console.log("todo..");
        } else if (params.field === "Delete") {
            deleteUserById(params.id);
        } else if (params.field === 'Entities') {
            console.log("entities clicked");
        }
    }


    const fillRowsWithData = () => {
        let newRows = [];
        for (let r of allUsers) {
            if (r.userType.name === "ROLE_ADMIN" || r.userType.name === "ROLE_SUPER_ADMIN")
                continue;

            let showEntities = true;
            if (r.userType.name === "ROLE_CLIENT")
                showEntities = false;

            let rowToAdd = {
                'id': r.id,
                'User': r.firstName + ' ' + r.lastName,
                'Email': r.email,
                'Type': r.userType.name,
                'Entities': showEntities
            }
            newRows.push(rowToAdd);
        }
        rows = newRows;
    }


    const loadUsers = () => {
        setLoadingAllUsers(true);
        getAllUsers().then(res => {
            setAllUsers(res.data);
            setLoadingAllUsers(false);
        })
    }

    useEffect(() => {
        if (userLoggedInAsSuperAdminOrAdminWithResetedPassword(history)) {
            loadUsers();
        }
    }, [])

    if (isLoadingAllUsers) {
        return <div className="App">Loading...</div>
    }
    else {
        { fillRowsWithData() }

        return (
            <Box
                alignItems="center"
                justifyContent="center"
                style={{ margin: '1% 5% 1% 22%' }}
            >
                <ConfirmPassDialog
                    handleClickOpenConfirmPassDialog={handleClickOpenConfirmPassDialog}
                    handleCloseConfirmPassDialog={handleCloseConfirmPassDialog}
                    loadUsers={loadUsers}
                    selectedId={selectedId}
                    openedConfirmPassDialog={openedConfirmPassDialog}
                />

                <h3 color="rgba(17,16,29,255)">All users</h3>
                <br />
                <Divider sx={{ borderBottomWidth: 2, color: "rgba(17,16,29,255)" }} />
                <br />
                <UsersDataGrid allUsers={allUsers} rows={rows} handleOnCellClick={handleOnCellClick} />
            </Box>
        )
    }
}