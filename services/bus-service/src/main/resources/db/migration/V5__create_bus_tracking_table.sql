-- V5__create_bus_tracking_table.sql
CREATE TABLE IF NOT EXISTS bus_tracking (
                                            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    bus_id UUID NOT NULL REFERENCES buses(id),
    driver_id UUID,
    tracking_source VARCHAR(20) NOT NULL,
    latitude DECIMAL(10,8) NOT NULL,
    longitude DECIMAL(11,8) NOT NULL,
    location GEOGRAPHY(POINT, 4326),
    speed_kmh DECIMAL(5,2),
    heading DECIMAL(5,2),
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
                             );

-- Create BRIN index for time-series queries
CREATE INDEX idx_bus_tracking_timestamp ON bus_tracking
    USING BRIN (timestamp) WITH (pages_per_range = 128);

CREATE INDEX idx_bus_tracking_bus_timestamp ON bus_tracking(bus_id, timestamp DESC);
CREATE INDEX idx_bus_tracking_location ON bus_tracking USING GIST(location);
