import React from 'react'
import LoggedHeader from '../../components/VisualComponents/LoggedHeader/LoggedHeader'
import Footer from '../../components/VisualComponents/Footer/Footer'
import "./TablePage.css"
import TableComponent from '../../components/VisualComponents/TableComponent/TableComponent'

function TablePage() {
  return (
    <div className='table-page'>
        <LoggedHeader />
        <div>
          <TableComponent />
        </div>
        <Footer />
    </div>
  )
}

export default TablePage