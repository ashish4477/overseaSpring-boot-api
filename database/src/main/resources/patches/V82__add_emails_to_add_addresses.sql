ALTER TABLE `eod_additional_address`
ADD COLUMN `email` VARCHAR(255) DEFAULT "" AFTER `ordering`,
ADD COLUMN `website` VARCHAR(255) DEFAULT "" AFTER `email`;

ALTER TABLE `correction_additional_address`
ADD COLUMN `email` VARCHAR(255) DEFAULT "" AFTER `ordering`,
ADD COLUMN `website` VARCHAR(255) DEFAULT "" AFTER `email`;
