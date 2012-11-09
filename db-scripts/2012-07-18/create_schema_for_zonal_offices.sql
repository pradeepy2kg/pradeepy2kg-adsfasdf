-- Database: COMMON
USE COMMON;

--
-- Table structure for ZONAL_OFFICES
--

DROP TABLE IF EXISTS `ZONAL_OFFICES`;

CREATE TABLE  `COMMON`.`ZONAL_OFFICES` (
  `zonalOfficeUKey` int(11) NOT NULL AUTO_INCREMENT,
  `enZonalOfficeMailAddress` varchar(255) DEFAULT NULL,
  `enZonalOfficeName` varchar(120) NOT NULL,
  `active` bit(1) NOT NULL,
  `createdTimestamp` datetime DEFAULT NULL,
  `lastUpdatedTimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `siZonalOfficeMailAddress` varchar(255) DEFAULT NULL,
  `siZonalOfficeName` varchar(120) NOT NULL,
  `taZonalOfficeMailAddress` varchar(255) DEFAULT NULL,
  `taZonalOfficeName` varchar(120) NOT NULL,
  `zonalOfficeEmail` varchar(50) DEFAULT NULL,
  `zonalOfficeFax` varchar(30) DEFAULT NULL,
  `zonalOfficeLandPhone` varchar(30) DEFAULT NULL,
  `createdUserId` varchar(30) DEFAULT NULL,
  `lastUpdatedUserId` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`zonalOfficeUKey`),
  UNIQUE KEY `enZonalOfficeName` (`enZonalOfficeName`),
  UNIQUE KEY `siZonalOfficeName` (`siZonalOfficeName`),
  UNIQUE KEY `taZonalOfficeName` (`taZonalOfficeName`),
  KEY `FKFD93417CF46583DA` (`createdUserId`),
  KEY `FKFD93417C4B52CC97` (`lastUpdatedUserId`),
  CONSTRAINT `FKFD93417C4B52CC97` FOREIGN KEY (`lastUpdatedUserId`) REFERENCES `USERS` (`userId`),
  CONSTRAINT `FKFD93417CF46583DA` FOREIGN KEY (`createdUserId`) REFERENCES `USERS` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for ZONAL_OFFICE_DISTRICTS
--

DROP TABLE IF EXISTS `ZONAL_OFFICE_DISTRICTS`;

CREATE TABLE  `COMMON`.`ZONAL_OFFICE_DISTRICTS` (
  `zonalOfficeUKey` int(11) NOT NULL,
  `districtUKey` int(11) NOT NULL,
  PRIMARY KEY (`zonalOfficeUKey`,`districtUKey`),
  UNIQUE KEY `districtUKey` (`districtUKey`),
  KEY `FKC08F45FDCA3A0A67` (`districtUKey`),
  KEY `FKC08F45FD64AD2CC9` (`zonalOfficeUKey`),
  CONSTRAINT `FKC08F45FD64AD2CC9` FOREIGN KEY (`zonalOfficeUKey`) REFERENCES `ZONAL_OFFICES` (`zonalOfficeUKey`),
  CONSTRAINT `FKC08F45FDCA3A0A67` FOREIGN KEY (`districtUKey`) REFERENCES `DISTRICTS` (`districtUKey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;