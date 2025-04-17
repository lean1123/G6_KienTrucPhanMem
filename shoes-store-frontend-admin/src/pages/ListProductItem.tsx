import React from "react";
import Breadcrumb from "../components/Breadcrumbs/Breadcrumb";
import TableProductItem from "../components/Tables/TableProductItem";

function ListProductItem() {
  return (
    <>
      <Breadcrumb pageName="List Product Items" />

      <div className="flex flex-col gap-10">
        <TableProductItem />
      </div>
    </>
  );
}

export default ListProductItem;
