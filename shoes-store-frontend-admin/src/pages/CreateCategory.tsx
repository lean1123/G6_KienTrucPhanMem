import React from "react";
import { Formik, Form, Field, ErrorMessage } from "formik";
import * as Yup from "yup";
import { useNavigate } from "react-router-dom";
import { CircularProgress } from "@mui/material";
import { enqueueSnackbar } from "notistack";
import categoryApi from "../api/categoryApi";
import Breadcrumb from "../components/Breadcrumbs/Breadcrumb";

// Validation schema using Yup
const validationSchema = Yup.object({
  name: Yup.string()
    .required("Category Name is required")
    .min(3, "Category Name must be at least 3 characters"),
});

function CreateCategory() {
  const [loading, setLoading] = React.useState(false);
  const navigation = useNavigate();
  // Handle form submission
  const handleSubmit = async (values: { name: string }) => {
    setLoading(true);
    try {
      const response = await categoryApi.addNew({
        name: values.name,
      });
      if (response.status === 200) {
        enqueueSnackbar("Category created successfully!", {
          variant: "success",
        });
      }
    } catch (error: any) {
      console.error("Failed to create product:", error);

      // Nếu BE trả về lỗi validation
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

          // 🔥 Dùng enqueueSnackbar luôn cho mỗi field lỗi
          enqueueSnackbar(`${message}`, { variant: "error" });
        });
        console.log("serverErrors:", serverErrors);
      } else {
        // Các lỗi khác (không phải validation)
        enqueueSnackbar("Failed to create category!", { variant: "error" });
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <Breadcrumb pageName="Create Category" />
      <Formik
        initialValues={{ name: "" }}
        validationSchema={validationSchema}
        onSubmit={(values) => {
          handleSubmit(values);
        }}
      >
        {() => (
          <Form className="grid grid-cols-12 gap-4 p-3 rounded-md shadow-md bg-white">
            {/* Category Name */}
            <div className="col-span-12">
              <label className="text-black" htmlFor="name">
                Name
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
                {loading ? <CircularProgress size={20} /> : "Create Category"}
              </button>
              <button
                type="button"
                disabled={loading}
                onClick={() => navigation(-1)}
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

export default CreateCategory;
