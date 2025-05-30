-- V4__create_fare_structures_table.sql
CREATE TABLE IF NOT EXISTS fare_structures (
                                               id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    effective_date DATE NOT NULL,
    minimum_fare DECIMAL(10,2) NOT NULL,
    fare_per_km DECIMAL(10,4) NOT NULL,
    created_by UUID,
    notes TEXT,
    is_active BOOLEAN DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
                             );

CREATE INDEX idx_fare_structure_active ON fare_structures(is_active);
CREATE INDEX idx_fare_structure_date ON fare_structures(effective_date);
