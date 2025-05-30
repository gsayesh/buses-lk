-- V6__insert_initial_data.sql
-- Insert service categories
INSERT INTO service_categories (category, fare_multiplier, display_name_en, display_name_si, display_name_ta, amenities) VALUES
                                                                                                                             ('NORMAL', 1.0, 'Normal', 'සාමාන්‍ය', 'சாதாரண', '["basic-seating"]'),
                                                                                                                             ('SEMI_LUXURY', 1.5, 'Semi-Luxury', 'අර්ධ සුඛෝපභෝගී', 'அரை ஆடம்பர', '["cushioned-seats", "fans"]'),
                                                                                                                             ('AC_LUXURY', 2.0, 'A/C Luxury', 'වායු සමීකරණ සුඛෝපභෝගී', 'ஏசி ஆடம்பர', '["ac", "cushioned-seats", "curtains"]'),
                                                                                                                             ('SUPER_LUXURY', 3.0, 'Super Luxury', 'සුපිරි සුඛෝපභෝගී', 'சூப்பர் ஆடம்பர', '["ac", "reclining-seats", "entertainment", "wifi"]');

-- Insert sample operators
INSERT INTO bus_operators (operator_code, operator_name, operator_type, contact_number, email) VALUES
                                                                                                   ('SLTB-001', 'Sri Lanka Transport Board - Colombo', 'SLTB', '0112581120', 'colombo@sltb.lk'),
                                                                                                   ('SLTB-002', 'Sri Lanka Transport Board - Galle', 'SLTB', '0912234567', 'galle@sltb.lk'),
                                                                                                   ('PVT-001', 'Deluxe Bus Service', 'PRIVATE', '0777123456', 'info@deluxebus.lk'),
                                                                                                   ('PVT-002', 'Express Transport Lanka', 'PRIVATE', '0767890123', 'contact@expresslanka.lk');