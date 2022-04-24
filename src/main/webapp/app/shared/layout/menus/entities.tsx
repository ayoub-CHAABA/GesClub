import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';

import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown icon="th-list" name="Gestion" id="entity-menu" data-cy="entity" style={{ maxHeight: '80vh', overflow: 'auto' }}>
    <>{/* to avoid warnings when empty */}</>
    <MenuItem icon="asterisk" to="/student">
      Student
    </MenuItem>
    <MenuItem icon="asterisk" to="/club">
      Club
    </MenuItem>
    <MenuItem icon="asterisk" to="/event">
      Event
    </MenuItem>
    <MenuItem icon="asterisk" to="/meeting">
      Meeting
    </MenuItem>
    <MenuItem icon="asterisk" to="/invoice">
      Invoice
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
