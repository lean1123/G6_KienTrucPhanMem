import React, { useState, useEffect } from "react";
import Breadcrumb from "../components/Breadcrumbs/Breadcrumb";

import { Formik, Form, Field, ErrorMessage } from "formik";
import * as Yup from "yup";
import { useNavigate, useParams } from "react-router";
import productItemApi from "../api/productItemApi";
import { Replay } from "@mui/icons-material";
import { enqueueSnackbar } from "notistack";
import DragDropImageUploader from "../common/DragDropImageUploader";

export default function EditProductItem() {
  const [images, setImages] = useState<File[]>([]);
  const [oldImages, setOldImages] = useState<string[]>([]);
  const [indexImageDelete, setIndexImageDelete] = useState<number[]>([]);
  const { id, itemId } = useParams();
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
    // listDetailImages: Yup.array()
    // 	.min(1, 'At least one image is required')
    // 	.required('Images are required'),
  });

  // Initial values for the form
  const [initialValues, setInitialValues] = useState({
    quantity: "",
    price: "",
    size: "",
    color: "",
    listDetailImages: images,
    status: "INSTOCK",
    product: id,
    listImagesDelete: indexImageDelete,
  });

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
      values.listImagesDelete.forEach((indexDelete: any) => {
        formData.append("listImagesDelete", indexDelete);
      });

      // Call API to add product item
      const response = await productItemApi.updateProductItem(itemId, formData);
      if (response.status === 200) {
        enqueueSnackbar("Product item updated successfully!", {
          variant: "success",
        });
      }
    } catch (error) {
      console.error("Failed to edit product item:", error);
      enqueueSnackbar("Failed to edit product item!", { variant: "error" });
    } finally {
      setLoading(false);
    }
  };

  const fetchProductItem = async () => {
    setLoading(true);
    try {
      const response = await productItemApi.getProductItemById(itemId);
      console.log(response.data);
      const productItem = response.data.data;

      // Set initial values for the form
      setInitialValues({
        quantity: productItem.quantity || 0,
        price: productItem.price || 0,
        size: productItem.size || "",
        color: productItem.color || "",
        listDetailImages: productItem.listDetailImages || [],
        status: productItem.status || "INSTOCK",
        product: id,
        listImagesDelete: indexImageDelete,
      });
      setOldImages(productItem.listDetailImages || []);
    } catch (error) {
      console.error("Failed to fetch product item:", error);
    } finally {
      setLoading(false);
    }
  };

  const deleteImage = (index: any) => {
    setIndexImageDelete([...indexImageDelete, index]);
  };

  const refreshImages = (index: any) => {
    setIndexImageDelete(indexImageDelete.filter((item) => item !== index));
  };

  useEffect(() => {
    console.log("itemId", itemId);
  }, [itemId]);

  useEffect(() => {
    fetchProductItem();
  }, []);

  return (
    <>
      <Breadcrumb pageName="Edit Product Item" />
      <DragDropImageUploader onImagesChange={setImages} />
      <div className="p-3 rounded-md shadow-md mt-4 bg-white">
        <p className="font-bold ">List Old Image</p>
        <div className="w-full h-auto flex justify-start items-start flex-wrap max-h-[150px] overflow-y-auto overflow-x-scroll no-scrollbar mt-2">
          {oldImages.map((image, index) => {
            const isDeleted = indexImageDelete.includes(index);
            return (
              <div
                key={index}
                className="w-[100px] mr-1 h-[100px] relative mb-2"
              >
                {isDeleted && (
                  <div className="absolute rounded-md top-0 left-0 w-full h-full bg-black bg-opacity-50 flex justify-center items-center">
                    <p className="text-white">Deleted</p>
                  </div>
                )}
                {isDeleted ? (
                  <span
                    className="absolute -top-1 right-2 text-lg cursor-pointer z-[999]"
                    onClick={() => refreshImages(index)}
                  >
                    <Replay className="text-white" />
                  </span>
                ) : (
                  <span
                    className="absolute -top-1 right-2 text-lg cursor-pointer z-[999]"
                    onClick={() => deleteImage(index)}
                  >
                    &times;
                  </span>
                )}
                <img src={image} alt="" className="w-full h-full rounded" />
              </div>
            );
          })}
        </div>
        <Formik
          initialValues={initialValues}
          validationSchema={validationSchema}
          onSubmit={(values) => handleSubmit(values)}
          enableReinitialize
        >
          {({ setFieldValue }) => {
            // Sử dụng useEffect để đồng bộ images với Formik
            useEffect(() => {
              setFieldValue("listDetailImages", images);
              setFieldValue("listImagesDelete", indexImageDelete);
            }, [images, setFieldValue, indexImageDelete]);

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
                      {loading ? "Loading..." : "Save"}
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
