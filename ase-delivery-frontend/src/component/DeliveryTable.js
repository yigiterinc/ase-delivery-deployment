import React, { useEffect, useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import {
  deleteDelivery,
  getActiveDeliveriesByCustomerId,
  getDeliveries,
  getPastDeliveriesByCustomerId,
  updateDelivery,
} from "../store/slices/deliverySlice";
import MaterialTable from "material-table";
import BaseModal from "./BaseModal";

import CreateDeliveryForm from "./CreateDeliveryForm";

import { Button, TextField } from "@material-ui/core";

const DeliveryTable = (props) => {
  const dispatch = useDispatch();
  const allDeliveries = useSelector((state) => state.delivery.allDeliveries);

  const pastDeliveries = useSelector(
    (state) => state.delivery.userPastDeliveries
  );
  const activeDeliveries = useSelector(
    (state) => state.delivery.userActiveDeliveries
  );

  const [showCreateModal, setShowCreateModal] = useState(false);

  const [updatePerformed, setUpdatePerformed] = useState(false);

  const [deliveryData, setDeliveryData] = useState();

  const [tableShown, setTableShown] = useState(false);

  useEffect(async () => {
    if (!deliveryData || updatePerformed) {
      const user = JSON.parse(localStorage.getItem("user"));
      console.log(user);
      const id = user.id;

      let deliveryDataToSet = [];
      if (props.activeDeliveries) {
        await dispatch(getActiveDeliveriesByCustomerId({ id }));
        deliveryDataToSet = activeDeliveries;
      } else if (props.pastDeliveries) {
        await dispatch(getPastDeliveriesByCustomerId({ id }));
        deliveryDataToSet = pastDeliveries;
      } else {
        await dispatch(getDeliveries());
        deliveryDataToSet = allDeliveries;
      }

      if (deliveryDataToSet) {
        setDeliveryData(JSON.parse(JSON.stringify(deliveryDataToSet)));
        console.log(deliveryData);
      }

      setTableShown(true);
      setUpdatePerformed(false);
    }
  }, [
    allDeliveries,
    activeDeliveries,
    pastDeliveries,
    props.activeDeliveries,
    props.pastDeliveries,
    deliveryData,
  ]);

  const user = JSON.parse(localStorage.getItem("user"));

  const columns = [
    { field: "id", title: "ID/Tracking Code", editable: "never" },
    { field: "targetPickupBox.id", title: "BoxID" },
    { field: "customerId", title: "CustomerID" },
    {
      field: "targetPickupBox.stationName",
      title: "Box Name",
      editable: "never",
    },
    {
      field: "targetPickupBox.stationAddress",
      title: "Box Address",
      editable: "never",
    },
    { field: "delivererId", title: "DelivererID" },
    { field: "deliveryStatus", title: "DeliveryStatus" },
  ];

  const CreateDeliveryModal = (props) => {
    return (
      <BaseModal {...props}>
        <CreateDeliveryForm
          setShowCreateModal={setShowCreateModal}
          setUpdatePerformed={setUpdatePerformed}
        />
      </BaseModal>
    );
  };

  return (
    <>
      <div
        style={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          marginTop: "4vh",
        }}
      >
        <MaterialTable
          actions={[
            {
              icon: "add",
              tooltip: "Create a Delivery",
              position: "toolbar",
              onClick: () => {
                setShowCreateModal(true);
              },
            },
          ]}
          editable={
            user.role !== "DISPATCHER"
              ? {}
              : {
                  onRowUpdate: (newData, oldData) =>
                    new Promise((resolve, reject) => {
                      setTimeout(async () => {
                        const dataUpdate = [...deliveryData];
                        const index = oldData.tableData.id;
                        const idToUpdate = oldData.id;
                        console.log(newData);
                        const data = {
                          targetPickupBox: newData.targetPickupBox,
                          delivererId: newData.delivererId,
                          customerId: newData.customerId,
                          deliveryStatus: newData.deliveryStatus,
                          id: idToUpdate,
                        };

                        dispatch(updateDelivery({ data }));
                        dataUpdate[index] = newData;
                        setDeliveryData([...dataUpdate]);
                        resolve();
                      }, 1000);
                    }),
                  onRowDelete: (oldData) =>
                    new Promise((resolve, reject) => {
                      setTimeout(async () => {
                        const dataDelete = [...deliveryData];
                        const index = oldData.tableData.id;
                        const idToDelete = oldData.id;
                        console.log(idToDelete);
                        await dispatch(deleteDelivery({ id: idToDelete }));
                        dataDelete.splice(index, 1);
                        setDeliveryData([...dataDelete]);
                        setUpdatePerformed(true);
                        resolve();
                      }, 1000);
                    }),
                }
          }
          style={{ width: "90vw" }}
          columns={columns}
          data={deliveryData}
          title={
            props.activeDeliveries
              ? "Active Deliveries"
              : props.pastDeliveries
              ? "Past Deliveries"
              : "Deliveries"
          }
        ></MaterialTable>
      </div>

      <CreateDeliveryModal
        title={"Create Delivery"}
        openModal={showCreateModal}
        setOpenModal={setShowCreateModal}
      />
    </>
  );
};

export default DeliveryTable;
