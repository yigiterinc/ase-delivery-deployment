import React from 'react';
import { Route, Redirect } from 'react-router-dom';

export const SecureRoute = ({ component: Component, allowedRoles, ...rest }) => (
	<Route {...rest} render={props => {
		const currentUser = JSON.parse(localStorage.getItem('user'));
		if (!currentUser) {
			// not logged in so redirect to login page with the return url
			return <Redirect to={{ pathname: '/', state: { from: props.location } }} />
		}

		// check if route is restricted by role
		if (allowedRoles && allowedRoles.indexOf(currentUser.role) === -1) {
			// role not authorised so redirect to login
			return <Redirect to={{ pathname: '/'}} />
		}

		// authorised so return component
		return <Component {...props} />
	}} />
)
