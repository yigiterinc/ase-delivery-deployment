import Dashboard from "./view/Dashboard";
import Login from "./view/Login";
import { Router, Route, Switch } from "react-router-dom";

import { history } from "./history";
import { SecureRoute } from "./routes/SecureRoute";

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
      </Switch>
    </Router>
  );
}

export default App;
