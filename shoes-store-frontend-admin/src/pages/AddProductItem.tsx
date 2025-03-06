import { useEffect, useState } from "react";

import { ErrorMessage, Field, Form, Formik } from "formik";
import { enqueueSnackbar } from "notistack";
import { useNavigate, useParams } from "react-router";
import * as Yup from "yup";
import productItemApi from "../api/productItemApi";
import DragDropImageUploader from "../common/DragDropImageUploader";
import Breadcrumb from "../components/Breadcrumbs/Breadcrumb";

export default function AddProductItem() {
  const [images, setImages] = useState<File[]>([]);
  const { id } = useParams();
  const navigation = useNavigate();
  const [loading, setLoading] = useState(false);

  // Schema validate
  const validationSchema = Yup.object({
    quantity: Yup.number()
      .min(1, "Quantity must be at least 1")
      .required("Quantity is required"),
    price: Yup.number()
      .min(1, "Price must be at least 1")
      .required("Price is required"),
    size: Yup.string().required("Size is required"),
    color: Yup.string().required("Color is required"),
    listDetailImages: Yup.array()
      .min(1, "At least one image is required")
      .required("Images are required"),
  });

  // Initial values for the form
  const initialValues = {
    quantity: "",
    price: "",
    size: "",
    color: "",
    listDetailImages: images,
    status: "INSTOCK",
    product: id,
  };

  // Handle submit
  const handleSubmit = async (values: any) => {
    console.log("Form Data:", values);
    setLoading(true);
    try {
      const formData = new FormData();
      formData.append("quantity", values.quantity);
      formData.append("price", values.price);
      formData.append("size", values.size);
      formData.append("color", values.color);
      formData.append("status", values.status);
      values.listDetailImages.forEach((image: any) => {
        formData.append("listDetailImages", image);
      });
      formData.append("product", id as string);

      // Call API to add product item
      const response = await productItemApi.addNewProductItem(formData);
      if (response.status === 200) {
        enqueueSnackbar("Product item added successfully!", {
          variant: "success",
        });
      }
    } catch (error) {
      console.error("Failed to add product item:", error);
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
          {({ setFieldValue }) => {
            // Sử dụng useEffect để đồng bộ images với Formik
            useEffect(() => {
              setFieldValue("listDetailImages", images);
            }, [images, setFieldValue]);

            return (
              <Form>
                <div className="grid grid-cols-12 gap-6">
                  <div className="col-span-6">
                    <label className="text-black" htmlFor="quantity">
                      Quantity
                    </label>
                    <Field
                      type="number"
                      name="quantity"
                      className="w-full rounded-md border border-gray-300 p-2"
                    />
                    <ErrorMessage
                      name="quantity"
                      component="div"
                      className="text-red-500 text-sm mt-1"
                    />
                  </div>
                  <div className="col-span-6">
                    <label className="text-black" htmlFor="price">
                      Product Price
                    </label>
                    <Field
                      type="number"
                      name="price"
                      className="w-full rounded-md border border-gray-300 p-2"
                    />
                    <ErrorMessage
                      name="price"
                      component="div"
                      className="text-red-500 text-sm mt-1"
                    />
                  </div>
                  <div className="col-span-6">
                    <label className="text-black" htmlFor="size">
                      Product Size
                    </label>
                    <Field
                      as="select"
                      name="size"
                      className="w-full rounded-md border border-gray-300 p-2"
                    >
                      <option value="" disabled>
                        Select Size
                      </option>
                      <option value="29">29</option>
                      <option value="30">30</option>
                      <option value="31">31</option>
                      <option value="32">32</option>
                      <option value="33">33</option>
                      <option value="34">34</option>
                      <option value="35">35</option>
                      <option value="36">36</option>
                    </Field>
                    <ErrorMessage
                      name="size"
                      component="div"
                      className="text-red-500 text-sm mt-1"
                    />
                  </div>
                  <div className="col-span-6">
                    <label className="text-black" htmlFor="color">
                      Product Color
                    </label>
                    <Field
                      as="select"
                      name="color"
                      className="w-full rounded-md border border-gray-300 p-2"
                    >
                      <option value="" disabled>
                        Select Color
                      </option>
                      <option value="RED">Red</option>
                      <option value="GREEN">Green</option>
                      <option value="BLUE">Blue</option>
                      <option value="YELLOW">Yellow</option>
                      <option value="BLACK">Black</option>
                      <option value="WHITE">White</option>
                    </Field>
                    <ErrorMessage
                      name="color"
                      component="div"
                      className="text-red-500 text-sm mt-1"
                    />
                  </div>
                  <div className="col-span-12">
                    <ErrorMessage
                      name="images"
                      component="div"
                      className="text-red-500 text-sm mt-1"
                    />
                  </div>
                  <div className="col-span-12">
                    <button
                      type="submit"
                      className="w-full bg-blue-500 text-white rounded-md p-2"
                    >
                      {loading ? "Loading..." : "Add Product Item"}
                    </button>
                  </div>
                  <div className="col-span-12">
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
