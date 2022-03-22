import React, { useState, useEffect } from "react";
import {
  TextField,
  Button,
  MenuItem,
  Select,
  FormControl,
  InputLabel,
  Typography,
} from "@material-ui/core";
import { useDispatch, useSelector } from "react-redux";
import { createDelivery, getDeliveries } from "../store/slices/deliverySlice";

function CreateDeliveryForm(props) {
  const [createDeliveryCustomerId, setCreateDeliveryCustomerId] = useState("");
  const [createDeliveryBoxId, setCreateDeliveryBoxId] = useState("");
  const [createDeliveryDelivererId, setCreateDeliveryDelivererId] =
    useState("");

  const [customerEmail, setCustomerEmail] = useState();
  const [boxName, setBoxName] = useState();

  const [delivererEmail, setDelivererEmail] = useState();

  const users = useSelector((state) => state.user);
  const boxes = useSelector((state) => state.box.boxes);

  const dispatch = useDispatch();

  const onCreateDelivery = async () => {
    if (
      !(
        createDeliveryBoxId &&
        createDeliveryCustomerId &&
        createDeliveryDelivererId
      )
    )
      return;

    const data = {
      delivererId: createDeliveryDelivererId,
      customerId: createDeliveryCustomerId,
      boxId: createDeliveryBoxId,
    };

    console.log(data);

    await dispatch(createDelivery(data));
    dispatch(getDeliveries());
    props.setShowCreateModal(false);
    props.setUpdatePerformed(true);
  };

  const getUserEmailSelect = () => {
    return (
      <FormControl required variant="outlined">
        <Typography style={{ fontSize: "18px" }}>Customer</Typography>
        <Select
          onChange={(e) => {
            setCreateDeliveryCustomerId(e.target.value);
            setCustomerEmail(
              users.find((user) => user.id === e.target.value).email
            );
          }}
          style={{
            marginTop: "10px",
            marginBottom: "20px",
            minWidth: "20ch",
          }}
          value={customerEmail}
        >
          <MenuItem value={""}>
            <em>None</em>
          </MenuItem>
          {users
            .filter((user) => user.role === "CUSTOMER")
            .map((user) => {
              return <MenuItem value={user.id}>{user.email}</MenuItem>;
            })}
        </Select>
      </FormControl>
    );
  };

  const getDelivererEmailSelect = () => {
    return (
      <FormControl required variant="outlined">
        <Typography style={{ fontSize: "18px" }}>Deliverer</Typography>
        <Select
          onChange={(e) => {
            setCreateDeliveryDelivererId(e.target.value);
            setDelivererEmail(
              users.find((user) => user.id === e.target.value).email
            );
          }}
          style={{
            marginTop: "10px",
            marginBottom: "20px",
            minWidth: "20ch",
          }}
          value={delivererEmail}
        >
          <MenuItem value={""}>
            <em>None</em>
          </MenuItem>
          {users
            .filter((user) => user.role === "DELIVERER")
            .map((user) => {
              return <MenuItem value={user.id}>{user.email}</MenuItem>;
            })}
        </Select>
      </FormControl>
    );
  };
  const getBoxNameSelect = () => {
    return (
      <FormControl required variant="outlined">
        <Typography style={{ fontSize: "18px" }}>Box Name</Typography>
        <Select
          onChange={(e) => {
            setCreateDeliveryBoxId(e.target.value);
            setBoxName(
              boxes.find((box) => box.id === e.target.value).stationName
            );
          }}
          style={{
            marginTop: "10px",
            marginBottom: "20px",
            minWidth: "20ch",
          }}
          value={boxName}
        >
          <MenuItem value={""}>
            <em>None</em>
          </MenuItem>
          {boxes.map((box) => {
            return <MenuItem value={box.id}>{box.stationName}</MenuItem>;
          })}
        </Select>
      </FormControl>
    );
  };

  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "center",
        padding: "30px 40px",
        position: "relative",
      }}
    >
      {getUserEmailSelect()}
      {getDelivererEmailSelect()}
      {getBoxNameSelect()}

      <Button
        variant="contained"
        color="primary"
        onClick={() => onCreateDelivery()}
        style={{
          marginTop: "15px",
          minWidth: "20ch",
        }}
      >
        Create
      </Button>
    </div>
  );
}

export default CreateDeliveryForm;
