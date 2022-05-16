import api from "./baseApi";

export function getReservationsByOwnerId(ownerId, type) {
    return api.get('reservations/owner/' + ownerId + "/" + type);
}

export function getFastReservationsByBookingEntityId(bookingEntityId) {
    return api.get('reservations/fast/' + bookingEntityId);
}


export function getReservationsByOwnerIdAndFilter(ownerId, name, time, type) {
    return api.get('reservations/owner/' + ownerId + "/" + type + "/filter/name/" + name + "/time/" + time);
}

export function addNewFastReservation(newFastReservation){
    return api.post('reservations', newFastReservation);
}

export function addReservation(reservationDTO){
    return api.post('reservations/addReservation', reservationDTO);
}

