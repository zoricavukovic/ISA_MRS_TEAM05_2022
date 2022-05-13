import { getCurrentUser } from "./AuthService";
import api from "./baseApi";


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


export function userLoggedIn(history) {
    if (getCurrentUser() === null || getCurrentUser() === undefined) {
        history.push('/login');
        return false;
    }
    return true;
}

function userLoggedInWithRole(history, role) {
    if (userLoggedIn(history)) {
        if (getCurrentUser().userType.name !== role) {
            history.push('/forbiddenPage');
            return false;
        }
    }
    return true;
}

export function userLoggedInAsInstructor(history) {
    return userLoggedInWithRole(history, "ROLE_INSTRUCTOR");
}
export function userLoggedInAsCottageOwner(history) {
    return userLoggedInWithRole(history, "ROLE_COTTAGE_OWNER");
}
export function userLoggedInAsShipOwner(history) {
    return userLoggedInWithRole(history, "ROLE_SHIP_OWNER");
}
export function userLoggedInAsAdmin(history) {
    return userLoggedInWithRole(history, "ROLE_ADMIN");
}
export function userLoggedInAsClient(history) {
    return userLoggedInWithRole(history, "ROLE_CLIENT");
}
