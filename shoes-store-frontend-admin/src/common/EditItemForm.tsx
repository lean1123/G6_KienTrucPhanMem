import { enqueueSnackbar } from "notistack";
import React, { useEffect, useRef, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import colorApi from "../api/colorApi";
import * as Yup from "yup";
import productItemApi from "../api/productItemApi";
import Breadcrumb from "../components/Breadcrumbs/Breadcrumb";
import DragDropImageUploader from "./DragDropImageUploader";
import { ErrorMessage, Field, Form, Formik } from "formik";
import { Delete, PlusOneRounded } from "@mui/icons-material";

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
  createdDate?: Date;
  modifiedDate?: Date;
  rating: number;
}

interface Color {
  id: string;
  name: string;
  code: string;
}

interface QuantityOfSize {
  size: number;
  quantity: number;
}
interface ProductItem {
  id: string;
  price: number;
  images: string[];
  color: Color;
  quantityOfSize: QuantityOfSize[];
  status: string;
  product: Product;
  isActive?: boolean;
}

interface Props {
  productItem: ProductItem;
  open: boolean;
  onClose: () => void;
  reloadData?: () => void;
}

export default function EditItemForm({
  productItem,
  open,
  onClose,
  reloadData,
}: Props) {
  const [images, setImages] = useState<File[]>([]);
  const { id } = useParams();
  const navigation = useNavigate();
  const [loading, setLoading] = useState(false);
  const [colors, setColors] = useState<Color[]>([]);
  const [sizes, setSizes] = useState<QuantityOfSize[]>([
    { size: 29, quantity: 1 },
  ]);
  const fetchColors = async () => {
    try {
      const response = await colorApi.getAllColors();
      setColors(response.data.result);
    } catch (error) {
      enqueueSnackbar("Failed to fetch colors!", { variant: "error" });
    }
  };

  useEffect(() => {
    if (productItem) {
      // setImages(productItem.images.map((image) => image));
      setSizes(productItem.quantityOfSize);
    }
  }, [productItem]);

  useEffect(() => {
    fetchColors();
  }, []);

  const validationSchema = Yup.object({
    price: Yup.number()
      .min(1, "Price must be at least 1")
      .required("Price is required"),
    color: Yup.string().required("Color is required"),
    listDetailImages: Yup.array(),
    sizes: Yup.array()
      .of(
        Yup.object().shape({
          size: Yup.string().required("Size is required"),
          quantity: Yup.number()
            .min(1, "Quantity must be at least 1")
            .required("Quantity is required"),
        })
      )
      .test("unique-size", "Sizes must be unique", (sizes) => {
        const sizeValues = sizes?.map((s) => s.size);
        return sizeValues?.length === new Set(sizeValues).size;
      }),
  });

  const initialValues = {
    price: productItem.price || "",
    color: productItem.color.id || "",
    listDetailImages: images,
    sizes: productItem.quantityOfSize || [],
    status: "IN_STOCK",
    product: productItem.product.id || id,
  };

  const isFirstSubmit = useRef(true);

  const handleSubmit = async (values: any) => {
    setLoading(true);
    try {
      const formData = new FormData();
      formData.append("price", values.price);
      formData.append("colorId", values.color);
      formData.append("status", values.status);
      formData.append("productId", id as string);
      formData.append("quantityOfSize", JSON.stringify(values.sizes));

      // 🟡 Nếu có ảnh thì append, nếu không thì thêm ảnh rỗng để giữ format
      if (values.listDetailImages.length > 0) {
        values.listDetailImages.forEach((image: File) => {
          formData.append("images", image);
        });
      } else {
        // ⛔ Tùy backend, nếu yêu cầu luôn có `images`, bạn có thể append một field rỗng như:
        formData.append("images", new Blob([]), "empty.jpg");
      }

      const response = await productItemApi.updateProductItem(
        productItem.id,
        formData
      );
      if (response.status === 200) {
        enqueueSnackbar("Product item updated successfully!", {
          variant: "success",
        });
        onClose();
        reloadData && reloadData();
      }
    } catch (error: any) {
      if (
        error.response &&
        error.response.data &&
        error.response.data.code === 1008
      ) {
        const serverErrors: { [key: string]: string } = {};
        error.response.data.result.forEach((err: any) => {
          const key = Object.keys(err)[0];
          const message = err[key];
          serverErrors[key] = message;
          enqueueSnackbar(`${message}`, { variant: "error" });
        });
      } else {
        enqueueSnackbar("Failed to update product!", { variant: "error" });
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div
      className={`w-full h-full bg-gray-100 rounded-md ${
        open ? "block" : "hidden"
      }`}
    >
      <DragDropImageUploader onImagesChange={setImages} />
      <div className="p-3 bg-white">
        <Formik
          initialValues={initialValues}
          enableReinitialize={true}
          validationSchema={validationSchema}
          onSubmit={(values) => handleSubmit(values)}
        >
          {({ setFieldValue, values, isSubmitting, errors }) => {
            useEffect(() => {
              setFieldValue("listDetailImages", images);
            }, [images, setFieldValue]);
            return (
              <Form>
                <div className="grid grid-cols-12 gap-6">
                  {/* Price */}
                  <div className="col-span-6">
                    <label className="text-black">Price</label>
                    <Field
                      type="number"
                      name="price"
                      className="w-full rounded-md border p-2"
                    />
                    <ErrorMessage
                      name="price"
                      component="div"
                      className="text-red-500 text-sm mt-1"
                    />
                  </div>

                  {/* Color */}
                  <div className="col-span-6">
                    <label className="text-black">Color</label>
                    <Field
                      as="select"
                      name="color"
                      className="w-full rounded-md border p-2"
                    >
                      <option value="" disabled>
                        Select Color
                      </option>
                      {colors &&
                        colors.map((color) => (
                          <option key={color.id} value={color.id}>
                            {color.name}
                          </option>
                        ))}
                    </Field>
                    <ErrorMessage
                      name="color"
                      component="div"
                      className="text-red-500 text-sm mt-1"
                    />
                  </div>

                  {/* Size & Quantity */}
                  <div className="col-span-7">
                    <label className="text-black">Size & Quantity</label>
                    {values.sizes.map((_, index) => (
                      <div key={index} className="grid grid-cols-12 gap-4 mt-2">
                        <div className="col-span-5">
                          <Field
                            as="select"
                            name={`sizes[${index}].size`}
                            className="w-full h-[42px] rounded-md border p-2"
                          >
                            <option value="" disabled>
                              Select Size
                            </option>
                            {[35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45].map(
                              (size) => {
                                const isSelected = values.sizes.some(
                                  (s, i2) => s.size === size && i2 !== index
                                );
                                return (
                                  <option
                                    key={size}
                                    value={size}
                                    disabled={isSelected}
                                  >
                                    {size}
                                  </option>
                                );
                              }
                            )}
                          </Field>
                          <ErrorMessage
                            name={`sizes[${index}].size`}
                            component="div"
                            className="text-red-500 text-sm"
                          />
                        </div>
                        <div className="col-span-5">
                          <Field
                            type="number"
                            name={`sizes[${index}].quantity`}
                            className="w-full rounded-md border p-2"
                            placeholder="Quantity"
                          />
                          <ErrorMessage
                            name={`sizes[${index}].quantity`}
                            component="div"
                            className="text-red-500 text-sm"
                          />
                        </div>
                        <div className="col-span-2">
                          <button
                            type="button"
                            className="p-2 bg-red-500 text-white rounded-md"
                            onClick={() => {
                              const newSizes = [...values.sizes];
                              newSizes.splice(index, 1);
                              setFieldValue("sizes", newSizes);
                            }}
                          >
                            <Delete />
                          </button>
                        </div>
                      </div>
                    ))}

                    {/* Button add more sizes */}
                    <div className="grid grid-cols-12 gap-4">
                      <span className="col-span-10"></span>
                      <div className="col-span-1">
                        <button
                          type="button"
                          className="mt-2 p-2 bg-blue-500 text-white rounded-md "
                          onClick={() =>
                            setFieldValue("sizes", [
                              ...values.sizes,
                              { size: "", quantity: "" },
                            ])
                          }
                        >
                          <PlusOneRounded />
                        </button>
                      </div>
                    </div>
                  </div>

                  {/* Submit & Back Buttons */}
                  <div className="col-span-12">
                    <button
                      type="submit"
                      className="w-full bg-blue-500 text-white rounded-md p-2"
                      // onClick={() => handleSubmit(values)}
                      disabled={loading}
                    >
                      {loading ? "Loading..." : "Update"}
                    </button>
                  </div>
                  {/* <div className="col-span-6">
                    <button
                      type="button"
                      onClick={() => navigation(-1)}
                      className="w-full bg-gray-500 text-white rounded-md p-2"
                    >
                      Cancel
                    </button>
                  </div> */}
                </div>
              </Form>
            );
          }}
        </Formik>
      </div>
    </div>
  );
}
