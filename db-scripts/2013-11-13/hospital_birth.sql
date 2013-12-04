
--select the database : COMMON
USE COMMON;

--create HOSPITAL table
CREATE TABLE  `COMMON`.`HOSPITAL` (
  `hospitalUKey` int(11) NOT NULL AUTO_INCREMENT,
  `hospitalId` int(11) NOT NULL,
  `hospitalNameEn` varchar(255) NOT NULL,
  `hospitalNameSi` varchar(255) NOT NULL,
  `hospitalNameTa` varchar(255) NOT NULL,
  `dsDivisionUKey` int(11) NOT NULL,
  `active` smallint(6) NOT NULL DEFAULT '1',  
  PRIMARY KEY (`hospitalUKey`),
  UNIQUE KEY `hospitalId` (`hospitalId`),
  UNIQUE KEY `hospitalNameEn` (`hospitalNameEn`),
  UNIQUE KEY `hospitalNameSi` (`hospitalNameSi`),
  UNIQUE KEY `hospitalNameTa` (`hospitalNameTa`),
  KEY `FK1C8CEE3A8BC88083` (`dsDivisionUKey`),
  CONSTRAINT `FK1C8CEE3A8BC88083` FOREIGN KEY (`dsDivisionUKey`) REFERENCES `DS_DIVISIONS` (`dsDivisionUKey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--select the database :COMMON
USE CRS;

--add hospitalId to BIRTH_REGISTER table

ALTER TABLE BIRTH_REGISTER ADD COLUMN `hospitalUKey` int(11);
ALTER TABLE BIRTH_REGISTER ADD CONSTRAINT `FK_BirthRegister_Hospital_hospitalUkey` FOREIGN KEY (`hospitalUKey`) REFERENCES `COMMON`.`HOSPITAL` (`hospitalUKey`);