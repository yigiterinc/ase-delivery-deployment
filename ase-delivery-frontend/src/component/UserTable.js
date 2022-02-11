import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { getActiveDeliveriesByCustomerId } from "../store/slices/deliverySlice";
import MaterialTable from "material-table";
import { getAllUsers, deleteUser, updateUser } from "../store/slices/userSlice";
import { updateBox } from "../store/slices/boxSlice";

import { Button } from "@material-ui/core";
import BaseModal from "./BaseModal";
import CreateUserForm from "./CreateUserForm";

const UserTable = (props) => {
  const dispatch = useDispatch();
  const users = JSON.parse(JSON.stringify(useSelector((state) => state.user)));

  const [tableShown, setTableShown] = useState(false);
  const [userData, setUserData] = useState();
  const [updatePerformed, setUpdatePerformed] = useState();

  const [showCreateModal, setShowCreateModal] = useState(false);

  const CreateUserModal = (props) => {
    return (
      <BaseModal {...props}>
        <CreateUserForm
          setShowCreateModal={setShowCreateModal}
          setUpdatePerformed={setUpdatePerformed}
        />
      </BaseModal>
    );
  };

  useEffect(async () => {
    if (updatePerformed || (!userData && !tableShown)) {
      await dispatch(getAllUsers());
      console.log(users);
      setUserData(users);
      setTableShown(true);
      setUpdatePerformed(false);
    }
  }, [users, tableShown, userData, updatePerformed]);

  const user = JSON.parse(localStorage.getItem("user"));

  const columns = [
    { field: "id", title: "ID", editable: "never" },
    { field: "email", title: "Email" },
    { field: "role", title: "Role", editable: "never" },
    { field: "rfid", title: "RFID" },
  ];

  return (
    users && (
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
            data={users}
            title={"Users"}
            actions={[
              {
                icon: "add",
                tooltip: "Add a user",
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
                          const dataUpdate = [...userData];
                          const index = oldData.tableData.id;
                          const idToUpdate = oldData.id;
                          const data = {
                            ...oldData,
                            email: newData.email,
                          };
                          await dispatch(updateUser({ ...data }));

                          dataUpdate[index] = newData;
                          setUserData([...dataUpdate]);
                          console.log(oldData);

                          resolve();
                        }, 1000);
                      }),
                    onRowDelete: (oldData) =>
                      new Promise((resolve, reject) => {
                        setTimeout(async () => {
                          const dataDelete = [...userData];
                          const index = oldData.tableData.id;
                          const idToDelete = oldData.id;
                          await dispatch(deleteUser({ id: idToDelete }));
                          dataDelete.splice(index, 1);
                          setUserData([...dataDelete]);
                          setUpdatePerformed(true);
                          resolve();
                        }, 1000);
                      }),
                  }
            }
          ></MaterialTable>
        </div>
        <CreateUserModal
          title={"Create User"}
          openModal={showCreateModal}
          setOpenModal={setShowCreateModal}
        />
      </>
    )
  );
};

export default UserTable;
