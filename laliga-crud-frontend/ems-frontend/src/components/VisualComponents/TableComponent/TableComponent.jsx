import React, { useEffect, useState } from 'react'
import "./TableComponent.css"
import { table } from '../../../services/TableService'

  const getRowClass = (position) => {
  if (position <= 4) return "ucl-position";
  if (position === 5) return "uel-position";
  if (position >= 18) return "relegation-position";
  return "";
}


const TableComponent = () => {
  const [rows, setRows] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);


  useEffect(() => {
    table()
    .then((res) => {
      setRows(Array.isArray(res.data) ? res.data : []);
    })
    .catch((err) => {
      console.error(err);
      setError("Nie udało się!")
    })
    .finally(() => setLoading(false))
  }, []);

if (loading) {
  return <p>Loading...</p>
}

if (error) {
  return <p>{error}</p>
}

  return (
    <div><table className="laliga-table">
        <thead>
          <tr>
            <th>#</th>
            <th>Team</th>
            <th>P</th>
            <th>G+</th>
            <th>G-</th>
            <th>GD</th>
            <th>Points</th>
          </tr>
        </thead>
        <tbody>
          {rows.map((team) => (
            <tr key={team.position} className={getRowClass(team.position)}>
              <td>{team.position}</td>
              <td>
                {team.logoUrl && (
                <img className="team-logo" src={team.logoUrl} alt={team.teamName} />
                )}
                <span>{team.teamName}</span>
                </td>
                <td>{team.played}</td>
                <td>{team.goalsFor}</td>
                <td>{team.goalsAgainst}</td>
                <td>{team.goalsDiff}</td>
                <td>{team.points}</td>
            </tr>
          ))}
        </tbody>
      </table></div>
  )
}

export default TableComponent