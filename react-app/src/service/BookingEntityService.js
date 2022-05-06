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

export function checkIfCanEditEntityById(id) {
    return api.get('/bookingEntities/checkIfCanEdit/' + id)
}

export function logicalDeliteBookingEntityById(id, confirmPass) {
    return api.delete('/bookingEntities/' + id, {data : confirmPass});
}