import AdminAxiosClient from "./axiosClient";

const brandApi = {
  getAllBrands: () => {
    return AdminAxiosClient.get("/brands");
  },
  getBrandById: (id: string) => {
    return AdminAxiosClient.get(`/brands/${id}`);
  },
  searchBrands: (keyword: string) => {
    return AdminAxiosClient.get(`/brands/search`, {
      params: { keyword },
    });
  },
  addNewBrand: (brandData: any) => {
    return AdminAxiosClient.post("/brands", brandData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },
  updateBrand: (id: string, brandData: any) => {
    return AdminAxiosClient.put(`/brands/${id}`, brandData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },
  deleteBrand: (id: string) => {
    return AdminAxiosClient.delete(`/brands/${id}`);
  },
};

export default brandApi;
