-- V2__create_bus_operators_table.sql
CREATE TABLE IF NOT EXISTS bus_operators (
                                             id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    operator_code VARCHAR(20) UNIQUE NOT NULL,
    operator_name VARCHAR(100) NOT NULL,
    operator_type VARCHAR(20) NOT NULL,
    contact_number VARCHAR(20),
    email VARCHAR(100),
    address TEXT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
                             );

CREATE INDEX idx_bus_operators_code ON bus_operators(operator_code);
CREATE INDEX idx_bus_operators_type ON bus_operators(operator_type);
