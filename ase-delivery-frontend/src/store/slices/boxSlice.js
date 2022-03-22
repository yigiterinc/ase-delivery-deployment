import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import BoxDataService from "../../services/boxService";

const initialState = {
  boxes: [],
  delivererAssignedBoxes: [],
};

export const createBox = createAsyncThunk(
  "boxes/create",
  async ({ stationName, stationAddress }) => {
    const res = await BoxDataService.createBox({ stationName, stationAddress });
    return res.data;
  }
);

export const getBox = createAsyncThunk("boxes/getById", async ({ id }) => {
  const res = await BoxDataService.getBoxById(id);
  return res.data;
});

export const getBoxes = createAsyncThunk("/boxes/all", async () => {
  const res = await BoxDataService.getAllBoxes();
  return res.data;
});

export const updateBox = createAsyncThunk(
  "boxes/updateById",
  async ({ id, data }) => {
    console.log(id, data);
    const res = await BoxDataService.updateBox(id, data);
    return res.data;
  }
);

export const deleteBox = createAsyncThunk(
  "boxes/deleteById",
  async ({ id }) => {
    console.log(id);
    await BoxDataService.deleteBox(id);
    return { id };
  }
);

export const getBoxByDelivererId = createAsyncThunk(
  "boxes/getByDelivererId",
  async ({ id }) => {
    const res = await BoxDataService.getBoxByDelivererId(id);
    return res.data;
  }
);

const boxSlice = createSlice({
  name: "boxes",
  initialState,
  extraReducers: {
    [createBox.fulfilled]: (state, action) => {
      return {
        ...state,
        boxes: state.boxes.concat(action.payload),
      };
    },
    [getBoxes.fulfilled]: (state, action) => {
      return {
        ...state,
        boxes: action.payload,
      };
    },
    [updateBox.fulfilled]: (state, action) => {
      const payload = action.payload;

      return {
        ...state,
        boxes: state.boxes.map((box) =>
          payload.id === box.id ? action.payload : box
        ),
      };
    },
    [deleteBox.fulfilled]: (state, action) => {
      return {
        ...state,
        boxes: [state.boxes.filter((box) => box.id !== action.payload.id)],
      };
    },
    [getBoxByDelivererId.fulfilled]: (state, action) => {
      return {
        ...state,
        delivererAssignedBoxes: action.payload,
      };
    },
    [getBox.fulfilled]: (state, action) => {
      return state;
    },
  },
});

const { reducer, actions } = boxSlice;

export default reducer;
