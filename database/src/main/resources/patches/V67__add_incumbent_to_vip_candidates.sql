ALTER TABLE `vip_candidates`
   ADD COLUMN `incumbent` TINYINT(4) NOT NULL DEFAULT '0' AFTER `party`;
