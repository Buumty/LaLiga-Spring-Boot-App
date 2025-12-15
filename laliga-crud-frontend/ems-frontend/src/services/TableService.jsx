import { request } from "../components/HelperComponents/axios_helper";

export const table = () => {
    return request('GET', '/api/v1/table');
};