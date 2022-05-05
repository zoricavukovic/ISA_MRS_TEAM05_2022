import api from "./baseApi";


export function getCalendarValuesByBookintEntityId(id) {
    return api.get('/calendar/entity/' + id);
}
