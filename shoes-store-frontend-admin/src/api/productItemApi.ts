import AdminAxiosClient from "./axiosClient";

const productItemApi = {
  getAllProductItems: (productId: any) => {
    return AdminAxiosClient.get(
      `/product-items/getListProductItems/${productId}`
    );
  },

  getProductItemById: (id: any) => {
    return AdminAxiosClient.get(`/product-items/${id}`, {
      withCredentials: true,
    });
  },

  addNewProductItem: (productItemData: any) => {
    return AdminAxiosClient.post("/product-items", productItemData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },

  updateProductItem: (id: any, productItemData: any) => {
    return AdminAxiosClient.put(`/product-items/${id}`, productItemData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },

  deleteProductItem: (id: any) => {
    return AdminAxiosClient.delete(`/product-items/${id}`);
  },

  getRecentProducts: () => {
    return AdminAxiosClient.get("/product-items/recent", {
      withCredentials: true,
    });
  },

  getTopSaleProductItems: (page = 0, size = 9) => {
    return AdminAxiosClient.get("/product-items/top-sale", {
      params: { page, size },
    });
  },

  getNewProductItems: (page = 0, size = 9) => {
    return AdminAxiosClient.get("/product-items/new", {
      params: { page, size },
    });
  },

  getProductItemByColorAndSize: (productId: any, color: any, size: any) => {
    return AdminAxiosClient.get(`/product-items/get-by-color-and-size`, {
      params: {
        productId,
        color,
        size,
      },
    });
  },
};

export default productItemApi;
