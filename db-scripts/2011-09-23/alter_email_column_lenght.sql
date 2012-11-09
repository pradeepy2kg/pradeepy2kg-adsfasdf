-- alter email column length in ecivil

USE PRS;

-- alter email column length of Person table
ALTER TABLE PERSON MODIFY personEmail VARCHAR(60);


USE CRS;

-- alter email column length of Registrar table
ALTER TABLE REGISTRAR MODIFY emailAddress VARCHAR(60);
-- alter email column length of Birth Register table
ALTER TABLE BIRTH_REGISTER MODIFY motherEmail VARCHAR(60);
ALTER TABLE BIRTH_REGISTER MODIFY informantEmail VARCHAR(60);

