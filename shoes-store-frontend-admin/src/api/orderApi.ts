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
  getOrderById: (orderId: any) => {
    const url = `/orders/${orderId}`;
    return AdminAxiosClient.get(url);
  },
  search: (keyword: any) => {
    const url = `/orders/search`;
    return AdminAxiosClient.get(url, {
      params: { keyword },
    });
  },
};

export default orderApi;
