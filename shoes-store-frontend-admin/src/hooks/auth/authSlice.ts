import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import AuthAPI from "../../api/authApi";

type LoginRequest = {
  email: string;
  password: string;
};

export const login = createAsyncThunk(
  "/login",
  async (payload: LoginRequest, { rejectWithValue }) => {
    try {
      const response = await AuthAPI.login(payload);

      if (response.status.valueOf() !== 200)
        return rejectWithValue(response.data);

      const accessToken = response.data.result.token;
      const refreshToken = response.data.result.refreshToken;

      localStorage.setItem("accessToken", accessToken);
      localStorage.setItem("refreshToken", refreshToken);

      return response.data;
    } catch (error) {
      console.error("Error in login", error);
      return rejectWithValue(error);
    }
  }
);

export const logout = createAsyncThunk(
  "/logout",
  async (_, { rejectWithValue }) => {
    try {
      const token = localStorage.getItem("accessToken");
      const refreshToken = localStorage.getItem("refreshToken");
      const response = await AuthAPI.logout(
        token as string,
        refreshToken as string
      );

      if (response.status.valueOf() !== 200) {
        return rejectWithValue(response.data);
      }

      localStorage.removeItem("accessToken");
      localStorage.removeItem("refreshToken");

      return response.data;
    } catch (error) {
      console.error("Error in logout", error);
      return rejectWithValue(error);
    }
  }
);

export const introspectToken = createAsyncThunk(
  "/introspectToken",
  async (_, { rejectWithValue }) => {
    try {
      const token = localStorage.getItem("accessToken");
      const response = await AuthAPI.introspectToken(token as string);

      if (response.status.valueOf() !== 200) {
        return rejectWithValue(response.data);
      }
      return response.data.result.valid;
    } catch (error) {
      console.error("Error in introspectToken", error);
      return rejectWithValue(error);
    }
  }
);

export const refreshToken = createAsyncThunk(
  "/refreshToken",
  async (_, { rejectWithValue }) => {
    try {
      const refreshToken = localStorage.getItem("refreshToken");
      const response = await AuthAPI.refreshToken(refreshToken as string);

      if (response.status.valueOf() !== 200) {
        return rejectWithValue(response.data);
      }

      const accessToken = response.data.result.token;
      const newRefreshToken = response.data.result.refreshToken;

      localStorage.setItem("accessToken", accessToken);
      localStorage.setItem("refreshToken", newRefreshToken);

      return response.data;
    } catch (error) {
      console.error("Error in refreshToken", error);
      return rejectWithValue(error);
    }
  }
);

const AuthSlice = createSlice({
  name: "auth",
  initialState: {
    accessToken: null,
    refreshToken: null,
    isAuthenticated: false,
  },
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(login.fulfilled, (state, action) => {
        state.accessToken = action.payload.token;
        state.refreshToken = action.payload.refreshToken;
        state.isAuthenticated = true;
      })
      .addCase(login.rejected, (state, action) => {
        console.error("Error in login", action.payload);
      })
      .addCase(logout.fulfilled, (state) => {
        state.accessToken = null;
        state.refreshToken = null;
        state.isAuthenticated = false;
      })
      .addCase(logout.rejected, (state, action) => {
        state.accessToken = null;
        state.refreshToken = null;
        state.isAuthenticated = false;
        console.error("Error in logout", action.payload);
      })
      .addCase(introspectToken.fulfilled, (state, action) => {
        state.isAuthenticated = action.payload;
      })
      .addCase(introspectToken.rejected, (state, action) => {
        state.accessToken = null;
        state.refreshToken = null;
        state.isAuthenticated = false;
        console.error("Error in introspectToken", action.payload);
      })
      .addCase(refreshToken.fulfilled, (state, action) => {
        state.accessToken = action.payload.token;
        state.refreshToken = action.payload.refreshToken;
      })
      .addCase(refreshToken.rejected, (state, action) => {
        state.isAuthenticated = false;
        console.error("Error in refreshToken", action.payload);
      });
  },
});
export default AuthSlice.reducer;
