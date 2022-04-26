import api from "./baseApi";

export function getReservationsByOwnerId(ownerId) {
    return api.get('reservations/owner/' + ownerId);
}

export function getReservationsByOwnerIdAndFilter(ownerId, filter) {
    return api.get('reservations/owner/' + ownerId + "/filter/" + filter);
}

