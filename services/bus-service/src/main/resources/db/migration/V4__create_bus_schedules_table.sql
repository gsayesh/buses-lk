-- V4__create_bus_schedules_table.sql
CREATE TABLE IF NOT EXISTS bus_schedules (
                                             id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    route_id UUID NOT NULL,
    bus_id UUID REFERENCES buses(id),
    departure_time TIME NOT NULL,
    arrival_time TIME NOT NULL,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
                             );

CREATE TABLE IF NOT EXISTS schedule_days (
                                             schedule_id UUID NOT NULL REFERENCES bus_schedules(id) ON DELETE CASCADE,
    day_of_week INTEGER NOT NULL,
    PRIMARY KEY (schedule_id, day_of_week)
    );

CREATE INDEX idx_schedules_route ON bus_schedules(route_id);
CREATE INDEX idx_schedules_bus ON bus_schedules(bus_id);
CREATE INDEX idx_schedules_active ON bus_schedules(is_active);
