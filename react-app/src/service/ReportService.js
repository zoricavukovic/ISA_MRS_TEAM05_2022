import api from "./baseApi";

export function isReportedResByReservationId(reservationId) {
    return api.get('/reports/reported/' + reservationId)
}

export function addReport(report) {
    return api.post('/reports', report);
}
