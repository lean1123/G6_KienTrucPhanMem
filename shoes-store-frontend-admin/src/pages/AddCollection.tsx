import React, { useEffect } from "react";
import { Formik, Form, Field, ErrorMessage } from "formik";
import * as Yup from "yup";
import { useNavigate, useParams } from "react-router-dom"; // Sử dụng useParams từ react-router-dom
import { CircularProgress } from "@mui/material";
import { enqueueSnackbar } from "notistack";
import brandApi from "../api/colorApi";
import Breadcrumb from "../components/Breadcrumbs/Breadcrumb";
import collectionApi from "../api/collectionApi";

// Xác thực với Yup
const validationSchema = Yup.object({
  name: Yup.string()
    .required("Collection Name is required")
    .min(3, "Collection Name must be at least 3 characters"),
});

interface Brand {
  id: number;
  brandName: string;
  avatar: string;
}

function AddCollection() {
  const [loading, setLoading] = React.useState(false); // Trạng thái tải dữ liệu brand // Trạng thái khi form đang submit
  const [brand, setBrand] = React.useState<Brand>();

  const { id } = useParams();
  const navigation = useNavigate();

  useEffect(() => {
    console.log("id:", id);
    const fetchBrand = async () => {
      setLoading(true);
      try {
        const response = await brandApi.getBrandById(id as string);
        console.log("Brand:", response.data);
        setBrand(response.data);
      } catch (error) {
        console.error("Failed to fetch brand:", error);
      } finally {
        setLoading(false);
      }
    };
    fetchBrand();
  }, [id]);

  const handleSubmit = async (values: { name: string }) => {
    console.log("Form data:", values);
    try {
      const response = await collectionApi.addNewCollection({
        name: values.name,
        brandId: id,
      });
      console.log(response);
      if (response.status === 200) {
        enqueueSnackbar("Collection added successfully!", {
          variant: "success",
        });
      }
    } catch (error) {
      console.error("Failed to add collection:", error);
      enqueueSnackbar("Failed to add collection!", { variant: "error" });
    } finally {
      setLoading(false);
    }
  };

  // Hiển thị giao diện chờ khi đang tải dữ liệu
  if (loading) {
    return (
      <div className="flex justify-center items-center h-full">
        <CircularProgress />
      </div>
    );
  }

  // Nếu brand chưa được tải
  if (!brand) {
    return <div>No brand found!</div>;
  }

  return (
    <>
      <Breadcrumb pageName="Add Collection" />
      <Formik
        initialValues={{
          name: "",
          brand: brand?.brandName || "",
        }}
        validationSchema={validationSchema}
        onSubmit={(values) => handleSubmit(values)}
        enableReinitialize
      >
        {() => (
          <Form className="grid grid-cols-12 gap-4 bg-white p-3 shadow-md rounded-md">
            {/* Collection Name */}
            <div className="col-span-6">
              <label className="text-black" htmlFor="name">
                Collection Name
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

            {/* Brand */}
            <div className="col-span-6">
              <label className="text-black" htmlFor="brand">
                Brand
              </label>
              <Field
                type="text"
                id="brand"
                name="brand"
                className="w-full p-2 rounded-md border border-gray-300 bg-gray-100"
                disabled
              />
            </div>

            {/* Submit button */}
            <div className="col-span-2">
              <button
                type="submit"
                className="bg-blue-500 text-white p-2 rounded-md mt-3 min-w-[150px] flex items-center justify-center"
                onClick={() => console.log("Button clicked")}
                disabled={loading}
              >
                {loading ? <CircularProgress size={20} /> : "Add Collection"}
              </button>
            </div>

            {/* Back button */}
            <div className="col-span-2">
              <button
                type="button"
                className="bg-gray-500 text-white p-2 rounded-md mt-3"
                onClick={() => navigation(-1)}
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

export default AddCollection;
