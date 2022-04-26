import api from "./baseApi";


export function getAllBookingEntitiesByOwnerId(id) {
    return api.get('/bookingEntities/allByOwner/' + id);
}

export function getAllSearchedAdventuresBySimpleCriteria(searchCriteria) {
    return api.post('/bookingEntities/simpleSearch', searchCriteria);
}