import React from "react";
import Breadcrumb from "../components/Breadcrumbs/Breadcrumb";
import TableCollection from "../components/Tables/TableCollection";

function ListCollection() {
  return (
    <>
      <Breadcrumb pageName="List Collections" />

      <div className="flex flex-col gap-10">
        <TableCollection />
      </div>
    </>
  );
}

export default ListCollection;
