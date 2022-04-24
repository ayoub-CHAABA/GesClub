import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './meeting.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const MeetingDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const meetingEntity = useAppSelector(state => state.meeting.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="meetingDetailsHeading">Meeting</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{meetingEntity.id}</dd>
          <dt>
            <span id="title">Title</span>
          </dt>
          <dd>{meetingEntity.title}</dd>
          <dt>
            <span id="meetingDate">Meeting Date</span>
          </dt>
          <dd>
            {meetingEntity.meetingDate ? <TextFormat value={meetingEntity.meetingDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="meetingPlace">Meeting Place</span>
          </dt>
          <dd>{meetingEntity.meetingPlace}</dd>
          <dt>
            <span id="content">Content</span>
          </dt>
          <dd>{meetingEntity.content}</dd>
          <dt>
            <span id="statut">Statut</span>
          </dt>
          <dd>{meetingEntity.statut}</dd>
          <dt>Club</dt>
          <dd>{meetingEntity.club ? meetingEntity.club.clubName : ''}</dd>
        </dl>
        <Button tag={Link} to="/meeting" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/meeting/${meetingEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default MeetingDetail;
