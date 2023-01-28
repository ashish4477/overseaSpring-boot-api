CREATE TABLE `send_grid_log` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `log_date` DATETIME NULL,
  `log_level` VARCHAR(127) NULL,
  `message` TEXT,
  PRIMARY KEY (`id`)
);