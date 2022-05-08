import api from "./baseApi";

export function getAllUsers() {
    return api.get('/users');
}

export function getUserById(id) {
    return api.get('/users/' + id);
}

export function editUserById(id, editedUser) {
    return api.put('/users/' + id, editedUser);
}

export function sendLogInForm(form){
    return api.post('/users/login',form);
}

export function setNewPassword(changePassword) {
    return api.post('/users/changePassword', changePassword)
}