-- Database CRS
USE CRS;

-- Clear court details.
/* We can clear court details as they do not have relationship with other tables yet. (As Adoption is not LIVE YET)*/
TRUNCATE COURTS;

-- Insert New court details.
-- Courts in Western Province
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(1,'District Court of Colombo','කොළඹ දිසා අධිකරණය','கொழும்பு மாவட்ட நீதிமன்றம்', 1);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(2,'District Court of Mt.Lavinia','ගල්කිස්ස දිසා අධිකරණය','கல்கிசை மாவட்ட நீதிமன்றம்', 1);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(3,'District Court of Kolonnawa','කොළොන්නාව දිසා අධිකරණය','கொலன்னாவ மாவட்ட நீதிமன்றம்', 1);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(4,'District Court of Gangodawila','ගංගොඩවිල දිසා අධිකරණය','கங்கொடவில மாவட்ட நீதிமன்றம்', 1);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(5,'District Court of Kaduwela','කඩුවෙල දිසා අධිකරණය','கடுவலை மாவட்ட நீதிமன்றம்', 1);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(6,'District Court of Gampaha','ගම්පහ දිසා අධිකරණය','கம்பஹா மாவட்ட நீதிமன்றம்', 1);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(7,'District Court of Mahara','මහර දිසා අධිකරණය','மஹர மாவட்ட நீதிமன்றம்', 1);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(8,'District Court of Wattala','වත්තල දිසා අධිකරණය','வத்தளை மாவட்ட நீதிமன்றம்', 1);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(9,'District Court of Pugoda','පූගොඩ දිසා අධිකරණය','பூகொட மாவட்ட நீதிமன்றம்', 1);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(10,'District Court of Attanagalla','අත්තනගල්ල දිසා අධිකරණය','அத்தனகல்ல மாவட்ட நீதிமன்றம்', 1);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(11,'District Court of Mirigama','මීරිගම දිසා අධිකරණය','மீரிகம மாவட்ட நீதிமன்றம்', 1);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(12,'District Court of Negombo','මීගමුව දිසා අධිකරණය','நீர்க்கொழும்பு மாவட்ட நீதிமன்றம்', 1);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(13,'District Court of Ja-ela','ජාඇල දිසා අධිකරණය','ஜா-எல மாவட்ட நீதிமன்றம்', 1);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(14,'District Court of Minuwangoda','මිනුවන්ගොඩ දිසා අධිකරණය','மினுவங்கொடை மாவட்ட நீதிமன்றம்', 1);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(15,'District Court of Panadura','පානදුර දිසා අධිකරණය','பாணந்துறை மாவட்ட நீதிமன்றம்', 1);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(16,'District Court of Kesbewa','කැස්බෑව දිසා අධිකරණය','கெஸ்பேவை மாவட்ட நீதிமன்றம்', 1);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(17,'District Court of Moratuwa','මොරටුව දිසා අධිකරණය','மொரட்டுவை மாவட்ட நீதிமன்றம்', 1);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(18,'District Court of Kalutara','කළුතර දිසා අධිකරණය','களுத்துறை மாவட்ட நீதிமன்றம்', 1);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(19,'District Court of Beruwala','බේරුවල දිසා අධිකරණය','பேருவலை மாவட்ட நீதிமன்றம்', 1);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(20,'District Court of Horana','හොරණ දිසා අධිකරණය','ஹொரணை மாவட்ட நீதிமன்றம்', 1);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(21,'District Court of Matugama','මතුගම දිසා අධිකරණය','மத்துகம மாவட்ட நீதிமன்றம்', 1);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(22,'District Court of Avissawella','අවිස්සාවේල්ල දිසා අධිකරණය','அவிசாவலை மாவட்ட நீதிமன்றம்', 1);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(23,'District Court of Homagama','හෝමාගම දිසා අධිකරණය','ஹோமாகம மாவட்ட நீதிமன்றம்', 1);

-- Courts in Central Province
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(24,'District Court of Kandy','නුවර දිසා අධිකරණය','கண்டி மாவட்ட நீதிமன்றம்', 2);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(25,'District Court of Galagedara','ගලගෙදර දිසා අධිකරණය','கலகெதர மாவட்ட நீதிமன்றம்', 2);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(26,'District Court of Teldeniya','තෙල්දෙණිය දිසා අධිකරණය','தெல்தெனிய மாவட்ட நீதிமன்றம்', 2);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(27,'District Court of Gampola','ගම්පොළ දිසා අධිකරණය','கம்பளை மாவட்ட நீதிமன்றம்', 2);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(28,'District Court of Nawalapitya','නාවලපිටිය දිසා අධිකරණය','நாவலபிட்டி மாவட்ட நீதிமன்றம்', 2);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(29,'District Court of Matale','මාතලේ දිසා අධිකරණය','மாத்தளை மாவட்ட நீதிமன்றம்', 2);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(30,'District Court of Naula','නාඋල දිසා අධිකරණය','நாவுல மாவட்ட நீதிமன்றம்', 2);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(31,'District Court of Dambulla','දඹුල්ල දිසා අධිකරණය','தம்புள்ள மாவட்ட நீதிமன்றம்', 2);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(32,'District Court of Nuwaraeliya','නුවරඑළිය දිසා අධිකරණය','நுவரெலியா மாவட்ட நீதிமன்றம்', 2);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(33,'District Court of Walapana','වලපනේ දිසා අධිකරණය','வலப்பனை மாவட்ட நீதிமன்றம்', 2);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(34,'District Court of Hatton','හැටන් දිසා අධිකරණය','ஹட்டன் மாவட்ட நீதிமன்றம்', 2);

-- Courts in Southern Province
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(35,'District Court of Balapitya','බලපිටිය දිසා අධිකරණය','பலபிட்டி மாவட்ட நீதிமன்றம்', 3);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(36,'District Court of Elpitiya','ඇල්පිටිය දිසා අධිකරණය','எல்பிட்டி மாவட்ட நீதிமன்றம்', 3);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(37,'District Court of Galle','ගාල්ල දිසා අධිකරණය','காலி மாவட்ட நீதிமன்றம்', 3);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(38,'District Court of Udugama','උඩුගම දිසා අධිකරණය','உடுகம மாவட்ட நீதிமன்றம்', 3);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(39,'District Court of Baddegama','බද්දේගම දිසා අධිකරණය','பத்தேகம மாவட்ட நீதிமன்றம்', 3);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(40,'District Court of Matara','මාතර දිසා අධිකරණය','மாத்தறை மாவட்ட நீதிமன்றம்', 3);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(41,'District Court of Deyyandara','දෙයියන්දර දිසා අධිකරණය','தெய்யந்தர மாவட்ட நீதிமன்றம்', 3);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(42,'District Court of Kotapola','කොටපොල දිසා අධිකරණය','கொட்டபொல மாவட்ட நீதிமன்றம்', 3);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(43,'District Court of Tangalle','තංගල්ල දිසා අධිකරණය','தங்காலை மாவட்ட நீதிமன்றம்', 3);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(44,'District Court of Agunukolapelessa','අඟුණකොලපැලැස්ස දිසා අධිකරණය','அகுனகொலபெலெஸ்ச மாவட்ட நீதிமன்றம்', 3);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(45,'District Court of Walassmulla','වලස්මුල්ල දිසා අධිකරණය','வலஸ்முல்ல மாவட்ட நீதிமன்றம்', 3);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(46,'District Court of Hambanthota','හම්බන්තොට දිසා අධිකරණය','அம்பாந்தோட்டை மாவட்ட நீதிமன்றம்', 3);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(47,'District Court of Thisssamaharama','තිස්සමහාරාම දිසා අධිකරණය','திஸ்சமஹாராமை மாவட்ட நீதிமன்றம்', 3);

-- Courts in Northern Province
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(48,'District Court of Jaffna','යාපනය දිසා අධිකරණය','யாழ்ப்பாணம்  மாவட்ட நீதிமன்றம்', 4);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(49,'District Court of Mallakam','මලක්කම් දිසා අධිකරණය','மல்லாகம்  மாவட்ட நீதிமன்றம்', 4);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(50,'District Court of Kayts','කයිට්ස් දිසා අධිකරණය','ஊர்க்காவற்றுறை  மாவட்ட நீதிமன்றம்', 4);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(51,'District Court of Chavakachcheri','චාවකච්චේරි දිසා අධිකරණය','சாவகச்சேரி  மாவட்ட நீதிமன்றம்', 4);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(52,'District Court of Point Pedro','පේදුරු තුඩුව දිසා අධිකරණය','பருத்தித்துறை  மாவட்ட நீதிமன்றம்', 4);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(53,'District Court of Mannar','මන්නාරම දිසා අධිකරණය','மன்னார்  மாவட்ட நீதிமன்றம்', 4);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(54,'District Court of Vavuniya','වව්නියාව දිසා අධිකරණය','வவுனியா  மாவட்ட நீதிமன்றம்', 4);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(55,'District Court of Killinochchi','කිලිනොච්චිය දිසා අධිකරණය','கிளிநொச்சி  மாவட்ட நீதிமன்றம்', 4);

-- Courts in Eastern Province
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(56,'District Court of Ampara','අම්පාර දිසා අධිකරණය','அம்பாறை மாவட்ட நீதிமன்றம்', 5);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(57,'District Court of Dehiattakandiya','දෙහිඅත්තකණ්ඩිය දිසා අධිකරණය','தெஹயத்தகண்டி மாவட்ட நீதிமன்றம்', 5);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(58,'District Court of Lahugala','ලාහුගල දිසා අධිකරණය','லாஹூகல மாவட்ட நீதிமன்றம்', 5);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(59,'District Court of Kalmunai','කල්මුණේ දිසා අධිකරණය','கல்முனை மாவட்ட நீதிமன்றம்', 5);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(60,'District Court of Akkaraipattu','අක්කරපත්තු දිසා අධිකරණය','அக்கரைப்பற்று மாவட்ட நீதிமன்றம்', 5);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(61,'District Court of Pothuvil','පොතුවිල් දිසා අධිකරණය','பொத்துவில் மாவட்ட நீதிமன்றம்', 5);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(62,'District Court of Batticoloa','මඩකලපුව දිසා අධිකරණය','மட்டகளப்பு  மாவட்ட நீதிமன்றம்', 5);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(63,'District Court of Valachchenei','වාලච්චේනෙ දිසා අධිකරණය','வாழைச்சேனை  மாவட்ட நீதிமன்றம்', 5);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(64,'District Court of Trincomalee','ත්‍රිකුණාමලය දිසා අධිකරණය','திருகோணமலை  மாவட்ட நீதிமன்றம்', 5);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(65,'District Court of Mutur','මුතූර් දිසා අධිකරණය','மூதூர்  மாவட்ட நீதிமன்றம்', 5);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(66,'District Court of Kantale','කන්තලේ දිසා අධිකරණය','கந்தளை  மாவட்ட நீதிமன்றம்', 5);

-- Courts in North-Western Province
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(67,'District Court of Kurunegla','කුරුණෑගල දිසා අධිකරණය','குருநாகலை மாவட்ட நீதிமன்றம்', 6);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(68,'District Court of Polgahawela','පොල්ගහවෙල දිසා අධිකරණය','பொல்கஹாவலை மாவட்ட நீதிமன்றம்', 6);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(69,'District Court of Wariyapola','වාරියපොල දිසා අධිකරණය','வாரியபொல மாவட்ட நீதிமன்றம்', 6);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(70,'District Court of Maho','මහව දිසා අධිකරණය','மாஹோ மாவட்ட நீதிமன்றம்', 6);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(71,'District Court of Galgamuwa','ගල්ගමුව දිසා අධිකරණය','கல்கமுவ மாவட்ட நீதிமன்றம்', 6);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(72,'District Court of Nikaweratiya','නිකවැරටිය දිසා අධිකරණය','நிக்கவெரட்டி மாவட்ட நீதிமன்றம்', 6);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(73,'District Court of Kuliyapitya','කුලියාපිටිය දිසා අධිකරණය','குளியாபிட்டி மாவட்ட நீதிமன்றம்', 6);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(74,'District Court of Hettipola','හෙට්ටිපොල දිසා අධිකරණය','ஹெட்டிபொல மாவட்ட நீதிமன்றம்', 6);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(75,'District Court of Chilaw','හලාවත දිසා අධිකරණය','சிலாபம் மாவட்ட நீதிமன்றம்', 6);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(76,'District Court of Marawila','මාරවිල දිසා අධිකරණය','மாரவில மாவட்ட நீதிமன்றம்', 6);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(77,'District Court of Puttalam','පුත්තලම දිසා අධිකරණය','புத்தளம் மாவட்ட நீதிமன்றம்', 6);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(78,'District Court of Anamaduwa','ආණමඩුව දිසා අධිකරණය','ஆனைமடு மாவட்ட நீதிமன்றம்', 6);

-- Courts in North-Central Province
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(79,'District Court of Anuradhapura','අනුරාධපුර දිසා අධිකරණය','அநுராதபுரம் மாவட்ட நீதிமன்றம்', 7);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(80,'District Court of Thambuttegama','තඹුත්තේගම දිසා අධිකරණය','தம்புத்தேகம மாவட்ட நீதிமன்றம்', 7);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(81,'District Court of Kekirawa','කැකිරාව දිසා අධිකරණය','கெக்கிராவை மாவட்ட நீதிமன்றம்', 7);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(82,'District Court of Kebithigollewa','කැබතිගොල්ලෑව දිසා අධිකරණය','கெபிட்டிகொல்ல மாவட்ட நீதிமன்றம்', 7);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(83,'District Court of Polonnaruwa','පොළොන්නරුව දිසා අධිකරණය','பொலன்னறுவை மாவட்ட நீதிமன்றம்', 7);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(84,'District Court of Hingurakgoda','හිඟුරක්ගොඩ දිසා අධිකරණය','ஹிங்குரக்கொட மாவட்ட நீதிமன்றம்', 7);

-- Courts in Uva Province
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(85,'District Court of Monaragala','මොණරාගල දිසා අධිකරණය','மொணராகலை மாவட்ட நீதிமன்றம்', 8);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(86,'District Court of Siyambalanduwa','සියඹලාණ්ඩුව දිසා අධිකරණය','சியம்பலாண்டுவ மாவட்ட நீதிமன்றம்', 8);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(87,'District Court of Bibile','බිබිල දිසා අධිකරණය','பிபில மாவட்ட நீதிமன்றம்', 8);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(88,'District Court of Wellawaya','වැල්ලවාය දිසා අධිකරණය','வெல்லவாய மாவட்ட நீதிமன்றம்', 8);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(89,'District Court of Badulla','බදුල්ල දිසා අධිකරණය','பதுளை மாவட்ட நீதிமன்றம்', 8);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(90,'District Court of Passara','පස්සර දිසා අධිකරණය','பஸ்சரை மாவட்ட நீதிமன்றம்', 8);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(91,'District Court of Bandarawela','බණ්ඩාරවෙල දිසා අධිකරණය','பண்டாரவலை மாவட்ட நீதிமன்றம்', 8);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(92,'District Court of Welimada','වැලිමඩ දිසා අධිකරණය','வெலிமடை மாவட்ட நீதிமன்றம்', 8);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(93,'District Court of Mahiyanganaya','මහියංගනය දිසා අධිකරණය','மையங்கனை மாவட்ட நீதிமன்றம்', 8);

--  Courts in Sabaragamuwa Province
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(94,'District Court of Ruwanwella','රුවන්වැල්ල දිසා අධිකරණය','ருவன்வெல்லை மாவட்ட நீதிமன்றம்', 9);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(95,'District Court of Kegalle','කෑගල්ල දිසා අධිකරණය','கேகாலை மாவட்ட நீதிமன்றம்', 9);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(96,'District Court of Warakapola','වරකාපොල දිසා අධිකරණය','வரக்காபொல மாவட்ட நீதிமன்றம்', 9);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(97,'District Court of Mawanella','මාවනැල්ල දිසා අධිකරණය','மாவனெல்லை மாவட்ட நீதிமன்றம்', 9);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(98,'District Court of Rathnapura','රත්නපුර දිසා අධිකරණය','இரத்தினபுரி மாவட்ட நீதிமன்றம்', 9);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(99,'District Court of Balangoda','බලංගොඩ දිසා අධිකරණය','பலாங்கொடை மாவட்ட நீதிமன்றம்', 9);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(100,'District Court of Pelmadulla','පැල්මඩුල්ල දිසා අධිකරණය','பெல்மடுலை மாவட்ட நீதிமன்றம்', 9);
INSERT INTO COURTS (COURTID, ENCOURTNAME, SICOURTNAME, TACOURTNAME, provinceUKey) VALUES(101,'District Court of Embilipitiya','ඇඹිලිපිටිය දිසා අධිකරණය','எம்பிலிபிட்டி மாவட்ட நீதிமன்றம்', 9);
