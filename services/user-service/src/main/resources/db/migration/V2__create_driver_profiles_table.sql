-- V2__create_driver_profiles_table.sql
CREATE TABLE IF NOT EXISTS driver_profiles (
                                               id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID UNIQUE NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    license_number VARCHAR(50) UNIQUE NOT NULL,
    operator_id UUID,
    assigned_bus_id UUID,
    is_approved BOOLEAN DEFAULT false,
    approved_by UUID REFERENCES users(id),
    approved_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
                                                          );

CREATE INDEX idx_driver_profiles_license ON driver_profiles(license_number);
CREATE INDEX idx_driver_profiles_operator ON driver_profiles(operator_id);
CREATE INDEX idx_driver_profiles_bus ON driver_profiles(assigned_bus_id);
CREATE INDEX idx_driver_profiles_approved ON driver_profiles(is_approved);