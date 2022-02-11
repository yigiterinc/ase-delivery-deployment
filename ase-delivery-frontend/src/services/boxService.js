import http from "../services/http-common";

const DELIVERY_SERVICE_BASE_URL = "/ds";
const BOX_BASE_URL = DELIVERY_SERVICE_BASE_URL + "/boxes";

const getAllBoxes = () => {
  return http.get(BOX_BASE_URL + "/all");
};

const getBoxById = ({ id }) => {
  return http.get(BOX_BASE_URL + `/${id}`);
};

const getBoxByDelivererId = (id) => {
  return http.get(BOX_BASE_URL + `/deliverer/${id}`);
};

const createBox = (data) => {
  return http.post(BOX_BASE_URL, data);
};

const updateBox = (id, data) => {
  return http.put(BOX_BASE_URL + `/${id}`, data);
};

const deleteBox = (id) => {
  return http.delete(BOX_BASE_URL + `/${id}`);
};

const boxService = {
  getAllBoxes,
  getBoxById,
  getBoxByDelivererId,
  createBox,
  updateBox,
  deleteBox,
};

export default boxService;
