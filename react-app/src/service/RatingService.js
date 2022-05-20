import api from "./baseApi";

export function getAllRatingsForViewByType(type) {
    return api.get('/ratings/all/' + type);
}

export function putReviewForPublication(review) {
    return api.put('/ratings/putForPublication', review)
}

export function deleteReviewById(id) {
    return api.delete('ratings/' + id)
}