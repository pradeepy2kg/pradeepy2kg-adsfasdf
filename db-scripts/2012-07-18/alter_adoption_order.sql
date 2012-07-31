-- Database: CRS
USE CRS;

-- Add Column to store adoptionSerialNo.
ALTER TABLE ADOPTION_ORDER ADD COLUMN adoptionSerialNo bigint(20) DEFAULT NULL;

-- Add column to store adoptionEntryNo
ALTER TABLE ADOPTION_ORDER ADD COLUMN adoptionEntryNo bigint(20) NOT NULL;