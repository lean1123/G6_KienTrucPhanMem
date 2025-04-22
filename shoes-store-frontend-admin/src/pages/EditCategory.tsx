import React, { useEffect, useState } from "react";
import { Formik, Form, Field, ErrorMessage } from "formik";
import * as Yup from "yup";
import { useNavigate, useParams } from "react-router-dom";
import categoryApi from "../api/categoryApi";
import { CircularProgress } from "@mui/material";
import { enqueueSnackbar } from "notistack";
import Breadcrumb from "../components/Breadcrumbs/Breadcrumb";

// Validation schema using Yup
const validationSchema = Yup.object({
  name: Yup.string()
    .required("Category Name is required")
    .min(3, "Category Name must be at least 3 characters"),
});

function EditCategory() {
  const [loading, setLoading] = useState(false);
  const [category, setCategory] = useState<{
    name: string;
    description: string;
  } | null>(null);
  const navigate = useNavigate();
  const { id } = useParams();

  const fetchCategory = async () => {
    setLoading(true);
    try {
      const response = await categoryApi.getById(id);
      setCategory(response.data.result);
    } catch (error) {
      console.error("Failed to fetch category:", error);
    } finally {
      setLoading(false);
    }
  };
  useEffect(() => {
    // Fetch category data by ID

    fetchCategory();
  }, [id]);

  // Handle form submission
  const handleSubmit = async (values: { name: string }) => {
    setLoading(true);
    try {
      const response = await categoryApi.update(id, {
        name: values.name,
        description: category?.description || "",
      });
      console.log(response);
      if (response.status === 200) {
        enqueueSnackbar("Category updated successfully!", {
          variant: "success",
        });
      }
    } catch (error) {
      console.error("Failed to update category:", error);
      enqueueSnackbar("Failed to update category!", { variant: "error" });
    } finally {
      setLoading(false);
    }
  };

  // Show loading spinner while fetching category data
  if (loading) {
    return (
      <div className="flex justify-center items-center h-full">
        <CircularProgress />
      </div>
    );
  }

  // Handle case when category is not found
  if (!category) {
    return <div>Category not found!</div>;
  }

  return (
    <>
      <Breadcrumb pageName="Edit Category" />
      <Formik
        initialValues={{ name: category.name }}
        validationSchema={validationSchema}
        onSubmit={(values) => {
          handleSubmit(values);
        }}
        enableReinitialize // Allow Formik to reinitialize when category data is loaded
      >
        {() => (
          <Form className="grid grid-cols-12 gap-4 p-3 rounded-md shadow-md bg-white">
            <h2 className="text-black text-xl font-medium col-span-12">
              ID: {id}
            </h2>
            {/* Category Name */}
            <div className="col-span-12">
              <label className="text-black" htmlFor="name">
                Category Name
              </label>
              <Field
                type="text"
                id="name"
                name="name"
                className="w-full p-2 rounded-md border border-gray-300"
              />
              <ErrorMessage
                name="name"
                component="div"
                className="text-red-500 text-sm mt-1"
              />
            </div>

            {/* Submit button */}
            <div className="col-span-12 flex gap-4">
              <button
                type="submit"
                disabled={loading}
                className="bg-blue-500 text-white p-2 rounded-md mt-3 min-w-[150px] flex items-center justify-center"
              >
                {loading ? <CircularProgress size={20} /> : "Update Category"}
              </button>

              <button
                type="button"
                disabled={loading}
                onClick={() => navigate(-1)}
                className="bg-gray-500 text-white p-2 rounded-md mt-3 min-w-[150px] flex items-center justify-center"
              >
                Back
              </button>
            </div>
          </Form>
        )}
      </Formik>
    </>
  );
}

export default EditCategory;
