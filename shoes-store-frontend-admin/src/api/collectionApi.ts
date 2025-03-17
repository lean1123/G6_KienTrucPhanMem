import AdminAxiosClient from "./axiosClient";

const collectionApi = {
  getCollectionByBrand: (brand: any) => {
    return AdminAxiosClient.get(`/collections/brand/${brand}`);
  },
  getCollectionById: (id: any) => {
    return AdminAxiosClient.get(`/collections/${id}`);
  },
  searchCollections: (brandId: any, keyword: any) => {
    return AdminAxiosClient.get(`/collections/${brandId}/search`, {
      params: { keyword },
    });
  },
  addNewCollection: (collectionData: any) => {
    return AdminAxiosClient.post("/collections", collectionData);
  },
  updateCollection: (id: any, collectionData: any) => {
    return AdminAxiosClient.put(`/collections/${id}`, collectionData);
  },
  deleteCollection: (id: any) => {
    return AdminAxiosClient.delete(`/collections/${id}`);
  },
};

export default collectionApi;
