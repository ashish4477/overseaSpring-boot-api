-- phpMyAdmin SQL Dump
-- version 2.10.2
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Apr 08, 2011 at 06:10 PM
-- Server version: 5.1.48
-- PHP Version: 5.3.3

SET FOREIGN_KEY_CHECKS=0;

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

--
-- Database: `overseas_foundation`
--

-- --------------------------------------------------------

--
-- Table structure for table `addresses`
--

CREATE TABLE `addresses` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address_to` varchar(255) DEFAULT NULL,
  `street1` varchar(255) DEFAULT NULL,
  `street2` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `state` varchar(3) DEFAULT NULL,
  `zip` varchar(6) DEFAULT NULL,
  `zip4` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `addresses`
--


-- --------------------------------------------------------

--
-- Table structure for table `answers`
--

CREATE TABLE `answers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(32) NOT NULL DEFAULT '',
  `user_id` int(11) NOT NULL DEFAULT '0',
  `field_id` int(11) NOT NULL DEFAULT '0',
  `value` text,
  `selected_value` int(11) DEFAULT NULL,
  `pdf_answers_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `answer_users_fk` (`user_id`),
  KEY `answer_fields_key` (`field_id`),
  KEY `pdf_answers_id` (`pdf_answers_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `answers`
--


-- --------------------------------------------------------

--
-- Table structure for table `corrections_svid`
--

CREATE TABLE `corrections_svid` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `svid_id` int(11) NOT NULL DEFAULT '0',
  `contact_title` varchar(16) NOT NULL DEFAULT '',
  `contact_first_name` varchar(255) NOT NULL DEFAULT '',
  `contact_initial` varchar(10) NOT NULL DEFAULT '',
  `contact_last_name` varchar(255) NOT NULL DEFAULT '',
  `contact_suffix` varchar(255) NOT NULL DEFAULT '',
  `contact_phone` varchar(255) NOT NULL DEFAULT '',
  `contact_fax` varchar(255) NOT NULL DEFAULT '',
  `contact_email` varchar(255) NOT NULL DEFAULT '',
  `website` varchar(255) NOT NULL DEFAULT '',
  `physical_address_id` int(11) DEFAULT NULL,
  `mailing_address_id` int(11) DEFAULT NULL,
  `confirmation` text,
  `deadline` text,
  `citizen_reg_post` tinyint(4) DEFAULT '0',
  `citizen_reg_fax` tinyint(4) DEFAULT '0',
  `citizen_reg_email` tinyint(4) DEFAULT '0',
  `citizen_reg_tel` tinyint(4) DEFAULT '0',
  `citizen_ballot_post` tinyint(4) DEFAULT '0',
  `citizen_ballot_fax` tinyint(4) DEFAULT '0',
  `citizen_ballot_email` tinyint(4) DEFAULT '0',
  `citizen_ballot_tel` tinyint(4) DEFAULT '0',
  `citizen_blank_post` tinyint(4) DEFAULT '0',
  `citizen_blank_fax` tinyint(4) DEFAULT '0',
  `citizen_blank_email` tinyint(4) DEFAULT '0',
  `citizen_blank_tel` tinyint(4) DEFAULT '0',
  `citizen_return_post` tinyint(4) DEFAULT '0',
  `citizen_return_fax` tinyint(4) DEFAULT '0',
  `citizen_return_email` tinyint(4) DEFAULT '0',
  `citizen_return_tel` tinyint(4) DEFAULT '0',
  `military_reg_post` tinyint(4) DEFAULT '0',
  `military_reg_fax` tinyint(4) DEFAULT '0',
  `military_reg_email` tinyint(4) DEFAULT '0',
  `military_reg_tel` tinyint(4) DEFAULT '0',
  `military_ballot_post` tinyint(4) DEFAULT '0',
  `military_ballot_fax` tinyint(4) DEFAULT '0',
  `military_ballot_email` tinyint(4) DEFAULT '0',
  `military_ballot_tel` tinyint(4) DEFAULT '0',
  `military_blank_post` tinyint(4) DEFAULT '0',
  `military_blank_fax` tinyint(4) DEFAULT '0',
  `military_blank_email` tinyint(4) DEFAULT '0',
  `military_blank_tel` tinyint(4) DEFAULT '0',
  `military_return_post` tinyint(4) DEFAULT '0',
  `military_return_fax` tinyint(4) DEFAULT '0',
  `military_return_email` tinyint(4) DEFAULT '0',
  `military_return_tel` tinyint(4) DEFAULT '0',
  `citizen_notes` text,
  `military_notes` text,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created` datetime DEFAULT NULL,
  `message` text,
  `status` int(5) DEFAULT NULL,
  `editor_id` int(11) DEFAULT NULL,
  `admin_notes` text,
  `contact_notes` text,
  PRIMARY KEY (`id`),
  KEY `corrections_to_svid_fk` (`svid_id`),
  KEY `corrections_svid_users_fk` (`editor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `corrections_svid`
--


-- --------------------------------------------------------

--
-- Table structure for table `countries`
--

CREATE TABLE `countries` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `abbreviation` varchar(3) NOT NULL,
  `active` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `countries`
--


-- --------------------------------------------------------

--
-- Table structure for table `dependents`
--

CREATE TABLE `dependents` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `dependents`
--


-- --------------------------------------------------------

--
-- Table structure for table `elections`
--

CREATE TABLE `elections` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `svid_id` int(11) DEFAULT NULL,
  `title` varchar(255) NOT NULL DEFAULT '',
  `held_on_str` varchar(255) NOT NULL DEFAULT '',
  `citizen_reg_str` varchar(255) NOT NULL DEFAULT '',
  `citizen_request_str` varchar(255) NOT NULL DEFAULT '',
  `citizen_return_str` varchar(255) NOT NULL DEFAULT '',
  `military_reg_str` varchar(255) NOT NULL DEFAULT '',
  `military_request_str` varchar(255) NOT NULL DEFAULT '',
  `military_return_str` varchar(255) NOT NULL DEFAULT '',
  `notes` text,
  `order_ticket` int(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `election_svid_fk` (`svid_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `elections`
--


-- --------------------------------------------------------

--
-- Table structure for table `faces_config`
--

CREATE TABLE `faces_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url_path` varchar(255) DEFAULT NULL,
  `path_prefix` varchar(255) DEFAULT NULL,
  `default` tinyint(4) NOT NULL DEFAULT '0',
  `scytl_integration` tinyint(4) NOT NULL DEFAULT '0',
  `envelope` TINYINT(4) NOT NULL DEFAULT '0',
  `external_content_url` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `faces_config`
--


-- --------------------------------------------------------

--
-- Table structure for table `fedex_countries`
--

CREATE TABLE `fedex_countries` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `pickup` tinyint(4) DEFAULT NULL,
  `dropoff` tinyint(4) DEFAULT NULL,
  `service_phone` varchar(255) DEFAULT NULL,
  `fedex_url` varchar(255) DEFAULT NULL,
  `service_url` varchar(255) DEFAULT NULL,
  `dropoff_url` varchar(255) DEFAULT NULL,
  `tc_url` varchar(255) DEFAULT NULL,
  `rate` float DEFAULT NULL,
  `local_currency` varchar(16) DEFAULT NULL,
  `exchange_rate` float DEFAULT NULL,
  `time` varchar(255) DEFAULT NULL,
  `account_number` varchar(32) DEFAULT NULL,
  `account_pass` varchar(32) DEFAULT NULL,
  `last_date_gmt` datetime DEFAULT NULL,
  `last_date_text` varchar(64) NOT NULL DEFAULT '',
  `gmt_offset` double NOT NULL DEFAULT '0',
  `group` varchar(16) DEFAULT NULL,
  `zip_pattern` varchar(128) DEFAULT NULL,
  `country_code_2` varchar(4) DEFAULT NULL,
  `comment` text NOT NULL,
  `last_date_gmt_text` varchar(32) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `fedex_countries`
--


-- --------------------------------------------------------

--
-- Table structure for table `fedex_labels`
--

CREATE TABLE `fedex_labels` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tracking_number` varchar(32) DEFAULT NULL,
  `filename` varchar(255) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `message` text,
  `payment` tinyint(4) DEFAULT NULL,
  `transaction` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `traking_indx` (`tracking_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `fedex_labels`
--


-- --------------------------------------------------------

--
-- Table structure for table `field_dependencies`
--

CREATE TABLE `field_dependencies` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field_id` int(11) DEFAULT NULL,
  `depends_on` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `field_key` (`field_id`),
  KEY `depends_on_key` (`depends_on`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `field_dependencies`
--


-- --------------------------------------------------------

--
-- Table structure for table `field_dictionaries`
--

CREATE TABLE `field_dictionaries` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(16) DEFAULT NULL,
  `type_id` int(11) DEFAULT NULL,
  `field_id` int(11) DEFAULT NULL,
  `string_value` text,
  `external_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `field_dictionaries`
--


-- --------------------------------------------------------

--
-- Table structure for table `leo_corrections`
--

CREATE TABLE `leo_corrections` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `leo_id` int(11) NOT NULL DEFAULT '0',
  `physical_address_to` varchar(255) DEFAULT NULL,
  `physical_street1` varchar(255) DEFAULT NULL,
  `physical_street2` varchar(255) DEFAULT NULL,
  `physical_city` varchar(255) DEFAULT NULL,
  `physical_state` varchar(3) DEFAULT NULL,
  `physical_zip` varchar(6) DEFAULT NULL,
  `physical_zip4` varchar(5) DEFAULT NULL,
  `mailing_address_to` varchar(255) DEFAULT NULL,
  `mailing_street1` varchar(255) DEFAULT NULL,
  `mailing_street2` varchar(255) DEFAULT NULL,
  `mailing_city` varchar(255) DEFAULT NULL,
  `mailing_state` varchar(3) DEFAULT NULL,
  `mailing_zip` varchar(6) DEFAULT NULL,
  `mailing_zip4` varchar(5) DEFAULT NULL,
  `leo_title` varchar(16) DEFAULT NULL,
  `leo_first_name` varchar(255) DEFAULT NULL,
  `leo_initial` varchar(10) DEFAULT NULL,
  `leo_last_name` varchar(255) DEFAULT NULL,
  `leo_suffix` varchar(255) DEFAULT NULL,
  `lovc_title` varchar(16) DEFAULT NULL,
  `lovc_first_name` varchar(255) DEFAULT NULL,
  `lovc_initial` varchar(10) DEFAULT NULL,
  `lovc_last_name` varchar(255) DEFAULT NULL,
  `lovc_suffix` varchar(255) DEFAULT NULL,
  `leo_phone` varchar(255) DEFAULT NULL,
  `leo_fax` varchar(255) DEFAULT NULL,
  `leo_email` varchar(255) DEFAULT NULL,
  `dsn_phone` varchar(255) DEFAULT NULL,
  `lovc_phone` varchar(255) DEFAULT NULL,
  `lovc_fax` varchar(255) DEFAULT NULL,
  `lovc_email` varchar(255) DEFAULT NULL,
  `addc_title` varchar(16) DEFAULT NULL,
  `addc_first_name` varchar(255) DEFAULT NULL,
  `addc_initial` varchar(10) DEFAULT NULL,
  `addc_last_name` varchar(255) DEFAULT NULL,
  `addc_suffix` varchar(255) DEFAULT NULL,
  `addc_phone` varchar(255) DEFAULT NULL,
  `addc_email` varchar(255) DEFAULT NULL,
  `website` varchar(255) DEFAULT NULL,
  `hours` varchar(255) DEFAULT NULL,
  `further_instruction` text,
  `created` datetime DEFAULT NULL,
  `message` text,
  `submitter_name` varchar(64) NOT NULL DEFAULT '',
  `submitter_email` varchar(64) NOT NULL DEFAULT '',
  `submitter_phone` varchar(64) NOT NULL DEFAULT '',
  `status` int(5) DEFAULT NULL,
  `editor_id` int(11) DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `corrections_leo_fk` (`leo_id`),
  KEY `corrections_admin_fk` (`editor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `leo_corrections`
--


-- --------------------------------------------------------

--
-- Table structure for table `local_officials`
--

CREATE TABLE `local_officials` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `region_id` int(11) NOT NULL DEFAULT '0',
  `physical_address_id` int(11) DEFAULT NULL,
  `mailing_address_id` int(11) DEFAULT NULL,
  `leo_title` varchar(16) DEFAULT NULL,
  `leo_first_name` varchar(255) DEFAULT NULL,
  `leo_initial` varchar(10) DEFAULT NULL,
  `leo_last_name` varchar(255) DEFAULT NULL,
  `leo_suffix` varchar(255) DEFAULT NULL,
  `lovc_title` varchar(16) DEFAULT NULL,
  `lovc_first_name` varchar(255) DEFAULT NULL,
  `lovc_initial` varchar(10) DEFAULT NULL,
  `lovc_last_name` varchar(255) DEFAULT NULL,
  `lovc_suffix` varchar(255) DEFAULT NULL,
  `leo_phone` varchar(255) DEFAULT NULL,
  `leo_fax` varchar(255) DEFAULT NULL,
  `leo_email` varchar(255) DEFAULT NULL,
  `dsn_phone` varchar(255) DEFAULT NULL,
  `lovc_phone` varchar(255) DEFAULT NULL,
  `lovc_fax` varchar(255) DEFAULT NULL,
  `lovc_email` varchar(255) DEFAULT NULL,
  `addc_title` varchar(16) DEFAULT NULL,
  `addc_first_name` varchar(255) DEFAULT NULL,
  `addc_initial` varchar(10) DEFAULT NULL,
  `addc_last_name` varchar(255) DEFAULT NULL,
  `addc_suffix` varchar(255) DEFAULT NULL,
  `addc_phone` varchar(255) DEFAULT NULL,
  `addc_email` varchar(255) DEFAULT NULL,
  `website` varchar(255) DEFAULT NULL,
  `hours` varchar(255) DEFAULT NULL,
  `further_instruction` text,
  `status` int(5) DEFAULT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `leo_region_id` (`region_id`),
  KEY `local_officials_physical_address_fk` (`physical_address_id`),
  KEY `local_officials_mailing_address_fk` (`mailing_address_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `local_officials`
--


-- --------------------------------------------------------

--
-- Table structure for table `mailinglist`
--

CREATE TABLE `mailinglist` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `first_name` varchar(100) NOT NULL DEFAULT '',
  `last_name` varchar(100) DEFAULT NULL,
  `email` varchar(100) NOT NULL DEFAULT '',
  `state` int(10) unsigned DEFAULT NULL,
  `region` int(10) unsigned DEFAULT NULL,
  `country` int(10) unsigned DEFAULT NULL,
  `url` varchar(255) NOT NULL DEFAULT '',
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `mailinglist`
--


-- --------------------------------------------------------

--
-- Table structure for table `pdf_answers`
--

CREATE TABLE `pdf_answers` (
	 `id` int(11) NOT NULL AUTO_INCREMENT,
	 `created` datetime NOT NULL DEFAULT '1900-01-01 00:00:00',
	 `updated` datetime NOT NULL DEFAULT '1900-01-01 00:00:00',
	 `flow_type` varchar(12) NOT NULL,
	 `face_name` varchar(64) NOT NULL,
	 `page_title` text NOT NULL,
	 `user_id` int(11) DEFAULT NULL,
	 `downloaded` tinyint(4) NOT NULL DEFAULT '0',
	 `username` varchar(128) DEFAULT NULL,
	 `first_name` varchar(128) DEFAULT NULL,
	 `last_name` varchar(255) DEFAULT NULL,
	 `middle_name` varchar(255) DEFAULT NULL,
	 `previous_name` varchar(255) DEFAULT NULL,
	 `suffix` varchar(32) DEFAULT NULL,
	 `phone` varchar(32) DEFAULT NULL,
	 `alternate_email` VARCHAR(255) NULL DEFAULT NULL,
	 `voting_region` int(11) DEFAULT NULL,
  	 `voting_region_name` varchar(128) NOT NULL DEFAULT '',
	 `voting_region_state` char(2) NOT NULL DEFAULT '',
	 `voter_type` varchar(32) DEFAULT NULL,
	 `voter_history` varchar(64) DEFAULT NULL,
	 `ballot_pref` varchar(255) DEFAULT NULL,
	 `birth_year` int(11) DEFAULT NULL,
	 `birth_month` int(11) DEFAULT NULL,
	 `race` varchar(32) DEFAULT NULL,
	 `ethnicity` varchar(32) DEFAULT NULL,
	 `gender` char(1) DEFAULT NULL,
	 `party` varchar(32) DEFAULT NULL,
	 `voting_address_street1` varchar(255) DEFAULT NULL,
	 `voting_address_street2` varchar(255) DEFAULT NULL,
	 `voting_address_city` varchar(255) DEFAULT NULL,
	 `voting_address_state` varchar(32) DEFAULT NULL,
	 `voting_address_zip` varchar(10) DEFAULT NULL,
	 `voting_address_zip4` varchar(5) DEFAULT NULL,
	 `voting_address_country` varchar(255) DEFAULT NULL,
	 `voting_address_description` TEXT NULL DEFAULT NULL,
     `voting_address_type` VARCHAR(32) NOT NULL DEFAULT 'UNKNOWN',
	 `current_address_street1` varchar(255) DEFAULT NULL,
	 `current_address_street2` varchar(255) DEFAULT NULL,
	 `current_address_city` varchar(255) DEFAULT NULL,
	 `current_address_state` varchar(32) DEFAULT NULL,
	 `current_address_zip` varchar(10) DEFAULT NULL,
	 `current_address_zip4` varchar(5) DEFAULT NULL,
	 `current_address_country` varchar(255) DEFAULT NULL,
	 `current_address_description` TEXT NULL DEFAULT NULL,
     `current_address_type` VARCHAR(32) NOT NULL DEFAULT 'UNKNOWN',
	 `forwarding_address_street1` varchar(255) DEFAULT NULL,
	 `forwarding_address_street2` varchar(255) DEFAULT NULL,
	 `forwarding_address_city` varchar(255) DEFAULT NULL,
	 `forwarding_address_state` varchar(32) DEFAULT NULL,
	 `forwarding_address_zip` varchar(10) DEFAULT NULL,
	 `forwarding_address_zip4` varchar(5) DEFAULT NULL,
	 `forwarding_address_country` varchar(255) DEFAULT NULL,
	 `forwarding_address_type` VARCHAR(32) NOT NULL DEFAULT 'UNKNOWN',
     `forwarding_address_description` TEXT NULL,
	 PRIMARY KEY (`id`),
	 KEY `user_id` (`user_id`),
	 KEY `voting_region` (`voting_region`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `pdf_answers`
--


-- --------------------------------------------------------

--
-- Table structure for table `pdf_fillings`
--

CREATE TABLE `pdf_fillings` (
  `id` int(11) NOT NULL DEFAULT '0',
  `name` varchar(255) NOT NULL DEFAULT '',
  `in_pdf_name` varchar(255) DEFAULT NULL,
  `text` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `pdf_fillings`
--


-- --------------------------------------------------------

--
-- Table structure for table `queries`
--

CREATE TABLE `queries` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `description` text,
  `date_from` date DEFAULT NULL,
  `date_to` date DEFAULT NULL,
  `apply_faces` tinyint(4) NOT NULL DEFAULT '0',
  `user_id` int(11) DEFAULT NULL,
  `log_type` varchar(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `queries_users_fk` (`user_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

--
-- Dumping data for table `queries`
--


-- --------------------------------------------------------

--
-- Table structure for table `query_elements`
--

CREATE TABLE `query_elements` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `query_id` int(11) NOT NULL DEFAULT '0',
  `ordering_number` int(6) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `queries_fk` (`query_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

--
-- Dumping data for table `query_elements`
--


-- --------------------------------------------------------

--
-- Table structure for table `query_faces`
--

CREATE TABLE `query_faces` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `query_id` int(11) NOT NULL DEFAULT '0',
  `face_name` varchar(127) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `query_faces_fk` (`query_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `query_faces`
--


-- --------------------------------------------------------

--
-- Table structure for table `query_given_answers`
--

CREATE TABLE `query_given_answers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `term_id` int(11) NOT NULL DEFAULT '0',
  `value` varchar(255) DEFAULT NULL,
  `predefined_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `query_answers_term_fk` (`term_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `query_given_answers`
--


-- --------------------------------------------------------

--
-- Table structure for table `query_terms`
--

CREATE TABLE `query_terms` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `element_id` int(11) NOT NULL DEFAULT '0',
  `field_id` int(11) NOT NULL DEFAULT '0',
  `given_answer` varchar(255) NOT NULL DEFAULT '',
  `given_predefined_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `term_elements_fk` (`element_id`),
  KEY `term_fields_fk` (`field_id`),
  KEY `terms_dictionaries_fk` (`given_predefined_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

--
-- Dumping data for table `query_terms`
--


-- --------------------------------------------------------

--
-- Table structure for table `questionary_pages`
--

CREATE TABLE `questionary_pages` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `number` int(6) DEFAULT NULL,
  `title` text,
  `stepNumber` int(6) NOT NULL DEFAULT '0',
  `popupBubble` text NOT NULL,
  `type` VARCHAR( 32 ) NOT NULL DEFAULT '',
  `form_type` VARCHAR(32) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

--
-- Dumping data for table `questionary_pages`
--


-- --------------------------------------------------------

--
-- Table structure for table `questions`
--

CREATE TABLE `questions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `page_id` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `order` int(6) DEFAULT NULL,
  `export_to_pdf` tinyint(4) DEFAULT '0',
  `number_in_pdf` int(6) DEFAULT '0',
  `title` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

--
-- Dumping data for table `questions`
--


-- --------------------------------------------------------

--
-- Table structure for table `question_dependencies`
--

CREATE TABLE `question_dependencies` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `kind` varchar(4) DEFAULT NULL,
  `variant_id` int(11) DEFAULT NULL,
  `depends_on_id` int(11) DEFAULT NULL,
  `condition_id` int(11) DEFAULT NULL,
  `face_id` int(11) DEFAULT NULL,
  `field_name` VARCHAR( 32 ) NULL DEFAULT NULL,
  `field_value` VARCHAR( 64 ) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `variant_id_key` (`variant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `question_dependencies`
--


-- --------------------------------------------------------

--
-- Table structure for table `question_fields`
--

CREATE TABLE `question_fields` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type_id` int(11) DEFAULT NULL,
  `question_variant_id` int(11) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `order_number` int(6) DEFAULT NULL,
  `encoded` tinyint(4) DEFAULT NULL,
  `security` tinyint(4) DEFAULT NULL,
  `required` tinyint(4) DEFAULT NULL,
  `verification_pattern` varchar(255) DEFAULT NULL,
  `in_pdf_name` varchar(255) NOT NULL DEFAULT '',
  `help_text` text,
  `additional_help` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `question_fields`
--


-- --------------------------------------------------------

--
-- Table structure for table `question_field_types`
--

CREATE TABLE `question_field_types` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(16) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `view_template` varchar(255) NOT NULL DEFAULT '''''',
  `use_generic_options` tinyint(4) DEFAULT '0',
  `use_pattern` tinyint(4) DEFAULT '0',
  `admin_template` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `question_field_types`
--


-- --------------------------------------------------------

--
-- Table structure for table `question_variants`
--

CREATE TABLE `question_variants` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `question_id` int(11) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `description` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `question_variants`
--


-- --------------------------------------------------------

--
-- Table structure for table `roles`
--

CREATE TABLE `roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

--
-- Dumping data for table `roles`
--


-- --------------------------------------------------------

--
-- Table structure for table `states`
--

CREATE TABLE `states` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  `abbr` varchar(3) NOT NULL DEFAULT '',
  `fips_code` int(6) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

--
-- Dumping data for table `states`
--


-- --------------------------------------------------------

--
-- Table structure for table `state_voter_info`
--

CREATE TABLE `state_voter_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `state_id` int(11) NOT NULL DEFAULT '0',
  `contact_title` varchar(16) NOT NULL DEFAULT '',
  `contact_first_name` varchar(255) NOT NULL DEFAULT '',
  `contact_initial` varchar(10) NOT NULL DEFAULT '',
  `contact_last_name` varchar(255) NOT NULL DEFAULT '',
  `contact_suffix` varchar(255) NOT NULL DEFAULT '',
  `contact_phone` varchar(255) NOT NULL DEFAULT '',
  `contact_fax` varchar(255) NOT NULL DEFAULT '',
  `contact_email` varchar(255) NOT NULL DEFAULT '',
  `website` varchar(255) NOT NULL DEFAULT '',
  `physical_address_id` int(11) DEFAULT NULL,
  `mailing_address_id` int(11) DEFAULT NULL,
  `confirmation` text,
  `deadline` text,
  `citizen_reg_post` tinyint(4) DEFAULT '0',
  `citizen_reg_fax` tinyint(4) DEFAULT '0',
  `citizen_reg_email` tinyint(4) DEFAULT '0',
  `citizen_reg_tel` tinyint(4) DEFAULT '0',
  `citizen_ballot_post` tinyint(4) DEFAULT '0',
  `citizen_ballot_fax` tinyint(4) DEFAULT '0',
  `citizen_ballot_email` tinyint(4) DEFAULT '0',
  `citizen_ballot_tel` tinyint(4) DEFAULT '0',
  `citizen_blank_post` tinyint(4) DEFAULT '0',
  `citizen_blank_fax` tinyint(4) DEFAULT '0',
  `citizen_blank_email` tinyint(4) DEFAULT '0',
  `citizen_blank_tel` tinyint(4) DEFAULT '0',
  `citizen_return_post` tinyint(4) DEFAULT '0',
  `citizen_return_fax` tinyint(4) DEFAULT '0',
  `citizen_return_email` tinyint(4) DEFAULT '0',
  `citizen_return_tel` tinyint(4) DEFAULT '0',
  `military_reg_post` tinyint(4) DEFAULT '0',
  `military_reg_fax` tinyint(4) DEFAULT '0',
  `military_reg_email` tinyint(4) DEFAULT '0',
  `military_reg_tel` tinyint(4) DEFAULT '0',
  `military_ballot_post` tinyint(4) DEFAULT '0',
  `military_ballot_fax` tinyint(4) DEFAULT '0',
  `military_ballot_email` tinyint(4) DEFAULT '0',
  `military_ballot_tel` tinyint(4) DEFAULT '0',
  `military_blank_post` tinyint(4) DEFAULT '0',
  `military_blank_fax` tinyint(4) DEFAULT '0',
  `military_blank_email` tinyint(4) DEFAULT '0',
  `military_blank_tel` tinyint(4) DEFAULT '0',
  `military_return_post` tinyint(4) DEFAULT '0',
  `military_return_fax` tinyint(4) DEFAULT '0',
  `military_return_email` tinyint(4) DEFAULT '0',
  `military_return_tel` tinyint(4) DEFAULT '0',
  `citizen_notes` text,
  `military_notes` text,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `admin_notes` text,
  `contact_notes` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `state_unique_indx` (`state_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

--
-- Dumping data for table `state_voter_info`
--


-- --------------------------------------------------------

CREATE TABLE `user_addresses` (
`id` int( 11 ) NOT NULL AUTO_INCREMENT ,
`address_to` varchar( 255 ) DEFAULT NULL ,
`street1` varchar( 255 ) DEFAULT NULL ,
`street2` varchar( 255 ) DEFAULT NULL ,
`city` varchar( 255 ) DEFAULT NULL ,
`state` varchar( 32 ) DEFAULT NULL ,
`zip` varchar( 10 ) DEFAULT NULL ,
`zip4` varchar( 5 ) DEFAULT NULL ,
`country` varchar( 255 ) DEFAULT NULL ,
`type` varchar( 32 ) NOT NULL DEFAULT 'UNKNOWN',
`description` TEXT NULL,
PRIMARY KEY ( `id` )
) ENGINE = InnoDB DEFAULT CHARSET = utf8;

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(128) NOT NULL DEFAULT '',
  `password` varchar(35) NOT NULL DEFAULT '',
  `first_name` varchar(128) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `middle_name` varchar(255) DEFAULT NULL,
  `suffix` varchar(32) DEFAULT NULL,
  `previous_name` varchar(255) DEFAULT NULL,
  `real_email` varchar(255) DEFAULT NULL,
  `scytl_integration` tinyint(4) NOT NULL DEFAULT '0',
  `facebook_integration` TINYINT( 1 ) NOT NULL DEFAULT '0',
  `scytl_password` varchar(64) NOT NULL DEFAULT '',
  `last_updated` datetime NOT NULL DEFAULT '1900-01-01 00:00:00',
  `created` datetime NOT NULL DEFAULT '1900-01-01 00:00:00',
  `phone` varchar(32) NOT NULL DEFAULT '',
  `alternate_email` VARCHAR(255) NOT NULL DEFAULT '',
  `current_address` int(11) DEFAULT NULL,
  `voting_address` int(11) DEFAULT NULL,
  `forwarding_address` int(11) DEFAULT NULL,
  `voter_type` varchar(32) DEFAULT NULL,
  `birth_year` int(11) NOT NULL DEFAULT '0',
  `birth_month` int(11) NOT NULL DEFAULT '0',
  `party` varchar(32) NOT NULL DEFAULT '',
  `race` varchar(32) DEFAULT NULL,
  `ethnicity` varchar(32) DEFAULT NULL,
  `gender` char(1) DEFAULT NULL,
  `ballot_pref` varchar(255) DEFAULT NULL,
  `voting_region` int(11) DEFAULT NULL,
  `face_name` varchar(64) NOT NULL DEFAULT '',
  `voter_history` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  KEY `current_address` (`current_address`),
  KEY `voting_address` (`voting_address`),
  KEY `forwarding_address` (`forwarding_address`),
  KEY `voter_type` (`voter_type`),
  KEY `voting_region` (`voting_region`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

--
-- Dumping data for table `users`
--


-- --------------------------------------------------------

--
-- Table structure for table `users_admin_faces`
--

CREATE TABLE `users_admin_faces` (
  `user_id` int(11) NOT NULL DEFAULT '0',
  `face_id` int(11) NOT NULL DEFAULT '0',
  KEY `admin_faces_users_fk` (`user_id`),
  KEY `admin_faces_faces_fk` (`face_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `users_admin_faces`
--


-- --------------------------------------------------------

--
-- Table structure for table `users_roles`
--

CREATE TABLE `users_roles` (
  `user_id` int(11) NOT NULL DEFAULT '0',
  `role_id` int(11) NOT NULL DEFAULT '0',
  KEY `user_id` (`user_id`),
  KEY `role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `users_roles`
--


-- --------------------------------------------------------

--
-- Table structure for table `voting_regions`
--

CREATE TABLE `voting_regions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  `state_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `regions_state_id` (`state_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

--
-- Table structure for table `face_flow_instructions`
--

CREATE TABLE `face_flow_instructions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `face_flow` varchar(64) NOT NULL,
  `face_config_id` int(11) NOT NULL,
  `text` longtext NOT NULL,
  `updated_time` datetime NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

--
-- Table structure for table `face_flow_logos`
--

CREATE TABLE `face_flow_logos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `face_config_id` int(11) NOT NULL,
  `logo` mediumblob NOT NULL,
  `content_type` varchar(64) NOT NULL,
  `updated_time` datetime NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


--
-- Dumping data for table `voting_regions`
--


--
-- Constraints for dumped tables
--

--
-- Constraints for table `answers`
--
ALTER TABLE `answers`
  ADD CONSTRAINT `answers_ibfk_1` FOREIGN KEY (`pdf_answers_id`) REFERENCES `pdf_answers` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `corrections_svid`
--
ALTER TABLE `corrections_svid`
  ADD CONSTRAINT `corrections_svid_users_fk` FOREIGN KEY (`editor_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `corrections_to_svid_fk` FOREIGN KEY (`svid_id`) REFERENCES `state_voter_info` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `elections`
--
ALTER TABLE `elections`
  ADD CONSTRAINT `election_svid_fk` FOREIGN KEY (`svid_id`) REFERENCES `state_voter_info` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `leo_corrections`
--
ALTER TABLE `leo_corrections`
  ADD CONSTRAINT `corrections_admin_fk` FOREIGN KEY (`editor_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `corrections_leo_fk` FOREIGN KEY (`leo_id`) REFERENCES `local_officials` (`id`);

--
-- Constraints for table `local_officials`
--
ALTER TABLE `local_officials`
  ADD CONSTRAINT `local_officials_mailing_address_fk` FOREIGN KEY (`mailing_address_id`) REFERENCES `addresses` (`id`),
  ADD CONSTRAINT `local_officials_physical_address_fk` FOREIGN KEY (`physical_address_id`) REFERENCES `addresses` (`id`),
  ADD CONSTRAINT `local_officials_region_fk` FOREIGN KEY (`region_id`) REFERENCES `voting_regions` (`id`);

--
-- Constraints for table `pdf_answers`
--
ALTER TABLE `pdf_answers`
  ADD CONSTRAINT `pdf_answers_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `pdf_answers_ibfk_2` FOREIGN KEY (`voting_region`) REFERENCES `voting_regions` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `queries`
--
ALTER TABLE `queries`
  ADD CONSTRAINT `queries_users_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `query_elements`
--
ALTER TABLE `query_elements`
  ADD CONSTRAINT `queries_fk` FOREIGN KEY (`query_id`) REFERENCES `queries` (`id`);

--
-- Constraints for table `query_faces`
--
ALTER TABLE `query_faces`
  ADD CONSTRAINT `query_faces_fk` FOREIGN KEY (`query_id`) REFERENCES `queries` (`id`);

--
-- Constraints for table `query_given_answers`
--
ALTER TABLE `query_given_answers`
  ADD CONSTRAINT `query_answers_term_fk` FOREIGN KEY (`term_id`) REFERENCES `query_terms` (`id`);

--
-- Constraints for table `query_terms`
--
ALTER TABLE `query_terms`
  ADD CONSTRAINT `terms_dictionaries_fk` FOREIGN KEY (`given_predefined_id`) REFERENCES `field_dictionaries` (`id`),
  ADD CONSTRAINT `term_elements_fk` FOREIGN KEY (`element_id`) REFERENCES `query_elements` (`id`),
  ADD CONSTRAINT `term_fields_fk` FOREIGN KEY (`field_id`) REFERENCES `question_fields` (`id`);

--
-- Constraints for table `state_voter_info`
--
ALTER TABLE `state_voter_info`
  ADD CONSTRAINT `voter_info_state_fk` FOREIGN KEY (`state_id`) REFERENCES `states` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `users_ibfk_4` FOREIGN KEY (`voting_region`) REFERENCES `voting_regions` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `users_ibfk_1` FOREIGN KEY (`current_address`) REFERENCES `user_addresses` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `users_ibfk_2` FOREIGN KEY (`voting_address`) REFERENCES `user_addresses` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `users_ibfk_3` FOREIGN KEY (`forwarding_address`) REFERENCES `user_addresses` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `users_admin_faces`
--
ALTER TABLE `users_admin_faces`
  ADD CONSTRAINT `admin_faces_faces_fk` FOREIGN KEY (`face_id`) REFERENCES `faces_config` (`id`),
  ADD CONSTRAINT `admin_faces_users_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `users_roles`
--
ALTER TABLE `users_roles`
  ADD CONSTRAINT `users_roles_role_fk` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  ADD CONSTRAINT `users_roles_user_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `voting_regions`
--
ALTER TABLE `voting_regions`
  ADD CONSTRAINT `regions_state_fk` FOREIGN KEY (`state_id`) REFERENCES `states` (`id`);

--
-- Constraints for table `face_flow_logos`
--
ALTER TABLE `face_flow_logos`
  ADD CONSTRAINT `06062011_01_fk` FOREIGN KEY (`face_config_id`) REFERENCES `faces_config` (`id`),
  ADD CONSTRAINT `06062011_02_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `face_flow_instructions`
--
ALTER TABLE `face_flow_instructions`
  ADD CONSTRAINT `06062011_03_fk` FOREIGN KEY (`face_config_id`) REFERENCES `faces_config` (`id`),
  ADD CONSTRAINT `06062011_04_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

SET FOREIGN_KEY_CHECKS=1;
