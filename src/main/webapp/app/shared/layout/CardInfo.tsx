import React from 'react';

function CardInfo(props) {
  return (

    <div className="col-xl-3 col-lg-6">
    <div className="card card-stats mb-4 mb-xl-0">
      <div className="card-body">
        <div className="row">
          <div className="col">
            <h5 className="card-title text-uppercase text-muted mb-0">{props.title}</h5>
            <span className="h2 font-weight-bold mb-0">{props.num}</span>
          </div>
          <div className="col-auto">
            <div className="icon icon-shape bg-danger text-white rounded-circle shadow">
              <i className="fas fa-chart-bar"></i>
            </div>
          </div>
        </div>
        <p className="mt-3 mb-0 text-muted text-sm">
          <span className="text-success mr-2"><i className="fa fa-arrow-up"></i> {props.ratio}</span>
          <span className="text-nowrap">Since last month</span>
        </p>
      </div>
    </div>
    </div>
  );
}

export default CardInfo;
