-- Swap 'dateOfRegistration' and 'notifyingAuthoritySignDate' in BIRTH_REGISTER.
USE CRS;

-- Rename dateOfRegistration Column to a Temporary column
ALTER TABLE BIRTH_REGISTER CHANGE COLUMN dateOfRegistration dateOfRegTemp date NOT NULL;

-- Rename notifyingAuthoritySignDate to a Temporary column
ALTER TABLE BIRTH_REGISTER CHANGE COLUMN notifyingAuthoritySignDate notifySignTemp date NOT NULL;

-- Change the temporary dateOfRegistration to notifyingAuthoritySignDate
ALTER TABLE BIRTH_REGISTER CHANGE COLUMN dateOfRegTemp notifyingAuthoritySignDate date NOT NULL;

-- Change the temporary notifyingAuthoritySignDate to dateOfRegistration
ALTER TABLE BIRTH_REGISTER CHANGE COLUMN notifySignTemp dateOfRegistration date NOT NULL;