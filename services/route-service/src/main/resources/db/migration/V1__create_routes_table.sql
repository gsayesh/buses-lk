-- V1__create_routes_table.sql
CREATE TABLE IF NOT EXISTS routes (
                                      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    route_number VARCHAR(20) UNIQUE NOT NULL,
    route_name_en VARCHAR(200) NOT NULL,
    route_name_si VARCHAR(200) NOT NULL,
    route_name_ta VARCHAR(200) NOT NULL,
    origin_city VARCHAR(100) NOT NULL,
    destination_city VARCHAR(100) NOT NULL,
    total_distance_km DOUBLE PRECISION,
    is_rotational BOOLEAN DEFAULT false,
    route_photo_url TEXT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
                             );

CREATE INDEX idx_routes_route_number ON routes(route_number);
CREATE INDEX idx_routes_active ON routes(is_active);