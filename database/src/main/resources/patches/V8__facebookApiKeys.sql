CREATE TABLE `facebook_api` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_key` varchar(128) NOT NULL,
  `app_secret` varchar(255) NOT NULL,
  `domain` varchar(128) NOT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;
INSERT INTO `facebook_api` (`id`, `app_key`, `app_secret`, `domain`, `updated`, `active`) VALUES (1, '232697990112981', 'acac029fad3b92c60344f0198c6966e4', 'www.ovf', '2012-01-20 14:41:54', 1);
INSERT INTO `facebook_api` (`id`, `app_key`, `app_secret`, `domain`, `updated`, `active`) VALUES (2, '176160572443769', '1ff6036d16f768eb2f1bc7d035f79836', 'overseasvotefoundation.org', '2012-01-20 14:45:06', 1);
INSERT INTO `facebook_api` (`id`, `app_key`, `app_secret`, `domain`, `updated`, `active`) VALUES (3, '209636885746043', 'e6bb2249121b5a7164fba0a278986471', 'staging.overseasvotefoundation.org', '2012-01-20 14:45:06', 1);
