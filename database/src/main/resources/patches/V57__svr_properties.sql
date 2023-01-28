CREATE TABLE `svr_properties` (
   `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
   `state_abbreviation` VARCHAR(4) NOT NULL,
   `voting_region_name` VARCHAR(128) DEFAULT NULL,
   `property_name` VARCHAR(256) NOT NULL,
   `property_value` text,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;