
ALTER TABLE `mailing_list`
	CHANGE COLUMN `company_identifier` `campaign_id` VARCHAR(255) NOT NULL AFTER `api_key`;