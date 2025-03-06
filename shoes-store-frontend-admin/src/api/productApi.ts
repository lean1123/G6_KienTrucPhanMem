import AdminAxiosClient from "./axiosClient";

const productApi = {
  getAll: () => {
    const url = "/products";
    return AdminAxiosClient.get(url);
  },
  getById: (id: any) => {
    const url = `/products/${id}`;
    return AdminAxiosClient.get(url);
  },
  addNew: (productData: any) => {
    const url = "/products";
    return AdminAxiosClient.post(url, productData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },
  update: (id: any, productData: any) => {
    const url = `/products/${id}`;
    return AdminAxiosClient.put(url, productData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },
  searchByKeyword: (keyword: any) => {
    const url = `/products/searchByKeyword`;
    return AdminAxiosClient.get(url, {
      params: { keyword },
    });
  },
  delete: (id: any) => {
    const url = `/products/${id}`;
    return AdminAxiosClient.delete(url);
  },
};

export default productApi;
