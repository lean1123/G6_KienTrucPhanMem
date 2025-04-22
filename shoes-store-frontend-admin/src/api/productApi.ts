import AdminAxiosClient from "./axiosClient";

const prefix = "/product";

const productApi = {
  getAll: () => {
    const url = prefix + "/products";
    return AdminAxiosClient.get(url);
  },
  getById: (id: any) => {
    const url = prefix + `/products/${id}`;
    return AdminAxiosClient.get(url);
  },
  addNew: (productData: any) => {
    const url = prefix + "/products";
    return AdminAxiosClient.post(url, productData, {});
  },
  update: (id: any, productData: any) => {
    const url = prefix + `/products/${id}`;
    return AdminAxiosClient.put(url, productData, {});
  },
  search: (keyword: any) => {
    const url = prefix + `/products/search`;
    return AdminAxiosClient.get(url, {
      params: { keyword },
    });
  },
  delete: (id: any) => {
    const url = prefix + `/products/${id}`;
    return AdminAxiosClient.delete(url);
  },
};

export default productApi;
