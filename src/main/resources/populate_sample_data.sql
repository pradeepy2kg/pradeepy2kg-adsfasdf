-- Configurable Application Parameters
INSERT INTO COMMON.APP_PARAMETERS(NAME, VALUE) VALUES('crs.birth.late_reg_days', '90');
INSERT INTO COMMON.APP_PARAMETERS(NAME, VALUE) VALUES('crs.br_approval_rows_per_page', '10');

-- Countries
INSERT INTO COMMON.COUNTRIES (COUNTRYID, COUNTRYCODE, SICOUNTRYNAME, ENCOUNTRYNAME, TACOUNTRYNAME) VALUES(1, 'LK', 'ශ්‍රී ලංකාව', 'Sri Lanka', 'ஸ்ரீ லங்கா');
INSERT INTO COMMON.COUNTRIES (COUNTRYID, COUNTRYCODE, SICOUNTRYNAME, ENCOUNTRYNAME, TACOUNTRYNAME) VALUES(2, 'JP', 'ජපානය', 'Japan', 'ஜப்பான்');
INSERT INTO COMMON.COUNTRIES (COUNTRYID, COUNTRYCODE, SICOUNTRYNAME, ENCOUNTRYNAME, TACOUNTRYNAME) VALUES(3, 'IN', 'ඉන්දියාව', 'India', 'இந்திய');

-- Races
INSERT INTO COMMON.RACES (RACEID, SIRACENAME, ENRACENAME, TARACENAME) VALUES(1, 'සිංහල', 'Sinhalese', 'சின்ஹலேசே');
INSERT INTO COMMON.RACES (RACEID, SIRACENAME, ENRACENAME, TARACENAME) VALUES(2, 'ශ්‍රී ලංකික දෙමල', 'Sri Lankan Tamil', 'ஸ்ரீ லங்கன் தமிழ்');
INSERT INTO COMMON.RACES (RACEID, SIRACENAME, ENRACENAME, TARACENAME) VALUES(3, 'ඉන්දියානු දෙමල', 'Indian Tamil', 'இந்தியன் தமிழ்');

-- Districts
INSERT INTO COMMON.DISTRICTS (districtId, siDistrictName, enDistrictName, taDistrictName)  VALUES(11, 'කොළඹ', 'Colombo', '‎கொழும்பு');
INSERT INTO COMMON.DISTRICTS (districtId, siDistrictName, enDistrictName, taDistrictName)  VALUES(12, 'ගම්පහ', 'Gampaha', 'கம்‍‍‎ப‍‍ஹ');
INSERT INTO COMMON.DISTRICTS (districtId, siDistrictName, enDistrictName, taDistrictName)  VALUES(13, 'කළුතර', 'Kalutara', 'களுத்து‍றை');
INSERT INTO COMMON.DISTRICTS (districtId, siDistrictName, enDistrictName, taDistrictName)  VALUES(21, 'මහනුවර', 'Kandy', 'கண்டி');
INSERT INTO COMMON.DISTRICTS (districtId, siDistrictName, enDistrictName, taDistrictName)  VALUES(22, 'මාතලේ', 'Matale', 'மாத்த‍‍‍ளை');
INSERT INTO COMMON.DISTRICTS (districtId, siDistrictName, enDistrictName, taDistrictName)  VALUES(23, 'නුවරඑළිය', 'Nuwara Eliya', 'நுவ‎ரெளியா');
INSERT INTO COMMON.DISTRICTS (districtId, siDistrictName, enDistrictName, taDistrictName)  VALUES(31, 'ගාල්ල', 'Galle', 'காலி');
INSERT INTO COMMON.DISTRICTS (districtId, siDistrictName, enDistrictName, taDistrictName)  VALUES(32, 'මාතර', 'Matara', 'மாத்தறை');
INSERT INTO COMMON.DISTRICTS (districtId, siDistrictName, enDistrictName, taDistrictName)  VALUES(33, 'හම්බන්තොට', 'Hambantota', 'ஹம்பாந்தோட்‍டை');
INSERT INTO COMMON.DISTRICTS (districtId, siDistrictName, enDistrictName, taDistrictName)  VALUES(41, 'යාපනය', 'Jaffna', 'யாழ்பாணம்');
INSERT INTO COMMON.DISTRICTS (districtId, siDistrictName, enDistrictName, taDistrictName)  VALUES(42, 'මන්නාරම', 'Mannar', 'மன்னார்');
INSERT INTO COMMON.DISTRICTS (districtId, siDistrictName, enDistrictName, taDistrictName)  VALUES(43, 'වව්නියාව', 'Vavuniya', 'வவுனியா');
INSERT INTO COMMON.DISTRICTS (districtId, siDistrictName, enDistrictName, taDistrictName)  VALUES(44, 'මුලතිව්', 'Mullaitivu', 'முள்ளைத்தீவு');
INSERT INTO COMMON.DISTRICTS (districtId, siDistrictName, enDistrictName, taDistrictName)  VALUES(45, 'කිලිනොච්චි', 'Kilinochchi', 'கிளி‎‎நொச்சி');
INSERT INTO COMMON.DISTRICTS (districtId, siDistrictName, enDistrictName, taDistrictName)  VALUES(51, 'මඩකලපුව', 'Batticaloa', 'மட்டக்களப்பு');
INSERT INTO COMMON.DISTRICTS (districtId, siDistrictName, enDistrictName, taDistrictName)  VALUES(52, 'අම්පාර', 'Ampara', 'அம்பா‍‍றை');
INSERT INTO COMMON.DISTRICTS (districtId, siDistrictName, enDistrictName, taDistrictName)  VALUES(53, 'ත්‍රිකුණාමලය', 'Trincomalee', 'திரு‍கோண‍மலை');
INSERT INTO COMMON.DISTRICTS (districtId, siDistrictName, enDistrictName, taDistrictName)  VALUES(61, 'කුරුණෑගල', 'Kurunegala', 'குருணாக‍லை');
INSERT INTO COMMON.DISTRICTS (districtId, siDistrictName, enDistrictName, taDistrictName)  VALUES(62, 'පුත්තලම', 'Puttalam', 'புத்தளம்');
INSERT INTO COMMON.DISTRICTS (districtId, siDistrictName, enDistrictName, taDistrictName)  VALUES(71, 'අනුරාධපුරය', 'Anuradhapura', 'அநுராதபுரம்');
INSERT INTO COMMON.DISTRICTS (districtId, siDistrictName, enDistrictName, taDistrictName)  VALUES(72, 'පොළොන්නරුව', 'Polonnaruwa', '‎பொலண்ணரு‍வை');
INSERT INTO COMMON.DISTRICTS (districtId, siDistrictName, enDistrictName, taDistrictName)  VALUES(81, 'බදුල්ල', 'Badulla', 'பது‍ளை');
INSERT INTO COMMON.DISTRICTS (districtId, siDistrictName, enDistrictName, taDistrictName)  VALUES(82, 'මොනරාගල', 'Moneragala', '‎மொணராக‍லை');
INSERT INTO COMMON.DISTRICTS (districtId, siDistrictName, enDistrictName, taDistrictName)  VALUES(91, 'රත්නපුර', 'Ratnapura', 'இரத்திணபுரி');
INSERT INTO COMMON.DISTRICTS (districtId, siDistrictName, enDistrictName, taDistrictName)  VALUES(92, 'කෑගල්ල', 'Kegalle', '‍‎‍‍‍கேகா‍லை');

-- districtUKey = 1 is DistrictID 11 - Colombo
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(1, 3, 'කොළඹ', 'Colombo', 'கொழும்பு');
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(1, 6, 'කොළොන්නාව', 'Kolonnawa', 'கொ‎லொண்ணா‍வை');
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(1, 9, 'කඩුවෙළ', 'Kaduwela', 'கடு‎வெ‍லை');
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(1, 12, 'හෝමගම', 'Homagama', 'ஹோமாகம');
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(1, 15, 'හංවැල්ල', 'Hanwella', 'ஹங்‎வெல்‍லை');
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(1, 18, 'පාදුක්ක', 'Padukka', 'பாதுக்‍கை');
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(1, 21, 'මහරගම', 'Maharagama', 'மஹரகம');
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(1, 24, 'ශ්‍රී ජයවර්ධනපුර කෝට්ටෙ', 'Sri Jayawardanapura Kotte', 'ஸ்ரீ ஜயவர்தணபுர ‍கோட்‍டை');
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(1, 27, 'තිඹිරිගස්යාය', 'Thimbirigasyaya', 'திம்பிரிகஸ்யா‍யை');
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(1, 30, 'දෙහිවල -ගල්කිස්ස', 'Dehiwala-Mount Lavinia', 'தெஹிவளை-கல்கிஸ்‍ஸ');
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(1, 31, 'රත්මලාන', 'Ratmalana', 'இரத்மழா‍ணை');
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(1, 33, 'මොරටුව', 'Moratuwa', 'மொரட்டு‍வை');
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(1, 36, 'කැස්බෑව', 'Kesbewa', '‎கெஸ்‍‍பே‍வை');
-- districtUKey = 2 is DistrictID 12 - Gampaha
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(2, 3, 'මීගමුව', 'Negombo', 'நீர்கொழும்பு');
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(2, 6, 'කටාන', 'Katana', 'கடா‍ணை');
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(2, 9, 'දිවුලපිටිය', 'Divulapitiya', 'திவுலப்பிட்டிய');
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(2, 12, 'මීරිගම', 'Mirigama', 'மீரிகமை');
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(2, 15, 'මිනුවන්ගොඩ', 'Minuwangoda', 'மிணுவாங்‎‎கொ‍டை');
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(2, 18, 'වත්තල', 'Wattala', 'வத்தளை');
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(2, 21, 'ජා-ඇල', 'Ja-Ela', 'ஜா-எல');
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(2, 24, 'ගම්පහ', 'Gampaha', 'கம்பஹ');
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(2, 27, 'අත්තනගල්ල', 'Attanagalla', 'அத்தணகல்‍லை');
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(2, 30, 'දොම්පෙ', 'Dompe', '‎தொம்‍பே');
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(2, 33, 'මහර', 'Mahara', 'மஹற');
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(2, 36, 'කැළණිය', 'Kelaniya', 'களணி');
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(2, 39, 'බියගම', 'Biyagama', 'பியக‍மை');
-- districtUKey = 3 is DistrictID 13 - Kalutara
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(3, 3, 'පානදුර', 'Panadura', 'பாணத்து‍றை');
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(3, 6, 'බණ්ඩාරගම', 'Bandaragama', 'பண்டாரக‍மை');
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(3, 9, 'හොරණ', 'Horana', '‎ஹொற‍ணை');
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(3, 10, 'ඉංගිරිය', 'Ingiriya', 'இங்கிரிய');
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(3, 12, 'බුලත්සිංහල', 'Bulathsinhala', 'புழத்சிங்கள');
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(3, 15, 'මදුරාවල', 'Madurawela', 'மதுரா‎வெள');
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(3, 18, 'මිල්ලනිය', 'Millaniya', 'மில்லணிய');
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(3, 21, 'කළුතර', 'Kalutara', 'களுத்து‍றை');
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(3, 24, 'බේරුවල', 'Beruwala', '‍‍பேறுவ‍ளை');
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(3, 27, 'දොඩම්ගොඩ', 'Dodangoda', '‎தொடந்தூவை');
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(3, 30, 'මතුගම', 'Mathugama', 'மத்துக‍மை');
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(3, 33, 'අගලවත්ත', 'Agalawatta', 'அகளவத்‍தை');
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(3, 36, 'පාලින්ද නුවර', 'Palindanuwara', 'பாலிந்தநுவர');
INSERT INTO COMMON.DS_DIVISIONS(districtUKey, divisionId, siDivisionName, enDivisionName, taDivisionName) VALUES(3, 39, 'වලල්ලාවිට', 'Walallavita', 'வளள்ளாவிட்ட');

-- Birth Death Declaration Divisions
INSERT INTO CRS.BD_DIVISIONS (DSDIVISIONUKEY, DIVISIONID, SIDIVISIONNAME, ENDIVISIONNAME, TADIVISIONNAME) VALUES(1, 1, 'කොළඹ කොටුව (Medical)', 'Colombo Fort (Medical)', 'கொலோம்போ போர');
INSERT INTO CRS.BD_DIVISIONS (DSDIVISIONUKEY, DIVISIONID, SIDIVISIONNAME, ENDIVISIONNAME, TADIVISIONNAME) VALUES(1, 2, 'කොම්පන්න වීදීය (Medical)', 'Slave Island (Medical)', 'ச்லவே இச்லாந்து');
INSERT INTO CRS.BD_DIVISIONS (DSDIVISIONUKEY, DIVISIONID, SIDIVISIONNAME, ENDIVISIONNAME, TADIVISIONNAME) VALUES(1, 3, 'අලුත් කඩේ (Medical)', 'New Bazzar (Medical)', 'அழுத் கடி');
INSERT INTO CRS.BD_DIVISIONS (DSDIVISIONUKEY, DIVISIONID, SIDIVISIONNAME, ENDIVISIONNAME, TADIVISIONNAME) VALUES(2, 8, 'කොලොන්නාව (Medical)', 'Kolonnawa (Medical)', 'கொலோம்போ போர்ட');
INSERT INTO CRS.BD_DIVISIONS (DSDIVISIONUKEY, DIVISIONID, SIDIVISIONNAME, ENDIVISIONNAME, TADIVISIONNAME) VALUES(2, 9, 'අම්බතලෙන් පහල', 'Ambathalen Pahala', 'ச்லவே இச்லாந்து');
INSERT INTO CRS.BD_DIVISIONS (DSDIVISIONUKEY, DIVISIONID, SIDIVISIONNAME, ENDIVISIONNAME, TADIVISIONNAME) VALUES(2, 10, 'අංගොඩ', 'Angoda', 'அழுத் கடி (MEDICAL)');
INSERT INTO CRS.BD_DIVISIONS (DSDIVISIONUKEY, DIVISIONID, SIDIVISIONNAME, ENDIVISIONNAME, TADIVISIONNAME) VALUES(3, 12, 'රණාල  / නවගමුව', 'Ranala / Nawagamuwa', 'கொலோம்போ போர்');
INSERT INTO CRS.BD_DIVISIONS (DSDIVISIONUKEY, DIVISIONID, SIDIVISIONNAME, ENDIVISIONNAME, TADIVISIONNAME) VALUES(3, 13, 'බත්තරමුල්ල', 'Battaramulla', 'ச்லவே இச்லாந்து');

INSERT INTO CRS.BD_DIVISIONS (DSDIVISIONUKEY, DIVISIONID, SIDIVISIONNAME, ENDIVISIONNAME, TADIVISIONNAME) VALUES(14, 1, 'මීගමුව රෝහල(Medical)', 'Negombo Hospital (Medical)', 'கொலோம்போ போர்ட');
INSERT INTO CRS.BD_DIVISIONS (DSDIVISIONUKEY, DIVISIONID, SIDIVISIONNAME, ENDIVISIONNAME, TADIVISIONNAME) VALUES(14, 2, 'මීගමුව නගරය', 'Negombo Town', 'ச்லவே இச்லாந்து');
INSERT INTO CRS.BD_DIVISIONS (DSDIVISIONUKEY, DIVISIONID, SIDIVISIONNAME, ENDIVISIONNAME, TADIVISIONNAME) VALUES(14, 3, 'තලාහේන', 'Talahena', 'அழுத் கடி');
INSERT INTO CRS.BD_DIVISIONS (DSDIVISIONUKEY, DIVISIONID, SIDIVISIONNAME, ENDIVISIONNAME, TADIVISIONNAME) VALUES(15, 5, 'ඔටාර බටහිර', 'Otara West', 'கொலோம்போ போர்ட');
INSERT INTO CRS.BD_DIVISIONS (DSDIVISIONUKEY, DIVISIONID, SIDIVISIONNAME, ENDIVISIONNAME, TADIVISIONNAME) VALUES(15, 3, 'ආඬිඅම්බලම', 'Andiambalama', 'ச்லவே இச்லாந்து');
INSERT INTO CRS.BD_DIVISIONS (DSDIVISIONUKEY, DIVISIONID, SIDIVISIONNAME, ENDIVISIONNAME, TADIVISIONNAME) VALUES(15, 7, 'කටුනායක සීදුව', 'Katunayaka-Seeduwa', 'அழுத் கடி');
INSERT INTO CRS.BD_DIVISIONS (DSDIVISIONUKEY, DIVISIONID, SIDIVISIONNAME, ENDIVISIONNAME, TADIVISIONNAME) VALUES(16, 9, 'ඔටාර  නැගෙනහිර', 'Otara East', 'கொலோம்போ போர்ட');
INSERT INTO CRS.BD_DIVISIONS (DSDIVISIONUKEY, DIVISIONID, SIDIVISIONNAME, ENDIVISIONNAME, TADIVISIONNAME) VALUES(16, 10, 'යටිගහ  උතුර', 'Yatigaha North', 'ச்லவே இச்லாந்து');
INSERT INTO CRS.BD_DIVISIONS (DSDIVISIONUKEY, DIVISIONID, SIDIVISIONNAME, ENDIVISIONNAME, TADIVISIONNAME) VALUES(16, 11, 'ගොඩකහ', 'Godakaha', 'அழுத் கடி');

INSERT INTO CRS.BD_DIVISIONS (DSDIVISIONUKEY, DIVISIONID, SIDIVISIONNAME, ENDIVISIONNAME, TADIVISIONNAME) VALUES(27, 1, 'පානදුර  (Medical)', 'Panadura (Medical)', 'கொலோம்போ போர்ட');
INSERT INTO CRS.BD_DIVISIONS (DSDIVISIONUKEY, DIVISIONID, SIDIVISIONNAME, ENDIVISIONNAME, TADIVISIONNAME) VALUES(27, 2, 'වාද්දුව', 'Wadduwa', 'ச்லவே இச்லாந்து');
INSERT INTO CRS.BD_DIVISIONS (DSDIVISIONUKEY, DIVISIONID, SIDIVISIONNAME, ENDIVISIONNAME, TADIVISIONNAME) VALUES(27, 3, 'ගොරකපොල', 'Gorakapola', 'அழுத் கடி');
INSERT INTO CRS.BD_DIVISIONS (DSDIVISIONUKEY, DIVISIONID, SIDIVISIONNAME, ENDIVISIONNAME, TADIVISIONNAME) VALUES(28, 5, 'බණ්ඩාරගම ', 'Bandaragama', 'கொலோம்போ போர்ட');
INSERT INTO CRS.BD_DIVISIONS (DSDIVISIONUKEY, DIVISIONID, SIDIVISIONNAME, ENDIVISIONNAME, TADIVISIONNAME) VALUES(28, 3, 'රයිගම', 'Raigama', 'ச்லவே இச்லாந்து');
INSERT INTO CRS.BD_DIVISIONS (DSDIVISIONUKEY, DIVISIONID, SIDIVISIONNAME, ENDIVISIONNAME, TADIVISIONNAME) VALUES(29, 0, 'ප්‍රාදේශීය ලේකම් කොට්ටාශය', 'Divisional Secretarial', 'கொலோம்போ போர்ட');
INSERT INTO CRS.BD_DIVISIONS (DSDIVISIONUKEY, DIVISIONID, SIDIVISIONNAME, ENDIVISIONNAME, TADIVISIONNAME) VALUES(29, 7, 'හොරණ නගරය (Medical)', 'Horana Town (Medical)', 'ச்லவே இச்லாந்து');
INSERT INTO CRS.BD_DIVISIONS (DSDIVISIONUKEY, DIVISIONID, SIDIVISIONNAME, ENDIVISIONNAME, TADIVISIONNAME) VALUES(29, 8, 'කුඹුක', 'Kumbuka', 'அழுத் கடி');

INSERT INTO COMMON.ROLES (ROLEID, NAME) VALUES('ADMIN',  'System Administrator');
INSERT INTO COMMON.ROLES (ROLEID, NAME) VALUES('RG',  'Registrar General');
INSERT INTO COMMON.ROLES (ROLEID, NAME) VALUES('ARG', 'Assistant Registrar General / Provincial Registrar');
INSERT INTO COMMON.ROLES (ROLEID, NAME) VALUES('DR',  'Additional Registrar General / District Registrar');
INSERT INTO COMMON.ROLES (ROLEID, NAME) VALUES('ADR', 'Additional District Registrar');
INSERT INTO COMMON.ROLES (ROLEID, NAME) VALUES('DEO', 'Data Entry Operator / Clerk');

INSERT INTO COMMON.USERS (USERID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE, PREFDISTRICTUKEY, PREFDSDIVISIONUKEY) VALUES('admin', '105', 'System Administrator', 'W6ph5Mm5Pz8GgiULbPgzG37mj9g=', 'SI', 1, 1);
INSERT INTO COMMON.USERS (USERID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE, PREFDISTRICTUKEY, PREFDSDIVISIONUKEY) VALUES('rg', '104', 'Registrar General Sri Lanka', 'W6ph5Mm5Pz8GgiULbPgzG37mj9g=', 'SI', 1, 1);
INSERT INTO COMMON.USERS (USERID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE, PREFDISTRICTUKEY, PREFDSDIVISIONUKEY) VALUES('arg-western', '103', 'Western Province Registrar', 'W6ph5Mm5Pz8GgiULbPgzG37mj9g=', 'SI', 1, 1);

INSERT INTO COMMON.USERS (USERID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE) VALUES('dr-colombo', '301', 'District Registrar - Colombo', 'W6ph5Mm5Pz8GgiULbPgzG37mj9g=', 'SI');
INSERT INTO COMMON.USERS (USERID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE) VALUES('dr-gampaha', '302', 'District Registrar - Gampaha', 'W6ph5Mm5Pz8GgiULbPgzG37mj9g=', 'SI');
INSERT INTO COMMON.USERS (USERID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE) VALUES('dr-kalutara', '302', 'District Registrar - Kalutara', 'W6ph5Mm5Pz8GgiULbPgzG37mj9g=', 'SI');

INSERT INTO COMMON.USERS (USERID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE) VALUES('adr-colombo-colombo', '303', 'ADR - Colombo/Colombo', 'W6ph5Mm5Pz8GgiULbPgzG37mj9g=', 'SI');
INSERT INTO COMMON.USERS (USERID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE) VALUES('adr-gampaha-negambo', '306', 'ADR - Gampaha/Negambo', 'W6ph5Mm5Pz8GgiULbPgzG37mj9g=', 'SI');
INSERT INTO COMMON.USERS (USERID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE) VALUES('adr-kalutara-panadura', '309', 'ADR  - Kalutara/Panadura', 'W6ph5Mm5Pz8GgiULbPgzG37mj9g=', 'SI');

INSERT INTO COMMON.USERS (USERID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE) VALUES('deo-colombo-colombo', '103', 'Data Entry Operator Colombo/Colombo', 'W6ph5Mm5Pz8GgiULbPgzG37mj9g=', 'SI');
INSERT INTO COMMON.USERS (USERID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE) VALUES('deo-gampaha-negambo', '103', 'Data Entry Operator Gampaha/Negambo', 'W6ph5Mm5Pz8GgiULbPgzG37mj9g=', 'SI');
INSERT INTO COMMON.USERS (USERID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE) VALUES('deo-kalutara-panadura', '103', 'Data Entry Operator Kalutara/Panadura', 'W6ph5Mm5Pz8GgiULbPgzG37mj9g=', 'SI');

INSERT INTO COMMON.USERS (USERID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE) VALUES('ashoka', '201', 'Ashoka Ekanayake - ADR Colombo / Colombo Fort (Medical)', 'XJRyDE28JvEY2IGylnz+w5CnnTA=', 'SI');
INSERT INTO COMMON.USERS (USERID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE) VALUES('asankha', '202', 'Asankha Perera - ADR Colombo / Colombo Fort (Medical)', 'JZiuu/n4ImMnYfxJ+ttGR9LyYmo=', 'SI');
INSERT INTO COMMON.USERS (USERID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE) VALUES('duminda', '203', 'Duminda - ADR Colombo / Colombo Fort (Medical)', '8OYuek4Vs7ebel1Trtm4sy8tB8w=', 'SI');
INSERT INTO COMMON.USERS (USERID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE) VALUES('indunil', '204', 'Indunil - ADR Colombo / Colombo Fort (Medical)', 'JZEs/79kaLxs0n4DOER6fN1vKLY=', 'SI');
INSERT INTO COMMON.USERS (USERID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE) VALUES('amith', '205', 'Amith - ADR Colombo / Colombo Fort (Medical)', 'RI/xsIJu3GrYsfWr2sLPf88I+Ks=', 'SI');
INSERT INTO COMMON.USERS (USERID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE) VALUES('chathuranga', '206', 'Chathuranga - ADR Colombo / Colombo Fort (Medical)', 'hEFeb6UFV1Kxst3H0N9p/Ics/dI=', 'SI');

INSERT INTO COMMON.USER_ROLES VALUES('rg', 'RG');
INSERT INTO COMMON.USER_ROLES VALUES('arg-western', 'ARG');
INSERT INTO COMMON.USER_ROLES VALUES('dr-colombo', 'DR');
INSERT INTO COMMON.USER_ROLES VALUES('dr-gampaha', 'DR');
INSERT INTO COMMON.USER_ROLES VALUES('dr-kalutara', 'DR');
INSERT INTO COMMON.USER_ROLES VALUES('adr-colombo-colombo', 'ADR');
INSERT INTO COMMON.USER_ROLES VALUES('adr-gampaha-negambo', 'ADR');
INSERT INTO COMMON.USER_ROLES VALUES('adr-kalutara-panadura', 'ADR');
INSERT INTO COMMON.USER_ROLES VALUES('deo-colombo-colombo', 'DEO');
INSERT INTO COMMON.USER_ROLES VALUES('deo-gampaha-negambo', 'DEO');
INSERT INTO COMMON.USER_ROLES VALUES('deo-kalutara-panadura', 'DEO');

INSERT INTO COMMON.USER_ROLES VALUES('ashoka', 'ADR');
INSERT INTO COMMON.USER_ROLES VALUES('asankha', 'ADR');
INSERT INTO COMMON.USER_ROLES VALUES('duminda', 'ADR');
INSERT INTO COMMON.USER_ROLES VALUES('indunil', 'ADR');
INSERT INTO COMMON.USER_ROLES VALUES('amith', 'ADR');
INSERT INTO COMMON.USER_ROLES VALUES('chathuranga', 'ADR');

-- ARG western province has access to all districts
INSERT INTO COMMON.USER_DISTRICTS VALUES('arg-western', 1);
INSERT INTO COMMON.USER_DISTRICTS VALUES('arg-western', 2);
INSERT INTO COMMON.USER_DISTRICTS VALUES('arg-western', 3);
-- DR of Colombo district has access only to Colombo district, similarly other DRs for Gampaha and Kalutara
INSERT INTO COMMON.USER_DISTRICTS VALUES('dr-colombo', 1);
INSERT INTO COMMON.USER_DISTRICTS VALUES('dr-gampaha', 2);
INSERT INTO COMMON.USER_DISTRICTS VALUES('dr-kalutara', 3);
-- ADR Colombo/Colombo has access to Colombo district, similarly other ADRs and DEOs..
INSERT INTO COMMON.USER_DISTRICTS VALUES('adr-colombo-colombo', 1);
INSERT INTO COMMON.USER_DISTRICTS VALUES('adr-gampaha-negambo', 2);
INSERT INTO COMMON.USER_DISTRICTS VALUES('adr-kalutara-panadura', 3);
INSERT INTO COMMON.USER_DISTRICTS VALUES('deo-colombo-colombo', 1);
INSERT INTO COMMON.USER_DISTRICTS VALUES('deo-gampaha-negambo', 2);
INSERT INTO COMMON.USER_DISTRICTS VALUES('deo-kalutara-panadura', 2);

INSERT INTO COMMON.USER_DISTRICTS VALUES('ashoka', 1);
INSERT INTO COMMON.USER_DISTRICTS VALUES('asankha', 1);
INSERT INTO COMMON.USER_DISTRICTS VALUES('duminda', 1);
INSERT INTO COMMON.USER_DISTRICTS VALUES('indunil', 1);
INSERT INTO COMMON.USER_DISTRICTS VALUES('amith', 1);
INSERT INTO COMMON.USER_DISTRICTS VALUES('chathuranga', 1);

-- ADR Colombo/Colombo has access to Colombo district/Colombo DS Division, similarly other ADRs and DEOs..
INSERT INTO COMMON.USER_DSDIVISIONS VALUES('adr-colombo-colombo', 1);
INSERT INTO COMMON.USER_DSDIVISIONS VALUES('adr-gampaha-negambo', 14);
INSERT INTO COMMON.USER_DSDIVISIONS VALUES('adr-kalutara-panadura', 27);
INSERT INTO COMMON.USER_DSDIVISIONS VALUES('deo-colombo-colombo', 1);
INSERT INTO COMMON.USER_DSDIVISIONS VALUES('deo-gampaha-negambo', 14);
INSERT INTO COMMON.USER_DSDIVISIONS VALUES('deo-kalutara-panadura', 27);

INSERT INTO COMMON.USER_DSDIVISIONS VALUES('ashoka', 1);
INSERT INTO COMMON.USER_DSDIVISIONS VALUES('asankha', 1);
INSERT INTO COMMON.USER_DSDIVISIONS VALUES('duminda', 1);
INSERT INTO COMMON.USER_DSDIVISIONS VALUES('indunil', 1);
INSERT INTO COMMON.USER_DSDIVISIONS VALUES('amith', 1);
INSERT INTO COMMON.USER_DSDIVISIONS VALUES('chathuranga', 1);


INSERT INTO CRS.BIRTH_REGISTER (bdDivisionUKey, bdfSerialNo, childFullNameEnglish, childFullNameOfficialLang, dateOfBirth, dateOfRegistration, childGender, status, informantType, informantName, informantAddress, informantSignDate, notifyingAuthorityPIN, notifyingAuthorityName, notifyingAuthorityAddress, notifyingAuthoritySignDate)
 values (1, 'A1000', 'A1000 Baby name in English', 'A1000 බබාගේ නම සිංහලෙන්', '2010-02-26', '2010-03-01', 0, 1, 1,'Informant name for A1000', 'Informant address for A1000', '2010-03-01', '1222233453', 'Notifying Authority 1 Name', 'Notifying Authority 1 Address', '2010-03-01');
INSERT INTO CRS.BIRTH_REGISTER (bdDivisionUKey, bdfSerialNo, childFullNameEnglish, childFullNameOfficialLang, dateOfBirth, dateOfRegistration, childGender, status, informantType, informantName, informantAddress, informantSignDate, notifyingAuthorityPIN, notifyingAuthorityName, notifyingAuthorityAddress, notifyingAuthoritySignDate)
 values (1, 'A1001', 'A1001 Baby name in English', 'A1001 බබාගේ නම සිංහලෙන්', '2010-02-26', '2010-03-01', 1, 1, 0,'Informant name for A1001', 'Informant address for A1001', '2010-03-01', '1222233453', 'Notifying Authority 1 Name', 'Notifying Authority 1 Address', '2010-03-01');
INSERT INTO CRS.BIRTH_REGISTER (bdDivisionUKey, bdfSerialNo, childFullNameEnglish, childFullNameOfficialLang, dateOfBirth, dateOfRegistration, childGender, status, informantType, informantName, informantAddress, informantSignDate, notifyingAuthorityPIN, notifyingAuthorityName, notifyingAuthorityAddress, notifyingAuthoritySignDate)
 values (1, 'A1002', 'A1002 Baby name in English', 'A1002 බබාගේ නම සිංහලෙන්', '2010-02-26', '2010-03-01', 0, 5, 1,'Informant name for A1002', 'Informant address for A1002', '2010-03-01', '1222233453', 'Notifying Authority 1 Name', 'Notifying Authority 1 Address', '2010-03-01');
INSERT INTO CRS.BIRTH_REGISTER (bdDivisionUKey, bdfSerialNo, childFullNameEnglish, childFullNameOfficialLang, dateOfBirth, dateOfRegistration, childGender, status, informantType, informantName, informantAddress, informantSignDate, notifyingAuthorityPIN, notifyingAuthorityName, notifyingAuthorityAddress, notifyingAuthoritySignDate)
 values (1, 'A1003', 'A1003 Baby name in English', 'A1003 බබාගේ නම සිංහලෙන්', '2010-02-26', '2010-03-01', 1, 2, 0,'Informant name for A1003', 'Informant address for A1003', '2010-03-01', '1222233453', 'Notifying Authority 1 Name', 'Notifying Authority 1 Address', '2010-03-01');
INSERT INTO CRS.BIRTH_REGISTER (bdDivisionUKey, bdfSerialNo, childFullNameEnglish, childFullNameOfficialLang, dateOfBirth, dateOfRegistration, childGender, status, informantType, informantName, informantAddress, informantSignDate, notifyingAuthorityPIN, notifyingAuthorityName, notifyingAuthorityAddress, notifyingAuthoritySignDate)
 values (1, 'A1004', 'A1004 Baby name in English', 'A1004 බබාගේ නම සිංහලෙන්', '2010-02-26', '2010-03-01', 0, 5, 1,'Informant name for A1004', 'Informant address for A1004', '2010-03-01', '1222233453', 'Notifying Authority 1 Name', 'Notifying Authority 1 Address', '2010-03-01');
INSERT INTO CRS.BIRTH_REGISTER (bdDivisionUKey, bdfSerialNo, childFullNameEnglish, childFullNameOfficialLang, dateOfBirth, dateOfRegistration, childGender, status, informantType, informantName, informantAddress, informantSignDate, notifyingAuthorityPIN, notifyingAuthorityName, notifyingAuthorityAddress, notifyingAuthoritySignDate)
 values (1, 'A1005', 'A1005 Baby name in English', 'A1005 බබාගේ නම සිංහලෙන්', '2010-02-26', '2010-03-01', 1, 5, 0,'Informant name for A1005', 'Informant address for A1005', '2010-03-01', '1222233453', 'Notifying Authority 1 Name', 'Notifying Authority 1 Address', '2010-03-01');
INSERT INTO CRS.BIRTH_REGISTER (bdDivisionUKey, bdfSerialNo, childFullNameEnglish, childFullNameOfficialLang, dateOfBirth, dateOfRegistration, childGender, status, informantType, informantName, informantAddress, informantSignDate, notifyingAuthorityPIN, notifyingAuthorityName, notifyingAuthorityAddress, notifyingAuthoritySignDate)
 values (1, 'A1006', 'A1006 Baby name in English', 'A1006 බබාගේ නම සිංහලෙන්', '2010-02-26', '2010-03-01', 0, 5, 1,'Informant name for A1006', 'Informant address for A1006', '2010-03-01', '1222233453', 'Notifying Authority 1 Name', 'Notifying Authority 1 Address', '2010-03-01');
INSERT INTO CRS.BIRTH_REGISTER (bdDivisionUKey, bdfSerialNo, childFullNameEnglish, childFullNameOfficialLang, dateOfBirth, dateOfRegistration, childGender, status, informantType, informantName, informantAddress, informantSignDate, notifyingAuthorityPIN, notifyingAuthorityName, notifyingAuthorityAddress, notifyingAuthoritySignDate)
 values (1, 'A1007', 'A1007 Baby name in English', 'A1007 බබාගේ නම සිංහලෙන්', '2010-02-26', '2010-03-01', 1, 2, 0,'Informant name for A1007', 'Informant address for A1007', '2010-03-01', '1222233453', 'Notifying Authority 1 Name', 'Notifying Authority 1 Address', '2010-03-01');
INSERT INTO CRS.BIRTH_REGISTER (bdDivisionUKey, bdfSerialNo, childFullNameEnglish, childFullNameOfficialLang, dateOfBirth, dateOfRegistration, childGender, status, informantType, informantName, informantAddress, informantSignDate, notifyingAuthorityPIN, notifyingAuthorityName, notifyingAuthorityAddress, notifyingAuthoritySignDate)
 values (1, 'A1008', 'A1008 Baby name in English', 'A1008 බබාගේ නම සිංහලෙන්', '2010-02-26', '2010-03-01', 0, 2, 1,'Informant name for A1008', 'Informant address for A1008', '2010-03-01', '1222233453', 'Notifying Authority 1 Name', 'Notifying Authority 1 Address', '2010-03-01');
INSERT INTO CRS.BIRTH_REGISTER (bdDivisionUKey, bdfSerialNo, childFullNameEnglish, childFullNameOfficialLang, dateOfBirth, dateOfRegistration, childGender, status, informantType, informantName, informantAddress, informantSignDate, notifyingAuthorityPIN, notifyingAuthorityName, notifyingAuthorityAddress, notifyingAuthoritySignDate)
 values (1, 'A1009', 'A1009 Baby name in English', 'A1009 බබාගේ නම සිංහලෙන්', '2010-02-26', '2010-03-01', 1, 1, 0,'Informant name for A1009', 'Informant address for A1009', '2010-03-01', '1222233453', 'Notifying Authority 1 Name', 'Notifying Authority 1 Address', '2010-03-01');

INSERT INTO CRS.BIRTH_REGISTER (bdDivisionUKey, bdfSerialNo, childFullNameEnglish, childFullNameOfficialLang, dateOfBirth, dateOfRegistration, childGender, status, informantType, informantName, informantAddress, informantSignDate, notifyingAuthorityPIN, notifyingAuthorityName, notifyingAuthorityAddress, notifyingAuthoritySignDate)
 values (1, 'A1010', 'A1010 Baby name in English', 'A1010 බබාගේ නම සිංහලෙන්', '2010-02-26', '2010-03-01', 0, 1, 1,'Informant name for A1010', 'Informant address for A1010', '2010-03-01', '1222233453', 'Notifying Authority 1 Name', 'Notifying Authority 1 Address', '2010-03-01');
INSERT INTO CRS.BIRTH_REGISTER (bdDivisionUKey, bdfSerialNo, childFullNameEnglish, childFullNameOfficialLang, dateOfBirth, dateOfRegistration, childGender, status, informantType, informantName, informantAddress, informantSignDate, notifyingAuthorityPIN, notifyingAuthorityName, notifyingAuthorityAddress, notifyingAuthoritySignDate)
 values (1, 'A1011', 'A1011 Baby name in English', 'A1011 බබාගේ නම සිංහලෙන්', '2010-02-27', '2010-03-01', 1, 1, 0,'Informant name for A1011', 'Informant address for A1011', '2010-03-01', '1222233453', 'Notifying Authority 1 Name', 'Notifying Authority 1 Address', '2010-03-01');
INSERT INTO CRS.BIRTH_REGISTER (bdDivisionUKey, bdfSerialNo, childFullNameEnglish, childFullNameOfficialLang, dateOfBirth, dateOfRegistration, childGender, status, informantType, informantName, informantAddress, informantSignDate, notifyingAuthorityPIN, notifyingAuthorityName, notifyingAuthorityAddress, notifyingAuthoritySignDate)
 values (1, 'A1012', 'A1012 Baby name in English', 'A1012 බබාගේ නම සිංහලෙන්', '2010-02-27', '2010-03-01', 0, 5, 1,'Informant name for A1012', 'Informant address for A1012', '2010-03-01', '1222233453', 'Notifying Authority 1 Name', 'Notifying Authority 1 Address', '2010-03-01');
INSERT INTO CRS.BIRTH_REGISTER (bdDivisionUKey, bdfSerialNo, childFullNameEnglish, childFullNameOfficialLang, dateOfBirth, dateOfRegistration, childGender, status, informantType, informantName, informantAddress, informantSignDate, notifyingAuthorityPIN, notifyingAuthorityName, notifyingAuthorityAddress, notifyingAuthoritySignDate)
 values (1, 'A1013', 'A1013 Baby name in English', 'A1013 බබාගේ නම සිංහලෙන්', '2010-02-27', '2010-03-01', 1, 2, 0,'Informant name for A1013', 'Informant address for A1013', '2010-03-01', '1222233453', 'Notifying Authority 1 Name', 'Notifying Authority 1 Address', '2010-03-01');
INSERT INTO CRS.BIRTH_REGISTER (bdDivisionUKey, bdfSerialNo, childFullNameEnglish, childFullNameOfficialLang, dateOfBirth, dateOfRegistration, childGender, status, informantType, informantName, informantAddress, informantSignDate, notifyingAuthorityPIN, notifyingAuthorityName, notifyingAuthorityAddress, notifyingAuthoritySignDate)
 values (1, 'A1014', 'A1014 Baby name in English', 'A1014 බබාගේ නම සිංහලෙන්', '2010-02-27', '2010-03-01', 0, 5, 1,'Informant name for A1014', 'Informant address for A1014', '2010-03-01', '1222233453', 'Notifying Authority 1 Name', 'Notifying Authority 1 Address', '2010-03-01');
INSERT INTO CRS.BIRTH_REGISTER (bdDivisionUKey, bdfSerialNo, childFullNameEnglish, childFullNameOfficialLang, dateOfBirth, dateOfRegistration, childGender, status, informantType, informantName, informantAddress, informantSignDate, notifyingAuthorityPIN, notifyingAuthorityName, notifyingAuthorityAddress, notifyingAuthoritySignDate)
 values (1, 'A1015', 'A1015 Baby name in English', 'A1015 බබාගේ නම සිංහලෙන්', '2010-02-27', '2010-03-01', 1, 5, 0,'Informant name for A1015', 'Informant address for A1015', '2010-03-01', '1222233453', 'Notifying Authority 1 Name', 'Notifying Authority 1 Address', '2010-03-01');
INSERT INTO CRS.BIRTH_REGISTER (bdDivisionUKey, bdfSerialNo, childFullNameEnglish, childFullNameOfficialLang, dateOfBirth, dateOfRegistration, childGender, status, informantType, informantName, informantAddress, informantSignDate, notifyingAuthorityPIN, notifyingAuthorityName, notifyingAuthorityAddress, notifyingAuthoritySignDate)
 values (1, 'A1016', 'A1016 Baby name in English', 'A1016 බබාගේ නම සිංහලෙන්', '2010-02-27', '2010-03-01', 0, 5, 1,'Informant name for A1016', 'Informant address for A1016', '2010-03-01', '1222233453', 'Notifying Authority 1 Name', 'Notifying Authority 1 Address', '2010-03-01');
INSERT INTO CRS.BIRTH_REGISTER (bdDivisionUKey, bdfSerialNo, childFullNameEnglish, childFullNameOfficialLang, dateOfBirth, dateOfRegistration, childGender, status, informantType, informantName, informantAddress, informantSignDate, notifyingAuthorityPIN, notifyingAuthorityName, notifyingAuthorityAddress, notifyingAuthoritySignDate)
 values (1, 'A1017', 'A1017 Baby name in English', 'A1017 බබාගේ නම සිංහලෙන්', '2010-02-27', '2010-03-01', 1, 2, 0,'Informant name for A1017', 'Informant address for A1017', '2010-03-01', '1222233453', 'Notifying Authority 1 Name', 'Notifying Authority 1 Address', '2010-03-01');
INSERT INTO CRS.BIRTH_REGISTER (bdDivisionUKey, bdfSerialNo, childFullNameEnglish, childFullNameOfficialLang, dateOfBirth, dateOfRegistration, childGender, status, informantType, informantName, informantAddress, informantSignDate, notifyingAuthorityPIN, notifyingAuthorityName, notifyingAuthorityAddress, notifyingAuthoritySignDate)
 values (1, 'A1018', 'A1018 Baby name in English', 'A1018 බබාගේ නම සිංහලෙන්', '2010-02-28', '2010-03-01', 0, 2, 1,'Informant name for A1018', 'Informant address for A1018', '2010-03-01', '1222233453', 'Notifying Authority 1 Name', 'Notifying Authority 1 Address', '2010-03-01');
INSERT INTO CRS.BIRTH_REGISTER (bdDivisionUKey, bdfSerialNo, childFullNameEnglish, childFullNameOfficialLang, dateOfBirth, dateOfRegistration, childGender, status, informantType, informantName, informantAddress, informantSignDate, notifyingAuthorityPIN, notifyingAuthorityName, notifyingAuthorityAddress, notifyingAuthoritySignDate)
 values (1, 'A1019', 'A1019 Baby name in English', 'A1019 බබාගේ නම සිංහලෙන්', '2010-02-28', '2010-03-01', 1, 1, 0,'Informant name for A1019', 'Informant address for A1019', '2010-03-01', '1222233453', 'Notifying Authority 1 Name', 'Notifying Authority 1 Address', '2010-03-01');

INSERT INTO CRS.BIRTH_REGISTER (bdDivisionUKey, bdfSerialNo, childFullNameEnglish, childFullNameOfficialLang, dateOfBirth, dateOfRegistration, childGender, status, informantType, informantName, informantAddress, informantSignDate, notifyingAuthorityPIN, notifyingAuthorityName, notifyingAuthorityAddress, notifyingAuthoritySignDate)
 values (1, 'A1020', 'A1020 Baby name in English', 'A1020 බබාගේ නම සිංහලෙන්', '2010-02-28', '2010-03-01', 0, 1, 1,'Informant name for A1020', 'Informant address for A1020', '2010-03-01', '1222233453', 'Notifying Authority 1 Name', 'Notifying Authority 1 Address', '2010-03-01');
INSERT INTO CRS.BIRTH_REGISTER (bdDivisionUKey, bdfSerialNo, childFullNameEnglish, childFullNameOfficialLang, dateOfBirth, dateOfRegistration, childGender, status, informantType, informantName, informantAddress, informantSignDate, notifyingAuthorityPIN, notifyingAuthorityName, notifyingAuthorityAddress, notifyingAuthoritySignDate)
 values (1, 'A1021', 'A1021 Baby name in English', 'A1021 බබාගේ නම සිංහලෙන්', '2010-02-28', '2010-03-01', 1, 1, 0,'Informant name for A1021', 'Informant address for A1021', '2010-03-01', '1222233453', 'Notifying Authority 1 Name', 'Notifying Authority 1 Address', '2010-03-01');
INSERT INTO CRS.BIRTH_REGISTER (bdDivisionUKey, bdfSerialNo, childFullNameEnglish, childFullNameOfficialLang, dateOfBirth, dateOfRegistration, childGender, status, informantType, informantName, informantAddress, informantSignDate, notifyingAuthorityPIN, notifyingAuthorityName, notifyingAuthorityAddress, notifyingAuthoritySignDate)
 values (1, 'A1022', 'A1022 Baby name in English', 'A1022 බබාගේ නම සිංහලෙන්', '2010-02-28', '2010-03-01', 0, 5, 1,'Informant name for A1022', 'Informant address for A1022', '2010-03-01', '1222233453', 'Notifying Authority 1 Name', 'Notifying Authority 1 Address', '2010-03-01');
INSERT INTO CRS.BIRTH_REGISTER (bdDivisionUKey, bdfSerialNo, childFullNameEnglish, childFullNameOfficialLang, dateOfBirth, dateOfRegistration, childGender, status, informantType, informantName, informantAddress, informantSignDate, notifyingAuthorityPIN, notifyingAuthorityName, notifyingAuthorityAddress, notifyingAuthoritySignDate)
 values (1, 'A1023', 'A1023 Baby name in English', 'A1023 බබාගේ නම සිංහලෙන්', '2010-02-28', '2010-03-01', 1, 2, 0,'Informant name for A1023', 'Informant address for A1023', '2010-03-01', '1222233453', 'Notifying Authority 1 Name', 'Notifying Authority 1 Address', '2010-03-01');
INSERT INTO CRS.BIRTH_REGISTER (bdDivisionUKey, bdfSerialNo, childFullNameEnglish, childFullNameOfficialLang, dateOfBirth, dateOfRegistration, childGender, status, informantType, informantName, informantAddress, informantSignDate, notifyingAuthorityPIN, notifyingAuthorityName, notifyingAuthorityAddress, notifyingAuthoritySignDate)
 values (1, 'A1024', 'A1024 Baby name in English', 'A1024 බබාගේ නම සිංහලෙන්', '2010-02-28', '2010-03-01', 0, 5, 1,'Informant name for A1024', 'Informant address for A1024', '2010-03-01', '1222233453', 'Notifying Authority 1 Name', 'Notifying Authority 1 Address', '2010-03-01');
INSERT INTO CRS.BIRTH_REGISTER (bdDivisionUKey, bdfSerialNo, childFullNameEnglish, childFullNameOfficialLang, dateOfBirth, dateOfRegistration, childGender, status, informantType, informantName, informantAddress, informantSignDate, notifyingAuthorityPIN, notifyingAuthorityName, notifyingAuthorityAddress, notifyingAuthoritySignDate)
 values (1, 'A1025', 'A1025 Baby name in English', 'A1025 බබාගේ නම සිංහලෙන්', '2010-02-28', '2010-03-02', 1, 5, 0,'Informant name for A1025', 'Informant address for A1025', '2010-03-02', '1222233453', 'Notifying Authority 1 Name', 'Notifying Authority 1 Address', '2010-03-02');
INSERT INTO CRS.BIRTH_REGISTER (bdDivisionUKey, bdfSerialNo, childFullNameEnglish, childFullNameOfficialLang, dateOfBirth, dateOfRegistration, childGender, status, informantType, informantName, informantAddress, informantSignDate, notifyingAuthorityPIN, notifyingAuthorityName, notifyingAuthorityAddress, notifyingAuthoritySignDate)
 values (1, 'A1026', 'A1026 Baby name in English', 'A1026 බබාගේ නම සිංහලෙන්', '2010-02-28', '2010-03-02', 0, 5, 1,'Informant name for A1026', 'Informant address for A1026', '2010-03-02', '1222233453', 'Notifying Authority 1 Name', 'Notifying Authority 1 Address', '2010-03-02');
INSERT INTO CRS.BIRTH_REGISTER (bdDivisionUKey, bdfSerialNo, childFullNameEnglish, childFullNameOfficialLang, dateOfBirth, dateOfRegistration, childGender, status, informantType, informantName, informantAddress, informantSignDate, notifyingAuthorityPIN, notifyingAuthorityName, notifyingAuthorityAddress, notifyingAuthoritySignDate)
 values (1, 'A1027', 'A1027 Baby name in English', 'A1027 බබාගේ නම සිංහලෙන්', '2010-02-28', '2010-03-02', 1, 2, 0,'Informant name for A1027', 'Informant address for A1027', '2010-03-02', '1222233453', 'Notifying Authority 1 Name', 'Notifying Authority 1 Address', '2010-03-02');
INSERT INTO CRS.BIRTH_REGISTER (bdDivisionUKey, bdfSerialNo, childFullNameEnglish, childFullNameOfficialLang, dateOfBirth, dateOfRegistration, childGender, status, informantType, informantName, informantAddress, informantSignDate, notifyingAuthorityPIN, notifyingAuthorityName, notifyingAuthorityAddress, notifyingAuthoritySignDate)
 values (1, 'A1028', 'A1028 Baby name in English', 'A1028 බබාගේ නම සිංහලෙන්', '2010-02-28', '2010-03-02', 0, 2, 1,'Informant name for A1028', 'Informant address for A1028', '2010-03-02', '1222233453', 'Notifying Authority 1 Name', 'Notifying Authority 1 Address', '2010-03-02');
INSERT INTO CRS.BIRTH_REGISTER (bdDivisionUKey, bdfSerialNo, childFullNameEnglish, childFullNameOfficialLang, dateOfBirth, dateOfRegistration, childGender, status, informantType, informantName, informantAddress, informantSignDate, notifyingAuthorityPIN, notifyingAuthorityName, notifyingAuthorityAddress, notifyingAuthoritySignDate)
 values (1, 'A1029', 'A1029 Baby name in English', 'A1029 බබාගේ නම සිංහලෙන්', '2010-02-28', '2010-03-02', 1, 1, 0,'Informant name for A1029', 'Informant address for A1029', '2010-03-02', '1222233453', 'Notifying Authority 1 Name', 'Notifying Authority 1 Address', '2010-03-02');
