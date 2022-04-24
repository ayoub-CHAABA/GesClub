import './header.scss';
import React, {useState} from 'react';

import {Navbar, Nav, NavbarToggler, Collapse} from 'reactstrap';
import LoadingBar from 'react-redux-loading-bar';

import {Home, Brand} from './header-components';
import {AdminMenu, EntitiesMenu, AccountMenu} from '../menus';

export interface IHeaderProps {
  isAuthenticated: boolean;
  isAdmin: boolean;
  ribbonEnv: string;
  isInProduction: boolean;
  isOpenAPIEnabled: boolean;
}

const Header = (props: IHeaderProps) => {
  const [menuOpen, setMenuOpen] = useState(false);

  const renderDevRibbon = () =>
    props.isInProduction === false ? (
      <div className="ribbon dev">
        <a href="">Development</a>
      </div>
    ) : null;

  const toggleMenu = () => setMenuOpen(!menuOpen);

  /* jhipster-needle-add-element-to-menu - JHipster will add new menu items here */

  return (
    <div id="header" className="fixed-top header-transparent">
      <div className="container d-flex align-items-center justify-content-between">

        <LoadingBar className="loading-bar" />


        <a href="index.html"><img className="logo" src="content/assets/img/uir-logo.png"/></a>

        <nav id="navbar" className="navbar">
          <ul>
            <li><a className="nav-link scrollto active" href="/">Home</a></li>
            {props.isAuthenticated && <EntitiesMenu />}
            {props.isAuthenticated && props.isAdmin && (
              <AdminMenu showOpenAPI={props.isOpenAPIEnabled} showDatabase={!props.isInProduction} />
            )}
            <AccountMenu isAuthenticated={props.isAuthenticated} />
            <li><a className="nav-link scrollto" href="#contact">Contact</a></li>
          </ul>
          <i className="bi bi-list mobile-nav-toggle"></i>
        </nav>

      </div>
    </div>
  );
};

export default Header;
