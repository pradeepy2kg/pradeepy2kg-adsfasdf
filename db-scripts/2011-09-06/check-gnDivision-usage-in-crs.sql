USE CRS;

DELIMITER $$
DROP PROCEDURE IF EXISTS gnDivisionsUsage$$

CREATE PROCEDURE gnDivisionsUsage()
  READS SQL DATA
BEGIN
  DECLARE births INT DEFAULT 0;
  DECLARE deaths INT DEFAULT 0;
  DECLARE result TEXT DEFAULT '';
  DECLARE gnDivisionMappingRecords TEXT DEFAULT '';

  SELECT COUNT(*) INTO births FROM BIRTH_REGISTER WHERE
	 motherGNDivisionUKey=489 OR
	 motherGNDivisionUKey=527 OR
	 motherGNDivisionUKey=529 OR
	 motherGNDivisionUKey=537 OR
	 motherGNDivisionUKey=539 OR
	 motherGNDivisionUKey=555 OR
	 motherGNDivisionUKey=574 OR
	 motherGNDivisionUKey=583 OR
	 motherGNDivisionUKey=597 OR
	 motherGNDivisionUKey=725 OR
	 motherGNDivisionUKey=1360 OR
	 motherGNDivisionUKey=1789 OR
	 motherGNDivisionUKey=1807 OR
	 motherGNDivisionUKey=1854 OR
	 motherGNDivisionUKey=1859 OR
	 motherGNDivisionUKey=1863 OR
	 motherGNDivisionUKey=1888 OR
	 motherGNDivisionUKey=2082 OR
	 motherGNDivisionUKey=2372 OR
	 motherGNDivisionUKey=4969 OR
	 motherGNDivisionUKey=5046 OR
	 motherGNDivisionUKey=6496 OR
	 motherGNDivisionUKey=6527 OR
	 motherGNDivisionUKey=10782 OR
	 motherGNDivisionUKey=12173 OR
	 motherGNDivisionUKey=13227;

  SELECT COUNT(*) INTO deaths FROM DEATH_REGISTER WHERE 
	 gnDivisionUKey=489 OR
	 gnDivisionUKey=527 OR
	 gnDivisionUKey=529 OR
	 gnDivisionUKey=537 OR
	 gnDivisionUKey=539 OR
	 gnDivisionUKey=555 OR
	 gnDivisionUKey=574 OR
	 gnDivisionUKey=583 OR
	 gnDivisionUKey=597 OR
	 gnDivisionUKey=725 OR
	 gnDivisionUKey=1360 OR
	 gnDivisionUKey=1789 OR
	 gnDivisionUKey=1807 OR
	 gnDivisionUKey=1854 OR
	 gnDivisionUKey=1859 OR
	 gnDivisionUKey=1863 OR
	 gnDivisionUKey=1888 OR
	 gnDivisionUKey=2082 OR
	 gnDivisionUKey=2372 OR
	 gnDivisionUKey=4969 OR
	 gnDivisionUKey=5046 OR
	 gnDivisionUKey=6496 OR
	 gnDivisionUKey=6527 OR
	 gnDivisionUKey=10782 OR
	 gnDivisionUKey=12173 OR
	 gnDivisionUKey=13227;

  IF (births+deaths) = 0 THEN
	SET result='PASS: Birth and Death declarations not using deleted GN divisions, its OK to update GN Divisions';
  ELSE
	SET result='FAIL: Birth or Death declarations using one/more of the deleted GN division, Do not Update GN Divisions';
	CASE
	    WHEN (births != 0 AND deaths !=0) THEN 
			SET gnDivisionMappingRecords='*** Usage of deleted GN Divisions in birth declarations ***';
			SELECT gnDivisionMappingRecords;
			SELECT idUKey, motherGNDivisionUKey FROM BIRTH_REGISTER WHERE
				 motherGNDivisionUKey=489 OR
				 motherGNDivisionUKey=527 OR
				 motherGNDivisionUKey=529 OR
				 motherGNDivisionUKey=537 OR
				 motherGNDivisionUKey=539 OR
				 motherGNDivisionUKey=555 OR
				 motherGNDivisionUKey=574 OR
				 motherGNDivisionUKey=583 OR
				 motherGNDivisionUKey=597 OR
				 motherGNDivisionUKey=725 OR
				 motherGNDivisionUKey=1360 OR
				 motherGNDivisionUKey=1789 OR
				 motherGNDivisionUKey=1807 OR
				 motherGNDivisionUKey=1854 OR
				 motherGNDivisionUKey=1859 OR
				 motherGNDivisionUKey=1863 OR
				 motherGNDivisionUKey=1888 OR
				 motherGNDivisionUKey=2082 OR
				 motherGNDivisionUKey=2372 OR
				 motherGNDivisionUKey=4969 OR
				 motherGNDivisionUKey=5046 OR
				 motherGNDivisionUKey=6496 OR
				 motherGNDivisionUKey=6527 OR
				 motherGNDivisionUKey=10782 OR
				 motherGNDivisionUKey=12173 OR
				 motherGNDivisionUKey=13227;	

			SET gnDivisionMappingRecords='*** Usage of deleted GN Divisions in death declarations ***';
			SELECT gnDivisionMappingRecords;
			SELECT idUKey, gnDivisionUKey FROM DEATH_REGISTER WHERE 
				 gnDivisionUKey=489 OR
				 gnDivisionUKey=527 OR
				 gnDivisionUKey=529 OR
				 gnDivisionUKey=537 OR
				 gnDivisionUKey=539 OR
				 gnDivisionUKey=555 OR
				 gnDivisionUKey=574 OR
				 gnDivisionUKey=583 OR
				 gnDivisionUKey=597 OR
				 gnDivisionUKey=725 OR
				 gnDivisionUKey=1360 OR
				 gnDivisionUKey=1789 OR
				 gnDivisionUKey=1807 OR
				 gnDivisionUKey=1854 OR
				 gnDivisionUKey=1859 OR
				 gnDivisionUKey=1863 OR
				 gnDivisionUKey=1888 OR
				 gnDivisionUKey=2082 OR
				 gnDivisionUKey=2372 OR
				 gnDivisionUKey=4969 OR
				 gnDivisionUKey=5046 OR
				 gnDivisionUKey=6496 OR
				 gnDivisionUKey=6527 OR
				 gnDivisionUKey=10782 OR
				 gnDivisionUKey=12173 OR
				 gnDivisionUKey=13227;
			
	    WHEN births != 0 THEN SELECT CONCAT_WS(' ', result, 'Deleted GN Divisions used in birth records') INTO result;
	    WHEN deaths != 0 THEN SELECT CONCAT_WS(' ', result, 'Deleted GN Divisions used in death records') INTO result;
	END CASE;	
  END IF;

  SELECT result;
END$$

DELIMITER ;

CALL gnDivisionsUsage();

DROP PROCEDURE gnDivisionsUsage;

