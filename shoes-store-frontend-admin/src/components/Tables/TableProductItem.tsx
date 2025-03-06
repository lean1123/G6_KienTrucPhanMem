import {
  DeleteForeverOutlined,
  EditOutlined,
  VisibilityOutlined,
} from "@mui/icons-material";
import React, { useEffect } from "react";
import { useNavigate, useParams } from "react-router";
import { enqueueSnackbar } from "notistack";
import productApi from "../../api/productApi";
import brandApi from "../../api/brandApi";
import productItemApi from "../../api/productItemApi";

interface Product {
  id?: string;
  name: string;
  description: string;
  category: {
    id: string;
    name: string;
    description: string;
  };
  collection: {
    id: string;
    name: string;
    brandId: string;
  };
  gender: string;
  createDate?: Date;
  rating: number;
  brand?: string;
  image?: string;
}

interface ProductItem {
  id: string;
  price: number;
  quantity: number;
  listDetailImages: string[];
  color: string;
  size: string;
  status: string;
  product: {
    id: string;
    name: string;
    description: string;
    category: {
      id: string;
      name: string;
      description: string;
    };
    collection: {
      id: string;
      name: string;
      brandId: string;
    };
    gender: string;
    createDate: Date;
    rating: number;
  };
}

function TableProductItem() {
  const { id } = useParams();
  const [loading, setLoading] = React.useState(false);
  const [product, setProduct] = React.useState<Product>();
  const [productItems, setProductItems] = React.useState<ProductItem[]>([]);
  const [brand, setBrand] = React.useState<string>();
  const navigate = useNavigate();

  const fetchProduct = async () => {
    setLoading(true);
    try {
      const response = await productApi.getById(id);
      console.log(response.data);
      setProduct(response.data);
    } catch (error) {
      console.error("Failed to fetch products:", error);
    } finally {
      setLoading(false);
    }
  };

  const fetchBrand = async () => {
    setLoading(true);
    try {
      const response = await brandApi.getBrandById(
        product?.collection.brandId as string
      );
      console.log(response.data);
      setBrand(response.data.avatar);
    } catch (error) {
      console.error("Failed to fetch products:", error);
    } finally {
      setLoading(false);
    }
  };

  const fetchProductItem = async () => {
    setLoading(true);
    try {
      const response = await productItemApi.getAllProductItems(id);
      console.log(response.data);
      setProductItems(response.data.data);
    } catch (error) {
      console.error("Failed to fetch product:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id: any) => {
    setLoading(true);
    try {
      await productItemApi.deleteProductItem(id);
      fetchProductItem();
      enqueueSnackbar("Delete product item successfully", {
        variant: "success",
      });
    } catch (error) {
      console.error("Failed to fetch products:", error);
      enqueueSnackbar("Delete product item failed", { variant: "error" });
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchProduct();
  }, []);

  useEffect(() => {
    fetchProductItem();
    fetchBrand();
    console.log("productItems:", productItems);
  }, [product]);

  return (
    <div className="rounded-md border border-gray-300 bg-white shadow-sm ">
      <div className="py-6 px-4 md:px-6 xl:px-7 flex flex-row justify-between">
        <div>
          <p className="text-xl font-semibold text-black">ID: {id}</p>
          <p className="text-lg font-medium text-black">
            Name: {product?.name}
          </p>
          <p className="text-lg font-medium text-black">
            Rating: {product?.rating}⭐
          </p>
        </div>
        <div className="flex flex-col gap-2 justify-center items-center">
          <img src={brand} className="w-10 h-10" />
          <button
            className="bg-gray-500 hover:bg-gray-700 text-white font-bold py-2 px-4 rounded"
            onClick={() => {
              navigate(-1);
            }}
          >
            Back
          </button>
        </div>
      </div>
      <div className="max-w-full overflow-x-auto">
        {productItems.length > 0 ? (
          productItems.map((item, key) => (
            <div
              className="border-t-2 border-gray-300 bg-white mb-4 flex flex-row justify-between overflow-hidden relative"
              key={key}
            >
              <div className="absolute   border-r-2 border-b-2 rounded-full px-1">
                #{key + 1}
              </div>
              <div className="flex flex-row gap-6 p-6">
                <div className="flex flex-row h-full gap-1">
                  <div className="col-span-3 overflow-y-scroll no-scrollbar h-[160px]">
                    {item.listDetailImages.map((image, key) => (
                      <img
                        src={image}
                        alt="product"
                        className=" object-cover rounded-md mb-2 h-[50px] w-[50px]"
                        key={key}
                      />
                    ))}
                  </div>
                  <img
                    src={
                      item.listDetailImages.length > 0
                        ? item.listDetailImages[0]
                        : ""
                    } // set default image
                    className="w-[160px] rounded-md object-cover h-[160px]"
                    alt="main-product"
                  />
                </div>

                <div className="">
                  <p className="text-xl font-semibold text-black">
                    ID: {item.id}
                  </p>
                  <p className="text-lg font-medium text-black">
                    Color: {item.color}
                  </p>
                  <p className="text-lg font-medium text-black">
                    Size: {item.size}
                  </p>
                  <p className="text-lg font-medium text-black">
                    Price: {item.price}
                  </p>
                  <p className="text-lg font-medium text-black">
                    Quantity: {item.quantity}
                  </p>
                </div>
              </div>

              <div className="flex flex-col gap-4 p-6">
                <p className="text-lg font-medium text-black">
                  Status: {item.status}
                </p>
                <div className="flex flex-row gap-4 justify-end">
                  {/* Edit button */}
                  <div className="relative group">
                    <button
                      className="hover:text-yellow-500"
                      onClick={() => {
                        navigate(
                          `/admin/products/${id}/list-item/${item.id}/edit`
                        );
                      }}
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
                      onClick={() => handleDelete(item.id)}
                    >
                      <DeleteForeverOutlined className="w-5 h-5" />
                    </button>
                    <span className="absolute opacity-0 group-hover:opacity-100 bg-black text-white text-xs rounded py-1 px-2 -top-8 left-1/2 -translate-x-1/2 whitespace-nowrap">
                      Remove
                    </span>
                  </div>
                </div>
              </div>
            </div>
          ))
        ) : (
          <p className="text-center py-4">No product items available.</p>
        )}
      </div>
    </div>
  );
}

export default TableProductItem;
