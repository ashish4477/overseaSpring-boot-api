ALTER TABLE `user_addresses`
   MODIFY COLUMN `zip` VARCHAR(24) NULL DEFAULT '';

ALTER TABLE `wizard_result_addresses`
   MODIFY COLUMN `zip` VARCHAR(24) NULL DEFAULT '';