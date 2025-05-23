import AdminAxiosClient from "./axiosClient";

const orderApi = {
  createOrder: (orderRequest: any) => {
    const url = "/orders";
    return AdminAxiosClient.post(url, orderRequest, {
      withCredentials: true,
    });
  },
  getOrdersByUserId: (userId: any) => {
    const url = `/orders/user/${userId}`;
    return AdminAxiosClient.get(url);
  },
  getAll: () => {
    const url = "/orders";
    return AdminAxiosClient.get(url);
  },

  search: (keyword: any) => {
    const url = `/orders/search`;
    return AdminAxiosClient.get(url, {
      params: { keyword },
    });
  },

  updateStatusForOrder: (orderId: string, status: string) => {
    const url = `/orders/update-order-status/${orderId}`;
    return AdminAxiosClient.put(url, null, {
      params: { status },
      withCredentials: true,
    });
  },

  getOrderById(orderId: string) {
    const url = `/orders/get-order-by-id-has-user/${orderId}`;
    return AdminAxiosClient.get(url);
  },
};

export default orderApi;
