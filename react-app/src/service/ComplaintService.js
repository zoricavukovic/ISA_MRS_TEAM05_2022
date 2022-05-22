import api from "./baseApi";

export function getComplaintByReservationId(id) {
    return api.get("/complaint/getByReservationId/"+id);
}

export function createComplaint(complaintDTO) {
    return api.post('/complaint/createComplaint', complaintDTO);
}