ALTER TABLE `pdf_answers`
ADD COLUMN `phone_type` VARCHAR(32) NOT NULL DEFAULT '' AFTER `phone`,
ADD COLUMN `alternate_phone_type` VARCHAR(32) NOT NULL DEFAULT '' AFTER `alternate_phone`;

ALTER TABLE `users`
ADD COLUMN `phone_type` VARCHAR(32) NOT NULL DEFAULT '' AFTER `phone`,
ADD COLUMN `alternate_phone_type` VARCHAR(32) NOT NULL DEFAULT '' AFTER `alternate_phone`;
