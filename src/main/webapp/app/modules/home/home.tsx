import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Alert } from 'reactstrap';

import { useAppSelector } from 'app/config/store';
import CardInfo from "app/shared/layout/CardInfo";

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <>
      <section id="hero" className="d-flex align-items-center justify-content-center">
        <div className="container position-relative">

          <h2>CLUBS</h2>
          <h1>Découvrez les clubs diponibles</h1>
        </div>
      </section>

      <main id="main">
        <section id="services" className="services">
          <div className="container">

            <div className="section-title">
              <h2>Nos Clubs</h2>
            </div>
            <div className="section-title">
              <p>Les Clubs de l’UIR ont pour vocation de mener des actions étudiantes sur différents fronts : activités sportives, engagement pour l’environnement, actions humanitaires, manifestations artistiques et culturelles. Si vous avez une idée de Club qui ne figure pas sur la liste ci-dessous, vous pouvez effectuer une demande de création de Club auprès du Service des clubs et des organisations étudiantes (rachid.hadre@uir.ac.ma).
                <br/>
                L’Université Internationale de Rabat a la fierté de compter 48 Clubs étudiants qui veillent à participer activement à l’animation de leur campus et contribuent au rayonnement et au développement de leur communauté.
                <br/> <a href="../CLUB/CLUB.html">Voir tous les Clubs</a></p>
            </div>

            <div className="row">
              <div className="row mb-3">
                <CardInfo
                  title="Club"
                  num="9"
                  ratio={"0%"}
                />
                <CardInfo
                  title="Utilisateurs"
                  num="110"
                  ratio={"15%"}
                />
                <CardInfo
                  title="Evenements"
                  num="6"
                  ratio={"2%"}
                />

                <CardInfo
                  title="Publication"
                  num="3"
                  ratio={"0%"}
                />
              </div>
              <div className="col-lg-4 col-md-6 d-flex align-items-stretch" data-aos="zoom-in" data-aos-delay="100">
                <div className="icon-box iconbox-blue">
                  <a href=""><img src="content/assets/img/7.jpg" /></a>
                </div>
              </div>

              <div className="col-lg-4 col-md-6 d-flex align-items-stretch mt-4 mt-md-0" data-aos="zoom-in"
                   data-aos-delay="200">
                <div className="icon-box iconbox-orange ">
                  <a href=""><img src="content/assets/img/2.png" /></a>
                </div>
              </div>

              <div className="col-lg-4 col-md-6 d-flex align-items-stretch mt-4 mt-lg-0" data-aos="zoom-in"
                   data-aos-delay="300">
                <div className="icon-box iconbox-pink">
                  <a href=""><img src="content/assets/img/3.png" /></a>
                </div>
              </div>

              <div className="col-lg-4 col-md-6 d-flex align-items-stretch mt-4" data-aos="zoom-in"
                   data-aos-delay="100">
                <div className="icon-box iconbox-yellow">
                  <a href=""><img id="4" src="content/assets/img/4.png" /></a>
                </div>
              </div>

              <div className="col-lg-4 col-md-6 d-flex align-items-stretch mt-4" data-aos="zoom-in"
                   data-aos-delay="200">
                <div className="icon-box iconbox-red">
                  <a href=""><img id="5" src="content/assets/img/5.png" /></a>
                </div>
              </div>

              <div className="col-lg-4 col-md-6 d-flex align-items-stretch mt-4" data-aos="zoom-in"
                   data-aos-delay="300">
                <div className="icon-box iconbox-teal">
                  <a href=""><img src="content/assets/img/6.png" /></a>
                </div>
              </div>

              <div className="col-lg-4 col-md-6 d-flex align-items-stretch mt-4" data-aos="zoom-in"
                   data-aos-delay="300">
                <div className="icon-box iconbox-6">
                  <a href=""><img src="content/assets/img/7.jpg" /></a>
                </div>
              </div>

              <div className="col-lg-4 col-md-6 d-flex align-items-stretch mt-4" data-aos="zoom-in"
                   data-aos-delay="300">
                <div className="icon-box iconbox-1">
                  <a href=""><img src="content/assets/img/8.jpg" /></a>
                </div>
              </div>

              <div className="col-lg-4 col-md-6 d-flex align-items-stretch mt-4" data-aos="zoom-in"
                   data-aos-delay="300">
                <div className="icon-box iconbox-2">
                  <a href=""><img src="content/assets/img/9.png" /></a>
                </div>
              </div>

              <div className="col-lg-4 col-md-6 d-flex align-items-stretch mt-4" data-aos="zoom-in"
                   data-aos-delay="300">
                <div className="icon-box iconbox-3">
                  <a href=""><img src="content/assets/img/10.png" /></a>
                </div>
              </div>

              <div className="col-lg-4 col-md-6 d-flex align-items-stretch mt-4" data-aos="zoom-in"
                   data-aos-delay="300">
                <div className="icon-box iconbox-4">
                  <a href=""><img src="content/assets/img/11.png" /></a>
                </div>
              </div>

              <div className="col-lg-4 col-md-6 d-flex align-items-stretch mt-4" data-aos="zoom-in"
                   data-aos-delay="300">
                <div className="icon-box iconbox-5">
                  <a href=""><img src="content/assets/img/12.png" /></a>
                </div>
              </div>

            </div>

          </div>
        </section>


      </main>



    </>
  );
};

export default Home;
