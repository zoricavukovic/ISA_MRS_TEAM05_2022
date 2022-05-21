import api from "./baseApi";

export function getAllComplaintsForViewByType(type) {
    return api.get('/complaints/all/' + type);
}

export function giveResponseForComplaint(response) {
    return api.put('/complaints/giveResponse', response);
}