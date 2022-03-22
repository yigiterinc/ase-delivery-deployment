import React from "react";
import { Navbar, Nav } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faShippingFast } from "@fortawesome/free-solid-svg-icons";
import { userActions } from "../store/actions/userAction";
import { useDispatch } from "react-redux";

import { history } from "../history";

export const Header = () => {
  const dispatch = useDispatch();

  return (
    <Navbar
      collapseOnSelect
      bg="info"
      variant="dark"
      expand="md"
      style={{ height: "7vh" }}
    >
      <Navbar.Brand>
        <FontAwesomeIcon
          icon={faShippingFast}
          style={{ marginLeft: "10px", marginRight: "10px" }}
        />
        <span
          style={{
            fontSize: "20px",
            letterSpacing: "1.1px",
            cursor: "pointer",
          }}
          onClick={() => history.push("/dashboard")}
        >
          ASE Delivery
        </span>
      </Navbar.Brand>
      <Navbar.Toggle aria-controls="basic-navbar-nav" />
      <Navbar.Collapse id="basic-navbar-nav">
        <Nav className="ml-auto">
          <Nav.Link
            onClick={() => {
              dispatch(userActions.logout());
            }}
            style={{
              fontSize: "20px",
            }}
          >
            Logout
          </Nav.Link>
        </Nav>
      </Navbar.Collapse>
    </Navbar>
  );
};
