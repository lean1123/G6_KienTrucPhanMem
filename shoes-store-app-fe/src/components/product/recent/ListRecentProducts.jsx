import FilterByBrand from "../../filters/FilterByBrand";

function ListRecentProducts() {
  return (
    <div className="w-full flex justify-center p-20">
      <div className="grid grid-cols-2">
        <div>
          <FilterByBrand />
        </div>
        <div className="grid grid-cols-4">
          <div>Product 1</div>
          <div>Product 2</div>
          <div>Product 3</div>
          <div>Product 4</div>
          <div>Product 1</div>
          <div>Product 2</div>
          <div>Product 3</div>
          <div>Product 4</div>
          <div>Product 1</div>
          <div>Product 2</div>
          <div>Product 3</div>
          <div>Product 4</div>
          <div>Product 1</div>
          <div>Product 2</div>
          <div>Product 3</div>
          <div>Product 4</div>
        </div>
      </div>
    </div>
  );
}

export default ListRecentProducts;
