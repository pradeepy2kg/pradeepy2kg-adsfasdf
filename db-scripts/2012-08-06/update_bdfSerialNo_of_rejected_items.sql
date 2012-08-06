-- Database: CRS
USE CRS;

-- Update bdfSerialNo of Rejected items in Birth Register
-- Replace the 5th Digit of the serial no as follows.( 0 --> 8 and 1--> 9)
-- eg: 2012100027 --> 2012900027

UPDATE BIRTH_REGISTER SET bdfSerialNo = 2012900027 WHERE bdfSerialNo = 2012100027 AND status = 7;
UPDATE BIRTH_REGISTER SET bdfSerialNo = 2012900046 WHERE bdfSerialNo = 2012100046 AND status = 7;
UPDATE BIRTH_REGISTER SET bdfSerialNo = 2012900050 WHERE bdfSerialNo = 2012100050 AND status = 7;
UPDATE BIRTH_REGISTER SET bdfSerialNo = 2012900064 WHERE bdfSerialNo = 2012100064 AND status = 7;
UPDATE BIRTH_REGISTER SET bdfSerialNo = 2012900067 WHERE bdfSerialNo = 2012100067 AND status = 7;
UPDATE BIRTH_REGISTER SET bdfSerialNo = 2012900078 WHERE bdfSerialNo = 2012100078 AND status = 7;
UPDATE BIRTH_REGISTER SET bdfSerialNo = 2012900088 WHERE bdfSerialNo = 2012100088 AND status = 7;