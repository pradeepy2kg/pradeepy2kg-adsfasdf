-- Select Database.
USE CRS;

-- Introduce new column to capture comments.
ALTER TABLE ADOPTION_ORDER ADD COLUMN `comments` longtext;
ALTER TABLE ADOPTION_ORDER ADD COLUMN `previousAdoptionIdUKey` bigint(20) DEFAULT NULL;


-- Add new column for get Application entered Date (2013-10-04)
ALTER TABLE ADOPTION_ORDER ADD COLUMN `applicationEnteredDate` DATETIME;