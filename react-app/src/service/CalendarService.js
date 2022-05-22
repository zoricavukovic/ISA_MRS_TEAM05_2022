import api from "./baseApi";


export function getCalendarValuesByBookintEntityId(id) {
    return api.get('/calendar/entity/' + id);
}

export function getAnalysisByBookintEntityId(id) {
    return api.get('/calendar/analysis/entity/' + id);
}
