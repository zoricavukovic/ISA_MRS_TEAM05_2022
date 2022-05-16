import api from "./baseApi";


export default function checkOverlapForUnavailableDate(date) {
    api.post('/unavailableDates/checkForOverlapUnavailableDate', date);
}