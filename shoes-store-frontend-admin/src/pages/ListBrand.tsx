import React from "react";
import Breadcrumb from "../components/Breadcrumbs/Breadcrumb";
import TableBrand from "../components/Tables/TableBrand";

export default function ListBrand() {
  return (
    <>
      <Breadcrumb pageName="List Brands" />

      <div className="flex flex-col gap-10">
        <TableBrand />
      </div>
    </>
  );
}
