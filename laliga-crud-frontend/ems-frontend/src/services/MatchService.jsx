import { request } from "../components/HelperComponents/axios_helper";

export const listMatches = (roundNumber) => request('GET', '/api/v1/matches',
    {
        params: {roundNumber},
    }
);