-- Database: CRS
USE CRS;

-- Update deathSerialNo of rejected items in Death Register

UPDATE DEATH_REGISTER SET deathSerialNo = 2011900023 WHERE deathSerialNo = 2011100023 AND status = 2;
UPDATE DEATH_REGISTER SET deathSerialNo = 2012900008 WHERE deathSerialNo = 2012100008 AND status = 2;