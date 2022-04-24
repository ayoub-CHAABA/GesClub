import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Meeting from './meeting';
import MeetingDetail from './meeting-detail';
import MeetingUpdate from './meeting-update';
import MeetingDeleteDialog from './meeting-delete-dialog';
import PrivateRoute from "app/shared/auth/private-route";
import EventUpdate from "app/entities/event/event-update";
import {AUTHORITIES} from "app/config/constants";

const Routes = ({ match }) => (
  <>
    <Switch>
      <PrivateRoute exact path={`${match.url}/new`} component={MeetingUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.PRESIDENT, AUTHORITIES.VP]} />
      <PrivateRoute exact path={`${match.url}/:id/edit`} component={MeetingUpdate} hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.PRESIDENT, AUTHORITIES.VP]} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={MeetingDetail} />
      <ErrorBoundaryRoute path={match.url} component={Meeting} />
    </Switch>
    <PrivateRoute exact path={`${match.url}/:id/delete`}component={MeetingDeleteDialog} hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.PRESIDENT, AUTHORITIES.VP]} />
  </>
);

export default Routes;
