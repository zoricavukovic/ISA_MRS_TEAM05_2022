import api from "./baseApi";


export function getAllShips() {
    return api.get('/ships');
}

export function getShipById(id) {
    return api.get('/ships/' + id);
}

export function editShipById(id, editedShip) {
    return api.put('/ships/' + id, editedShip);
}

export function addNewShip(newShip) {
    return api.post('/ships', newShip);
}

