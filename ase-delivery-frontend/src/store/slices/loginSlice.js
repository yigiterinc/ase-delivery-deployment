import { createSlice } from "@reduxjs/toolkit";

let user = JSON.parse(localStorage.getItem('user'));
const initialState = user ? { loggedIn: true, user } : { error: "" };

const loginSlice = createSlice({
    name: "login",
    initialState,
    reducers: {
        authentication(state = initialState, action) {
            switch (action.type) {
                case "LOGIN_PENDING":
                    return {
                        loggingIn: true,
                        user: action.user
                    };
                case "LOGIN_SUCCESS":
                    return {
                        loggedIn: true,
                        user: action.user
                    };
                case "LOGIN_FAILURE":
                    return {
                        error: action.error
                    };
                case "LOGOUT":
                    return {};
                default:
                    return state
            }
        }
    }
})

const { reducer, actions } = loginSlice;

export const { loginPending, loginSuccess, loginFail } = actions;

export default reducer;