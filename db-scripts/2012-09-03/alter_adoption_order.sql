-- Database: CRS
USE CRS;

-- Add Columns
ALTER TABLE ADOPTION_ORDER ADD COLUMN noticingZonalOffice int(11) NOT NULL;
ALTER TABLE ADOPTION_ORDER ADD COLUMN birthProvinceUKey int(11) DEFAULT NULL;
ALTER TABLE ADOPTION_ORDER ADD COLUMN birthDistrictId int(11) DEFAULT NULL;
ALTER TABLE ADOPTION_ORDER ADD COLUMN oldBirthDSName varchar(30) DEFAULT NULL;
ALTER TABLE ADOPTION_ORDER ADD COLUMN oldBirthRegistrationDivisionName varchar(30) DEFAULT NULL;
ALTER TABLE ADOPTION_ORDER ADD COLUMN oldBirthRegistrationDate date DEFAULT NULL;
ALTER TABLE ADOPTION_ORDER ADD COLUMN oldBirthSLIN bigint(20) DEFAULT NULL;

-- Remove Birth Serial number
ALTER TABLE ADOPTION_ORDER DROP COLUMN birthRegistrationSerial;

-- Add Foreign Key
ALTER TABLE ADOPTION_ORDER ADD CONSTRAINT `FK_AdoptionOrder_ZonalOffice_noticingZonalOffice` FOREIGN KEY (`noticingZonalOffice`) REFERENCES `COMMON`.`ZONAL_OFFICES` (`zonalOfficeUKey`);