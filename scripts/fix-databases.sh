#!/bin/bash
# save as fix-databases.sh

echo "Fixing databases..."

# Create databases manually
docker exec -i buses-postgres psql -U postgres << EOF
CREATE DATABASE users_db;
CREATE DATABASE buses_db;
CREATE DATABASE routes_db;
CREATE DATABASE bookings_db;
CREATE DATABASE payments_db;
CREATE DATABASE notifications_db;

\c users_db
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "postgis";

\c buses_db
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "postgis";

\c routes_db
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "postgis";

\c bookings_db
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "postgis";
EOF

echo "Databases created successfully!"
docker exec -it buses-postgres psql -U postgres -c "\l"