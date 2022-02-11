import { React, useEffect, useState } from "react";
import { Header } from "../component/Header";
import DeliveryTable from "../component/DeliveryTable";
import UserTable from "../component/UserTable";
import BoxTable from "../component/BoxTable";
import { CircularProgress } from "@material-ui/core";

import { history } from "../history";

import http from "../services/http-common";

const Dashboard = (props) => {
  const [user, setUser] = useState();
  const [userRole, setUserRole] = useState();

  useEffect(() => {
    const localStorageUser = localStorage.getItem("user");
    if (!user && localStorageUser) {
      const user = JSON.parse(localStorageUser);
      setUser(user);
      setUserRole(user.role);
      http.defaults.headers["Authorization"] = `Bearer ${user.token}`;
    }
  }, []);

  const getRenderContent = () => {
    if (userRole === "DISPATCHER") {
      return (
        <>
          <DeliveryTable />
          <BoxTable />
          <UserTable />
        </>
      );
    } else if (userRole === "CUSTOMER") {
      return (
        <>
          <DeliveryTable activeDeliveries />
          <DeliveryTable pastDeliveries />
        </>
      );
    } else if (userRole === "DELIVERER") {
      return (
        <>
          <BoxTable assignedBoxes />
        </>
      );
    } else {
      <CircularProgress />;
    }
  };

  return (
    <div>
      <Header />
      {getRenderContent()}
    </div>
  );
};

export default Dashboard;
