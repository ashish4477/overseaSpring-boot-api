UPDATE `questionary_pages` set form_type = 'OVERSEAS';

ALTER TABLE `faces_config` ADD COLUMN `fax_page` TINYINT(4) NOT NULL DEFAULT '0' AFTER `envelope`;
ALTER TABLE `faces_config` ADD COLUMN `notarization_page` TINYINT(4) NOT NULL DEFAULT '0' AFTER `fax_page`;
ALTER TABLE `faces_config` ADD COLUMN `blank_addendum_page` TINYINT(4) NOT NULL DEFAULT '0' AFTER `notarization_page`;

UPDATE `faces_config` SET `fax_page`=1, `notarization_page`=1, `blank_addendum_page`=1;
-- new york
UPDATE `faces_config` SET `blank_addendum_page`=0 WHERE id=26;
-- minnesota
UPDATE `faces_config` SET `fax_page`=0, `notarization_page`=0 WHERE id=4;

ALTER TABLE `pdf_answers` ADD COLUMN `email_sent` TINYINT(4) NOT NULL DEFAULT '0' AFTER `downloaded`;
ALTER TABLE `pdf_answers` CHANGE  `flow_type`  `flow_type` VARCHAR( 32 ) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL;


