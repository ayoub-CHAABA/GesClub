// src/components/MyOrgChart.jsx
import React, { useState } from 'react';
import { OrganizationChart } from 'primereact/organizationchart';
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';
import '../css/tailwind.css';
import Tailwind from '../tailwindConfig'; // Adjust the path as necessary

const nodeTemplate = (node) => {
  if (node.type === 'person') {
    return (
      <div className={Tailwind.organizationchart.node.className} style={{ textAlign: 'center', padding: '10px' }}>
        <img src={node.data.avatar} alt={node.data.name} style={{ width: '70px', height: '70px', borderRadius: '50%' }} />
        <div style={{ fontWeight: 'bold', fontSize: '16px', marginTop: '8px' }}>{node.data.name}</div>
        <div style={{ fontSize: '12px', color: '#555' }}>{node.data.title}</div>
      </div>
    );
  }
  return <div>{node.label}</div>;
};

const data = [{
  label: 'CEO',
  type: 'person',
  expanded: true,
  data: { name: 'Amy Elsner', title: 'CEO', avatar: 'https://primefaces.org/primereact/images/avatar/amyelsner.png' },
  children: [
    {
      label: 'CFO',
      type: 'person',
      expanded: true,
      data: { name: 'Anna Fali', title: 'CFO', avatar: 'https://primefaces.org/primereact/images/avatar/annafali.png' },
      children: [
        { label: 'Tax Department', type: 'department' },
        { label: 'Legal Department', type: 'department' }
      ]
    },
    {
      label: 'CTO',
      type: 'person',
      expanded: true,
      data: { name: 'Stephen Shaw', title: 'CTO', avatar: 'https://primefaces.org/primereact/images/avatar/stephenshaw.png' },
      children: [
        { label: 'IT Support', type: 'department' },
        { label: 'Product Development', type: 'department' }
      ]
    }
  ]
}];

const MyOrgChart = () => {
  const [selection, setSelection] = useState([]);

  return (
    <div className="organization-chart">
      <OrganizationChart
        value={data}
        nodeTemplate={nodeTemplate}
        selectionMode="multiple"
        selection={selection}
        onSelectionChange={(e) => setSelection(e.data)}
        className={Tailwind.organizationchart.table}
      />
    </div>
  );
};

export default MyOrgChart;
/*******************/
const nodeTemplate = (node) => {
    if (node.type === 'person') {
        return (
            <div style={{ textAlign: 'center', padding: '10px' }}>
                <img src={node.data.avatar} alt={node.data.name} style={{ width: '70px', height: '70px', borderRadius: '50%' }} />
                <div style={{ fontWeight: 'bold', fontSize: '16px', marginTop: '8px' }}>{node.data.name}</div>
                <div style={{ fontSize: '12px', color: '#555' }}>{node.data.title}</div>
            </div>
        );
    }
    return <div>{node.label}</div>;
};
/*****/
import React from 'react';
import { OrganizationChart } from 'primereact/organizationchart';

const MyOrgChart = () => {
    const data = [{
        label: 'CEO',
        type: 'person',
        expanded: true,
        data: { name: 'Amy Elsner', avatar: 'path_to_ceo_image.jpg' },
        children: [
            {
                label: 'CFO',
                type: 'person',
                expanded: true,
                data: { name: 'Anna Fali', avatar: 'path_to_cfo_image.jpg' },
                children: [
                    { label: 'Tax', type: 'department' },
                    { label: 'Legal', type: 'department' }
                ]
            },
            {
                label: 'CTO',
                type: 'person',
                expanded: true,
                data: { name: 'Stephen Shaw', avatar: 'path_to_cto_image.jpg' },
                children: [
                    { label: 'Operations', type: 'department' },
                    { label: 'Software Development', type: 'department' }
                ]
            }
        ]
    }];

    const nodeTemplate = (node) => {
        if (node.type === 'person') {
            return (
                <div>
                    <img src={node.data.avatar} alt={node.data.name} style={{ width: '32px', verticalAlign: 'middle' }} />
                    <span style={{ marginLeft: '5px' }}>{node.data.name}</span>
                </div>
            );
        } else {
            return node.label;
        }
    };

    return <OrganizationChart value={data} nodeTemplate={nodeTemplate} style={{ width: '100%' }} />;
};

export default MyOrgChart;
/*****/
import React from 'react';
import { OrganizationChart } from 'primereact/organizationchart';

const MyOrgChart = () => {
    const data = [{
        label: 'CEO',
        type: 'person',
        expanded: true,
        data: { name: 'Amy Elsner', avatar: 'path_to_ceo_image.jpg' },
        children: [
            {
                label: 'CFO',
                type: 'person',
                expanded: true,
                data: { name: 'Anna Fali', avatar: 'path_to_cfo_image.jpg' },
                children: [
                    { label: 'Tax', type: 'department' },
                    { label: 'Legal', type: 'department' }
                ]
            },
            {
                label: 'CTO',
                type: 'person',
                expanded: true,
                data: { name: 'Stephen Shaw', avatar: 'path_to_cto_image.jpg' },
                children: [
                    { label: 'Operations', type: 'department' },
                    { label: 'Software Development', type: 'department' }
                ]
            }
        ]
    }];

    const nodeTemplate = (node) => {
        if (node.type === 'person') {
            return (
                <div>
                    <img src={node.data.avatar} alt={node.data.name} style={{ width: '32px', verticalAlign: 'middle' }} />
                    <span style={{ marginLeft: '5px' }}>{node.data.name}</span>
                </div>
            );
        } else {
            return node.label;
        }
    };

    return <OrganizationChart value={data} nodeTemplate={nodeTemplate} style={{ width: '100%' }} />;
};

export default MyOrgChart;
// src/components/MyOrgChart.jsx
import React from 'react';
import { OrganizationChart } from 'primereact/organizationchart';

const MyOrgChart = () => {
    const data = [{
        label: 'CEO',
        expanded: true,
        children: [
            {
                label: 'CFO',
                expanded: true,
                children: [
                    { label: 'Tax' },
                    { label: 'Legal' }
                ]
            },
            {
                label: 'CTO',
                expanded: true,
                children: [
                    { label: 'Operations' },
                    { label: 'Software Development' }
                ]
            }
        ]
    }];

    return <OrganizationChart value={data} style={{ width: '100%' }} />;
};

export default MyOrgChart;