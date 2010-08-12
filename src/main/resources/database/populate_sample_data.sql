-- Configurable Application Parameters
INSERT INTO COMMON.APP_PARAMETERS(NAME, VALUE) VALUES('rgd.password_expiry_days', '30');
INSERT INTO COMMON.APP_PARAMETERS(NAME, VALUE) VALUES('crs.birth.late_reg_days', '90');
INSERT INTO COMMON.APP_PARAMETERS(NAME, VALUE) VALUES('crs.birth.auto_confirmation_days', '28');
INSERT INTO COMMON.APP_PARAMETERS(NAME, VALUE) VALUES('crs.birth.confirmation_days_printed', '14');
INSERT INTO COMMON.APP_PARAMETERS(NAME, VALUE) VALUES('crs.birth.belated_reg_days', '365');
INSERT INTO COMMON.APP_PARAMETERS(NAME, VALUE) VALUES('crs.birth.certificate.search.record.limit', '100');
INSERT INTO COMMON.APP_PARAMETERS(NAME, VALUE) VALUES('crs.br_approval_rows_per_page', '50');
INSERT INTO COMMON.APP_PARAMETERS(NAME, VALUE) VALUES('crs.dr_rows_per_page', '50');

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

INSERT INTO COMMON.USERS (USERID, ROLEID, PIN, USERNAME, PREFLANGUAGE) VALUES('system', 'RG', 0, 'Internal System User', 'en');
INSERT INTO COMMON.USERS (USERID, ROLEID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE, PREFBDDISTRICTUKEY, PREFBDDSDIVISIONUKEY, PASSWORDEXPIRY) VALUES('admin', 'ADMIN', 100, 'System Administrator', 'W6ph5Mm5Pz8GgiULbPgzG37mj9g=', 'en', 1, 1, '2012-12-31 12:00:00');
INSERT INTO COMMON.USERS (USERID, ROLEID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE, PREFBDDISTRICTUKEY, PREFBDDSDIVISIONUKEY, PASSWORDEXPIRY) VALUES('rg', 'RG',101, 'Registrar General Sri Lanka', 'W6ph5Mm5Pz8GgiULbPgzG37mj9g=', 'en', 1, 1, '2012-12-31 12:00:00');
INSERT INTO COMMON.USERS (USERID, ROLEID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE, PREFBDDISTRICTUKEY, PREFBDDSDIVISIONUKEY, PASSWORDEXPIRY) VALUES('arg-western', 'ARG',102, 'Western Province Registrar', 'W6ph5Mm5Pz8GgiULbPgzG37mj9g=', 'si', 1, 1, '2012-12-31 12:00:00');

INSERT INTO COMMON.USERS (USERID, ROLEID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE, PASSWORDEXPIRY) VALUES('dr-colombo', 'DR', 300, 'District Registrar - Colombo', 'W6ph5Mm5Pz8GgiULbPgzG37mj9g=', 'si', '2012-12-31 12:00:00');
INSERT INTO COMMON.USERS (USERID, ROLEID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE, PASSWORDEXPIRY) VALUES('dr-gampaha', 'DR',301, 'District Registrar - Gampaha', 'W6ph5Mm5Pz8GgiULbPgzG37mj9g=', 'si', '2012-12-31 12:00:00');
INSERT INTO COMMON.USERS (USERID, ROLEID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE, PASSWORDEXPIRY) VALUES('dr-kalutara', 'DR',302, 'District Registrar - Kalutara', 'W6ph5Mm5Pz8GgiULbPgzG37mj9g=', 'si', '2012-12-31 12:00:00');

INSERT INTO COMMON.USERS (USERID, ROLEID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE, PASSWORDEXPIRY) VALUES('adr-colombo-colombo', 'ADR', 303, 'ADR - Colombo/Colombo', 'W6ph5Mm5Pz8GgiULbPgzG37mj9g=', 'si', '2012-12-31 12:00:00');
INSERT INTO COMMON.USERS (USERID, ROLEID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE, PASSWORDEXPIRY) VALUES('adr-gampaha-negambo', 'ADR', 304, 'ADR - Gampaha/Negambo', 'W6ph5Mm5Pz8GgiULbPgzG37mj9g=', 'si', '2012-12-31 12:00:00');
INSERT INTO COMMON.USERS (USERID, ROLEID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE, PASSWORDEXPIRY) VALUES('adr-kalutara-panadura', 'ADR', 305, 'ADR  - Kalutara/Panadura', 'W6ph5Mm5Pz8GgiULbPgzG37mj9g=', 'si', '2012-12-31 12:00:00');

INSERT INTO COMMON.USERS (USERID, ROLEID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE, PASSWORDEXPIRY) VALUES('deo-colombo-colombo', 'DEO', 306, 'Data Entry Operator Colombo/Colombo', 'W6ph5Mm5Pz8GgiULbPgzG37mj9g=', 'si', '2012-12-31 12:00:00');
INSERT INTO COMMON.USERS (USERID, ROLEID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE, PASSWORDEXPIRY) VALUES('deo-gampaha-negambo', 'DEO', 307, 'Data Entry Operator Gampaha/Negambo', 'W6ph5Mm5Pz8GgiULbPgzG37mj9g=', 'si', '2012-12-31 12:00:00');
INSERT INTO COMMON.USERS (USERID, ROLEID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE, PASSWORDEXPIRY) VALUES('deo-kalutara-panadura', 'DEO', 308, 'Data Entry Operator Kalutara/Panadura', 'W6ph5Mm5Pz8GgiULbPgzG37mj9g=', 'si', '2012-12-31 12:00:00');

INSERT INTO COMMON.USERS (USERID, ROLEID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE, PASSWORDEXPIRY) VALUES('ashoka', 'ADR', 201, 'Ashoka Ekanayake - ADR Colombo / Colombo Fort (Medical)', 'XJRyDE28JvEY2IGylnz+w5CnnTA=', 'si', '2012-12-31 12:00:00');
INSERT INTO COMMON.USERS (USERID, ROLEID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE, PASSWORDEXPIRY) VALUES('asankha', 'ADR', 202, 'Asankha Perera - ADR Colombo / Colombo Fort (Medical)', 'JZiuu/n4ImMnYfxJ+ttGR9LyYmo=', 'si', '2012-12-31 12:00:00');
INSERT INTO COMMON.USERS (USERID, ROLEID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE, PASSWORDEXPIRY) VALUES('tharanga', 'ADR', 202, 'Tharanga Punchihewa - ADR Colombo / Colombo Fort (Medical)', 'JZiuu/n4ImMnYfxJ+ttGR9LyYmo=', 'si', '2012-12-31 12:00:00');
INSERT INTO COMMON.USERS (USERID, ROLEID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE, PASSWORDEXPIRY) VALUES('janith', 'ADR', 202, 'Janith Widarshana - ADR Colombo / Colombo Fort (Medical)', 'JZiuu/n4ImMnYfxJ+ttGR9LyYmo=', 'si', '2012-12-31 12:00:00');
INSERT INTO COMMON.USERS (USERID, ROLEID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE, PASSWORDEXPIRY) VALUES('duminda', 'ADR', 203, 'Duminda - ADR Colombo / Colombo Fort (Medical)', '8OYuek4Vs7ebel1Trtm4sy8tB8w=', 'si', '2012-12-31 12:00:00');
INSERT INTO COMMON.USERS (USERID, ROLEID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE, PASSWORDEXPIRY) VALUES('indunil', 'ADR', 204, 'Indunil - ADR Colombo / Colombo Fort (Medical)', 'JZEs/79kaLxs0n4DOER6fN1vKLY=', 'si', '2012-12-31 12:00:00');
INSERT INTO COMMON.USERS (USERID, ROLEID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE, PASSWORDEXPIRY) VALUES('amith', 'ADR', 205, 'Amith - ADR Colombo / Colombo Fort (Medical)', 'RI/xsIJu3GrYsfWr2sLPf88I+Ks=', 'EN', '2012-12-31 12:00:00');
INSERT INTO COMMON.USERS (USERID, ROLEID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE, PASSWORDEXPIRY) VALUES('chathuranga', 'ADR', 206, 'Chathuranga - ADR Colombo / Colombo Fort (Medical)', 'hEFeb6UFV1Kxst3H0N9p/Ics/dI=', 'si', '2012-12-31 12:00:00');
INSERT INTO COMMON.USERS (USERID, ROLEID, PIN, USERNAME, PASSWORDHASH, PREFLANGUAGE, PASSWORDEXPIRY) VALUES('firstuser', 'ADR', 207, 'user to test first login', 'W6ph5Mm5Pz8GgiULbPgzG37mj9g=', 'si', '2010-07-20 12:00:00');

-- ARG western province has access to all districts for BD, and colombo district for MR
INSERT INTO COMMON.USER_BDDISTRICTS VALUES('arg-western', 1);
INSERT INTO COMMON.USER_BDDISTRICTS VALUES('arg-western', 2);
INSERT INTO COMMON.USER_BDDISTRICTS VALUES('arg-western', 3);

INSERT INTO COMMON.USER_MRDISTRICTS VALUES('arg-western', 1);

-- DR of Colombo district has access only to Colombo district, similarly other DRs for Gampaha and Kalutara. MR is similar
INSERT INTO COMMON.USER_BDDISTRICTS VALUES('dr-colombo', 1);
INSERT INTO COMMON.USER_BDDISTRICTS VALUES('dr-gampaha', 2);
INSERT INTO COMMON.USER_BDDISTRICTS VALUES('dr-kalutara', 3);

INSERT INTO COMMON.USER_MRDISTRICTS VALUES('dr-colombo', 1);
INSERT INTO COMMON.USER_MRDISTRICTS VALUES('dr-gampaha', 2);
INSERT INTO COMMON.USER_MRDISTRICTS VALUES('dr-kalutara', 3);

-- ADR Colombo/Colombo has access to Colombo district, similarly other ADRs and DEOs... MR is similar for ADRs but DEOs are not MR
INSERT INTO COMMON.USER_BDDISTRICTS VALUES('adr-colombo-colombo', 1);
INSERT INTO COMMON.USER_BDDISTRICTS VALUES('adr-gampaha-negambo', 2);
INSERT INTO COMMON.USER_BDDISTRICTS VALUES('adr-kalutara-panadura', 3);
INSERT INTO COMMON.USER_BDDISTRICTS VALUES('deo-colombo-colombo', 1);
INSERT INTO COMMON.USER_BDDISTRICTS VALUES('deo-gampaha-negambo', 2);
INSERT INTO COMMON.USER_BDDISTRICTS VALUES('deo-kalutara-panadura', 2);

INSERT INTO COMMON.USER_MRDISTRICTS VALUES('adr-colombo-colombo', 1);
INSERT INTO COMMON.USER_MRDISTRICTS VALUES('adr-gampaha-negambo', 2);
INSERT INTO COMMON.USER_MRDISTRICTS VALUES('adr-kalutara-panadura', 3);

INSERT INTO COMMON.USER_BDDISTRICTS VALUES('ashoka', 1);
INSERT INTO COMMON.USER_BDDISTRICTS VALUES('asankha', 1);
INSERT INTO COMMON.USER_BDDISTRICTS VALUES('tharanga', 1);
INSERT INTO COMMON.USER_BDDISTRICTS VALUES('janith', 1);
INSERT INTO COMMON.USER_BDDISTRICTS VALUES('duminda', 1);
INSERT INTO COMMON.USER_BDDISTRICTS VALUES('indunil', 1);
INSERT INTO COMMON.USER_BDDISTRICTS VALUES('amith', 1);
INSERT INTO COMMON.USER_BDDISTRICTS VALUES('chathuranga', 1);

INSERT INTO COMMON.USER_MRDISTRICTS VALUES('ashoka', 1);
INSERT INTO COMMON.USER_MRDISTRICTS VALUES('asankha', 1);
INSERT INTO COMMON.USER_MRDISTRICTS VALUES('tharanga', 1);
INSERT INTO COMMON.USER_MRDISTRICTS VALUES('janith', 1);
INSERT INTO COMMON.USER_MRDISTRICTS VALUES('duminda', 1);
INSERT INTO COMMON.USER_MRDISTRICTS VALUES('indunil', 1);
INSERT INTO COMMON.USER_MRDISTRICTS VALUES('amith', 1);
INSERT INTO COMMON.USER_MRDISTRICTS VALUES('chathuranga', 1);

-- ADR Colombo/Colombo has access to Colombo district/Colombo DS Division, similarly other ADRs and DEOs..
INSERT INTO COMMON.USER_BDDSDIVISIONS VALUES('adr-colombo-colombo', 1);
INSERT INTO COMMON.USER_BDDSDIVISIONS VALUES('adr-gampaha-negambo', 14);
INSERT INTO COMMON.USER_BDDSDIVISIONS VALUES('adr-kalutara-panadura', 27);
INSERT INTO COMMON.USER_BDDSDIVISIONS VALUES('deo-colombo-colombo', 1);
INSERT INTO COMMON.USER_BDDSDIVISIONS VALUES('deo-gampaha-negambo', 14);
INSERT INTO COMMON.USER_BDDSDIVISIONS VALUES('deo-kalutara-panadura', 27);

INSERT INTO COMMON.USER_BDDSDIVISIONS VALUES('ashoka', 1);
INSERT INTO COMMON.USER_BDDSDIVISIONS VALUES('asankha', 1);
INSERT INTO COMMON.USER_BDDSDIVISIONS VALUES('tharanga', 1);
INSERT INTO COMMON.USER_BDDSDIVISIONS VALUES('janith', 1);
INSERT INTO COMMON.USER_BDDSDIVISIONS VALUES('duminda', 1);
INSERT INTO COMMON.USER_BDDSDIVISIONS VALUES('indunil', 1);
INSERT INTO COMMON.USER_BDDSDIVISIONS VALUES('amith', 1);
INSERT INTO COMMON.USER_BDDSDIVISIONS VALUES('chathuranga', 1);

