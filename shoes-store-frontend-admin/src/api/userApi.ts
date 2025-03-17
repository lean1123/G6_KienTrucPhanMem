import AdminAxiosClient from "./axiosClient";

const userApi = {
  getAll: async () => {
    return AdminAxiosClient.get("/users");
  },
  getAccount: async (id: any) => {
    return AdminAxiosClient.get(`/accounts/${id}`);
  },
  getById: async (id: any) => {
    return AdminAxiosClient.get(`/users/${id}`);
  },
  getUserInfo: async (userId: any) => {
    // Bỏ /api vì đã có trong baseURL
    const url = `/users/${userId}`;
    console.log("Calling getUserInfo with URL:", url);
    console.log("UserId received:", userId);

    try {
      const response = await AdminAxiosClient.get(url);
      console.log("getUserInfo success response:", response);
      return response;
    } catch (error) {
      const err = error as any;
      console.log("getUserInfo error details:", {
        status: err.response?.status,
        data: err.response?.data,
        message: err.message,
      });
      throw error;
    }
  },
  update: async (id: any, userData: any) => {
    return AdminAxiosClient.put(`/users/${id}`, userData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },

  search: async (keyword: any) => {
    return AdminAxiosClient.get(`/users/search`, {
      params: { keyword },
    });
  },

  delete: async (id: any) => {
    return AdminAxiosClient.delete(`/users/${id}`);
  },

  // Thêm các phương thức mới cho địa chỉ
  updateAddress: async (userId: any, addressId: any, addressData: any) => {
    const url = `/users/${userId}/addresses/${addressId}`;

    // Đảm bảo gửi đúng format
    const formattedData = {
      homeNumber: addressData.homeNumber,
      street: addressData.street,
      ward: addressData.ward,
      district: addressData.district,
      city: addressData.city,
    };

    console.log("Update address request:", {
      url,
      data: formattedData,
    });

    try {
      const response = await AdminAxiosClient.put(url, formattedData, {
        headers: {
          "Content-Type": "application/json",
        },
      });
      return response;
    } catch (error) {
      const err = error as any;
      console.error("Update address error:", err.response?.data);
      throw error;
    }
  },

  addAddress: async (userId: any, addressData: any) => {
    const url = `/users/${userId}/addresses`;
    return AdminAxiosClient.post(url, addressData, {
      headers: {
        "Content-Type": "application/json",
      },
    });
  },
};
export default userApi;
