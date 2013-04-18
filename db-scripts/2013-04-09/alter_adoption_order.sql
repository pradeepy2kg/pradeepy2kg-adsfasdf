-- Select Database.
USE CRS;

-- Introduce new column to capture comments.
ALTER TABLE ADOPTION_ORDER ADD COLUMN `comments` longtext;