-- Database: COMMON
USE COMMON;

-- Removing Foreign keys.
ALTER TABLE ZONAL_OFFICES DROP FOREIGN KEY FKFD93417C4B52CC97;
ALTER TABLE ZONAL_OFFICES DROP FOREIGN KEY FKFD93417CF46583DA;

-- Removing unnecessary fields.
ALTER TABLE ZONAL_OFFICES DROP COLUMN active;
ALTER TABLE ZONAL_OFFICES DROP COLUMN createdTimestamp;
ALTER TABLE ZONAL_OFFICES DROP COLUMN lastUpdatedTimestamp;
ALTER TABLE ZONAL_OFFICES DROP COLUMN createdUserId;
ALTER TABLE ZONAL_OFFICES DROP COLUMN lastUpdatedUserId;

-- Add column to capture status
ALTER TABLE ZONAL_OFFICES ADD COLUMN `active` smallint(6) NOT NULL DEFAULT '1';

-- Update phone number of central zone
UPDATE ZONAL_OFFICES SET zonalOfficeLandPhone = '0812224470' WHERE zonalOfficeUKey = 2;

-- Update Addresses
UPDATE ZONAL_OFFICES SET siZonalOfficeMailAddress = 'රෙජිස්ට්‍රාර් ජනරාල් දෙපාර්තමේන්තුව,\nඅංක 41-1/1A, \nශ්‍රී දේවමිත්ත මාවත,\nගාල්ල' WHERE zonalOfficeUKey = 3;
UPDATE ZONAL_OFFICES SET siZonalOfficeMailAddress = 'රෙජිස්ට්‍රාර් ජනරාල් දෙපාර්තමේන්තුව,\nකච්චේරි ගොඩනැඟිල්ල,\nනව නගරය,\nරත්නපුර' WHERE zonalOfficeUKey = 7;
UPDATE ZONAL_OFFICES SET siZonalOfficeMailAddress = 'රෙජිස්ට්‍රාර් ජනරාල් දෙපාර්තමේන්තුව,\nඉඩම් රෙජිස්ට්‍රාර් කාර්යාල ගොඩනැඟිල්ල,\nයාපනය' WHERE zonalOfficeUKey = 4;
UPDATE ZONAL_OFFICES SET siZonalOfficeMailAddress = 'රෙජිස්ට්‍රාර් ජනරාල් දෙපාර්තමේන්තුව,\nකච්චේරි ගොඩනැඟිල්ල,\nමඩකලපුව' WHERE zonalOfficeUKey = 5;

UPDATE ZONAL_OFFICES SET taZonalOfficeMailAddress = 'பதிவாளர் நாயகம் திணைக்களம்,\nஇ 41-1/1A, \nலஶ்ரீ தேவமித்த மாவத்தை ,\nகாலி ' WHERE zonalOfficeUKey = 3;
UPDATE ZONAL_OFFICES SET taZonalOfficeMailAddress = 'பதிவாளர் நாயகம் திணைக்களம்,\nகச்சேரி கட்டிடம் ,\nபுதிய நகரம் ,\nஇரத்தினபுரி ' WHERE zonalOfficeUKey = 7;
UPDATE ZONAL_OFFICES SET taZonalOfficeMailAddress = 'பதிவாளர் நாயகம் திணைக்களம்,\nகாணிப்பதிவக கட்டிடம் ,\nயாழ்ப்பாணம் ' WHERE zonalOfficeUKey = 4;
UPDATE ZONAL_OFFICES SET taZonalOfficeMailAddress = 'பதிவாளர் நாயகம் திணைக்களம்,\nகச்சேரி கட்டிடம் ,\nமட்டக்களப்பு' WHERE zonalOfficeUKey = 5;

UPDATE ZONAL_OFFICES SET enZonalOfficeMailAddress = 'Registrar General\'s Department,\nNo 41-1/1A, \nSri Devamitta Mawatha,\nGalle' WHERE zonalOfficeUKey = 3;
UPDATE ZONAL_OFFICES SET enZonalOfficeMailAddress = 'Registrar General\'s Department,\nKachcheri Complex, \nNew Town,\nRathnapura' WHERE zonalOfficeUKey = 7;
UPDATE ZONAL_OFFICES SET enZonalOfficeMailAddress = 'Registrar General\'s Department,\nLand Registry Building, \nJaffna' WHERE zonalOfficeUKey = 4;
UPDATE ZONAL_OFFICES SET enZonalOfficeMailAddress = 'Registrar General\'s Department,\nKachcheri Complex, \nBatticoloa' WHERE zonalOfficeUKey = 5;