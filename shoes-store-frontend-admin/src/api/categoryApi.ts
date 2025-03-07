import AdminAxiosClient from "./axiosClient";

const categoryApi = {
  getAll: () => {
    return AdminAxiosClient.get("/product/external/categories");
  },
  getById: (id: any) => {
    return AdminAxiosClient.get(`/product/categories/${id}`);
  },
  addNew: (categoryData: any) => {
    return AdminAxiosClient.post("/product/categories", categoryData);
  },
  search: (keyword: any) => {
    return AdminAxiosClient.get(`/product/categories/search`, {
      params: { keyword },
    });
  },
  update: (id: any, categoryData: any) => {
    return AdminAxiosClient.put(`/product/categories/${id}`, categoryData);
  },
  delete: (id: any) => {
    return AdminAxiosClient.delete(`/product/categories/${id}`);
  },
};

export default categoryApi;
