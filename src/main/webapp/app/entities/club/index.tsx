import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Club from './club';
import ClubDetail from './club-detail';
import ClubUpdate from './club-update';
import ClubDeleteDialog from './club-delete-dialog';
import PrivateRoute from "app/shared/auth/private-route";
import StudentUpdate from "app/entities/student/student-update";
import {AUTHORITIES} from "app/config/constants";
import StudentDeleteDialog from "app/entities/student/student-delete-dialog";

const Routes = ({ match }) => (
  <>
    <Switch>
      <PrivateRoute exact path={`${match.url}/new`} component={ClubUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <PrivateRoute exact path={`${match.url}/:id/edit`} component={ClubUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ClubDetail} />
      <ErrorBoundaryRoute path={match.url} component={Club} />
    </Switch>
    <PrivateRoute exact path={`${match.url}/:id/delete`} component={ClubDeleteDialog} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
  </>
);

export default Routes;
