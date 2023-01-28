ALTER TABLE `state_voter_info`
	CHANGE COLUMN `contact_title` `contact_title` VARCHAR(255) NOT NULL DEFAULT '' AFTER `state_id`;