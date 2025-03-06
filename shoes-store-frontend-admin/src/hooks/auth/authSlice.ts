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

      localStorage.setItem("accessToken", accessToken);

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
      const response = await AuthAPI.logout(token as string);

      if (response.status.valueOf() !== 200) {
        return rejectWithValue(response.data);
      }

      localStorage.removeItem("accessToken");

      return response.data;
    } catch (error) {
      console.error("Error in logout", error);
      return rejectWithValue(error);
    }
  }
);

const AuthSlice = createSlice({
  name: "auth",
  initialState: {
    accessToken: null,
    isAuthenticated: false,
  },
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(login.fulfilled, (state, action) => {
        state.accessToken = action.payload.token;
        state.isAuthenticated = true;
      })
      .addCase(login.rejected, (state, action) => {
        console.error("Error in login", action.payload);
      })
      .addCase(logout.fulfilled, (state) => {
        state.accessToken = null;
        state.isAuthenticated = false;
      })
      .addCase(logout.rejected, (state, action) => {
        state.accessToken = null;
        console.error("Error in logout", action.payload);
      });
  },
});
export default AuthSlice.reducer;
