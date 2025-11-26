import { request } from "../components/HelperComponents/axios_helper";

export const listPlayers = (page = 0, size = 20, sortBy = "goals", sortDir = "desc") => {
   return request('GET', '/api/v1/players', {
        params: {page, size, sortBy, sortDir}
    });
};