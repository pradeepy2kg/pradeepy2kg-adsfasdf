------------- DB Sripts ----------------------

- check-gnDivision-usage-in-crs.sql            - can be used to check whether it is ok or not to update the GN Divisions
- update-gnDivisions-birth-death-records.sql   - if any of birth/death record using any of deleted GN Divisions update
                                                 it to the current available GN Division
- ecivil-common-gnDivisions-complete-aug31.sql - GN Division dump to be restored.


------------- Steps to follow ----------------------

1. run check-gnDivision-usage-in-crs.sql to see its FAIL or PASS. Should be FAIL this time.
2. run update-gnDivisions-birth-death-records.sql
3. run check-gnDivision-usage-in-crs.sql again and the status should be PASS.
4. restore DB dump - ecivil-common-gnDivisions-complete-aug31.sql
