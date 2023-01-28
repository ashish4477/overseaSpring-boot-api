
ALTER TABLE `mailinglist` ADD `birth_year` INT NULL AFTER `email` ,
ADD `voter_type` VARCHAR( 64 ) NOT NULL AFTER `birth_year` ,
ADD `phone` VARCHAR( 32 ) NOT NULL AFTER `voter_type` ,
ADD `voting_city` VARCHAR( 128 ) NOT NULL AFTER `phone` ,
ADD `voting_state_name` CHAR( 2 ) NOT NULL AFTER `voting_city` ,
ADD `current_address` VARCHAR( 255 ) NOT NULL AFTER `voting_state_name` ,
ADD `current_city` VARCHAR( 128 ) NOT NULL AFTER `current_address` ,
ADD `current_postal_code` VARCHAR( 32 ) NOT NULL AFTER `current_city` ,
ADD `current_country_name` VARCHAR( 128 ) NOT NULL AFTER `current_postal_code` ;