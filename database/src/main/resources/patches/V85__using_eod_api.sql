ALTER TABLE `users`
ADD COLUMN `eod_region_id` VARCHAR(127) NOT NULL DEFAULT 0 AFTER `voting_region`;

ALTER TABLE `pdf_answers`
ADD COLUMN `eod_region_id` VARCHAR(127) NOT NULL DEFAULT 0 AFTER `voting_region`;
