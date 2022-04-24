import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Student from './student';
import StudentDetail from './student-detail';
import StudentUpdate from './student-update';
import StudentDeleteDialog from './student-delete-dialog';
import PrivateRoute from "app/shared/auth/private-route";
import Invoice from "app/entities/invoice";
import {AUTHORITIES} from "app/config/constants";

const Routes = ({ match }) => (
  <>
    <Switch>
      <PrivateRoute exact path={`${match.url}/new`} component={StudentUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <PrivateRoute exact path={`${match.url}/:id/edit`} component={StudentUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={StudentDetail} />
      <ErrorBoundaryRoute path={match.url} component={Student} />
    </Switch>
    <PrivateRoute exact path={`${match.url}/:id/delete`} component={StudentDeleteDialog} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />

  </>
);

export default Routes;
