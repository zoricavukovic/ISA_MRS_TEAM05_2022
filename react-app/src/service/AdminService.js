import api from "./baseApi";

export default function addNewAdmin(newAdmin) {
    return api.post('/admins', newAdmin);
}
