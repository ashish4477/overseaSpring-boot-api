CREATE TABLE `municipalities` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  `municipality_type` varchar(255) NOT NULL DEFAULT '',
  `county_id` int(11) NULL DEFAULT NULL,
  `state_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `municipalities_county_id` (`county_id`),
  KEY `municipalities_state_id` (`state_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE `counties` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  `county_type` varchar(255) NOT NULL DEFAULT '',
  `state_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `counties_state_id` (`state_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

ALTER TABLE `voting_regions`
  ADD COLUMN `municipality_id` int(11) NULL,
  ADD COLUMN `county_id` int(11) NULL,
  ADD KEY `regions_municipality_id` (`municipality_id`),
  ADD KEY `regions_county_id` (`county_id`);