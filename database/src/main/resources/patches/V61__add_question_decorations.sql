ALTER TABLE `questions`
	ADD COLUMN `html_class_fieldset` VARCHAR(255) NOT NULL DEFAULT '' AFTER `title`,
	ADD COLUMN `html_class_option` VARCHAR(255) NOT NULL DEFAULT '' AFTER `html_class_fieldset`;
