-- V6__insert_sample_routes.sql
-- Insert sample fare structure
INSERT INTO fare_structures (effective_date, minimum_fare, fare_per_km, is_active, notes)
VALUES ('2025-01-01', 20.00, 2.50, true, 'Initial fare structure');

-- Insert sample route: Matara to Colombo (02)
INSERT INTO routes (route_number, route_name_en, route_name_si, route_name_ta,
                    origin_city, destination_city, total_distance_km)
VALUES ('02', 'Matara - Colombo', 'මාතර - කොළඹ', 'மாத்தறை - கொழும்பு',
        'Matara', 'Colombo', 160.0);

-- Insert stops for route 02
WITH route AS (SELECT id FROM routes WHERE route_number = '02')
INSERT INTO route_stops (route_id, stop_sequence, stop_name_en, stop_name_si, stop_name_ta,
                        latitude, longitude, location, is_major_stop)
SELECT
    route.id,
    stop_sequence,
    stop_name_en,
    stop_name_si,
    stop_name_ta,
    latitude,
    longitude,
    ST_MakePoint(longitude, latitude)::geography,
    is_major_stop
FROM route,
     (VALUES
          (1, 'Matara', 'මාතර', 'மாத்தறை', 5.9485, 80.5353, true),
          (2, 'Weligama', 'වැලිගම', 'வெலிகம', 5.9667, 80.4297, false),
          (3, 'Habaraduwa', 'හබරාදූව', 'ஹபராதுவ', 5.9961, 80.3184, false),
          (4, 'Galle', 'ගාල්ල', 'காலி', 6.0535, 80.2210, true),
          (5, 'Hikkaduwa', 'හික්කඩුව', 'ஹிக்கடுவ', 6.1396, 80.1039, false),
          (6, 'Ambalangoda', 'අම්බලන්ගොඩ', 'அம்பலாங்கொட', 6.2367, 80.0540, false),
          (7, 'Aluthgama', 'අලුත්ගම', 'அளுத்கம', 6.4336, 79.9992, false),
          (8, 'Kalutara', 'කළුතර', 'களுத்துறை', 6.5854, 79.9607, true),
          (9, 'Panadura', 'පානදුර', 'பாணந்துறை', 6.7132, 79.9026, false),
          (10, 'Moratuwa', 'මොරටුව', 'மொறட்டுவ', 6.7733, 79.8825, false),
          (11, 'Mount Lavinia', 'ගල්කිස්ස', 'கல்கிஸ்ஸ', 6.8389, 79.8630, false),
          (12, 'Colombo', 'කොළඹ', 'கொழும்பு', 6.9271, 79.8612, true)
     ) AS stops(stop_sequence, stop_name_en, stop_name_si, stop_name_ta, latitude, longitude, is_major_stop);