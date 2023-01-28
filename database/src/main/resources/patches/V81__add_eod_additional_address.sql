CREATE TABLE `eod_additional_address_type` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
);

CREATE TABLE `eod_additional_address` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `type_id` INT(11) NOT NULL,
  `address_id` INT(11) NOT NULL,
  `local_official_id` INT(11) NOT NULL,
  `ordering` INT(6) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `additional_address_fk_idx` (`address_id` ASC),
  INDEX `additional_address_type_fk_idx` (`type_id` ASC),
  INDEX `additional_address_to_leo_fk_idx` (`local_official_id` ASC),
  CONSTRAINT `additional_address_fk` FOREIGN KEY (`address_id`) REFERENCES `overseas_foundation`.`addresses` (`id`),
  CONSTRAINT `additional_address_type_fk` FOREIGN KEY (`type_id`) REFERENCES `overseas_foundation`.`eod_additional_address_type` (`id`),
  CONSTRAINT `additional_address_to_leo_fk` FOREIGN KEY (`local_official_id`) REFERENCES `overseas_foundation`.`local_officials` (`id`)
);

CREATE TABLE `correction_additional_address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `correction_id` INT(11) NOT NULL,
  `address_type` varchar(255)  NOT NULL DEFAULT '',
  `address_to` varchar(255)  NOT NULL DEFAULT '',
  `street1` varchar(255)  NOT NULL DEFAULT '',
  `street2` varchar(255)  NOT NULL DEFAULT '',
  `city` varchar(255)  NOT NULL DEFAULT '',
  `state` varchar(3)  NOT NULL DEFAULT '',
  `zip` varchar(6)  NOT NULL DEFAULT '',
  `zip4` varchar(5)  NOT NULL DEFAULT '',
  `ordering` INT(6) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `correction_additional_address_fk_idx` (`correction_id` ASC),
  CONSTRAINT `correction_additional_address_fk` FOREIGN KEY (`correction_id`) REFERENCES `overseas_foundation`.`leo_corrections` (`id`)
)