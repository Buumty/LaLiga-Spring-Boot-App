import React, { useEffect, useState } from "react";
import { listPlayers } from "../../../services/PlayerService";
import './ListPlayerComponent.css'

const ListPlayerComponent = () => {
  const [players, setPlayers] = useState([]);
  const [page, setPage] = useState(0);
  const [size] = useState(20);
  const [totalPages, setTotalPages] = useState(0);

  const [sortBy, setSortBy] = useState("goals");
  const [sortDir, setSortDir] = useState("desc");

  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState("");

  useEffect(() => {
    setLoading(true);
    listPlayers(page, size, sortBy, sortDir)
      .then((res) => {
        setPlayers(res.data.content ?? []);
        setTotalPages(res.data.totalPages);
      })
      .catch((e) => setErr(e?.response?.statusText || e.message))
      .finally(() => setLoading(false));
  }, [page, size, sortBy, sortDir]);

    const handleSort = (field) => {
      if (sortBy === field) {
        setSortDir(sortDir === "asc" ? "desc" : "asc");
      } else {
        setSortBy(field);
        setSortDir("desc");
      }
      setPage(0);
    }

  if (loading) return <div>Ładowanie...</div>;
  if (err) return <div>Błąd: {err}</div>;

  return (
    <div>
      <table className="laliga-table">
        <thead>
          <tr>
            <th>#</th>
            <th>Team</th>
            <th>Player Name</th>
            <th>Minutes Played</th>
            <th className="sortable-header" onClick={() => handleSort("goals")}>Goals</th>
            <th className="sortable-header" onClick={() => handleSort("assists")}>Assists</th>
          </tr>
        </thead>
        <tbody>
          {players.map((p, index) => (
            <tr key={p.id}>
              <td>{index + 1}</td>
              <td>{p.teamName}</td>
              <td>{p.playerName}</td>
              <td>{p.minutes}</td>
              <td>{p.goals}</td>
              <td>{p.assists}</td>
            </tr>
          ))}
        </tbody>
      </table>

      {/* PAGINACJA */}
      <div>
        <button
        className="btn-laliga"
          disabled={page === 0}
          onClick={() => setPage((p) => p - 1)}
        >
          Previous
        </button>

        <span className="players-pagination">
          Page {page + 1} / {totalPages}
        </span>

        <button
        className="btn-laliga"
          disabled={page + 1 >= totalPages}
          onClick={() => setPage((p) => p + 1)}
        >
          Next
        </button>
      </div>
    </div>
  );
};

export default ListPlayerComponent;
