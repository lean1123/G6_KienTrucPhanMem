import { CloudUpload } from "@mui/icons-material";
import React, { useEffect, useState } from "react";
import { Formik, Form, Field, ErrorMessage } from "formik";
import * as Yup from "yup";
import userApi from "../api/userApi";
import { Alert, AlertTitle, CircularProgress } from "@mui/material";
import { useNavigate, useParams } from "react-router";
import { enqueueSnackbar } from "notistack";

interface User {
  id?: string;
  firstName: string;
  lastName: string;
  phone: string;
  avatar: string | null;
  gender: string;
  email?: string;
}

function EditUser() {
  const [image, setImage] = useState<File | null>(null);
  const [loading, setLoading] = useState(false);
  const [successMessage, setSuccessMessage] = useState("");
  const [user, setUser] = useState<User>();
  const { id } = useParams();
  const navigation = useNavigate();

  // Fetch user information
  const fetchUser = async () => {
    try {
      const response = await userApi.getById(id);
      console.log(response.data);
      setUser(response.data);
    } catch (error) {
      console.error("Failed to fetch user:", error);
    }
  };

  // Schema validation
  const validationSchema = Yup.object({
    firstName: Yup.string()
      .required("First Name is required")
      .min(2, "First Name must be at least 2 characters long"),
    lastName: Yup.string()
      .required("Last Name is required")
      .min(2, "Last Name must be at least 2 characters long"),
    phone: Yup.string()
      .required("Phone number is required")
      .matches(/^[0-9]+$/, "Phone number must contain only digits"),
    gender: Yup.string().required("Gender is required"),
    // image: Yup.mixed<File>().test(
    // 	'fileType',
    // 	'Only images are allowed',
    // 	(value) => {
    // 		return (
    // 			!value ||
    // 			(value instanceof File && ['image/jpeg', 'image/png'].includes(value.type))
    // 		);
    // 	},
    // ),
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

  // Handle form submission
  const handleSubmit = async (values: User) => {
    setLoading(true);
    try {
      const formData = new FormData();
      formData.append("firstName", values.firstName);
      formData.append("lastName", values.lastName);
      formData.append("phone", values.phone);
      formData.append("gender", values.gender);
      if (image) {
        formData.append("image", image);
      }
      console.log("Form Data:", values);
      const response = await userApi.update(id, formData);
      console.log(response);
      setSuccessMessage("User information updated successfully!");
      if (response.status === 200) {
        enqueueSnackbar("User information updated successfully!", {
          variant: "success",
        });
      }
    } catch (error) {
      console.error(error);
      enqueueSnackbar("Failed to update user information!", {
        variant: "error",
      });
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchUser();
    console.log("User:", user);
  }, []);

  return (
    <div className="p-3 shadow-md rounded-md grid grid-cols-12 gap-6 bg-white">
      <div className="col-span-12">
        <h1 className="font-semibold text-xl mb-2">Edit User Information</h1>
      </div>

      <Formik
        initialValues={{
          firstName: user?.firstName || "",
          lastName: user?.lastName || "",
          phone: user?.phone || "",
          gender: user?.gender || "",
          avatar: null,
        }}
        validationSchema={validationSchema}
        onSubmit={(values) => {
          handleSubmit(values as User);
        }}
        enableReinitialize
      >
        {({ setFieldValue }) => (
          <Form className="col-span-12 grid grid-cols-12 gap-6">
            {/* Image Upload */}
            <div className="col-span-3 flex flex-col">
              <h2 className="font-medium mb-2">Avatar</h2>
              <div
                className={`flex justify-center items-center h-[200px] w-full relative border-1 border-dashed rounded-lg border-blue-500`}
              >
                {image ? (
                  <img
                    alt="User Avatar"
                    src={URL.createObjectURL(image)}
                    className="absolute w-[200px]"
                  />
                ) : (
                  <img
                    alt="User Avatar"
                    src={user?.avatar || ""}
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

            {/* User Information */}
            <div className="col-span-9">
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="text-black" htmlFor="firstName">
                    First Name
                  </label>
                  <Field
                    type="text"
                    id="firstName"
                    name="firstName"
                    className="w-full rounded-md border border-gray-300 p-2"
                  />
                  <ErrorMessage
                    name="firstName"
                    component="div"
                    className="text-red-500 text-sm mt-1"
                  />
                </div>

                <div>
                  <label className="text-black" htmlFor="lastName">
                    Last Name
                  </label>
                  <Field
                    type="text"
                    id="lastName"
                    name="lastName"
                    className="w-full rounded-md border border-gray-300 p-2"
                  />
                  <ErrorMessage
                    name="lastName"
                    component="div"
                    className="text-red-500 text-sm mt-1"
                  />
                </div>

                <div>
                  <label className="text-black" htmlFor="phone">
                    Phone
                  </label>
                  <Field
                    type="text"
                    id="phone"
                    name="phone"
                    className="w-full rounded-md border border-gray-300 p-2"
                  />
                  <ErrorMessage
                    name="phone"
                    component="div"
                    className="text-red-500 text-sm mt-1"
                  />
                </div>

                <div>
                  <label className="text-black" htmlFor="gender">
                    Gender
                  </label>
                  <Field
                    as="select"
                    id="gender"
                    name="gender"
                    className="w-full rounded-md border border-gray-300 p-2"
                  >
                    <option value="" label="Select Gender" />
                    <option value="MALE" label="Male" />
                    <option value="FEMALE" label="Female" />
                  </Field>
                  <ErrorMessage
                    name="gender"
                    component="div"
                    className="text-red-500 text-sm mt-1"
                  />
                </div>
              </div>
              <div className="flex gap-4">
                <button
                  type="submit"
                  disabled={loading}
                  className="bg-blue-500 text-white p-2 rounded-md mt-3 min-w-[200px] flex items-center justify-center"
                >
                  {loading ? <CircularProgress size={20} /> : "Save Changes"}
                </button>
                <button
                  type="button"
                  onClick={() => navigation(-1)}
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

export default EditUser;
