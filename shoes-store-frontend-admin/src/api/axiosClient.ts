import axios from "axios";
import AuthAPI from "./authApi";
import { ref } from "yup";

interface PublicEndpoint {
  urlPattern: RegExp;
  methods: string[];
}

const AdminAxiosClient = axios.create({
  baseURL: "http://localhost:8888/api/v1",
  headers: {
    "Content-Type": "application/json",
  },
});

const getToken = () => {
  const token = localStorage.getItem("accessToken");
  return token ? token : "";
};

const getRefreshToken = () => {
  const refreshToken = localStorage.getItem("refreshToken");
  return refreshToken ? refreshToken : "";
};

AdminAxiosClient.interceptors.request.use(
  async (config) => {
    const publicEndpoints: PublicEndpoint[] = [
      { urlPattern: /\/identity\/auth\/token/, methods: ["POST"] }, // POST /auth/login
      { urlPattern: /\/identity\/auth\/refresh-token/, methods: ["POST"] }, // POST /auth/refreshToken
      { urlPattern: /\/identity\/auth\/introspect/, methods: ["POST"] },
    ];

    console.log("Request URL:", config.url);
    console.log("Request Method:", config.method);
    console.log("Authorization Header Before:", config.headers?.Authorization);

    const isPublicEndpoint = publicEndpoints.some((endpoint) => {
      if (!config.url || !config.method) return false; // Kiểm tra tránh lỗi undefined
      return (
        endpoint.urlPattern.test(config.url) &&
        endpoint.methods.includes(config.method.toUpperCase())
      );
    });

    if (isPublicEndpoint) {
      console.log("Public endpoint:", config.url);
      if (config.headers.Authorization) {
        delete config.headers.Authorization;
      }
      return config;
    }

    const token = getToken();

    config.headers.Authorization = `Bearer ${token}`;

    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

AdminAxiosClient.interceptors.response.use(
  (response) => {
    return response;
  },
  async (error) => {
    const originalRequest = error.config;

    if (error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      let refreshToken = getRefreshToken();

      if (refreshToken) {
        try {
          const refreshResponse = await AuthAPI.refreshToken(refreshToken);

          console.log("refreshedToken in admin axios: ", refreshResponse);
          if (refreshResponse.status !== 200) {
            return Promise.reject(error);
          }
          let token = await refreshResponse.data.result.token;
          localStorage.setItem("accessToken", token);
          let newRefreshToken = await refreshResponse.data.result.refreshToken;
          localStorage.setItem("refreshToken", newRefreshToken);
          originalRequest.headers.Authorization = `Bearer ${token}`;

          return AdminAxiosClient(originalRequest);
        } catch (error) {
          console.error("Error during token refresh request", error);
          //   logOutCurrentUser();
          return Promise.reject(error);
        }
      }

      return Promise.reject(error);
    }

    if (error.response) {
      console.error("Response error", error.response);
    } else if (error.request) {
      console.error("No response received", error.request);
    } else {
      console.error("Request setup error", error.message);
    }

    return Promise.reject(error);
  }
);

export default AdminAxiosClient;
