ALTER TABLE `state_voter_info`
ADD COLUMN `domestic_blank_email` tinyint(4) DEFAULT "0" AFTER `domestic_early_tel`,
ADD COLUMN `domestic_blank_fax` tinyint(4) DEFAULT "0" AFTER `domestic_blank_email`,
ADD COLUMN `domestic_blank_inperson` tinyint(4) DEFAULT "0" AFTER `domestic_blank_fax`,
ADD COLUMN `domestic_blank_post` tinyint(4) DEFAULT "0" AFTER `domestic_blank_inperson`,
ADD COLUMN `domestic_blank_tel` tinyint(4) DEFAULT "0" AFTER `domestic_blank_post`;