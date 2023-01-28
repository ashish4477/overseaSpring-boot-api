ALTER TABLE `mailing_list_address`
ADD COLUMN `voting_region_name` VARCHAR(255) NOT NULL DEFAULT '' AFTER `eod_region`;

ALTER TABLE `mailing_list_link`
    ADD COLUMN `error_message` VARCHAR(255) NOT NULL DEFAULT '' AFTER `error_count`;