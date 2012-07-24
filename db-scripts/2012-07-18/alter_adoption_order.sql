-- Database: CRS
USE CRS;

-- Add Column to store adoptionSerialNo.
ALTER TABLE ADOPTION_ORDER ADD COLUMN adoptionSerialNo bigint(20) DEFAULT NULL;