import { CircularProgress } from "@mui/material";
import { ErrorMessage, Field, Form, Formik } from "formik";
import { enqueueSnackbar } from "notistack";
import React, { useState } from "react";
import { HexColorPicker } from "react-colorful";
import * as Yup from "yup";
import ColorApi from "../api/colorApi";

function CreateColor() {
  const [color, setColor] = useState("");
  const [loading, setLoading] = useState(false);

  // Schema validation
  const validationSchema = Yup.object({
    ColorName: Yup.string()
      .required("Color Name is required")
      .min(2, "Color Name must be at least 2 characters long"),
    ColorCode: Yup.string().required("Color Code is required"),
  });

  const handleSubmit = async (values: {
    ColorName: string;
    ColorCode: string;
  }) => {
    setLoading(true);
    console.log(values);
    const formData = new FormData();
    formData.append("name", values.ColorName);
    formData.append("code", values.ColorCode);
    try {
      const response = await ColorApi.addNewColor(formData);
      console.log(response);
      if (response.status === 200) {
        enqueueSnackbar("Color created successfully!", { variant: "success" });
      }
    } catch (error) {
      console.error("Failed to create Color:", error);
      enqueueSnackbar("Failed to create Color", { variant: "error" });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-3 shadow-md rounded-md grid grid-cols-12 gap-6 bg-white">
      <div className="col-span-12">
        <h1 className="font-semibold text-xl mb-2">Create a New Color</h1>
      </div>

      <Formik
        initialValues={{ ColorName: "", ColorCode: color }}
        validationSchema={validationSchema}
        onSubmit={(values) => {
          handleSubmit(values);
        }}
      >
        {({ setFieldValue }) => (
          <Form className="col-span-12 grid grid-cols-12 gap-6">
            <div className="col-span-9">
              <div className="grid grid-cols-1 gap-4">
                <div>
                  <label className="text-black" htmlFor="ColorName">
                    Name
                  </label>
                  <Field
                    type="text"
                    id="ColorName"
                    name="ColorName"
                    className="w-full rounded-md border border-gray-300 p-2"
                    placeholder="Color Name"
                  />
                  <ErrorMessage
                    name="ColorName"
                    component="div"
                    className="text-red-500 text-sm mt-1"
                  />
                </div>
                <div>
                  <label className="text-black" htmlFor="ColorCode">
                    Code
                  </label>
                  <HexColorPicker
                    color={color}
                    onChange={(newColor) => {
                      setFieldValue("ColorCode", newColor);
                      setColor(newColor);
                    }}
                  />
                  <ErrorMessage
                    name="ColorCode"
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
                {loading ? <CircularProgress size={20} /> : "Create Color"}
              </button>
            </div>
          </Form>
        )}
      </Formik>
    </div>
  );
}

export default CreateColor;
