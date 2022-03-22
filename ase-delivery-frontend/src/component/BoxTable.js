import React, { useEffect, useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import MaterialTable, { MTableToolbar } from "material-table";
import {
  deleteBox,
  getBoxByDelivererId,
  getBoxes,
  updateBox,
} from "../store/slices/boxSlice";
import { onDeliveriesCollected } from "../store/slices/deliverySlice";
import CreateBoxForm from "./CreateBoxForm";
import BaseModal from "./BaseModal";
import { Button } from "@material-ui/core";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faBox } from "@fortawesome/free-solid-svg-icons";

const BoxTable = (props) => {
  const dispatch = useDispatch();
  const boxes = useSelector((state) => state.box.boxes);

  const delivererAssignedBoxes = useSelector(
    (state) => state.box.delivererAssignedBoxes
  );

  const [boxData, setBoxData] = useState();
  const [updatePerformed, setUpdatePerformed] = useState(false);
  const [showCreateModal, setShowCreateModal] = useState(false);

  const [showSpinner, setShowSpinner] = useState(true);

  useEffect(async () => {
    if (!boxData || updatePerformed) {
      setShowSpinner(true);
      const userId = JSON.parse(localStorage.getItem("user")).id;
      console.log(userId);

      let boxesDataToSet;
      if (props.assignedBoxes) {
        await dispatch(getBoxByDelivererId({ id: userId }));

        boxesDataToSet = delivererAssignedBoxes.map((dto) => {
          const box = dto.box;
          const statusOfDeliveries = dto.statusOfDeliveries;

          return {
            ...box,
            statusOfDeliveries,
          };
        });

        console.log(boxesDataToSet);
      } else {
        await dispatch(getBoxes());
        console.log(boxes);
        boxesDataToSet = boxes;
      }

      if (boxesDataToSet) {
        setBoxData(JSON.parse(JSON.stringify(boxesDataToSet)));
        console.log(boxesDataToSet);
        setShowSpinner(false);
      }
      setUpdatePerformed(false);
    }
  }, [
    boxes,
    delivererAssignedBoxes,
    updatePerformed,
    props.assignedBoxes,
    boxData,
  ]);

  const user = JSON.parse(localStorage.getItem("user"));

  const columns = [
    { field: "id", title: "ID", editable: "never" },
    {
      field: "stationAddress",
      title: "Station Address",
      editable: user.role === "DISPATCHER" ? "onUpdate" : "never",
    },
    {
      field: "stationName",
      title: "Station Name",
      editable: user.role === "DISPATCHER" ? "onUpdate" : "never",
    },
  ];

  const ChangeStatusButton = (
    <Button type="outlined" color="primary">
      Collected
    </Button>
  );

  const CreateBoxModal = (props) => {
    return (
      <BaseModal {...props}>
        <CreateBoxForm
          setShowCreateModal={setShowCreateModal}
          setUpdatePerformed={setUpdatePerformed}
        />
      </BaseModal>
    );
  };

  return (
    boxes && (
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
            style={{ width: "90vw" }}
            columns={columns}
            data={boxData}
            isLoading={showSpinner}
            title={props.assignedBoxes ? "Assigned Boxes" : "Boxes"}
            localization={{
              header: {
                actions: user.role === "DELIVERER" ? "Collect" : "Actions",
              },
            }}
            actions={
              user.role === "DISPATCHER"
                ? [
                    {
                      icon: "add",
                      tooltip: "Create a box",
                      position: "toolbar",
                      onClick: () => {
                        setShowCreateModal(true);
                      },
                    },
                  ]
                : user.role === "DELIVERER"
                ? [
                    (rowData) => ({
                      icon: () => <FontAwesomeIcon icon={faBox} />,
                      tooltip: "Collect",
                      disabled: rowData.statusOfDeliveries === "COLLECTED",
                      onClick: () => {
                        console.log(rowData);
                        const id = rowData.id;
                        const userId = JSON.parse(
                          localStorage.getItem("user")
                        ).id;
                        console.log(userId);
                        dispatch(
                          onDeliveriesCollected({
                            boxId: id,
                            delivererId: userId,
                          })
                        );
                      },
                    }),
                  ]
                : []
            }
            editable={
              user.role !== "DISPATCHER"
                ? {}
                : {
                    onRowUpdate: (newData, oldData) =>
                      new Promise((resolve, reject) => {
                        setTimeout(async () => {
                          const dataUpdate = [...boxData];
                          const index = oldData.tableData.id;
                          const idToUpdate = oldData.id;

                          await dispatch(
                            updateBox({
                              id: idToUpdate,
                              data: {
                                id: newData.id,
                                stationAddress: newData.stationAddress,
                                stationName: newData.stationName,
                              },
                            })
                          );
                          dataUpdate[index] = newData;
                          setBoxData([...dataUpdate]);

                          resolve();
                        }, 1000);
                      }),
                    onRowDelete: (oldData) =>
                      new Promise((resolve, reject) => {
                        setTimeout(async () => {
                          const dataDelete = [...boxData];
                          const index = oldData.tableData.id;
                          const idToDelete = oldData.id;
                          console.log(idToDelete);
                          await dispatch(deleteBox({ id: idToDelete }));
                          dataDelete.splice(index, 1);
                          setBoxData([...dataDelete]);
                          setUpdatePerformed(true);
                          resolve();
                        }, 1000);
                      }),
                  }
            }
          ></MaterialTable>
        </div>

        <CreateBoxModal
          title={"Create Box"}
          openModal={showCreateModal}
          setOpenModal={setShowCreateModal}
        />
      </>
    )
  );
};

export default BoxTable;
