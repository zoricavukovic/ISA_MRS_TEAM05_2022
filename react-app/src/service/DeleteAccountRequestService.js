import api from "./baseApi";

export function getAllUnprocessedDeleteAccountRequests() {
    return api.get('/deleteAccounts/unprocessed');
}

export function giveResponseForDeleteAccountRequest(response) {
    return api.put('/deleteAccounts/giveResponse', response);
}