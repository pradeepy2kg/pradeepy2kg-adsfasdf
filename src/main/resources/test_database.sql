CREATE TABLE APP_PARAMETERS (
    NAME      VARCHAR(25) NOT NULL,
    VALUE     VARCHAR(40),
    PRIMARY KEY(NAME)
);

INSERT INTO APP_PARAMETERS(NAME, VALUE) VALUES('crs.birth.late_reg_days', '90');

CREATE TABLE DISTRICTS ( 
    DISTRICTID       INTEGER NOT NULL,
    SIDISTRICTNAME   VARCHAR(25),
    ENDISTRICTNAME   VARCHAR(25),
    TADISTRICTNAME   VARCHAR(25),
    ACTIVE           SMALLINT WITH DEFAULT 1,
    PRIMARY KEY(DISTRICTID)
);

INSERT INTO DISTRICTS (DISTRICTID, SIDISTRICTNAME, ENDISTRICTNAME, TADISTRICTNAME) VALUES(11, 'කොළඹ', 'Colombo', 'கொழும்பு');
INSERT INTO DISTRICTS (DISTRICTID, SIDISTRICTNAME, ENDISTRICTNAME, TADISTRICTNAME) VALUES(31, 'ගාල්ල', 'Galle', 'காலி');
INSERT INTO DISTRICTS (DISTRICTID, SIDISTRICTNAME, ENDISTRICTNAME, TADISTRICTNAME) VALUES(22, 'මහනුවර', 'Kandy', 'கண்டி');

CREATE TABLE COUNTRIES (
    COUNTRYID        INTEGER NOT NULL,
    SICOUNTRYNAME    VARCHAR(25),
    ENCOUNTRYNAME    VARCHAR(25),
    TACOUNTRYNAME    VARCHAR(25),
    ACTIVE           SMALLINT WITH DEFAULT 1,
    PRIMARY KEY(COUNTRYID)
);

INSERT INTO COUNTRIES (COUNTRYID, SICOUNTRYNAME, ENCOUNTRYNAME, TACOUNTRYNAME) VALUES(1, 'ශ්‍රී ලංකාව', 'Sri Lanka', 'ஸ்ரீ லங்கா');
INSERT INTO COUNTRIES (COUNTRYID, SICOUNTRYNAME, ENCOUNTRYNAME, TACOUNTRYNAME) VALUES(2, 'ජපානය', 'Japan', 'ஜப்பான்');
INSERT INTO COUNTRIES (COUNTRYID, SICOUNTRYNAME, ENCOUNTRYNAME, TACOUNTRYNAME) VALUES(3, 'ඉන්දියාව', 'India', 'இந்திய');

CREATE TABLE RACES (
    RACEID           INTEGER NOT NULL,
    SIRACENAME       VARCHAR(25),
    ENRACENAME       VARCHAR(25),
    TARACENAME       VARCHAR(25),
    ACTIVE           SMALLINT WITH DEFAULT 1,
    PRIMARY KEY(RACEID)
);

INSERT INTO RACES (RACEID, SIRACENAME, ENRACENAME, TARACENAME) VALUES(1, 'සිංහල', 'Sinhalese', 'சின்ஹலேசே');
INSERT INTO RACES (RACEID, SIRACENAME, ENRACENAME, TARACENAME) VALUES(2, 'ශ්‍රී ලංකික දෙමල', 'Sri Lankan Tamil', 'ஸ்ரீ லங்கன் தமிழ்');
INSERT INTO RACES (RACEID, SIRACENAME, ENRACENAME, TARACENAME) VALUES(3, 'ඉන්දියානු දෙමල', 'Indian Tamil', 'இந்தியன் தமிழ்');

CREATE TABLE BD_DIVISIONS (
    DISTRICTID       INTEGER NOT NULL,
    DIVISIONID       INTEGER NOT NULL,
    SIDIVISIONNAME   VARCHAR(25),
    ENDIVISIONNAME   VARCHAR(25),
    TADIVISIONNAME   VARCHAR(25),
    ACTIVE           SMALLINT WITH DEFAULT 1,
    PRIMARY KEY(DISTRICTID, DIVISIONID),
    FOREIGN KEY (DISTRICTID) REFERENCES DISTRICTS(DISTRICTID)  
);

INSERT INTO BD_DIVISIONS (DISTRICTID, DIVISIONID, SIDIVISIONNAME, ENDIVISIONNAME, TADIVISIONNAME) VALUES(11, 1, 'කොළඹ කොටුව (Medical)', 'Colombo Fort (Medical)', 'கொலோம்போ போர்ட் (MEDICAL)');
INSERT INTO BD_DIVISIONS (DISTRICTID, DIVISIONID, SIDIVISIONNAME, ENDIVISIONNAME, TADIVISIONNAME) VALUES(11, 2, 'කොම්පන්න වීදීය (Medical)', 'Colombo Fort (Medical)', 'ச்லவே இச்லாந்து (MEDICAL)');
INSERT INTO BD_DIVISIONS (DISTRICTID, DIVISIONID, SIDIVISIONNAME, ENDIVISIONNAME, TADIVISIONNAME) VALUES(11, 3, 'අලුත් කඩේ (Medical)', 'Colombo Fort (Medical)', 'அழுத் கடி (MEDICAL)');


CREATE TABLE ROLES (
    ROLEID       VARCHAR(15) NOT NULL,
    NAME         VARCHAR(60) NOT NULL,
    PERMISSIONS  BLOB (1K),
    STATUS       SMALLINT WITH DEFAULT 1,
    PRIMARY KEY(ROLEID)
);

INSERT INTO ROLES (ROLEID, NAME) VALUES('RG', 'Registrar General');
INSERT INTO ROLES (ROLEID, NAME) VALUES('ARG', 'Additional Registrar General');
INSERT INTO ROLES (ROLEID, NAME) VALUES('DR', 'District Registrar');
INSERT INTO ROLES (ROLEID, NAME) VALUES('ADR', 'Additional District Registrar');
INSERT INTO ROLES (ROLEID, NAME) VALUES('DEO', 'Data Entry Operator / Clerk');

CREATE TABLE USERS (
    USERID              VARCHAR(15) NOT NULL,
    USERNAME            VARCHAR(30) NOT NULL,
    PIN                 VARCHAR(15),
    PASSWORDHASH        VARCHAR(56),
    PREFLANGUAGE        VARCHAR(2),
    PREFDISTRICT        INTEGER,
    PREFBDDIVISION      INTEGER,
    PREFMRDIVISION      INTEGER,
    ASSIGNEDDISTRICT    INTEGER,
    ASSIGNEDBDDIVISION  INTEGER,
    ASSIGNEDMRDIVISION  INTEGER,
    STATUS              SMALLINT WITH DEFAULT 1,
    PRIMARY KEY(USERID)
);

INSERT INTO USERS (USERID, USERNAME, PASSWORDHASH, PREFLANGUAGE, PREFDISTRICT, ASSIGNEDDISTRICT, ASSIGNEDBDDIVISION) VALUES('user', 'Sample DEO / Clerk', 'W6ph5Mm5Pz8GgiULbPgzG37mj9g=', 'SI', 11, 11, 1);
INSERT INTO USERS (USERID, USERNAME, PASSWORDHASH, PREFLANGUAGE, PREFDISTRICT, ASSIGNEDDISTRICT, ASSIGNEDBDDIVISION) VALUES('adr', 'Sample ADR user', 'W6ph5Mm5Pz8GgiULbPgzG37mj9g=', 'SI', 11, 11, 1);
INSERT INTO USERS (USERID, USERNAME, PASSWORDHASH, PREFLANGUAGE, PREFDISTRICT) VALUES('dr', 'Sample DR user', 'W6ph5Mm5Pz8GgiULbPgzG37mj9g=', 'SI', 11);
INSERT INTO USERS (USERID, USERNAME, PASSWORDHASH, PREFLANGUAGE, PREFDISTRICT) VALUES('arg', 'Sample ARG user', 'W6ph5Mm5Pz8GgiULbPgzG37mj9g=', 'SI', 11);
INSERT INTO USERS (USERID, USERNAME, PASSWORDHASH, PREFLANGUAGE, PREFDISTRICT) VALUES('rg', 'Sample RG user', 'W6ph5Mm5Pz8GgiULbPgzG37mj9g=', 'SI', 11);
INSERT INTO USERS (USERID, USERNAME, PASSWORDHASH, PREFLANGUAGE, PREFDISTRICT) VALUES('admin', 'Sample System Admin', 'W6ph5Mm5Pz8GgiULbPgzG37mj9g=', 'SI', 11);

INSERT INTO USERS (USERID, USERNAME, PASSWORDHASH, PREFLANGUAGE, PREFDISTRICT) VALUES('ashoka', 'Ashoka Ekanayake', 'XJRyDE28JvEY2IGylnz+w5CnnTA=', 'SI', 11);
INSERT INTO USERS (USERID, USERNAME, PASSWORDHASH, PREFLANGUAGE, PREFDISTRICT) VALUES('asankha', 'Asankha Perera', 'JZiuu/n4ImMnYfxJ+ttGR9LyYmo=', 'SI', 11);
INSERT INTO USERS (USERID, USERNAME, PASSWORDHASH, PREFLANGUAGE, PREFDISTRICT) VALUES('duminda', 'Duminda', '8OYuek4Vs7ebel1Trtm4sy8tB8w=', 'SI', 11);
INSERT INTO USERS (USERID, USERNAME, PASSWORDHASH, PREFLANGUAGE, PREFDISTRICT) VALUES('indunil', 'Indunil', 'JZEs/79kaLxs0n4DOER6fN1vKLY=', 'SI', 11);
INSERT INTO USERS (USERID, USERNAME, PASSWORDHASH, PREFLANGUAGE, PREFDISTRICT) VALUES('amith', 'Amith', 'RI/xsIJu3GrYsfWr2sLPf88I+Ks=', 'SI', 11);
INSERT INTO USERS (USERID, USERNAME, PASSWORDHASH, PREFLANGUAGE, PREFDISTRICT) VALUES('chathuranga', 'Chathuranga', 'hEFeb6UFV1Kxst3H0N9p/Ics/dI=', 'SI', 11);


CREATE TABLE USER_ROLES (
    USERID              VARCHAR(15) NOT NULL,
    ROLEID              VARCHAR(15) NOT NULL,
    PRIMARY KEY(USERID, ROLEID)
);

INSERT INTO USER_ROLES VALUES('user', 'DEO');
INSERT INTO USER_ROLES VALUES('adr', 'ADR');
INSERT INTO USER_ROLES VALUES('dr', 'DR');
INSERT INTO USER_ROLES VALUES('arg', 'ARG');
INSERT INTO USER_ROLES VALUES('rg', 'RG');
INSERT INTO USER_ROLES VALUES('rg', 'ADR');

INSERT INTO USER_ROLES VALUES('ashoka', 'ADR');
INSERT INTO USER_ROLES VALUES('asankha', 'ADR');
INSERT INTO USER_ROLES VALUES('duminda', 'ADR');
INSERT INTO USER_ROLES VALUES('indunil', 'ADR');
INSERT INTO USER_ROLES VALUES('amith', 'ADR');
INSERT INTO USER_ROLES VALUES('chathuranga', 'ADR');

CREATE TABLE BIRTH_REGISTER (
    id                      BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    birthDistrict           INTEGER NOT NULL,
    birthDivision           INTEGER NOT NULL,
    bdfSerialNo             VARCHAR(10) NOT NULL,
    dateOfBirth             DATE,
    dateOfRegistration      DATE,
    status                  INTEGER NOT NULL WITH DEFAULT 0,
    comments                VARCHAR(60),
    placeOfBirth            VARCHAR(60),
    childFullNameOfficialLang VARCHAR(600),
    childFullNameEnglish    VARCHAR(600),
    childGender             INTEGER NOT NULL,
    childBirthWeight        FLOAT,
    childRank               INTEGER,
    numberOfChildrenBorn    INTEGER,
    hospitalCode            VARCHAR(10),
    gnCode                  VARCHAR(10),

    fatherNICorPIN          VARCHAR(10),
    fatherPassportNo        VARCHAR(10),
    fatherCountry           VARCHAR(2),
    fatherFullName          VARCHAR(600),
    fatherDOB               DATE,
    fatherPlaceOfBirth      VARCHAR(80),
    fatherRace              INTEGER,

    motherNICorPIN          VARCHAR(10),
    motherPassportNo        VARCHAR(10),
    motherCountry           VARCHAR(2),
    motherAdmissionNoAndDate VARCHAR(20),
    motherFullName          VARCHAR(600),
    motherDOB               DATE,
    motherPlaceOfBirth      VARCHAR(80),
    motherRace              INTEGER,
    motherAgeAtBirth        INTEGER,
    motherAddress           VARCHAR(300),
    motherPhoneNo           VARCHAR(20),
    motherEmail             VARCHAR(20),

    parentsMarried          INTEGER,
    placeOfMarriage         VARCHAR(80),
    dateOfMarriage          DATE,
    motherSigned            INTEGER,
    fatherSigned            INTEGER,

    grandFatherFullName     VARCHAR(600),
    grandFatherBirthYear    INTEGER,
    grandFatherBirthPlace   VARCHAR(80),
    greatGrandFatherFullName   VARCHAR(600),
    greatGrandFatherBirthYear  INTEGER,
    greatGrandFatherBirthPlace VARCHAR(80),

    informantType           INTEGER,
    informantName           VARCHAR(600),
    informantNICorPIN       VARCHAR(10),
    informantAddress        VARCHAR(300),
    informantPhoneNo        VARCHAR(20),
    informantEmail          VARCHAR(20),
    informantSignDate       DATE,

    notifyingAuthorityPIN   VARCHAR(10),
    notifyingAuthorityName  VARCHAR(80),
    approvePIN              VARCHAR(10),
    approveDate             DATE,

    confirmationSerialNumber VARCHAR(10),
    confirmationPrinted     INTEGER,
    lastDateForConfirmation DATE,
    confirmantNICorPIN      VARCHAR(10),
    confirmantFullName      VARCHAR(600),
    confirmantSignDate      DATE,
    confirmationReceiveDate DATE,
    originalBCDateOfIssue   DATE,
    originalBCPlaceOfIssue  INTEGER,
    PRIMARY KEY(ID)
);

ALTER TABLE BIRTH_REGISTER ADD CONSTRAINT C_UNIQUE UNIQUE (birthDistrict, birthDivision, bdfSerialNo, status);

