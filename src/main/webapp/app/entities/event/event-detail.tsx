import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './event.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const EventDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const eventEntity = useAppSelector(state => state.event.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="eventDetailsHeading">Event</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{eventEntity.id}</dd>
          <dt>
            <span id="title">Title</span>
          </dt>
          <dd>{eventEntity.title}</dd>
          <dt>
            <span id="eventDate">Event Date</span>
          </dt>
          <dd>{eventEntity.eventDate ? <TextFormat value={eventEntity.eventDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="eventEnd">Event End</span>
          </dt>
          <dd>{eventEntity.eventEnd ? <TextFormat value={eventEntity.eventEnd} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="eventPlace">Event Place</span>
          </dt>
          <dd>{eventEntity.eventPlace}</dd>
          <dt>
            <span id="content">Content</span>
          </dt>
          <dd>{eventEntity.content}</dd>
          <dt>
            <span id="statut">Statut</span>
          </dt>
          <dd>{eventEntity.statut}</dd>
          <dt>
            <span id="budget">Budget</span>
          </dt>
          <dd>{eventEntity.budget}</dd>
          <dt>Club</dt>
          <dd>{eventEntity.club ? eventEntity.club.clubName : ''}</dd>
        </dl>
        <Button tag={Link} to="/event" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/event/${eventEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default EventDetail;
