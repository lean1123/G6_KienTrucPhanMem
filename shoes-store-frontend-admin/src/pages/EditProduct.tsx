import React, { useEffect } from "react";
import { useFormik } from "formik";
import * as Yup from "yup";
import Breadcrumb from "../components/Breadcrumbs/Breadcrumb";
import { useNavigate, useParams } from "react-router";
import brandApi from "../api/brandApi";
import collectionApi from "../api/collectionApi";
import categoryApi from "../api/categoryApi";
import productApi from "../api/productApi";
import { enqueueSnackbar } from "notistack";

interface Brand {
  id: string;
  brandName: string;
  avatar: string;
}

interface Collection {
  id: string;
  name: string;
  brandId: string;
}

interface Category {
  id: number;
  name: string;
  description: string;
}

interface Product {
  id?: string;
  name: string;
  description: string;
  category: string;
  collection: string;
  gender: string;
  createDate?: Date;
}

function EditProduct() {
  const navigate = useNavigate();
  const { id } = useParams();

  const [loading, setLoading] = React.useState(false);
  const [brands, setBrands] = React.useState<Brand[]>([]);
  const [selectedBrand, setSelectedBrand] = React.useState<Brand>();
  const [collections, setCollections] = React.useState<Collection[]>([]);
  const [categories, setCategories] = React.useState<Category[]>([]);

  const fetchProduct = async () => {
    setLoading(true);
    try {
      const response = await productApi.getById(id); // Gọi API lấy thông tin sản phẩm theo id
      console.log(response.data);
      const product = response.data;
      // brand
      const brandResponse = await brandApi.getBrandById(
        product.collection.brandId
      );
      setSelectedBrand(brandResponse.data);
      formik.setValues({
        name: product.name || "",
        description: product.description || "",
        category: product.category.id || "", // Thay đổi để khớp với định dạng của API
        collection: product.collection.id || "", // Thay đổi để khớp với định dạng của API
        gender: product.gender || "",
      });
    } catch (error) {
      console.error("Failed to fetch product:", error);
      alert("Failed to load product data");
    } finally {
      setLoading(false);
    }
  };

  const fetchBrands = async () => {
    setLoading(true);
    try {
      const response = await brandApi.getAllBrands();
      console.log(response.data);
      setBrands(response.data);
    } catch (error) {
      console.error("Failed to fetch brands:", error);
    } finally {
      setLoading(false);
    }
  };

  const fetchCollections = async () => {
    setLoading(true);
    try {
      const response = await collectionApi.getCollectionByBrand(
        selectedBrand?.id
      );
      console.log(response.data);
      setCollections(response.data);
    } catch (error) {
      console.error("Failed to fetch brands:", error);
    } finally {
      setLoading(false);
    }
  };

  const fetchCategories = async () => {
    setLoading(true);
    try {
      const response = await categoryApi.getAll();
      console.log(response.data);
      setCategories(response.data);
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
      category: "",
      collection: "",
      gender: "",
    },
    validationSchema,
    onSubmit: (values) => {
      handleSubmit(values);
    },
  });

  useEffect(() => {
    fetchProduct();
    fetchBrands();
    fetchCategories();
  }, []);

  useEffect(() => {
    if (brands.length > 0) {
      setSelectedBrand(brands[0]);
    }
  }, [brands]);

  useEffect(() => {
    if (selectedBrand) {
      fetchCollections();
    }
  }, [selectedBrand]);

  const handleSubmit = async (values: Product) => {
    console.log("Form data:", values);
    setLoading(true);
    try {
      const formData = new FormData();
      formData.append("name", values.name);
      formData.append("description", values.description);
      formData.append("category", values.category);
      formData.append("collection", values.collection);
      formData.append("gender", values.gender);
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

            <div>
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
            </div>

            <div>
              <label className="text-black" htmlFor="brand">
                Brand
              </label>
              <select
                id="brand"
                name="brand"
                className="w-full rounded-md border border-gray-300 p-2"
                value={
                  brands.find((brand) => brand.id === selectedBrand?.id)?.id
                }
                onChange={(e) => {
                  const selectedBrand = brands.find(
                    (brand) => brand.id === e.target.value
                  );
                  setSelectedBrand(selectedBrand);
                }}
                // onBlur={formik.handleBlur}
              >
                {brands.map((brand) => (
                  <option
                    key={brand.id}
                    value={brand.id}
                    label={brand.brandName}
                  />
                ))}
                {/* <option value='' label='Select Brand' />
								<option value='Nike' label='Nike' />
								<option value='Adidas' label='Adidas' />
								<option value='Puma' label='Puma' /> */}
              </select>
              {/* {formik.touched.brand && formik.errors.brand ? (
								<p className='text-red-500 text-sm'>{formik.errors.brand}</p>
							) : null} */}
            </div>

            <div>
              <label className="text-black" htmlFor="category">
                Category
              </label>
              <select
                id="category"
                name="category"
                className="w-full rounded-md border border-gray-300 p-2"
                value={formik.values.category}
                onChange={formik.handleChange}
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
                <p className="text-red-500 text-sm">{formik.errors.category}</p>
              ) : null}
            </div>

            <div>
              <label className="text-black" htmlFor="collection">
                Collection
              </label>
              <select
                id="collection"
                name="collection"
                className="w-full rounded-md border border-gray-300 p-2"
                value={formik.values.collection}
                onChange={formik.handleChange}
                onBlur={formik.handleBlur}
              >
                <option value="" label="Select collection" />
                {collections.map((collection) => (
                  <option
                    key={collection.id}
                    value={collection.id}
                    label={collection.name}
                  />
                ))}
              </select>
              {formik.touched.collection && formik.errors.collection ? (
                <p className="text-red-500 text-sm">
                  {formik.errors.collection}
                </p>
              ) : null}
            </div>

            <div>
              <label className="text-black" htmlFor="gender">
                Gender
              </label>
              <select
                id="gender"
                name="gender"
                className="w-full rounded-md border border-gray-300 p-2"
                value={formik.values.gender}
                onChange={formik.handleChange}
                onBlur={formik.handleBlur}
              >
                <option value="" label="Select gender" />
                <option value="MEN" label="MEN" />
                <option value="WOMAN" label="WOMAN" />
                <option value="UNISEX" label="UNISEX" />
              </select>
              {formik.touched.gender && formik.errors.gender ? (
                <p className="text-red-500 text-sm">{formik.errors.gender}</p>
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
                className="w-full bg-primary text-white rounded-md py-2"
                onClick={
                  () => console.log("clicked") // () => navigate('/admin/products')
                }
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
