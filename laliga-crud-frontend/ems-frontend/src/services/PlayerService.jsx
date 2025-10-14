import api from "../api/client";

export const listPlayers = () => {
  // ZWRACA Promise z danymi
  return api.get("/api/v1/players"); // <- dopasuj ścieżkę do backendu
};