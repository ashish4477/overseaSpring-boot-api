CREATE TABLE `data_export_configs` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `delivery_schedule` VARCHAR(64),
  `export_level` VARCHAR(64),
  `server_address` VARCHAR(255),
  `server_port` VARCHAR(32),
  `sftp_dir` VARCHAR(255),
  `sftp_user` VARCHAR(255),
  `sftp_password` VARCHAR(255),
  `sftp_private_key` VARCHAR(255),
  `sftp_passphrase` VARCHAR(255),
  `zip_password` VARCHAR(255),
  `enabled` TINYINT(4),
  `state_id` INT(11),
  `region_id` INT(11),
  PRIMARY KEY (`id`),
  INDEX (`delivery_schedule`),
  CONSTRAINT `export_state_fk` FOREIGN KEY (`state_id`) REFERENCES `states` (`id`),
  CONSTRAINT `export_region_fk` FOREIGN KEY (`region_id`) REFERENCES `voting_regions` (`id`)
) ENGINE=InnoDB;

CREATE TABLE `export_to_face_configs` (
  `export_config_id` INT(11),
  `face_config_id` INT(11),
  CONSTRAINT `export_config_fk` FOREIGN KEY (`export_config_id`) REFERENCES `data_export_configs` (`id`),
  CONSTRAINT `export_face_config_fk` FOREIGN KEY (`face_config_id`) REFERENCES `faces_config` (`id`)
) ENGINE=InnoDB;

CREATE TABLE `data_export_history` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `export_status` VARCHAR(64),
  `creation_date` DATETIME NOT NULL ,
  `export_date` DATETIME,
  `last_modification` TIMESTAMP,
  `wizard_results_id` INT(11),
  `export_config_id` INT(11),
  PRIMARY KEY (`id`),
  INDEX (`export_status`, `creation_date`),
  CONSTRAINT `history_export_config_fk` FOREIGN KEY (`export_config_id`) REFERENCES `data_export_configs` (`id`),
  CONSTRAINT `history_wizard_results_fk` FOREIGN KEY (`wizard_results_id`) REFERENCES `pdf_answers` (`id`)
) ENGINE=InnoDB;
