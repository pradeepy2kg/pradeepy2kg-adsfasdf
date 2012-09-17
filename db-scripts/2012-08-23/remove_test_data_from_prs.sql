-- Database: PRS
USE PRS;

-- Remove incorrect mappings between LIVE data and TEST data
UPDATE PERSON SET motherUKey = NULL WHERE motherUKey <= 500;
UPDATE PERSON SET fatherUKey = NULL WHERE fatherUKey <= 500;

-- Remove TEST data set
DELETE FROM PERSON WHERE personUKey <= 500;