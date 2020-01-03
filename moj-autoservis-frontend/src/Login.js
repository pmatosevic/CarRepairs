import React, {Component} from "react";
import {Button, Form} from "react-bootstrap";
import './Login.css'

class Login extends Component {

    constructor(props) {
        super(props);
        this.state = {showError: false};
        this.username = React.createRef();
        this.password = React.createRef();
        this.remember = React.createRef();
        this.handleSubmit = this.handleSubmit.bind(this);
    }


     handleSubmit(e) {
        e.preventDefault();
        const body = `username=${this.username.current.value}&password=${this.password.current.value}`;
        const options = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: body
        };
         fetch('/login', options)
             .then(response => {
                 if (response.status === 200) {
                     this.props.history.push('/')
                 } else {
                     this.setState({showError: true});
                 }
             })
    }

    render() {
        return (
          <Form onSubmit={this.handleSubmit}>
              <h3>Prijava</h3>
              {this.state.showError ? <p className="error">Neispravno korisničko ime ili lozinka</p> : null}
              <Form.Group controlId="username">
                  <Form.Label>Korisničko ime</Form.Label>
                  <Form.Control ref={this.username}  type="text" placeholder="Korisničko ime" />
              </Form.Group>

              <Form.Group controlId="password">
                  <Form.Label>Lozinka</Form.Label>
                  <Form.Control ref={this.password}  type="password" placeholder="Lozinka" />
              </Form.Group>
              {<Form.Group controlId="remember">
                  <Form.Check type="checkbox" label="Zapamti me" />
              </Form.Group>}
              <Button variant="primary" type="submit">
                  Prijava
              </Button>
          </Form>
        );
    }
}

export default Login;