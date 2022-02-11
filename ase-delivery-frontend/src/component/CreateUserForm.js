import { React, useState } from "react";
import { createUser } from "../store/slices/userSlice";
import {
  TextField,
  Button,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
} from "@material-ui/core";
import { useDispatch } from "react-redux";

function CreateUserForm(props) {
  const [email, setEmail] = useState();
  const [password, setPassword] = useState();
  const [role, setRole] = useState("CUSTOMER");

  const dispatch = useDispatch();

  const onCreateUser = () => {
    if (!(email && role && password)) {
      return;
    }

    dispatch(createUser({ email, password, role }));

    props.setShowCreateModal(false);
    props.setUpdatePerformed(true);
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
      <TextField
        required
        id="outlined-required"
        label="Email"
        style={{
          marginBottom: "20px",
          minWidth: "20ch",
        }}
        value={email}
        onChange={(e) => setEmail(e.target.value)}
      />

      <TextField
        required
        id="outlined-required"
        label="Password"
        inputProps={{
          type: "password",
          autoComplete: "new-password",
        }}
        style={{
          marginBottom: "20px",
          minWidth: "20ch",
        }}
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />
      <FormControl required variant="outlined">
        <Select
          value={role}
          onChange={(e) => setRole(e.target.value)}
          style={{
            marginTop: "10px",
            marginBottom: "20px",
            minWidth: "20ch",
          }}
        >
          <MenuItem value={""}>
            <em>None</em>
          </MenuItem>
          <MenuItem value={"DISPATCHER"}>Dispatcher</MenuItem>
          <MenuItem value={"DELIVERER"}>Deliverer</MenuItem>
          <MenuItem value={"CUSTOMER"}>Customer</MenuItem>
        </Select>
      </FormControl>

      <Button
        variant="contained"
        color="primary"
        onClick={() => onCreateUser()}
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

export default CreateUserForm;
