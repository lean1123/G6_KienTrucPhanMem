import { CloudUpload } from "@mui/icons-material";
import React, { useState, useEffect } from "react";
import { Formik, Form, Field, ErrorMessage } from "formik";
import * as Yup from "yup";
import { CircularProgress } from "@mui/material";
import { useNavigate, useParams } from "react-router";
import { enqueueSnackbar } from "notistack";
import brandApi from "../api/brandApi";

interface Brand {
  id: number;
  brandName: string;
  avatar: string;
}

function EditBrand() {
  const [image, setImage] = useState<File | null>(null);
  const [loading, setLoading] = useState(false);
  const [previewImage, setPreviewImage] = useState<string | null>(null);
  const { id } = useParams();
  const [brand, setBrand] = useState<Brand | null>(null);
  const [loadingBrand, setLoadingBrand] = useState(false);
  const navigate = useNavigate();

  // Schema validation
  const validationSchema = Yup.object({
    brandName: Yup.string()
      .required("Brand Name is required")
      .min(2, "Brand Name must be at least 2 characters long"),
  });

  // Handle image input change
  const handleImageChange = (
    e: React.ChangeEvent<HTMLInputElement>,
    setFieldValue: Function
  ) => {
    const file = e.target.files?.[0];
    if (file) {
      setImage(file);
      setPreviewImage(URL.createObjectURL(file));
      setFieldValue("avatar", file);
    }
  };

  const handleSubmit = async (values: {
    brandName: string;
    avatar: File | null;
  }) => {
    setLoading(true);
    const formData = new FormData();
    formData.append("brandName", values.brandName);
    if (image) {
      formData.append("avatar", image);
    }
    try {
      const response = await brandApi.updateBrand(id as string, formData);
      // alert('Brand updated successfully!');
      // navigate('/brands'); // Điều hướng về trang danh sách thương hiệu
      if (response.status === 200) {
        enqueueSnackbar("Brand updated successfully!", { variant: "success" });
      }
    } catch (error) {
      console.error(error);
      enqueueSnackbar("Failed to update brand!", { variant: "error" });
    } finally {
      setLoading(false);
    }
  };

  const fetchBrand = async () => {
    setLoadingBrand(true);
    try {
      const response = await brandApi.getBrandById(id as string);
      setBrand(response.data);
      setPreviewImage(response.data.avatar); // Hiển thị ảnh hiện tại
    } catch (error) {
      console.error("Failed to fetch brand:", error);
    } finally {
      setLoadingBrand(false);
    }
  };

  useEffect(() => {
    fetchBrand();
  }, [id]);

  if (loadingBrand) {
    return <div>Loading brand data...</div>;
  }

  if (!brand) {
    return <div>Brand not found!</div>;
  }

  return (
    <div className="p-3 shadow-md rounded-md grid grid-cols-12 gap-6 bg-white">
      <div className="col-span-12">
        <h1 className="font-semibold text-xl mb-2">Edit Brand</h1>
      </div>

      <Formik
        initialValues={{ brandName: brand.brandName, avatar: null }}
        validationSchema={validationSchema}
        onSubmit={(values) => handleSubmit(values)}
      >
        {({ setFieldValue }) => (
          <Form className="col-span-12 grid grid-cols-12 gap-6">
            {/* Image Upload */}
            <div className="col-span-3 flex flex-col">
              <h2 className="font-medium mb-2">Brand Image</h2>
              <div
                className={`flex justify-center items-center h-[200px] w-full relative border-1 border-dashed rounded-lg border-blue-500`}
              >
                {previewImage && (
                  <img
                    alt="Brand"
                    src={previewImage}
                    className="absolute w-[200px]"
                  />
                )}

                <input
                  type="file"
                  accept="image/*"
                  className={`block h-full w-full absolute opacity-0 z-10 cursor-pointer`}
                  onChange={(e) => handleImageChange(e, setFieldValue)}
                />
                {!previewImage && <CloudUpload className="text-blue-500 z-0" />}
              </div>
              <ErrorMessage
                name="avatar"
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
                {loading ? <CircularProgress size={20} /> : "Update Brand"}
              </button>

              <button
                type="button"
                disabled={loading}
                onClick={() => {
                  navigate(-1);
                }}
                className="bg-gray-500 text-white p-2 rounded-md mt-3 min-w-[200px] flex items-center justify-center"
              >
                Back
              </button>
            </div>
          </Form>
        )}
      </Formik>
    </div>
  );
}

export default EditBrand;
