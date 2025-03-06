import AdminAxiosClient from "./axiosClient";

const categoryApi = {
  getAll: () => {
    return AdminAxiosClient.get("/categories");
  },
  getById: (id: any) => {
    return AdminAxiosClient.get(`/categories/${id}`);
  },
  addNew: (categoryData: any) => {
    return AdminAxiosClient.post("/categories", categoryData);
  },
  search: (keyword: any) => {
    return AdminAxiosClient.get(`/categories/search`, {
      params: { keyword },
    });
  },
  update: (id: any, categoryData: any) => {
    return AdminAxiosClient.put(`/categories/${id}`, categoryData);
  },
  delete: (id: any) => {
    return AdminAxiosClient.delete(`/categories/${id}`);
  },
};

export default categoryApi;
