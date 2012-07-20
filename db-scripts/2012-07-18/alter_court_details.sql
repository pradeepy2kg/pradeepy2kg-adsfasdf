-- Database CRS
USE CRS;

-- Change English Court Name to temporary name
ALTER TABLE COURTS CHANGE COLUMN enCourtName enCourtNameTemp varchar(60) NOT NULL;

-- Change Sinhala Court Name to temporary name
ALTER TABLE COURTS CHANGE COLUMN siCourtName siCourtNameTemp varchar(60) NOT NULL;

-- Rename temporary English name to Sinhala Court Name
ALTER TABLE COURTS CHANGE COLUMN enCourtNameTemp siCourtName varchar(60) NOT NULL;

-- Rename temporary Sinhala name to English Court Name
ALTER TABLE COURTS CHANGE COLUMN siCourtNameTemp enCourtName varchar(60) NOT NULL;
