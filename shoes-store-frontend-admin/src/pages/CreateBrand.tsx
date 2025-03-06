import { CloudUpload } from "@mui/icons-material";
import React, { useState } from "react";
import { Formik, Form, Field, ErrorMessage } from "formik";
import * as Yup from "yup";
import { CircularProgress } from "@mui/material";
import { enqueueSnackbar } from "notistack";
import brandApi from "../api/brandApi";

function CreateBrand() {
  const [image, setImage] = useState<File | null>(null);
  const [loading, setLoading] = useState(false);

  // Schema validation
  const validationSchema = Yup.object({
    brandName: Yup.string()
      .required("Brand Name is required")
      .min(2, "Brand Name must be at least 2 characters long"),
    image: Yup.mixed<File>()
      .required("Brand image is required")
      .test("fileType", "Only images are allowed", (value) => {
        // Check if the value is a File and verify the type
        return (
          value instanceof File &&
          ["image/jpeg", "image/png", "image/jpg"].includes(value.type)
        );
      }),
  });

  // Handle image input change
  const handleImageChange = (
    e: React.ChangeEvent<HTMLInputElement>,
    setFieldValue: Function
  ) => {
    const file = e.target.files?.[0];
    if (file) {
      setImage(file);
      setFieldValue("image", file);
    }
  };

  const handleSubmit = async (values: {
    brandName: string;
    avatar: File | null;
  }) => {
    setLoading(true);
    if (!image) {
      alert("Please select an image");
      return;
    }
    console.log(values);
    const formData = new FormData();
    formData.append("brandName", values.brandName);
    formData.append("avatar", image);
    try {
      const response = await brandApi.addNewBrand(formData);
      console.log(response);
      if (response.status === 200) {
        enqueueSnackbar("Brand created successfully!", { variant: "success" });
      }
    } catch (error) {
      console.error("Failed to create brand:", error);
      enqueueSnackbar("Failed to create brand", { variant: "error" });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-3 shadow-md rounded-md grid grid-cols-12 gap-6 bg-white">
      <div className="col-span-12">
        <h1 className="font-semibold text-xl mb-2">Create a New Brand</h1>
      </div>

      <Formik
        initialValues={{ brandName: "", avatar: null }}
        validationSchema={validationSchema}
        onSubmit={(values) => {
          handleSubmit(values);
        }}
      >
        {({ setFieldValue }) => (
          <Form className="col-span-12 grid grid-cols-12 gap-6">
            {/* Image Upload */}
            <div className="col-span-3 flex flex-col">
              <h2 className="font-medium mb-2">Brand Image</h2>
              <div
                className={`flex justify-center items-center h-[200px] w-full relative border-1 border-dashed rounded-lg border-blue-500`}
              >
                {image && (
                  <img
                    alt="Brand"
                    src={URL.createObjectURL(image)}
                    className="absolute w-[200px]"
                  />
                )}

                <input
                  type="file"
                  accept="image/*"
                  className={`block h-full w-full absolute opacity-0 z-10 cursor-pointer`}
                  onChange={(e) => handleImageChange(e, setFieldValue)}
                />
                {!image && <CloudUpload className="text-blue-500 z-0" />}
              </div>
              <ErrorMessage
                name="image"
                component="div"
                className="text-red-500 text-sm mt-1"
              />
            </div>

            {/* Brand Information */}
            <div className="col-span-9">
              <div className="grid grid-cols-1 gap-4">
                <h2 className="text-black text-xl font-medium">
                  Brand Information
                </h2>
                <div>
                  <label className="text-black" htmlFor="brandName">
                    Brand Name
                  </label>
                  <Field
                    type="text"
                    id="brandName"
                    name="brandName"
                    className="w-full rounded-md border border-gray-300 p-2"
                  />
                  <ErrorMessage
                    name="brandName"
                    component="div"
                    className="text-red-500 text-sm mt-1"
                  />
                </div>
              </div>
              <button
                type="submit"
                disabled={loading}
                className="bg-blue-500 text-white p-2 rounded-md mt-3 min-w-[200px] flex items-center justify-center"
              >
                {loading ? <CircularProgress size={20} /> : "Create Brand"}
              </button>
            </div>
          </Form>
        )}
      </Formik>
    </div>
  );
}

export default CreateBrand;
