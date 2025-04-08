import React from "react";
import Breadcrumb from "../components/Breadcrumbs/Breadcrumb";
import TableProduct from "../components/Tables/TableProduct";

const ListProduct = () => {
  return (
    <>
      <Breadcrumb pageName="List Products" />

      <div className="flex flex-col gap-10">
        {/* <TableOne /> */}
        <TableProduct />
        {/* <TableThree /> */}
      </div>
    </>
  );
};

export default ListProduct;
