CREATE TABLE `send_grid` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `last_run` DATETIME NULL,
  `num_of_unsubscr` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `last_run_idx` (`last_run` DESC));