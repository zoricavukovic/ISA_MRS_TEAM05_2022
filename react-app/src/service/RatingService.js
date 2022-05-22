import api from "./baseApi";

export function getRatingByReservationId(id) {
    return api.get('/rating/byReservationId/'+id);
}

export function createRating(ratingDTO) {
    return api.post('/rating/createRating', ratingDTO);
}