-- additional two days added to late and belated birth registration days
UPDATE COMMON.APP_PARAMETERS SET VALUE='92' WHERE NAME='crs.birth.late_reg_days' AND VALUE='90';
UPDATE COMMON.APP_PARAMETERS SET VALUE='367' WHERE NAME='crs.birth.belated_reg_days' AND VALUE='365';
