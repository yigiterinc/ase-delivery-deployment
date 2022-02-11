import http from "../services/http-common";

const CUSTOMER_AUTHENTICATION_SERVICE_BASE_URL = "/cas";
const USER_BASE_URL = CUSTOMER_AUTHENTICATION_SERVICE_BASE_URL + "/users";
const LOGIN_URL = CUSTOMER_AUTHENTICATION_SERVICE_BASE_URL + "/login";
const FETCH_ROLE_URL = USER_BASE_URL + "/role";

const createUser = (email, password, role) => {
  const userDto = { email, password, role };
  return http.post(USER_BASE_URL, userDto);
};

const getAllUsers = () => {
  return http.get(USER_BASE_URL + "/all");
};

const getUserById = (id) => {
  return http.get(USER_BASE_URL + `/${id}`);
};

const getUserRole = (id) => {
  return http.get(USER_BASE_URL + `/${id}/role`);
};

const deleteUser = (id) => {
  console.log(id);
  return http.delete(USER_BASE_URL + `/${id}`);
};

export const updateUser = async (data) => {
  console.log(data);
  return http.put(USER_BASE_URL, data);
};

const userService = {
  createUser,
  getAllUsers,
  getUserById,
  getUserRole,
  deleteUser,
  updateUser,
};

export const loginUser = async (email, password) => {
  let loginDto = {
    email,
    password,
  };

  console.log(loginDto);

  const tokenReq = await http.post(LOGIN_URL, loginDto);
  const token = tokenReq.data;
  const user = await fetchUser(token);
  user.token = token;

  localStorage.setItem("user", JSON.stringify(user));

  return user;
};

export const fetchRoleByToken = async (token) => {
  const res = await http.get(FETCH_ROLE_URL, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  return res.data;
};

export const fetchUser = (jwt) => {
  return new Promise(async (resolve, reject) => {
    try {
      if (!jwt) {
        reject("Token Not Found!");
      }

      const res = await http.get(`${USER_BASE_URL}/token/${jwt.token}`);

      resolve(res.data);
    } catch (error) {
      console.log(error);
      reject(error.message);
    }
  });
};

export const logoutUser = () => {
  localStorage.removeItem("user");
};

export default userService;
