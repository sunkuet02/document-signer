-- version: $Id: create-index-signserver.sql 5120 2014-09-09 13:57:13Z netmackan $

-- Selecting log entries when verifying/exporting IntegrityProtectedDevice logs:
CREATE UNIQUE INDEX auditrecorddata_idx2 ON AuditRecordData (nodeId,sequenceNumber);

-- Selecting log entries from IntegrityProtectedDevice logs in the AdminGUI is usually
-- ordered by time stamp.
CREATE INDEX auditrecorddata_idx3 ON AuditRecordData (timeStamp);

-- Selecting archivables from the archive in the AdminGUI is usually
-- ordered by time stamp.
CREATE INDEX archivedata_idx3 ON ArchiveData (time);
