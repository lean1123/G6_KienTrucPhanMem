import React from "react";
import Breadcrumb from "../components/Breadcrumbs/Breadcrumb";
import TableCategory from "../components/Tables/TableCategory";

function ListCategory() {
  return (
    <>
      <Breadcrumb pageName="List Categories" />

      <div className="flex flex-col gap-10">
        <TableCategory />
      </div>
    </>
  );
}

export default ListCategory;
