-- Alter Statistics of ecivil system
USE COMMON;

-- Add column to store the count of birth confirmation printed items.
ALTER TABLE STATISTICS ADD COLUMN birthConfirmationPrintedItems int(11) NOT NULL;

-- Add column to store the count of birth confirmation approval pending items.
ALTER TABLE STATISTICS ADD COLUMN birthConfirmationApprovalPendingItems int(11) NOT NULL;

-- Add column to store the count of birth confirmation approved items.
ALTER TABLE STATISTICS ADD COLUMN birthConfirmationApprovedItems int(11) NOT NULL;

-- Add column to store the count of generated birth certificates.
ALTER TABLE STATISTICS ADD COLUMN birthCertificateGenerated int(11) NOT NULL;

-- Add column to store the count of printed birth certificates.
ALTER TABLE STATISTICS ADD COLUMN birthCertificatePrinted int(11) NOT NULL;

-- Add column to store the count of deleted birth records.
ALTER TABLE STATISTICS ADD COLUMN birthDeletedItems int(11) NOT NULL;

-- Add column to store the count of printed death certificates.
ALTER TABLE STATISTICS ADD COLUMN deathCertificatePrintedItems int(11) NOT NULL;

-- Add column to store the count of deleted death records.
ALTER TABLE STATISTICS ADD COLUMN deathsDeletedItems int(11) NOT NULL;
