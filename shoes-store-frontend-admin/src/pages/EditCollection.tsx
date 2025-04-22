import React, { useEffect, useState } from "react";
import { Formik, Form, Field, ErrorMessage } from "formik";
import * as Yup from "yup";

import { useNavigate, useParams } from "react-router-dom";
import brandApi from "../api/colorApi";
import collectionApi from "../api/collectionApi";
import { CircularProgress } from "@mui/material";
import { enqueueSnackbar } from "notistack";
import Breadcrumb from "../components/Breadcrumbs/Breadcrumb";

// Validation schema
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

interface Collection {
  id: number;
  name: string;
  brandId: number;
}

function EditCollection() {
  const [loading, setLoading] = useState(false);
  const [brand, setBrand] = useState<Brand | null>(null);
  const [collection, setCollection] = useState<Collection | null>(null);

  const { id: brandId, collectionId } = useParams();
  const navigate = useNavigate();

  useEffect(() => {
    // Fetch brand and collection details
    const fetchData = async () => {
      setLoading(true);
      try {
        // Fetch brand by ID
        const brandResponse = await brandApi.getBrandById(brandId as string);
        setBrand(brandResponse.data);

        // Fetch collection by ID
        const collectionResponse = await collectionApi.getCollectionById(
          collectionId
        );
        setCollection(collectionResponse.data);
      } catch (error) {
        console.error("Failed to fetch data:", error);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, [brandId, collectionId]);

  const handleSubmit = async (values: { name: string }) => {
    setLoading(true);
    try {
      const response = await collectionApi.updateCollection(collectionId, {
        name: values.name,
        brandId: brandId,
      });
      console.log("Collection updated:", response.data);
      if (response.status === 200) {
        enqueueSnackbar("Collection updated successfully!", {
          variant: "success",
        });
      }
    } catch (error) {
      console.error("Failed to update collection:", error);
      enqueueSnackbar("Failed to update collection!", { variant: "error" });
    } finally {
      setLoading(false);
    }
  };

  // Show loading state while fetching data
  if (loading) {
    return (
      <div className="flex justify-center items-center h-full">
        <CircularProgress />
      </div>
    );
  }

  // Handle case when data is not found
  if (!brand || !collection) {
    return <div>Brand or Collection not found!</div>;
  }

  return (
    <>
      <Breadcrumb pageName="Update Collection" />
      <Formik
        initialValues={{
          name: collection.name || "",
          brand: brand.brandName || "",
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
                disabled={loading}
              >
                {loading ? <CircularProgress size={20} /> : "Update Collection"}
              </button>
            </div>

            {/* Back button */}
            <div className="col-span-2">
              <button
                type="button"
                className="bg-gray-500 text-white p-2 rounded-md mt-3"
                onClick={() => navigate(-1)}
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

export default EditCollection;
