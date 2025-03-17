import React from "react";
import Breadcrumb from "../components/Breadcrumbs/Breadcrumb";
import TableBrand from "../components/Tables/TableColor";
import TableColor from "../components/Tables/TableColor";

export default function ListColor() {
  return (
    <>
      <Breadcrumb pageName="List Colors" />

      <div className="flex flex-col gap-10">
        <TableColor />
      </div>
    </>
  );
}
