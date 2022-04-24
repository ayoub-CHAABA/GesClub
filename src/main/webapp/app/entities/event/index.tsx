import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Event from './event';
import EventDetail from './event-detail';
import EventUpdate from './event-update';
import EventDeleteDialog from './event-delete-dialog';
import PrivateRoute from "app/shared/auth/private-route";
import ClubUpdate from "app/entities/club/club-update";
import {AUTHORITIES} from "app/config/constants";
import ClubDeleteDialog from "app/entities/club/club-delete-dialog";

const Routes = ({ match }) => (
  <>
    <Switch>
      <PrivateRoute exact path={`${match.url}/new`} component={EventUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.PRESIDENT, AUTHORITIES.VP]} />
      <PrivateRoute exact path={`${match.url}/:id/edit`} component={EventUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.PRESIDENT, AUTHORITIES.VP]} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={EventDetail} />
      <ErrorBoundaryRoute path={match.url} component={Event} />
    </Switch>
    <PrivateRoute exact path={`${match.url}/:id/delete`} component={EventDeleteDialog} hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.PRESIDENT, AUTHORITIES.VP]} />
  </>
);

export default Routes;
