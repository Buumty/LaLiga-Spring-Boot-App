import React from 'react'
import "./TableComponent.css"

function TableComponent() {
  return (
    <div><table className="laliga-table">
        <thead>
          <tr>
            <th>#</th>
            <th>Team</th>
            <th>P</th>
            <th>Z</th>
            <th>R</th>
            <th>P</th>
            <th>Różnica</th>
            <th>Bramki</th>
            <th>Punkty</th>
          </tr>
        </thead>
        <tbody>
        </tbody>
      </table></div>
  )
}

export default TableComponent