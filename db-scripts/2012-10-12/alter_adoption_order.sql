-- Database: CRS
USE CRS;

-- Clear previous data entered
TRUNCATE ADOPTION_ORDER;

-- Alter wife details as spouse details.
ALTER TABLE ADOPTION_ORDER CHANGE COLUMN wifeName spouseName varchar(255) DEFAULT NULL;
ALTER TABLE ADOPTION_ORDER CHANGE COLUMN wifeOccupation spouseOccupation varchar(255) DEFAULT NULL;
ALTER TABLE ADOPTION_ORDER CHANGE COLUMN wifePINorNIC spousePINorNIC varchar(12) DEFAULT NULL;
ALTER TABLE ADOPTION_ORDER CHANGE COLUMN wifeCountryId spouseCountryId int(11) DEFAULT NULL;
ALTER TABLE ADOPTION_ORDER CHANGE COLUMN wifePassport spousePassport varchar(255) DEFAULT NULL;

-- Add column to capture whether there are joint applicants. (Husband and Wife)
ALTER TABLE ADOPTION_ORDER ADD COLUMN jointApplicant bit(1) NOT NULL;