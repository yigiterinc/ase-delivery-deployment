import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import UserDataService from "../../services/userService";
import { createDelivery } from "./deliverySlice";

const initialState = [];

export const createUser = createAsyncThunk(
  "users/createUser",
  async ({ email, password, role }) => {
    const res = await UserDataService.createUser(email, password, role);
    return res.data;
  }
);

export const getAllUsers = createAsyncThunk("users/getAllUsers", async () => {
  const res = await UserDataService.getAllUsers();
  return res.data;
});

export const deleteUser = createAsyncThunk(
  "/users/deleteUser",
  async ({ id }) => {
    console.log(id);
    const res = await UserDataService.deleteUser(id);
    return res.data;
  }
);

export const updateUser = createAsyncThunk(
  "/users/updateUser",
  async (data) => {
    console.log(data);
    const res = await UserDataService.updateUser(data);
    return res.data;
  }
);

export const fetchUser = createAsyncThunk(
  "/users/fetchUser",
  async ({ jwt }) => {
    const res = await UserDataService.fetchUser(jwt);
    return res.data;
  }
);

const userSlice = createSlice({
  name: "users",
  initialState,
  extraReducers: {
    [createUser.fulfilled]: (state, action) => {
      const payload = action.payload;

      return state.concat(payload);
    },
    [getAllUsers.fulfilled]: (state, action) => {
      return [...action.payload];
    },
    [deleteUser.fulfilled]: (state, action) => {
      return state.filter((element) => element.id !== action.payload);
    },
    [fetchUser.fulfilled]: (state, action) => {
      const payload = action.payload;
      console.log(payload);

      return [...action.payload];
    },
    [updateUser.fulfilled]: (state, action) => {
      const payload = action.payload;

      return state.map((user) =>
        payload.id === user.id ? action.payload : user
      );
    },
  },
});

const { reducer, actions } = userSlice;

export default userSlice.reducer;
