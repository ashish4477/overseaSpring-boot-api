ALTER TABLE `vip_ballot_responses`
   DROP COLUMN `text`,
   ADD COLUMN `text` TEXT DEFAULT NULL;

ALTER TABLE `vip_candidates`
   DROP COLUMN `name`,
   DROP COLUMN `party`,
   ADD COLUMN `candidate_name` VARCHAR(128) DEFAULT NULL,
   ADD COLUMN `party` VARCHAR(64) DEFAULT NULL;

ALTER TABLE `vip_contests`
   ADD COLUMN `office` VARCHAR(128) DEFAULT NULL;

ALTER TABLE `vip_custom_ballots`
   DROP COLUMN `heading`,
   ADD COLUMN `heading` VARCHAR(128) DEFAULT NULL;
   
ALTER TABLE `vip_detail_addresses`
   DROP FOREIGN KEY `vip_da_source_fk`,
   DROP COLUMN `vip_id`,
   DROP COLUMN `source_id`;
   
ALTER TABLE `vip_electoral_districts`
   DROP COLUMN `district_name`,
   DROP COLUMN `district_type`,
   ADD COLUMN `district_name` VARCHAR(128) DEFAULT NULL,
   ADD COLUMN `district_type` VARCHAR(32) DEFAULT NULL;
   
ALTER TABLE `vip_referenda`
   DROP COLUMN `title`,
   ADD COLUMN `title` VARCHAR(128) DEFAULT NULL;