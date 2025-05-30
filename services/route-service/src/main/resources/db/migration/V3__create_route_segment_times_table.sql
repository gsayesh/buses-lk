-- V3__create_route_segment_times_table.sql
CREATE TABLE IF NOT EXISTS route_segment_times (
                                                   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    route_id UUID NOT NULL REFERENCES routes(id) ON DELETE CASCADE,
    from_stop_id UUID NOT NULL REFERENCES route_stops(id),
    to_stop_id UUID NOT NULL REFERENCES route_stops(id),
    time_period VARCHAR(20) NOT NULL, -- 'day', 'night', 'peak', 'off_peak'
    duration_minutes INTEGER NOT NULL,
    distance_km DECIMAL(10,2),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                                     UNIQUE(route_id, from_stop_id, to_stop_id, time_period)
    );

CREATE INDEX idx_segment_times_route ON route_segment_times(route_id);
CREATE INDEX idx_segment_times_stops ON route_segment_times(from_stop_id, to_stop_id);
