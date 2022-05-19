import api from "./baseApi";

export function getAllRatingsForViewByType(type) {
    return api.get('/ratings/all/' + type);
}
