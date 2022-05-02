import api from "./baseApi";


export function getAllBookingEntitiesByOwnerId(id) {
    return api.get('/bookingEntities/allByOwner/' + id);
}

export function getBookingEntityByIdForCardView(id) {
    return api.get('/bookingEntities/view/' + id);
}

export function getAllSearchedEntitiesBySimpleCriteria(searchCriteria) {
    return api.post('/bookingEntities/simpleSearch', searchCriteria);
}