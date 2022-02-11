import React, { useState, useEffect } from "react";
import {
  Container,
  Row,
  Col,
  Form,
  Button,
  Spinner,
  Alert,
} from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import { userActions } from "../store/actions/userAction";

import { history } from "../history";
import http from "../services/http-common";
import axios from "axios";

//import {getLoggedInUser, userActions} from "../../store/actions/userAction";

const Login = () => {
  const dispatch = useDispatch();

  const [loggedInUser, setLoggedInUser] = useState(null);
  const [email, setEmail] = useState("yigit.erinc@tum.de");
  const [password, setPassword] = useState("test123");

  useEffect(() => {
    if (loggedInUser) {
      history.push("/dashboard");
      //axios.defaults.headers.common["Authorization"] = loggedInUser.token;
      //console.log(axios.defaults);
    }
  }, [loggedInUser]);

  const handleOnSubmit = async (e) => {
    e.preventDefault();

    if (!email || !password) {
      return alert("Email and Password required!");
    }

    await dispatch(userActions.login(email, password));
    setLoggedInUser(JSON.parse(localStorage.getItem("user")));
  };

  return (
    <Container>
      <Row>
        <Col>
          <h3 className="text-info text-center">Login</h3>
          <Form autoComplete="off" onSubmit={handleOnSubmit}>
            <Form.Group>
              <Form.Label>Email Address</Form.Label>
              <Form.Control
                type="email"
                name="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="Email Address"
                required
              />
            </Form.Group>
            <Form.Group>
              <Form.Label>Password</Form.Label>
              <Form.Control
                type="password"
                name="password"
                onChange={(e) => setPassword(e.target.value)}
                value={password}
                placeholder="Password"
                required
              />
            </Form.Group>
            {/* {error && <Alert variant="danger">{error}</Alert>} */}
            <div className="text-center">
              <Button type="submit">Login</Button>
            </div>
            {/* {isLoading && <Spinner variant="primary" animation="border" />} */}
          </Form>
        </Col>
      </Row>
    </Container>
  );
};

export default Login;
