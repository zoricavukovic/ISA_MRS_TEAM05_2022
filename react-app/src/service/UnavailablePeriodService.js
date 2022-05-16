import api from "./baseApi";


export function checkOverlapForUnavailableDate(date) {
    return api.post('/unavailableDates/checkForOverlapUnavailableDate', date);
}


export function addNewUnavailableDate(data) {
    return api.post('/unavailableDates/', data);
}