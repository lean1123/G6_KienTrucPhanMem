import { useFormik } from "formik";
import { enqueueSnackbar } from "notistack";
import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import * as Yup from "yup";
import brandApi from "../api/colorApi";
import categoryApi from "../api/categoryApi";
import collectionApi from "../api/collectionApi";
import productApi from "../api/productApi";
import Breadcrumb from "../components/Breadcrumbs/Breadcrumb";

interface Category {
  id: string;
  name: string;
}

interface Product {
  id?: string;
  name: string;
  description: string;
  rating: number;
  category: Category;
  type: string;
  createdDate?: Date;
  modifiedDate?: Date;
}

function CreateProduct() {
  const navigate = useNavigate();

  const [loading, setLoading] = React.useState(false);
  const [success, setSuccess] = React.useState(false);
  const [categories, setCategories] = React.useState<Category[]>([]);

  const [productId, setProductId] = React.useState<String>("");
  console.log("productId:", productId);

  const fetchCategories = async () => {
    setLoading(true);
    try {
      const response = await categoryApi.getAll();
      console.log(response.data);
      setCategories(response.data.result);
    } catch (error) {
      console.error("Failed to fetch brands:", error);
    } finally {
      setLoading(false);
    }
  };

  const validationSchema = Yup.object({
    name: Yup.string().required("Product name is required"),
    description: Yup.string().required("Description is required"),
    category: Yup.object()
      .shape({
        id: Yup.string().required("Category is required"),
        name: Yup.string(),
      })
      .required("Category is required"),
    gender: Yup.string().required("Gender is required"),
  });

  const formik = useFormik({
    initialValues: {
      name: "",
      description: "",
      rating: 0,
      category: { id: "", name: "" },
      type: "MALE",
    },
    validationSchema,
    onSubmit: (values) => {
      handleSubmit(values);
    },
  });

  useEffect(() => {
    fetchCategories();
  }, []);

  const handleSubmit = async (values: Product) => {
    console.log("Form data:", values);
    setLoading(true);
    try {
      const formData = new FormData();
      formData.append("name", values.name);
      formData.append("description", values.description);
      formData.append("categoryId", values.category.id);
      formData.append("type", values.type);
      formData.append("createdDate", new Date().toISOString());
      formData.append("modifiedDate", new Date().toISOString());
      const response = await productApi.addNew(formData);
      console.log(response);
      if (response.status === 200) {
        setSuccess(true);
        setProductId(response.data.result.id);
        enqueueSnackbar("Product created successfully!", {
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

        // Set lỗi vào Formik để highlight input đỏ
        formik.setErrors(serverErrors);
      } else {
        // Các lỗi khác (không phải validation)
        enqueueSnackbar("Failed to create product!", { variant: "error" });
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <Breadcrumb pageName="Create Product" />
      <div className="grid grid-cols-12 gap-4">
        {/* Preview of the product */}
        <div className="col-span-3 max-h-[550px] p-6 rounded-md border border-gray-300 bg-white shadow-sm">
          <img
            src="https://th.bing.com/th/id/R.341824fb9731186e574fe00ab9a5da66?rik=uFs84f4Clc%2b7bw&pid=ImgRaw&r=0"
            alt="product"
            className="w-full rounded-md"
          />
          <h2 className="font-medium mt-4 text-black">Sneaker 01 black</h2>
          <p className="text-gray-500">Nike</p>
          <p className="text-black mt-4">Price:</p>
          <p className="font-medium text-black">$200</p>
          <p className="text-black mt-4">Size:</p>
          <div className="flex flex-row gap-2 mt-2">
            <button className="w-6 h-6 rounded-md bg-slate-500">
              <p className="text-white">S</p>
            </button>
            <button className="w-6 h-6 rounded-md bg-slate-500">
              <p className="text-white">M</p>
            </button>
            <button className="w-6 h-6 rounded-md bg-slate-500">
              <p className="text-white">L</p>
            </button>
          </div>
          <p className="text-black mt-4">Color:</p>
          <div className="flex flex-row gap-2 mt-2">
            <button className="w-6 h-6 rounded-full bg-black"></button>
            <button className="w-6 h-6 rounded-full bg-blue-500"></button>
            <button className="w-6 h-6 rounded-full bg-red-500"></button>
          </div>
        </div>

        {/* Form to create a product */}
        <div className="col-span-9">
          <form
            onSubmit={(e) => {
              e.preventDefault();
              formik.handleSubmit();
            }}
            className="grid grid-cols-1 gap-4 p-6 rounded-md border border-gray-300 bg-white shadow-sm"
          >
            <h2 className="text-black text-xl font-semibold">
              Product Information
            </h2>

            <div>
              <label className="text-black" htmlFor="name">
                Name
              </label>
              <input
                type="text"
                id="name"
                name="name"
                className="w-full rounded-md border border-gray-300 p-2"
                value={formik.values.name}
                onChange={formik.handleChange}
                onBlur={formik.handleBlur}
              />
              {formik.touched.name && formik.errors.name ? (
                <p className="text-red-500 text-sm">{formik.errors.name}</p>
              ) : null}
            </div>

            {/* <div>
              <label className="text-black" htmlFor="description">
                Description
              </label>
              <textarea
                id="description"
                name="description"
                className="w-full rounded-md border border-gray-300 p-2"
                value={formik.values.description}
                onChange={formik.handleChange}
                onBlur={formik.handleBlur}
              />
              {formik.touched.description && formik.errors.description ? (
                <p className="text-red-500 text-sm">
                  {formik.errors.description}
                </p>
              ) : null}
            </div> */}

            <div>
              <label className="text-black" htmlFor="category">
                Category
              </label>
              <select
                id="category"
                name="category"
                className="w-full rounded-md border border-gray-300 p-2"
                value={formik.values.category.id}
                onChange={(e) => {
                  const selectedCategory = categories.find(
                    (category) => category.id === e.target.value
                  );
                  formik.setFieldValue(
                    "category",
                    selectedCategory || { id: "", name: "" }
                  );
                }}
                onBlur={formik.handleBlur}
              >
                {/* <option value="" label="Select category" /> */}
                {categories.map((category) => (
                  <option
                    key={category.id}
                    value={category.id}
                    label={category.name}
                  />
                ))}
              </select>
              {formik.touched.category && formik.errors.category ? (
                <p className="text-red-500 text-sm">
                  {formik.errors.category.name}
                </p>
              ) : null}
            </div>

            <div>
              <label className="text-black" htmlFor="type">
                Type
              </label>
              <select
                id="type"
                name="type"
                className="w-full rounded-md border border-gray-300 p-2"
                value={formik.values.type}
                onChange={formik.handleChange}
                onBlur={formik.handleBlur}
              >
                {/* <option value="" label="Select type" /> */}
                <option value="MALE" label="Male" />
                <option value="FEMALE" label="Female" />
                <option value="CHILDREN" label="Children" />
              </select>
              {formik.touched.type && formik.errors.type ? (
                <p className="text-red-500 text-sm">{formik.errors.type}</p>
              ) : null}
            </div>

            <div className="mt-4 flex gap-6">
              <button
                type="button"
                className="w-full bg-red-500 text-white rounded-md py-2"
                onClick={() => navigate(-1)}
              >
                Cancel
              </button>
              <button
                type="submit"
                className="w-full bg-blue-500 text-white rounded-md py-2"
                onClick={() => handleSubmit(formik.values)}
                disabled={loading || success}
              >
                {loading ? "Loading..." : "Create Product"}
              </button>
              {/* <button
								type='button'
								className='w-full bg-green-500 text-white rounded-md py-2'
								onClick={() => navigate(`/admin/products/${productId}/add-item`)}
								disabled={!success}
							>
								Add Product Item
							</button> */}
            </div>
          </form>
        </div>
      </div>
    </>
  );
}

export default CreateProduct;
