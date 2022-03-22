import http from "../services/http-common";

const DELIVERY_SERVICE_BASE_URL = "/ds";
const DELIVERIES_BASE_URL = DELIVERY_SERVICE_BASE_URL + "/deliveries";

const getAllDeliveries = () => {
  return http.get(DELIVERIES_BASE_URL);
};

const getDeliveryById = ({ id }) => {
  return http.get(DELIVERIES_BASE_URL + `/${id}`);
};

const getActiveDeliveriesOfCustomer = (id) => {
  console.log(id);
  return http.get(DELIVERIES_BASE_URL + `/customer/${id}/status/active`);
};

const updateDelivery = (data) => {
  return http.put(DELIVERIES_BASE_URL, data);
};

const getPastDeliveriesOfCustomer = (id) => {
  return http.get(DELIVERIES_BASE_URL + `/customer/${id}/status/delivered`);
};

const createDelivery = (data) => {
  return http.post(DELIVERIES_BASE_URL, data);
};

const onDeliveriesCollected = ({ boxId, delivererId }) => {
  console.log(boxId, delivererId);
  return http.put(
    DELIVERIES_BASE_URL + `/box/${boxId}/collected/deliverer/${delivererId}`
  );
};

const onDeliveryDeposited = (deliveryId, delivererId, boxId) => {
  return http.put(
    DELIVERIES_BASE_URL +
      `/${deliveryId}/deposited/deliverer/${delivererId}/box/${boxId}`
  );
};

const onDeliveryDelivered = (userId, boxId) => {
  return http.put(
    DELIVERIES_BASE_URL + `/user/${userId}/delivered/box/${boxId}`
  );
};

const deleteDelivery = (id) => {
  return http.delete(`${DELIVERIES_BASE_URL}/${id}`);
};

const deliveryService = {
  getAllDeliveries,
  getDeliveryById,
  getActiveDeliveriesOfCustomer,
  getPastDeliveriesOfCustomer,
  createDelivery,
  onDeliveriesCollected,
  onDeliveryDeposited,
  onDeliveryDelivered,
  deleteDelivery,
  updateDelivery,
};

export default deliveryService;
