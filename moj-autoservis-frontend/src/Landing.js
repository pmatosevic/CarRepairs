import React, {Component} from "react";
import {Switch} from "react-bootstrap";
import Login from "./Login";
import {BrowserRouter, Route} from "react-router-dom";

class Landing extends Component {
    render() {
        return (
            <>
                <nav className="navbar navbar-expand-lg navbar-light fixed-top">
                    <div className="container">
                        <div className="collapse navbar-collapse" id="navbarTogglerDemo02">
                            <ul className="navbar-nav ml-auto">
                                <li className="nav-item">
                                    <a className="nav-link" to={"/login"}>Prijava</a>
                                </li>
                                <li className="nav-item">
                                    <a className="nav-link" to={"/register/user"}>Registracija korisnika</a>
                                </li>
                                <li className="nav-item">
                                    <a className="nav-link" to={"/register/autoservice"}>Registracija servisa</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </nav>
            <BrowserRouter>
            <div className="auth-wrapper">
                <div className="auth-inner">
                    <Switch>
                        <Route exact path='/register/user'>
                            <h1>Register user</h1>
                        </Route>
                        <Route path="/login" exact component={Login} />
                        <Route exact path='/'>
                            <h1>Home</h1>
                        </Route>
                    </Switch>
                </div>
            </div>
            </BrowserRouter>
            </>
        );
    }
}

export default Landing;