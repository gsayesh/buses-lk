-- V3__create_buses_table.sql
CREATE TABLE IF NOT EXISTS buses (
                                     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    registration_number VARCHAR(20) UNIQUE NOT NULL,
    operator_id UUID NOT NULL REFERENCES bus_operators(id),
    service_category_id UUID NOT NULL REFERENCES service_categories(id),
    make VARCHAR(50),
    model VARCHAR(50),
    year_of_manufacture INTEGER,
    seating_capacity INTEGER NOT NULL,
    has_gps_device BOOLEAN DEFAULT false,
    gps_device_id VARCHAR(100),
    bus_photo_url TEXT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
                             );

CREATE INDEX idx_buses_registration ON buses(registration_number);
CREATE INDEX idx_buses_operator ON buses(operator_id);
CREATE INDEX idx_buses_active ON buses(is_active);
