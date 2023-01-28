CREATE TABLE `vip_ballots` (
  `id` INT(11) UNIQUE NOT NULL AUTO_INCREMENT,
  `vip_id` INT(11) NOT NULL,
  `source_id` INT(11) NOT NULL,
  `custom_ballot_id` INT(11) DEFAULT NULL,
  `referendum_id` INT(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX (`vip_id`, `source_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE `vip_ballot_candidates` (
  `id` INT(11) UNIQUE NOT NULL AUTO_INCREMENT,
  `sort_order` INT(4) DEFAULT NULL,
  `ballot_id` INT(11) NOT NULL,
  `candidate_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX (`ballot_id`, `candidate_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE `vip_ballot_responses` (
  `id` INT(11) UNIQUE NOT NULL AUTO_INCREMENT,
  `vip_id` INT(11) NOT NULL,
  `source_id` INT(11) NOT NULL,
  `text` text NOT NULL,
  PRIMARY KEY (`id`),
  INDEX (`vip_id`, `source_id`)
)  ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE `vip_candidates` (
  `id` INT(11) UNIQUE NOT NULL AUTO_INCREMENT,
  `vip_id` INT(11) NOT NULL,
  `source_id` INT(11) NOT NULL,
  `name` VARCHAR(128) NOT NULL,
  `party` VARCHAR(64) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX (`vip_id`, `source_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE `vip_contests` (
  `id` INT(11) UNIQUE NOT NULL AUTO_INCREMENT,
  `vip_id` INT(11) NOT NULL,
  `election_id` INT(11) NOT NULL,
  `source_id` INT(11) NOT NULL,
  `ballot_id` INT(11) NOT NULL,
  `electoral_district_id` INT(11) NOT NULL,
  `contest_type` VARCHAR(64),
  PRIMARY KEY (`id`),
  INDEX(`election_id`),
  INDEX (`vip_id`, `source_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE `vip_custom_ballots` (
  `id` INT(11) UNIQUE NOT NULL AUTO_INCREMENT,
  `vip_id` INT(11) NOT NULL,
  `source_id` INT(11) NOT NULL,
  `heading` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX (`vip_id`, `source_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE `vip_custom_ballot_responses` (
  `id` INT(11) UNIQUE NOT NULL AUTO_INCREMENT,
  `sort_order` INT(4) DEFAULT NULL,
  `custom_ballot_id` INT(11) NOT NULL,
  `ballot_response_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX (`custom_ballot_id`, `ballot_response_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE `vip_detail_addresses` (
  `id` INT(11) UNIQUE NOT NULL AUTO_INCREMENT,
  `vip_id` INT(11) NOT NULL,
  `source_id` INT(11) NOT NULL,
  `address_direction` VARCHAR(8) DEFAULT NULL,
  `city` VARCHAR(64) NOT NULL,
  `state` VARCHAR(32) NOT NULL,
  `street_direction` VARCHAR(8) DEFAULT NULL,
  `street_name` VARCHAR(128) NOT NULL,
  `street_suffix` VARCHAR(16) DEFAULT NULL,
  `zip` VARCHAR(5) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX (`vip_id`, `source_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE `vip_elections` (
  `id` INT(11) UNIQUE NOT NULL AUTO_INCREMENT,
  `vip_id` INT(11) NOT NULL,
  `source_id` INT(11) NOT NULL,
  `election_date` DATE NOT NULL,
  `state_id` INT(11) NOT NULL,
  `election_type` VARCHAR(64) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX (`vip_id`, `source_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE `vip_electoral_districts` (
  `id` INT(11) UNIQUE NOT NULL AUTO_INCREMENT,
  `vip_id` INT(11) NOT NULL,
  `source_id` INT(11) NOT NULL,
  `district_name` VARCHAR(128) NOT NULL,
  `district_number` INT(8) DEFAULT 0,
  `district_type` VARCHAR(32) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX (`vip_id`, `source_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE `vip_localities` (
  `id` INT(11) UNIQUE NOT NULL AUTO_INCREMENT,
  `vip_id` INT(11) NOT NULL,
  `source_id` INT(11) NOT NULL,
  `locality_name` VARCHAR(128) NOT NULL,
  `state_id` INT(11) NOT NULL,
  `locality_type` VARCHAR(32) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX (`vip_id`, `source_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE `vip_precincts` (
  `id` INT(11) UNIQUE NOT NULL AUTO_INCREMENT,
  `vip_id` INT(11) NOT NULL,
  `source_id` INT(11) NOT NULL,
  `locality_id` INT(11),
  `precinct_name` VARCHAR(64),
  `precinct_number` VARCHAR(32) DEFAULT NULL,
  `ward` VARCHAR(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX (`vip_id`, `source_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE `vip_precinct_districts` (
  `precinct_id` INT(11) NOT NULL,
  `electoral_district_id` INT(11) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE `vip_precinct_splits` (
  `id` INT(11) UNIQUE NOT NULL AUTO_INCREMENT,
  `vip_id` INT(11) NOT NULL,
  `source_id` INT(11) NOT NULL,
  `precinct_id` INT(11) NOT NULL,
  `precinct_split_name` VARCHAR(64),
  PRIMARY KEY (`id`),
  INDEX (`vip_id`, `source_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE `vip_precinct_split_districts` (
  `precinct_split_id` INT(11) NOT NULL,
  `electoral_district_id` INT(11) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE `vip_referenda` (
  `id` INT(11) UNIQUE NOT NULL AUTO_INCREMENT,
  `vip_id` INT(11) NOT NULL,
  `source_id` INT(11) NOT NULL,
  `title` VARCHAR(128) NOT NULL,
  `sub_title` VARCHAR(128) DEFAULT NULL,
  `brief` VARCHAR(128) DEFAULT NULL,
  `referendum_text` text,
  PRIMARY KEY (`id`),
  INDEX (`vip_id`, `source_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE `vip_referendum_ballot_responses` (
  `id` INT(11) UNIQUE NOT NULL AUTO_INCREMENT,
  `sort_order` INT(4) DEFAULT NULL,
  `referendum_id` INT(11) NOT NULL,
  `ballot_response_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX (`referendum_id`, `ballot_response_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE `vip_sources` (
  `id` INT(11) UNIQUE NOT NULL AUTO_INCREMENT,
  `vip_id` INT(11) NOT NULL,
  `source_id` INT(11) NOT NULL,
  `source_name` VARCHAR(128) NOT NULL,
  `date_time` TIMESTAMP,
  `complete` BOOLEAN NOT NULL,
  PRIMARY KEY (`id`),
  INDEX (`vip_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE `vip_states` (
  `id` INT(11) UNIQUE NOT NULL AUTO_INCREMENT,
  `vip_id` INT(11) NOT NULL,
  `source_id` INT(11) NOT NULL,
  `state_name` VARCHAR(128),
  PRIMARY KEY (`id`),
  INDEX (`vip_id`, `source_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE `vip_street_segments` (
  `id` INT(11) UNIQUE NOT NULL AUTO_INCREMENT,
  `vip_id` INT(11) NOT NULL,
  `source_id` INT(11) NOT NULL,
  `end_house_number` INT(6) NOT NULL,
  `non_house_address_id` INT(11) NOT NULL,
  `odd_even_both` VARCHAR(4) DEFAULT NULL,
  `precinct_id` INT(11) NOT NULL,
  `precinct_split_id` INT(11) DEFAULT NULL,
  `start_house_number` INT(6) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX (`vip_id`, `source_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

ALTER TABLE `vip_ballots`
   ADD CONSTRAINT `vip_ballot_referenda_fk` FOREIGN KEY (`referendum_id`) REFERENCES `vip_referenda` (`id`),
   ADD CONSTRAINT `vip_ballot_source_fk` FOREIGN KEY (`source_id`) REFERENCES `vip_sources` (`id`),
   ADD CONSTRAINT `vip_ballot_custom_fk` FOREIGN KEY (`custom_ballot_id`) REFERENCES `vip_custom_ballots` (`id`);

ALTER TABLE `vip_ballot_candidates`
   ADD CONSTRAINT `vip_bc_ballot_fk` FOREIGN KEY (`ballot_id`) REFERENCES `vip_ballots` (`id`),
   ADD CONSTRAINT `vip_bc_candidate_fk` FOREIGN KEY (`candidate_id`) REFERENCES `vip_candidates` (`id`);

ALTER TABLE `vip_ballot_responses`
   ADD CONSTRAINT `vip_ballot_response_source_fk` FOREIGN KEY (`source_id`) REFERENCES `vip_sources` (`id`);

ALTER TABLE `vip_candidates`
   ADD CONSTRAINT `vip_candidate_source_fk` FOREIGN KEY (`source_id`) REFERENCES `vip_sources` (`id`);

ALTER TABLE `vip_contests`
   ADD CONSTRAINT `vip_contest_ballot_fk` FOREIGN KEY (`ballot_id`) REFERENCES `vip_ballots` (`id`),
   ADD CONSTRAINT `vip_contest_district_fk` FOREIGN KEY (`electoral_district_id`) REFERENCES `vip_electoral_districts` (`id`),
   ADD CONSTRAINT `vip_election_fk` FOREIGN KEY (`election_id`) REFERENCES `vip_elections` (`id`),
   ADD CONSTRAINT `vip_contest_source_fk` FOREIGN KEY (`source_id`) REFERENCES `vip_sources` (`id`);

ALTER TABLE `vip_custom_ballots`
   ADD CONSTRAINT `vip_custom_ballot_source_fk` FOREIGN KEY (`source_id`) REFERENCES `vip_sources` (`id`);
   
ALTER TABLE `vip_custom_ballot_responses`
   ADD CONSTRAINT `vip_cbr_custom_ballot_fk` FOREIGN KEY (`custom_ballot_id`) REFERENCES `vip_custom_ballots` (`id`),
   ADD CONSTRAINT `vip_cbr_ballot_response_fk` FOREIGN KEY (`ballot_response_id`) REFERENCES `vip_ballot_responses` (`id`);
  
ALTER TABLE `vip_detail_addresses`
   ADD CONSTRAINT `vip_da_source_fk` FOREIGN KEY (`source_id`) REFERENCES `vip_sources` (`id`);

ALTER TABLE `vip_elections`
   ADD CONSTRAINT `vip_election_source_fk` FOREIGN KEY (`source_id`) REFERENCES `vip_sources` (`id`);
   
ALTER TABLE `vip_electoral_districts`
   ADD CONSTRAINT `vip_ed_source_fk` FOREIGN KEY (`source_id`) REFERENCES `vip_sources` (`id`);

   ALTER TABLE `vip_localities`
   ADD CONSTRAINT `vip_locality_state_fk` FOREIGN KEY (`state_id`) REFERENCES `vip_states` (`id`),
   ADD CONSTRAINT `vip_locality_source_fk` FOREIGN KEY (`source_id`) REFERENCES `vip_sources` (`id`);

ALTER TABLE `vip_precincts`
   ADD CONSTRAINT `vip_precinct_locality_fk` FOREIGN KEY (`locality_id`) REFERENCES `vip_localities` (`id`),
   ADD CONSTRAINT `vip_precinct_source_fk` FOREIGN KEY (`source_id`) REFERENCES `vip_sources` (`id`);

ALTER TABLE `vip_precinct_districts`
   ADD CONSTRAINT `vip_precinct_fk` FOREIGN KEY (`precinct_id`) REFERENCES `vip_precincts` (`id`),
   ADD CONSTRAINT `vip_precinct_district_fk` FOREIGN KEY (`electoral_district_id`) REFERENCES `vip_electoral_districts` (`id`);

ALTER TABLE `vip_precinct_splits`
   ADD CONSTRAINT `vip_precinct_split_precinct_fk` FOREIGN KEY (`precinct_id`) REFERENCES `vip_precincts` (`id`),
   ADD CONSTRAINT `vip_precinct_split_source_fk` FOREIGN KEY (`source_id`) REFERENCES `vip_sources` (`id`);

ALTER TABLE `vip_precinct_split_districts`
   ADD CONSTRAINT `vip_precinct_split_fk` FOREIGN KEY (`precinct_split_id`) REFERENCES `vip_precinct_splits` (`id`),
   ADD CONSTRAINT `vip_precinct_split_district_fk` FOREIGN KEY (`electoral_district_id`) REFERENCES `vip_electoral_districts` (`id`);

ALTER TABLE `vip_referenda`
   ADD CONSTRAINT `vip_referenda_source_fk` FOREIGN KEY (`source_id`) REFERENCES `vip_sources` (`id`);
   
ALTER TABLE `vip_referendum_ballot_responses`
   ADD CONSTRAINT `vip_rbr_referendum_fk` FOREIGN KEY (`referendum_id`) REFERENCES `vip_referenda` (`id`),
   ADD CONSTRAINT `vip_rbr_ballot_response_fk` FOREIGN KEY (`ballot_response_id`) REFERENCES `vip_ballot_responses` (`id`);

ALTER TABLE `vip_states`
   ADD CONSTRAINT `vip_state_source_fk` FOREIGN KEY (`source_id`) REFERENCES `vip_sources` (`id`);
   
ALTER TABLE `vip_street_segments`
   ADD CONSTRAINT `vip_segment_precinct_fk` FOREIGN KEY (`precinct_id`) REFERENCES `vip_precincts` (`id`),
   ADD CONSTRAINT `vip_segment_precinct_split_fk` FOREIGN KEY (`precinct_split_id`) REFERENCES `vip_precinct_splits` (`id`),
   ADD CONSTRAINT `vip_segment_source_fk` FOREIGN KEY (`source_id`) REFERENCES `vip_sources` (`id`);
