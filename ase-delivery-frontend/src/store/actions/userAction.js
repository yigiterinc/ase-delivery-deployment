import { loginUser, logoutUser } from "../../services/userService";

import { history } from "../../history";

export const userActions = {
  login,
  logout,
};

function login(email, password) {
  return (dispatch) => {
    dispatch(request({ email }));

    loginUser(email, password).then(
      (user) => {
        dispatch(success(user));
        history.push("/dashboard");
      },
      (error) => {
        dispatch(failure(error));
      }
    );
  };

  function request(user) {
    return { type: "LOGIN_PENDING", user };
  }
  function success(user) {
    return { type: "LOGIN_SUCCESS", user };
  }
  function failure(error) {
    return { type: "LOGIN_FAILURE", error };
  }
}

function logout() {
  logoutUser();
  history.push("/");
  return { type: "LOGOUT" };
}
