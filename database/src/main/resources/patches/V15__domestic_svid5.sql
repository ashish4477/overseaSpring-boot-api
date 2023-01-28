ALTER TABLE `state_voter_info`
   ADD COLUMN `domestic_early_email` tinyint(4) DEFAULT "0" AFTER `domestic_return_tel`,
   ADD COLUMN `domestic_early_fax` tinyint(4) DEFAULT "0" AFTER `domestic_early_email`,
   ADD COLUMN `domestic_early_inperson` tinyint(4) DEFAULT "0" AFTER `domestic_early_fax`,
   ADD COLUMN `domestic_early_post` tinyint(4) DEFAULT "0" AFTER `domestic_early_inperson`,
   ADD COLUMN `domestic_early_tel` tinyint(4) DEFAULT "0" AFTER `domestic_early_post`;