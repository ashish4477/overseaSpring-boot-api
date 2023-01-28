CREATE TABLE `mailing_semaphore` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `busy_status` VARCHAR(45) NULL,
  `records_count` INT(11) NULL,
  `offset` INT(11) NULL,
  PRIMARY KEY (`id`));
