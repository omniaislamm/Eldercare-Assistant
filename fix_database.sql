-- Fix missing columns in database

-- Add schedule_times to medicines table
ALTER TABLE medicines ADD COLUMN IF NOT EXISTS schedule_times VARCHAR(255);

-- Add appointment_time to doctors table  
ALTER TABLE doctors ADD COLUMN IF NOT EXISTS appointment_time VARCHAR(10);

-- Verify changes
DESCRIBE medicines;
DESCRIBE doctors;
