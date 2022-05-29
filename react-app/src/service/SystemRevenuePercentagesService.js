import api from "./baseApi";

export function getCurrentSystemRevenuePercentage() {
    return api.get("/systemRevenuePercentages/current");
}

export function saveNewSystemRevenuePercentage(data) {
    return api.post("/systemRevenuePercentages/", data);
}