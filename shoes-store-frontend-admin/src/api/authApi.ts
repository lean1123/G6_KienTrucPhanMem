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

  refreshToken: (refreshToken: string) => {
    const url = "/identity/auth/refresh";
    const body = { refreshToken };
    return AdminAxiosClient.post(url, body, { withCredentials: true });
  },

  logout: (token: string, refreshToken: string) => {
    const url = "/identity/auth/logout";
    const body = { token, refreshToken };
    return AdminAxiosClient.post(url, body, { withCredentials: true });
  },

  introspectToken: (token: string) => {
    const url = "/identity/auth/introspect";
    return AdminAxiosClient.post(url, { token }, { withCredentials: true });
  },
};

export default AuthAPI;
