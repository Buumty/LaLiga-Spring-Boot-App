import React, { useEffect, useState } from "react";
import { listPlayers } from "../services/PlayerService";

const ListPlayerComponent = () => {
  const [players, setPlayers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState("");

  useEffect(() => {
    listPlayers()
      .then((res) => {
        const data = Array.isArray(res.data) ? res.data : res.data.content ?? [];
        setPlayers(data);
      })
      .catch((e) => setErr(e?.response?.statusText || e.message))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div>Ładowanie...</div>;
  if (err) return <div>Błąd: {err}</div>;

  return (
    <div className="container">
      <h2 className="text-center">List of Players</h2>
      <table className="table table-striped table-bordered">
        <thead>
          <tr>
            <th>Player Id</th>
            <th>Player Name</th>
            <th>Team</th>
            <th>Number</th>
            <th>Nation</th>
            <th>Position</th>
            <th>Age</th>
            <th>Minutes Played</th>
            <th>Goals</th>
            <th>Assists</th>
            <th>Penalties on Goal</th>
            <th>Penalties Scored</th>
          </tr>
        </thead>
        <tbody>
          {players.map((p) => (
            <tr key={p.id}>
              <td>{p.id}</td>
              <td>{p.name}</td>
              <td>{p.team}</td>
              <td>{p.number}</td>
              <td>{p.nation}</td>
              <td>{p.position}</td>
              <td>{p.age}</td>
              <td>{p.minutesPlayed}</td>
              <td>{p.goals}</td>
              <td>{p.assists}</td>
              <td>{p.penaltiesOnGoal}</td>
              <td>{p.penaltiesScored}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ListPlayerComponent;
