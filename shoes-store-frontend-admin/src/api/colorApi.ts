import AdminAxiosClient from "./axiosClient";

const prefix = "/product";

const colorApi = {
  getAllColors: () => {
    return AdminAxiosClient.get(prefix + "/external/colors");
  },
  getColorById: (id: string) => {
    return AdminAxiosClient.get(prefix + `/colors/${id}`);
  },
  searchColors: (keyword: string) => {
    return AdminAxiosClient.get(prefix + `/colors/search`, {
      params: { keyword },
    });
  },
  addNewColor: (ColorData: any) => {
    return AdminAxiosClient.post(prefix + "/colors", ColorData, {});
  },
  updateColor: (id: string, ColorData: any) => {
    return AdminAxiosClient.put(prefix + `/colors/${id}`, ColorData, {});
  },
  deleteColor: (id: string) => {
    return AdminAxiosClient.delete(prefix + `/colors/${id}`);
  },
};

export default colorApi;
