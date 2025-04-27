import React, { useEffect } from "react";
import { useFormik } from "formik";
import * as Yup from "yup";
import Breadcrumb from "../components/Breadcrumbs/Breadcrumb";
import { useNavigate, useParams } from "react-router-dom";
import brandApi from "../api/colorApi";
import collectionApi from "../api/collectionApi";
import categoryApi from "../api/categoryApi";
import productApi from "../api/productApi";
import { enqueueSnackbar } from "notistack";

interface Category {
  id: string;
  name: string;
}

interface Product {
  id?: string;
  name: string;
  description: string;
  rating?: number;
  category: Category;
  type: string;
  createdDate?: Date;
  modifiedDate?: Date;
}

function EditProduct() {
  const navigate = useNavigate();
  const { id } = useParams();

  const [loading, setLoading] = React.useState(false);
  const [categories, setCategories] = React.useState<Category[]>([]);

  const fetchProduct = async () => {
    setLoading(true);
    try {
      const response = await productApi.getById(id); // Gọi API lấy thông tin sản phẩm theo id
      console.log(response.data);
      const product = response.data.result;
      formik.setValues({
        name: product.name || "",
        description: product.description || "",
        category: product.category || { id: "", name: "" },
        type: product.type || "",
      });
    } catch (error) {
      console.error("Failed to fetch product:", error);
      alert("Failed to load product data");
    } finally {
      setLoading(false);
    }
  };

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
    category: Yup.string().required("Category is required"),
    collection: Yup.string().required("Collection is required"),
    gender: Yup.string().required("Gender is required"),
  });

  const formik = useFormik({
    initialValues: {
      name: "",
      description: "",
      category: { id: "", name: "" },
      type: "",
    },
    validationSchema,
    onSubmit: (values) => {
      handleSubmit(values);
    },
  });

  useEffect(() => {
    fetchProduct();
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
      formData.append("modifiedDate", new Date().toISOString());
      const response = await productApi.update(id, formData);
      console.log(response);
      enqueueSnackbar("Edit product successfully", { variant: "success" });
    } catch (error) {
      console.error("Failed to edit product:", error);
      alert("Failed to edit product!");
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <Breadcrumb pageName="Edit Product" />
      <div className="grid grid-cols-12 gap-4">
        {/* Form to create a product */}
        <div className="col-span-12">
          <form
            onSubmit={(e) => {
              e.preventDefault();
              formik.handleSubmit();
            }}
            className="grid grid-cols-1 gap-4 p-6 rounded-md border border-gray-300 bg-white shadow-sm"
          >
            <h2 className="text-black text-xl font-medium">ID: {id}</h2>

            <div>
              <label className="text-black" htmlFor="name">
                Product Name
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
                <option value="" label="Select category" />
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
                <option value="" label="Select type" />
                <option value="MALE" label="Male" />
                <option value="FEMALE" label="Female" />
                <option value="CHILDREN" label="Children" />
              </select>
              {formik.touched.type && formik.errors.type ? (
                <p className="text-red-500 text-sm">{formik.errors.type}</p>
              ) : null}
            </div>

            <div className="mt-4 grid grid-cols-3 gap-6">
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
                disabled={loading}
              >
                {loading ? "Loading..." : "Edit Product"}
              </button>
            </div>
          </form>
        </div>
      </div>
    </>
  );
}

export default EditProduct;
