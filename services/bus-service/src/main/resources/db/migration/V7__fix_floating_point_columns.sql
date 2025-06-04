-- V7__fix_floating_point_columns.sql
-- Fix floating point columns in bus_tracking table
ALTER TABLE bus_tracking
ALTER COLUMN speed_kmh TYPE DOUBLE PRECISION,
    ALTER COLUMN heading TYPE DOUBLE PRECISION;