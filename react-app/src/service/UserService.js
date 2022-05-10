import api from "./baseApi";

let currentUser = null;


export function getAllUsers() {
    return api.get('/users');
}

export function getUserById(id) {
    return api.get('/users/' + id);
}

export function editUserById(id, editedUser) {
    return api.put('/users/updateUser/' + id, editedUser);
}

export function sendLogInForm(form){
    return api.post('/users/login',form);
}

export function setNewPassword(changePassword) {
    return api.post('/users/changePassword', changePassword)
}