import api from "./baseApi";

export function getReservationsByOwnerId(ownerId) {
    return api.get('reservations/owner/' + ownerId);
}

export function getFastReservationsByBookingEntityId(bookingEntityId) {
    return api.get('reservations/fast/' + bookingEntityId);
}


export function getReservationsByOwnerIdAndFilter(ownerId, filter) {
    return api.get('reservations/owner/' + ownerId + "/filter/" + filter);
}

export function addNewFastReservation(newFastReservation){
    return api.post('reservations', newFastReservation);
}

export function addReservation(reservationDTO){
    return api.post('reservations/addReservation', reservationDTO);
}

