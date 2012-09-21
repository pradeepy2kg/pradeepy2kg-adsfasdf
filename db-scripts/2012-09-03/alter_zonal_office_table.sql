-- Database: COMMON
USE COMMON;

-- Removing Foreign keys.
ALTER TABLE ZONAL_OFFICES DROP FOREIGN KEY FKFD93417C4B52CC97;
ALTER TABLE ZONAL_OFFICES DROP FOREIGN KEY FKFD93417CF46583DA;

-- Removing unnecessary fields.
ALTER TABLE ZONAL_OFFICES DROP COLUMN active;
ALTER TABLE ZONAL_OFFICES DROP COLUMN createdTimestamp;
ALTER TABLE ZONAL_OFFICES DROP COLUMN lastUpdatedTimestamp;
ALTER TABLE ZONAL_OFFICES DROP COLUMN createdUserId;
ALTER TABLE ZONAL_OFFICES DROP COLUMN lastUpdatedUserId;

-- Add column to capture status
ALTER TABLE ZONAL_OFFICES ADD COLUMN `active` smallint(6) NOT NULL DEFAULT '1';

-- Update phone number of central zone
UPDATE ZONAL_OFFICES SET zonalOfficeLandPhone = '0812224470' WHERE zonalOfficeUKey = 2;

