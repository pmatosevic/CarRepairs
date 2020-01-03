import React, {Component} from 'react'
import './App.css';
import {BrowserRouter} from "react-router-dom";
import {Switch} from "react-bootstrap";
import Route from "react-router-dom/es/Route";
import 'bootstrap/dist/css/bootstrap.css';
import Landing from "./Landing";

class App extends Component {

  constructor(props) {
    super(props);
    this.roles = {
      UNAUTHORIZED: 'unauthorized',
      CAR_OWNER: 'car_owner',
      SERVICE_EMPLOYEE: 'service_employee',
      ADMINISTRATOR: 'administrator'
    };
    this.state = {
      loggedIn: false,
      role: this.roles.UNAUTHORIZED,
      id: -1,
      name: ''
    };
  }

  componentDidMount() {
    fetch('/current-user', { credentials: 'include' })
        .then(resp => {
          if (resp.status === 200) {
            resp.json().then(user => {
              this.setState({loggedIn: true, role: user.role, id: user.id, name: user.name})
            });
          }
        }).catch(console.error)
  }

  render() {
    return (
        <BrowserRouter>
          <div>
            {this.state.loggedIn ? (
                <Switch>
                  <Route path='/autoservice'>

                  </Route>
                  <Route path='/user'>
                    <h1>User</h1>
                  </Route>
                  <Route path='/admin'>

                  </Route>
                </Switch>
            ) : (
              <Landing />
            )}
          </div>
        </BrowserRouter>
    );
  }
}

export default App;
