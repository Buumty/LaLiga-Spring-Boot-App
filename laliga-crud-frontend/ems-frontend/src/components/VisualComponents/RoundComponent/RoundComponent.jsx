import React, { useEffect, useState } from "react";
import { listMatches } from "../../../services/MatchService";
import "./RoundComponent.css"

function RoundComponent() {
  const [matches, setMatches] = useState([]);
  const [loading, setLoading] = useState(true);
  const [round, setRound] = useState(1);

  useEffect(() => {
    setLoading(true);

    listMatches(round)
      .then((res) => {
        // u Ciebie res.data jest tablicą meczów
        setMatches(Array.isArray(res.data) ? res.data : []);
      })
      .catch((err) => {
        console.error("Błąd przy pobieraniu meczów:", err);
      })
      .finally(() => setLoading(false));
  }, [round]);

  const handlePrevious = () => {
    if (round > 1) {
      setRound(round - 1);
    }
  };

  const handleNext = () => {
    setRound(round + 1);
  };

  if (loading) return <p>Loading...</p>;

  return (
    <div className="round">
      <div className="navigate-rounds">
      <button onClick={handlePrevious} disabled={round===1}>Previous</button>
      <h2>Round {round}</h2>
      <button onClick={handleNext} disabled={round===38}>Next</button>
      </div>

      <ul>
        {matches.map((match) => (
          <li key={match.id}>
            {match.homeTeamName} vs {match.awayTeamName}{" "}
            ({new Date(match.matchDate).toLocaleString("pl-PL", {
              day: "2-digit",
              month: "2-digit",
              year: "numeric",
              hour: "2-digit",
              minute: "2-digit",
            })})
          </li>
        ))}
      </ul>
    </div>
  );
}

export default RoundComponent;
