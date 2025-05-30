-- V1__create_service_categories_table.sql
CREATE TABLE IF NOT EXISTS service_categories (
                                                  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    category VARCHAR(20) UNIQUE NOT NULL,
    fare_multiplier DECIMAL(3,2) NOT NULL,
    display_name_en VARCHAR(50) NOT NULL,
    display_name_si VARCHAR(50) NOT NULL,
    display_name_ta VARCHAR(50) NOT NULL,
    amenities JSONB DEFAULT '[]'::jsonb,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
                             );