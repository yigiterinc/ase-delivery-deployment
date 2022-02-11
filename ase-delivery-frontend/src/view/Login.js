import React, { useState, useEffect } from "react";
import {
  Container,
  Row,
  Col,
  Form,
  Button,
  Spinner,
  Alert,
  Jumbotron,
} from "react-bootstrap";
import { useDispatch, useSelector } from "react-redux";
import { userActions } from "../store/actions/userAction";

import "./Login.css";

import { history } from "../history";
import http from "../services/http-common";
import axios from "axios";
import { Typography } from "@material-ui/core";

//import {getLoggedInUser, userActions} from "../../store/actions/userAction";

const Login = () => {
  const dispatch = useDispatch();

  const [loggedInUser, setLoggedInUser] = useState(null);
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  useEffect(() => {
    if (loggedInUser) {
      history.push("/dashboard");
      axios.defaults.headers.common["Authorization"] = loggedInUser.token;
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
    <div
      style={{
        height: "100vh",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        backgroundImage: "url(images/logistics.jpg)",
      }}
      className="hero"
    >
      <Container>
        <Row>
          <Col className="md-8">
            <div style={{ paddingTop: "25px", maxWidth: "30vw" }}>
              <Typography
                style={{
                  fontSize: "55px",
                  letterSpacing: "1.5px",
                  fontWeight: "lighter",
                  color: "rgb(207, 222, 230)",
                }}
              >
                ASE DELIVERY
              </Typography>
              <Typography
                style={{
                  fontSize: "20px",
                  marginTop: "10px",
                  color: "rgb(207, 222, 230)",
                }}
              >
                The fastest and most reliable shipping company in Germany
              </Typography>
            </div>
          </Col>
          <Col className="md-4">
            <Jumbotron
              style={{
                boxShadow: "5px 5px 26px -1px rgba(0,0,0,0.5)",
                paddingTop: "25px",
                paddingBottom: "35px",
              }}
            >
              <Container>
                <Row>
                  <Col>
                    <h2 className="text-info text-center">Login</h2>
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
                        <Button
                          type="submit"
                          style={{
                            marginTop: "20px",
                            background: "#17a2b8",
                            borderColor: "#17a2b8",
                            padding: "12px 24px",
                          }}
                        >
                          Login
                        </Button>
                      </div>
                      {/* {isLoading && <Spinner variant="primary" animation="border" />} */}
                    </Form>
                  </Col>
                </Row>
              </Container>
            </Jumbotron>
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default Login;
