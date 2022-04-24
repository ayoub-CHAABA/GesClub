import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './club.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ClubDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const clubEntity = useAppSelector(state => state.club.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="clubDetailsHeading">Club</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>
            {clubEntity.logo ? (
              <div>
                <img className="w-25 rounded-circle" src={"data:"+clubEntity.logoContentType+";base64,"+clubEntity.logo }/>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="clubName">Club Name</span>
          </dt>
          <dd>{clubEntity.clubName}</dd>
          <dt>
            <span id="creationDate">Creation Date</span>
          </dt>
          <dd>{clubEntity.creationDate ? <TextFormat value={clubEntity.creationDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/club" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/club/${clubEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ClubDetail;
