import { request } from "../components/axios_helper";

export const listPlayers = () => request('GET', '/api/v1/players')