import Dashboard from "./view/Dashboard";
import Login from "./view/Login";
import { Router, Route, Switch } from "react-router-dom";

import { history } from "./history";
import { SecureRoute } from "./routes/SecureRoute";
import TrackDelivery from "./view/TrackDelivery";

function App() {
  return (
    <Router history={history}>
      <Switch>
        <Route exact path="/" component={Login} />
        <SecureRoute
          exact
          path="/dashboard"
          allowedRoles={["CUSTOMER", "DELIVERER", "DISPATCHER"]}
          component={Dashboard}
        />
        <SecureRoute
          exact
          path="/track/:deliveryId"
          allowedRoles={["CUSTOMER"]}
          component={TrackDelivery}
        />
      </Switch>
    </Router>
  );
}

export default App;
