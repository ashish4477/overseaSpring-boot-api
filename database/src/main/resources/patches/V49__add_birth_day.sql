ALTER TABLE `users`
	ADD COLUMN `birth_date` INT(11) NOT NULL DEFAULT '0' AFTER `birth_month`;

ALTER TABLE `pdf_answers`
	ADD COLUMN `birth_date` INT(11) NOT NULL DEFAULT '0' AFTER `birth_month`;
