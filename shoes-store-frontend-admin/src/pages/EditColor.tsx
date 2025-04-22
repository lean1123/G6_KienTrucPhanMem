import { CircularProgress } from "@mui/material";
import { ErrorMessage, Field, Form, Formik } from "formik";
import { enqueueSnackbar } from "notistack";
import { useEffect, useState } from "react";
import { HexColorPicker } from "react-colorful";
import { useNavigate, useParams } from "react-router-dom";
import * as Yup from "yup";
import { default as colorApi } from "../api/colorApi";

interface Color {
  id: string;
  name: string;
  code: string;
}

function EditColor() {
  const [loading, setLoading] = useState(false);
  const { id } = useParams();
  const [color, setColor] = useState<Color | null>(null);
  const [loadingColor, setLoadingColor] = useState(false);
  const navigate = useNavigate();

  // Schema validation
  const validationSchema = Yup.object({
    name: Yup.string()
      .required("Color Name is required")
      .min(2, "Color Name must be at least 2 characters long"),
    code: Yup.string().required("Color Code is required"),
  });

  const handleSubmit = async (values: { name: string; code: string }) => {
    setLoading(true);
    const formData = new FormData();
    formData.append("name", values.name);
    formData.append("code", values.code);
    try {
      const response = await colorApi.updateColor(id as string, formData);
      if (response.status === 200) {
        enqueueSnackbar("Color updated successfully!", { variant: "success" });
      }
    } catch (error) {
      console.error(error);
      enqueueSnackbar("Failed to update color!", { variant: "error" });
    } finally {
      setLoading(false);
    }
  };

  const fetchBrand = async () => {
    setLoadingColor(true);
    try {
      const response = await colorApi.getColorById(id as string);
      setColor(response.data.result);
    } catch (error) {
      console.error("Failed to fetch brand:", error);
    } finally {
      setLoadingColor(false);
    }
  };

  useEffect(() => {
    fetchBrand();
  }, [id]);

  if (loadingColor) {
    return <div>Loading color data...</div>;
  }

  if (!color) {
    return <div>Color not found!</div>;
  }

  return (
    <div className="p-3 shadow-md rounded-md grid grid-cols-12 gap-6 bg-white">
      <div className="col-span-12">
        <h1 className="font-semibold text-xl mb-2">Edit Color</h1>
      </div>

      <Formik
        initialValues={{ name: color.name, code: color.code }}
        validationSchema={validationSchema}
        onSubmit={(values) => handleSubmit(values)}
      >
        {({ setFieldValue }) => (
          <Form className="col-span-12 grid grid-cols-12 gap-6">
            {/* Brand Information */}
            <div className="col-span-9">
              <div className="grid grid-cols-1 gap-4">
                <h2 className="text-black text-xl font-medium">
                  ID: {color.id}
                </h2>
                <div>
                  <label className="text-black" htmlFor="brandName">
                    Name
                  </label>
                  <Field
                    type="text"
                    id="name"
                    name="name"
                    className="w-full rounded-md border border-gray-300 p-2"
                  />
                  <ErrorMessage
                    name="name"
                    component="div"
                    className="text-red-500 text-sm mt-1"
                  />
                </div>
                <div>
                  <label className="text-black" htmlFor="ColorCode">
                    Code
                  </label>
                  <HexColorPicker
                    color={color.code}
                    onChange={(newColor) => {
                      setFieldValue("code", newColor);
                      setColor({ ...color, code: newColor });
                    }}
                  />
                  <ErrorMessage
                    name="ColorCode"
                    component="div"
                    className="text-red-500 text-sm mt-1"
                  />
                </div>
              </div>
              <div className="flex gap-4 mt-3">
                <button
                  type="submit"
                  disabled={loading}
                  className="bg-blue-500 text-white p-2 rounded-md mt-3 min-w-[200px] flex items-center justify-center"
                >
                  {loading ? <CircularProgress size={20} /> : "Update Color"}
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
            </div>
          </Form>
        )}
      </Formik>
    </div>
  );
}

export default EditColor;
