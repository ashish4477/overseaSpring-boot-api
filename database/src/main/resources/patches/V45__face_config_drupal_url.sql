ALTER TABLE `faces_config`
  ADD COLUMN `drupal_url` varchar(255) NOT NULL DEFAULT '' AFTER `user_validation_skip_fields`;

UPDATE `faces_config` SET `drupal_url` = 'https://content.overseasvotefoundation.org' WHERE `default`=1;