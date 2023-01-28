ALTER TABLE `faces_config`
	ADD COLUMN `use_captcha` TINYINT NOT NULL DEFAULT '1' AFTER `drupal_url`,
	ADD COLUMN `login_allowed` TINYINT NOT NULL DEFAULT '1' AFTER `use_captcha`;