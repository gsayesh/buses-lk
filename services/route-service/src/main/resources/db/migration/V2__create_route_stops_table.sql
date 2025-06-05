-- V2__create_route_stops_table.sql
CREATE TABLE IF NOT EXISTS route_stops (
                                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    route_id UUID NOT NULL REFERENCES routes(id) ON DELETE CASCADE,
    stop_sequence INTEGER NOT NULL,
    stop_name_en VARCHAR(100) NOT NULL,
    stop_name_si VARCHAR(100) NOT NULL,
    stop_name_ta VARCHAR(100) NOT NULL,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    location GEOGRAPHY(POINT, 4326),
    is_major_stop BOOLEAN DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                                     UNIQUE(route_id, stop_sequence)
    );

CREATE INDEX idx_route_stops_route ON route_stops(route_id);
CREATE INDEX idx_route_stops_location ON route_stops USING GIST(location);