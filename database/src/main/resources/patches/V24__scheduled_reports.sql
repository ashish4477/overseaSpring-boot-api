CREATE TABLE `rd_scheduled_reports` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`user_id` INT(11) NOT NULL DEFAULT '0',
	`report_id` INT(11) NOT NULL DEFAULT '0',
	`duration` VARCHAR(32) NOT NULL,
	`repeat_interval` VARCHAR(32) NOT NULL,
	`next_execution_date` DATETIME NOT NULL,
	`last_sent_date` DATETIME,
   PRIMARY KEY (`id`),
   KEY `scheduled_user_id` (`user_id`),
   KEY `scheduled_report_id` (`report_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

ALTER TABLE `rd_scheduled_reports`
  ADD CONSTRAINT `scheduled_user_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `scheduler_report_fk` FOREIGN KEY (`report_id`) REFERENCES `rd_reports` (`id`);