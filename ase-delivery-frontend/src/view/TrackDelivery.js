import React from "react";
import { makeStyles } from "@material-ui/core/styles";
import Timeline from "@material-ui/lab/Timeline";
import TimelineItem from "@material-ui/lab/TimelineItem";
import TimelineSeparator from "@material-ui/lab/TimelineSeparator";
import TimelineConnector from "@material-ui/lab/TimelineConnector";
import TimelineContent from "@material-ui/lab/TimelineContent";
import TimelineOppositeContent from "@material-ui/lab/TimelineOppositeContent";
import TimelineDot from "@material-ui/lab/TimelineDot";
import AddBoxIcon from "@material-ui/icons/AddBox";
import HowToRegIcon from "@material-ui/icons/HowToReg";
import MoveToInboxIcon from "@material-ui/icons/MoveToInbox";
import CheckBoxIcon from "@material-ui/icons/CheckBox";
import Paper from "@material-ui/core/Paper";
import Typography from "@material-ui/core/Typography";
import deliveryService from "../services/deliveryService";
import { CircularProgress } from "@material-ui/core";

import { Header } from "../component/Header";

import { useParams } from "react-router-dom";
import { useState, useEffect } from "react";
import Footer from "../component/Footer";

const useStyles = makeStyles((theme) => ({
  timeline: {
    marginTop: "5vh",
    width: "80vw",
  },
  paper: {
    marginBottom: "40px",
    padding: "20px 25px",
  },
}));

const TrackDelivery = () => {
  const classes = useStyles();
  const { deliveryId } = useParams();
  const [delivery, setDelivery] = useState();

  useEffect(async () => {
    if (!delivery) {
      const deliveryReq = await deliveryService.getDeliveryById({
        id: deliveryId,
      });

      setDelivery(deliveryReq.data);
    }
  }, [deliveryId]);

  return (
    <>
      <Header />
      {!delivery ? (
        <CircularProgress
          style={{
            position: "absolute",
            left: "50%",
            top: "50%",
          }}
        />
      ) : (
        <div
          style={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            paddingTop: "5vh",
          }}
        >
          <Typography style={{ fontSize: "20px", letterSpacing: "1.2" }}>
            Your delivery is now <b>{delivery.deliveryStatus}</b> 📦
          </Typography>

          <Typography
            style={{ fontSize: "20px", letterSpacing: "1.2", marginTop: "2vh" }}
          >
            We will inform you whenever the status changes 😊
          </Typography>

          <Timeline align="alternate" className={classes.timeline}>
            <TimelineItem>
              <TimelineSeparator>
                <TimelineDot style={{ backgroundColor: "#ff8746" }}>
                  <AddBoxIcon />
                </TimelineDot>
                <TimelineConnector />
              </TimelineSeparator>
              <TimelineContent>
                <Paper elevation={3} className={classes.paper}>
                  <Typography variant="h6" component="h1">
                    Created
                  </Typography>
                  <Typography>Your order is created!</Typography>
                </Paper>
              </TimelineContent>
            </TimelineItem>

            <TimelineItem>
              <TimelineSeparator>
                <TimelineDot
                  style={{
                    backgroundColor: "#ffdc35",
                    opacity: ["CREATED"].includes(delivery.deliveryStatus)
                      ? "30%"
                      : "100%",
                  }}
                >
                  <HowToRegIcon />
                </TimelineDot>
                <TimelineConnector />
              </TimelineSeparator>
              <TimelineContent
                style={{
                  opacity: ["CREATED"].includes(delivery.deliveryStatus)
                    ? "30%"
                    : "100%",
                }}
              >
                <Paper elevation={3} className={classes.paper}>
                  <Typography variant="h6" component="h1">
                    Collected
                  </Typography>
                  <Typography>
                    Your order is collected by the deliverer!
                  </Typography>
                </Paper>
              </TimelineContent>
            </TimelineItem>

            <TimelineItem>
              <TimelineSeparator>
                <TimelineDot
                  style={{
                    backgroundColor: "#11bbf2",
                    opacity: ["CREATED", "COLLECTED"].includes(
                      delivery.deliveryStatus
                    )
                      ? "30%"
                      : "100%",
                  }}
                >
                  <MoveToInboxIcon />
                </TimelineDot>
                <TimelineConnector />
              </TimelineSeparator>
              <TimelineContent
                style={{
                  opacity: ["CREATED", "COLLECTED"].includes(
                    delivery.deliveryStatus
                  )
                    ? "30%"
                    : "100%",
                }}
              >
                <Paper elevation={3} className={classes.paper}>
                  <Typography variant="h6" component="h1">
                    Deposited
                  </Typography>
                  <Typography>Your order is ready to pick-up!</Typography>
                </Paper>
              </TimelineContent>
            </TimelineItem>

            <TimelineItem>
              <TimelineSeparator>
                <TimelineDot
                  style={{
                    backgroundColor: "#60e000",
                    opacity: ["CREATED", "COLLECTED", "DEPOSITED"].includes(
                      delivery.deliveryStatus
                    )
                      ? "30%"
                      : "100%",
                  }}
                >
                  <CheckBoxIcon />
                </TimelineDot>
              </TimelineSeparator>

              <TimelineContent
                style={{
                  opacity: ["CREATED", "COLLECTED", "DEPOSITED"].includes(
                    delivery.deliveryStatus
                  )
                    ? "30%"
                    : "100%",
                }}
              >
                <Paper elevation={3} className={classes.paper}>
                  <Typography variant="h6" component="h1">
                    Delivered
                  </Typography>
                  <Typography>Your order is delivered!</Typography>
                </Paper>
              </TimelineContent>
            </TimelineItem>
          </Timeline>
        </div>
      )}
      <Footer />
    </>
  );
};

export default TrackDelivery;
