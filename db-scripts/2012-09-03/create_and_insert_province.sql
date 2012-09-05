-- Database: COMMON
USE COMMON;

-- Create database table for Provinces
CREATE TABLE  `COMMON`.`PROVINCE` (
  `provinceUKey` int(11) NOT NULL AUTO_INCREMENT,
  `active` smallint(6) NOT NULL DEFAULT '1',
  `enProvinceName` varchar(30) NOT NULL,
  `siProvinceName` varchar(30) NOT NULL,
  `taProvinceName` varchar(30) NOT NULL,
  `zonalOfficeUKey` int(11) NOT NULL,
  PRIMARY KEY (`provinceUKey`),
  UNIQUE KEY `enProvinceName` (`enProvinceName`),
  UNIQUE KEY `siProvinceName` (`siProvinceName`),
  UNIQUE KEY `taProvinceName` (`taProvinceName`),
  KEY `FKF3CA1B3064AD2CC9` (`zonalOfficeUKey`),
  CONSTRAINT `FKF3CA1B3064AD2CC9` FOREIGN KEY (`zonalOfficeUKey`) REFERENCES `ZONAL_OFFICES` (`zonalOfficeUKey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Insert Province details
INSERT INTO PROVINCE (enProvinceName, siProvinceName, taProvinceName, zonalOfficeUKey)
            VALUES ('Western', 'බස්නාහිර', 'மேற்கு', 1);
INSERT INTO PROVINCE (enProvinceName, siProvinceName, taProvinceName, zonalOfficeUKey)
            VALUES ('Central', 'මධ්‍යම', 'மத்திய', 2);
INSERT INTO PROVINCE (enProvinceName, siProvinceName, taProvinceName, zonalOfficeUKey)
            VALUES ('Southern', 'දකුණ', 'தெற்கு', 3);
INSERT INTO PROVINCE (enProvinceName, siProvinceName, taProvinceName, zonalOfficeUKey)
            VALUES ('Northern', 'උතුර', 'வடக்கு', 4);
INSERT INTO PROVINCE (enProvinceName, siProvinceName, taProvinceName, zonalOfficeUKey)
            VALUES ('Eastern', 'නැගෙනහිර', 'கிழக்கு', 5);
INSERT INTO PROVINCE (enProvinceName, siProvinceName, taProvinceName, zonalOfficeUKey)
            VALUES ('North-Western', 'වයඹ', 'வட ‍மேற்கு', 6);
INSERT INTO PROVINCE (enProvinceName, siProvinceName, taProvinceName, zonalOfficeUKey)
            VALUES ('North-Central', 'උතුරු මැද', 'வட மத்திய', 6);
INSERT INTO PROVINCE (enProvinceName, siProvinceName, taProvinceName, zonalOfficeUKey)
            VALUES ('Uva', 'ඌව', 'ஊவா', 7);
INSERT INTO PROVINCE (enProvinceName, siProvinceName, taProvinceName, zonalOfficeUKey)
            VALUES ('Sabaragamuwa', 'සබරගමුව', 'சபரகமுவ', 7);