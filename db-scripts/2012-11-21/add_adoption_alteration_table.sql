-- Database : CRS
USE CRS;

-- Create table to store adoption alteration records.
CREATE TABLE  `CRS`.`ALT_ADOPTION` (
  `idUKey` bigint(20) NOT NULL AUTO_INCREMENT,
  `aoUKey` bigint(20) NOT NULL,
  `applicantAddress` varchar(255) NOT NULL,
  `applicantName` varchar(255) NOT NULL,
  `applicantOccupation` varchar(255) DEFAULT NULL,
  `applicantSecondAddress` varchar(255) DEFAULT NULL,
  `childBirthDate` date DEFAULT NULL,
  `childGender` int(11) DEFAULT NULL,
  `childName` varchar(255) DEFAULT NULL,
  `activeRecord` int(11) NOT NULL DEFAULT '1',
  `approvalOrRejectTimestamp` datetime DEFAULT NULL,
  `certificateGeneratedTimestamp` datetime DEFAULT NULL,
  `createdTimestamp` datetime NOT NULL,
  `lastUpdatedTimestamp` datetime NOT NULL,
  `spouseName` varchar(255) DEFAULT NULL,
  `spouseOccupation` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `approvalOrRejectUserId` varchar(30) DEFAULT NULL,
  `certificateGeneratedUserId` varchar(30) DEFAULT NULL,
  `createdUserId` varchar(30) NOT NULL,
  `lastUpdatedUserId` varchar(30) NOT NULL,
  PRIMARY KEY (`idUKey`),
  KEY `FK4E2FFC4EF46583DA` (`createdUserId`),
  KEY `FK4E2FFC4E1B8742CA` (`certificateGeneratedUserId`),
  KEY `FK4E2FFC4E4B52CC97` (`lastUpdatedUserId`),
  KEY `FK4E2FFC4E7518D9B7` (`approvalOrRejectUserId`),
  CONSTRAINT `FK4E2FFC4E7518D9B7` FOREIGN KEY (`approvalOrRejectUserId`) REFERENCES `COMMON`.`USERS` (`userId`),
  CONSTRAINT `FK4E2FFC4E1B8742CA` FOREIGN KEY (`certificateGeneratedUserId`) REFERENCES `COMMON`.`USERS` (`userId`),
  CONSTRAINT `FK4E2FFC4E4B52CC97` FOREIGN KEY (`lastUpdatedUserId`) REFERENCES `COMMON`.`USERS` (`userId`),
  CONSTRAINT `FK4E2FFC4EF46583DA` FOREIGN KEY (`createdUserId`) REFERENCES `COMMON`.`USERS` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8