-- Database: CRS
USE CRS;

-- Add column to capture occupation of the applicant
ALTER TABLE ADOPTION_ORDER ADD COLUMN applicantOccupation varchar(255) DEFAULT NULL;

-- Add column to capture mother occupation
ALTER TABLE ADOPTION_ORDER ADD COLUMN wifeOccupation varchar(255) DEFAULT NULL;