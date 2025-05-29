-- V5__create_route_fares_table.sql
CREATE TABLE IF NOT EXISTS route_fares (
                                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    route_id UUID NOT NULL REFERENCES routes(id) ON DELETE CASCADE,
    fare_structure_id UUID NOT NULL REFERENCES fare_structures(id),
    from_stop_id UUID NOT NULL REFERENCES route_stops(id),
    to_stop_id UUID NOT NULL REFERENCES route_stops(id),
    base_fare DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                                     UNIQUE(route_id, from_stop_id, to_stop_id, fare_structure_id)
    );

CREATE INDEX idx_route_fares_lookup ON route_fares(route_id, from_stop_id, to_stop_id);
