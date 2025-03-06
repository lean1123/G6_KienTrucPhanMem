import AdminAxiosClient from "./axiosClient";

type LoginRequest = {
  email: string;
  password: string;
};

const AuthAPI = {
  login: ({ email, password }: LoginRequest) => {
    const url = "/identity/auth/token";
    const body = { email, password };
    return AdminAxiosClient.post(url, body, { withCredentials: true });
  },

  refreshToken: (token: string) => {
    const url = "/identity/auth/refresh-token";
    const body = { token };
    return AdminAxiosClient.post(url, body, { withCredentials: true });
  },

  logout: (token: string) => {
    const url = "/identity/auth/logout";
    return AdminAxiosClient.post(url, { token }, { withCredentials: true });
  },
};

export default AuthAPI;
