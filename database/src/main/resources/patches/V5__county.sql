ALTER TABLE `wizard_result_addresses`
  ADD COLUMN `county` VARCHAR(255) NULL DEFAULT '' AFTER `country`;

ALTER TABLE `user_addresses`
  ADD COLUMN `county` VARCHAR(255) NULL DEFAULT '' AFTER `country`;