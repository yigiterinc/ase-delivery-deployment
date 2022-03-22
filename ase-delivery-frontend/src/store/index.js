import { configureStore } from "@reduxjs/toolkit";

import deliveryReducer from "./slices/deliverySlice"
import loginReducer from "./slices/loginSlice";
import userReducer from './slices/userSlice'
import boxReducer from "./slices/boxSlice";

const reducer = {
    login: loginReducer,
    delivery: deliveryReducer,
    user: userReducer,
    box: boxReducer,
}

const store = configureStore({
    reducer: reducer,
    devTools: true,
});

export default store;