import api from "./baseApi";


export function getAllAdventures() {
    return api.get('/adventures');
}

export function getAllAdventuresView() {
    return api.get('/adventures/view')
}

export function getAdventureById(id) {
    return api.get('/adventures/' + id);
}

export function editAdventureById(id, editedAdventure) {
    return api.put('/adventures/' + id, editedAdventure);
}

export function addNewAdventure(newAdventure) {
    return api.post('/adventures', newAdventure);
}
