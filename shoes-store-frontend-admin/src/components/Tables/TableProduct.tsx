import React, { useEffect } from "react";
import {
  Add,
  DeleteForeverOutlined,
  VisibilityOutlined,
} from "@mui/icons-material";
import { EditOutlined } from "@ant-design/icons";
import { useNavigate } from "react-router-dom";
import { enqueueSnackbar } from "notistack";
import productApi from "../../api/productApi";

interface Product {
  id?: string;
  code: string;
  name: string;
  description: string;
  category: {
    id: string;
    name: string;
  };
  type: string;
  createDate?: Date;
  modifiedDate?: Date;
  rating: number;
}

const TableProduct = () => {
  const [loading, setLoading] = React.useState(false);
  const [productData, setProductData] = React.useState<Product[]>([]);
  const [keyword, setKeyword] = React.useState("");
  const navigate = useNavigate();
  const fetchProduct = async () => {
    setLoading(true);
    try {
      const response = await productApi.getAll();
      console.log(response.data);
      const products = response.data.result;
      setProductData(products);
      if (response.data.code === 503) {
        enqueueSnackbar(response.data.message, { variant: "error" });
      }
    } catch (error) {
      console.error("Failed to fetch product:", error);
    } finally {
      setLoading(false);
    }
  };

  const filterProduct = async () => {
    setLoading(true);
    try {
      const response = await productApi.search(keyword);
      console.log(response.data);
      const products = response.data.result;
      setProductData(products);
      if (response.data.code === 503) {
        enqueueSnackbar(response.data.message, { variant: "error" });
      }
    } catch (error) {
      console.error("Failed to fetch product:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id: any) => {
    setLoading(true);
    try {
      await productApi.delete(id);
      fetchProduct();
      enqueueSnackbar("Delete product successfully", { variant: "success" });
    } catch (error) {
      console.error("Failed to fetch product:", error);
      enqueueSnackbar("Delete product failed", { variant: "error" });
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchProduct();
  }, []);

  return (
    <div className="rounded-md border border-gray-300 bg-white shadow-sm ">
      <div className="py-6 px-4 md:px-6 xl:px-7">
        {/* <h4 className='text-xl font-semibold text-black'>List Products</h4> */}
        <div className="mt-2 flex items-center justify-between max-w-[400px] gap-4">
          <input
            type="text"
            className="w-full border border-gray-300 rounded-md py-2 px-4"
            placeholder="Search product..."
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
          />
          <button
            className="bg-black text-white px-6 py-2 rounded-md"
            onClick={() => {
              filterProduct();
            }}
          >
            Search
          </button>
        </div>
      </div>

      <div className="max-w-full overflow-x-auto">
        <table className="w-full table-auto">
          <thead>
            <tr className="bg-gray-2 text-left">
              <th className="min-w-[80px] py-2 px-2 font-semibold text-black">
                Code
              </th>
              <th className="min-w-[200px] py-2 px-2 font-semibold text-black">
                Product Name
              </th>
              <th className="min-w-[120px] py-2 px-2 font-semibold text-black">
                Category
              </th>
              <th className="min-w-[120px] py-2 px-2 font-semibold text-black">
                Type
              </th>
              <th className="min-w-[80px] py-2 px-2 font-semibold text-black">
                Rating
              </th>
              <th className="py-2 px-4 font-semibold text-black">Actions</th>
            </tr>
          </thead>
          <tbody>
            {productData &&
              productData.length > 0 &&
              productData.map((productItem, key) => (
                <tr key={key}>
                  <td className="border-b border-[#eee] py-5 px-2">
                    <p className="text-black ">{productItem.code}</p>
                  </td>
                  <td className="border-b border-[#eee] py-2 px-2">
                    <p className="text-sm text-black">
                      {productItem.name.length > 30
                        ? productItem.name.slice(0, 30) + "..."
                        : productItem.name}
                    </p>
                  </td>
                  <td className="border-b border-[#eee] py-5 px-2">
                    <p className="text-black ">
                      {productItem.category
                        ? productItem.category.name
                        : "Unknown"}
                    </p>
                  </td>
                  <td className="border-b border-[#eee] py-5 px-2">
                    <p className="text-black ">
                      {productItem.type ? productItem.type : "Unknown"}
                    </p>
                  </td>
                  <td className="border-b border-[#eee] py-5 px-2">
                    <p className="text-black ">{productItem.rating + "⭐"}</p>
                  </td>
                  <td className="border-b border-[#eee] py-5 px-2">
                    <div className="flex items-center space-x-3.5">
                      {/* Add view item */}
                      <div className="relative group">
                        <button
                          className="hover:text-blue-500"
                          onClick={() =>
                            navigate(`/products/${productItem.id}/list-item`)
                          }
                        >
                          <VisibilityOutlined className="w-5 h-5" />
                        </button>
                        <span className="absolute opacity-0 group-hover:opacity-100 bg-black text-white text-xs rounded py-1 px-2 -top-8 left-1/2 -translate-x-1/2 whitespace-nowrap">
                          View item
                        </span>
                      </div>

                      {/* Add button */}
                      <div className="relative group">
                        <button
                          className="hover:text-green-500"
                          onClick={() =>
                            navigate(`/products/${productItem.id}/add-item`)
                          }
                        >
                          <Add className="w-5 h-5" />
                        </button>
                        <span className="absolute opacity-0 group-hover:opacity-100 bg-black text-white text-xs rounded py-1 px-2 -top-8 left-1/2 -translate-x-1/2 whitespace-nowrap">
                          Add product item
                        </span>
                      </div>

                      {/* Edit button */}
                      <div className="relative group">
                        <button
                          className="hover:text-yellow-500"
                          onClick={() =>
                            navigate(`/products/edit/${productItem.id}`)
                          }
                        >
                          <EditOutlined className="w-5 h-5" />
                        </button>
                        <span className="absolute opacity-0 group-hover:opacity-100 bg-black text-white text-xs rounded py-1 px-2 -top-8 left-1/2 -translate-x-1/2 whitespace-nowrap">
                          Edit
                        </span>
                      </div>

                      {/* Delete button */}
                      <div className="relative group">
                        <button
                          className="hover:text-red-500"
                          onClick={() => handleDelete(productItem.id)}
                        >
                          <DeleteForeverOutlined className="w-5 h-5" />
                        </button>
                        <span className="absolute opacity-0 group-hover:opacity-100 bg-black text-white text-xs rounded py-1 px-2 -top-8 left-1/2 -translate-x-1/2 whitespace-nowrap">
                          Remove
                        </span>
                      </div>
                    </div>
                  </td>
                </tr>
              ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default TableProduct;
