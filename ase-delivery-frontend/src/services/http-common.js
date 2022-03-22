import axios from "axios";
import { authHeader } from "../helpers";

import axiosRetry from "axios-retry";

const client = axios.create({
  baseURL: "http://ec2-18-197-77-236.eu-central-1.compute.amazonaws.com:10789/api",
  headers: {
    Authorization: authHeader(),
  },
});

axiosRetry(client, {
  retries: 10,
  retryDelay: (retryCount) => {
    console.log(`retry attempt: ${retryCount}`);
    return retryCount * 2000; // time interval between retries
  },
});

export default client;
