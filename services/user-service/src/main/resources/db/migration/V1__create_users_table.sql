-- V1__create_users_table.sql
CREATE TABLE IF NOT EXISTS users (
                                     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    mobile_number VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    preferred_language VARCHAR(2) NOT NULL DEFAULT 'en',
    is_verified BOOLEAN DEFAULT false,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                             version BIGINT DEFAULT 0
                             );

CREATE INDEX idx_users_mobile_number ON users(mobile_number);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);