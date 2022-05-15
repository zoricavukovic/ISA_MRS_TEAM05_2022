import api from "./baseApi";

export function getReportByReservationId(reservationId) {
    return api.get('/reports/' + reservationId)
}

export function addReport(report) {
    return api.post('/reports', report);
}
