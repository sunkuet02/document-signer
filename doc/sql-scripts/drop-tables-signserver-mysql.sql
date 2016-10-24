-- Dropping tables for SignServer 3.5.x on MySQL/MariaDB
-- ------------------------------------------------------
-- Version: $Id: drop-tables-signserver-mysql.sql 4150 2014-01-30 10:14:50Z netmackan $
-- Comment: 


--
-- Drop table `AuditRecordData`
--
DROP TABLE IF EXISTS `AuditRecordData`;

--
-- Drop table `GlobalConfigData`
--
DROP TABLE IF EXISTS `GlobalConfigData`;


--
-- Drop table `signerconfigdata`
--
DROP TABLE IF EXISTS `signerconfigdata`;


--
-- Drop table `KeyUsageCounter`
--
DROP TABLE IF EXISTS `KeyUsageCounter`;


--
-- Drop table `ArchiveData`
--
DROP TABLE IF EXISTS `ArchiveData`;


--
-- Drop table `SEQUENCE`
--
DROP TABLE IF EXISTS `SEQUENCE`;


-- End
