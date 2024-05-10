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