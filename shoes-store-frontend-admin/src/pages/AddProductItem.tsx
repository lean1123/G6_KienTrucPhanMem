import { useEffect, useState } from "react";
import { ErrorMessage, Field, Form, Formik } from "formik";
import { enqueueSnackbar } from "notistack";
import { useNavigate, useParams } from "react-router-dom";
import * as Yup from "yup";
import productItemApi from "../api/productItemApi";
import DragDropImageUploader from "../common/DragDropImageUploader";
import Breadcrumb from "../components/Breadcrumbs/Breadcrumb";
import colorApi from "../api/colorApi";
import { Delete, PlusOne, PlusOneRounded, X } from "@mui/icons-material";
import { PlusCircleTwoTone, PlusOutlined, XOutlined } from "@ant-design/icons";

interface Color {
  id: string;
  name: string;
  code: string;
}

interface SizeQuantity {
  size: number;
  quantity: number;
}

const listImages = [
  "https://product.hstatic.net/1000230642/product/giay-the-thao-nam-biti-s-dsm074500den-den-ld35m-color-den_976e670e055f496390b8ed30328f88b0_1024x1024.jpg",
  "https://product.hstatic.net/1000230642/product/giay-the-thao-nam-biti-s-dsm074500den-den-ld35m-color-den_976e670e055f496390b8ed30328f88b0_1024x1024.jpg",
  "https://product.hstatic.net/1000230642/product/giay-the-thao-nam-biti-s-dsm074500den-den-ld35m-color-den_976e670e055f496390b8ed30328f88b0_1024x1024.jpg",
];

export default function AddProductItem() {
  const [images, setImages] = useState<File[]>([]);
  const { id } = useParams();
  const navigation = useNavigate();
  const [loading, setLoading] = useState(false);
  const [colors, setColors] = useState<Color[]>([]);
  const [sizes, setSizes] = useState<SizeQuantity[]>([
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
    fetchColors();
  }, []);

  const validationSchema = Yup.object({
    price: Yup.number()
      .min(1, "Price must be at least 1")
      .required("Price is required"),
    color: Yup.string().required("Color is required"),
    listDetailImages: Yup.array()
      .min(1, "At least one image is required")
      .required("Images are required"),
    sizes: Yup.array().of(
      Yup.object().shape({
        size: Yup.string().required("Size is required"),
        quantity: Yup.number()
          .min(1, "Quantity must be at least 1")
          .required("Quantity is required"),
      })
    ),
  });

  const initialValues = {
    price: "",
    color: "",
    listDetailImages: images,
    sizes: sizes,
    status: "In Stock",
    product: id,
  };

  const handleSubmit = async (values: any) => {
    // console.log("Form Data:", values);
    setLoading(true);
    try {
      const formData = new FormData();
      formData.append("price", values.price);
      formData.append("colorId", values.color);
      formData.append("status", values.status);
      // values.listDetailImages.forEach((image: any) => {
      //   formData.append("listDetailImages", image);
      // });
      listImages.forEach((image: string, index: number) => {
        formData.append(`images[${index}]`, image);
      });
      //formData.append("quantityOfSize", JSON.stringify(values.sizes));
      values.sizes.forEach((item: any, index: number) => {
        formData.append(`quantityOfSize[${index}].size`, item.size);
        formData.append(`quantityOfSize[${index}].quantity`, item.quantity);
      });

      formData.append("productId", id as string);

      const response = await productItemApi.addNewProductItem(formData);
      if (response.status === 200) {
        enqueueSnackbar("Product item added successfully!", {
          variant: "success",
        });
      }
      console.log(
        "Form Data:",
        formData.forEach((value, key) => console.log(key, value))
      );
    } catch (error) {
      enqueueSnackbar("Failed to add product item!", { variant: "error" });
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <Breadcrumb pageName="Add Product Item" />
      <DragDropImageUploader onImagesChange={setImages} />
      <div className="p-3 rounded-md shadow-md mt-4 bg-white">
        <Formik
          initialValues={initialValues}
          validationSchema={validationSchema}
          onSubmit={(values) => handleSubmit(values)}
        >
          {({ setFieldValue, values }) => {
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
                            {[29, 30, 31, 32, 33, 34, 35, 36].map((size) => (
                              <option key={size} value={size}>
                                {size}
                              </option>
                            ))}
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
                  <div className="col-span-6">
                    <button
                      type="submit"
                      className="w-full bg-blue-500 text-white rounded-md p-2"
                      onClick={() => handleSubmit(values)}
                    >
                      {loading ? "Loading..." : "Add Product Item"}
                    </button>
                  </div>
                  <div className="col-span-6">
                    <button
                      type="button"
                      onClick={() => navigation(-1)}
                      className="w-full bg-gray-500 text-white rounded-md p-2"
                    >
                      Back
                    </button>
                  </div>
                </div>
              </Form>
            );
          }}
        </Formik>
      </div>
    </>
  );
}
