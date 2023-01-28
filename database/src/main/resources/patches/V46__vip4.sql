CREATE TABLE `vip_candidate_bios` (
   `id` INT(11) UNIQUE NOT NULL AUTO_INCREMENT,
   `candidate_id` INT(11) NOT NULL,
   `biography` LONGTEXT DEFAULT NULL,
   `candidate_url` TEXT  DEFAULT NULL,
   `email` VARCHAR(256) DEFAULT NULL,
   `filed_mailing_address_id` INT(11) DEFAULT NULL,
   `phone` VARCHAR(64) DEFAULT NULL,
   `photo_url` TEXT DEFAULT NULL,
   PRIMARY KEY (`id`),
   INDEX(`candidate_id`, `filed_mailing_address_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE `vip_referendum_details` (
   `id` INT(11) UNIQUE NOT NULL AUTO_INCREMENT,
   `referendum_id` INT(11) NOT NULL,
   `pro_statement` MEDIUMTEXT DEFAULT NULL,
   `con_statement` MEDIUMTEXT DEFAULT NULL,
   `passage_threshold` VARCHAR(256) DEFAULT NULL,
   `effect_of_abstain` VARCHAR(256) DEFAULT NULL,
   PRIMARY KEY (`id`),
   INDEX(`referendum_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

ALTER TABLE `vip_candidate_bios`
   ADD CONSTRAINT `vip_candidate_bio_candidate_fk` FOREIGN KEY (`candidate_id`) REFERENCES `vip_candidates` (`id`),
   ADD CONSTRAINT `vip_candidate_bio_address_fk` FOREIGN KEY (`filed_mailing_address_id`) REFERENCES `user_addresses` (`id`);

ALTER TABLE `vip_referendum_details`
   ADD CONSTRAINT `vip_referendum_detail_referendum_fk` FOREIGN KEY (`referendum_id`) REFERENCES `vip_referenda` (`id`);