-- Database migration script to drop unused payment_method column
-- This column is not being used in the application and all values are NULL

-- Backup the sales table structure first (optional, for safety)
-- CREATE TABLE sales_backup_20250913 AS SELECT * FROM sales;

-- Drop the unused payment_method column from sales table
ALTER TABLE sales DROP COLUMN payment_method;

-- Verify the change
-- DESCRIBE sales;