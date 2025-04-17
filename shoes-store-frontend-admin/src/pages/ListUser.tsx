import React from "react";
import Breadcrumb from "../components/Breadcrumbs/Breadcrumb";
import TableUser from "../components/Tables/TableUser";

function ListUser() {
  return (
    <>
      <Breadcrumb pageName="List Users" />

      <div className="flex flex-col gap-10">
        <TableUser />
      </div>
    </>
  );
}

export default ListUser;
