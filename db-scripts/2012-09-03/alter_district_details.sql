-- Database COMMON
USE COMMON;

-- Add column to capture Province
ALTER TABLE DISTRICTS ADD COLUMN provinceUKey int(11) NOT NULL;

-- Enter ProvinceUKey to Districts
UPDATE DISTRICTS SET provinceUKey = 1 WHERE districtUKey = 1;
UPDATE DISTRICTS SET provinceUKey = 1 WHERE districtUKey = 2;
UPDATE DISTRICTS SET provinceUKey = 1 WHERE districtUKey = 3;
UPDATE DISTRICTS SET provinceUKey = 2 WHERE districtUKey = 4;
UPDATE DISTRICTS SET provinceUKey = 2 WHERE districtUKey = 5;
UPDATE DISTRICTS SET provinceUKey = 2 WHERE districtUKey = 6;
UPDATE DISTRICTS SET provinceUKey = 3 WHERE districtUKey = 7;
UPDATE DISTRICTS SET provinceUKey = 3 WHERE districtUKey = 8;
UPDATE DISTRICTS SET provinceUKey = 3 WHERE districtUKey = 9;
UPDATE DISTRICTS SET provinceUKey = 4 WHERE districtUKey = 10;
UPDATE DISTRICTS SET provinceUKey = 4 WHERE districtUKey = 11;
UPDATE DISTRICTS SET provinceUKey = 4 WHERE districtUKey = 12;
UPDATE DISTRICTS SET provinceUKey = 4 WHERE districtUKey = 13;
UPDATE DISTRICTS SET provinceUKey = 4 WHERE districtUKey = 14;
UPDATE DISTRICTS SET provinceUKey = 5 WHERE districtUKey = 15;
UPDATE DISTRICTS SET provinceUKey = 5 WHERE districtUKey = 16;
UPDATE DISTRICTS SET provinceUKey = 5 WHERE districtUKey = 17;
UPDATE DISTRICTS SET provinceUKey = 6 WHERE districtUKey = 18;
UPDATE DISTRICTS SET provinceUKey = 6 WHERE districtUKey = 19;
UPDATE DISTRICTS SET provinceUKey = 7 WHERE districtUKey = 20;
UPDATE DISTRICTS SET provinceUKey = 7 WHERE districtUKey = 21;
UPDATE DISTRICTS SET provinceUKey = 8 WHERE districtUKey = 22;
UPDATE DISTRICTS SET provinceUKey = 8 WHERE districtUKey = 23;
UPDATE DISTRICTS SET provinceUKey = 9 WHERE districtUKey = 24;
UPDATE DISTRICTS SET provinceUKey = 9 WHERE districtUKey = 25;

-- Add Foreign key constraint
ALTER TABLE DISTRICTS ADD CONSTRAINT `FK_Districts_Province_provinceUKey` FOREIGN KEY (`provinceUKey`) REFERENCES `COMMON`.`PROVINCE` (`provinceUKey`);


