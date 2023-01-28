CREATE TABLE `faces_mailing_list` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `face_id` INT(11) NOT NULL,
  `mailing_list_id` INT(11) UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `faces_to_mailing_list_fk`
  FOREIGN KEY (`face_id`)
  REFERENCES `faces_config` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `mailing_list_to_faces_fk`
  FOREIGN KEY (`mailing_list_id`)
  REFERENCES `mailing_list` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
