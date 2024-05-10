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