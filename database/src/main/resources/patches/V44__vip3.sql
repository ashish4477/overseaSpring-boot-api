ALTER TABLE `vip_ballots`
   ADD COLUMN `write_in` TINYINT(1) NOT NULL;

ALTER TABLE `vip_contests`
   ADD COLUMN `partisan` TINYINT(1) NOT NULL,
   ADD COLUMN `primary_party` VARCHAR(128) DEFAULT NULL,
   ADD COLUMN `special` tinyint(1) NOT NULL,
   ADD COLUMN `number_elected` INT(11) DEFAULT NULL,
   ADD COLUMN `number_voting_for` INT(11) DEFAULT NULL;

ALTER TABLE `vip_detail_addresses`
   ADD COLUMN `house_number_prefix` VARCHAR(32) DEFAULT NULL,
   ADD COLUMN `house_number_suffix` VARCHAR(32) DEFAULT NULL;