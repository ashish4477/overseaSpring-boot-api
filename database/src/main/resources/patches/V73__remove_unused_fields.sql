ALTER TABLE `corrections_officers`
	DROP COLUMN `region_id`;

ALTER TABLE `officers`
	DROP COLUMN `region_id`;

ALTER TABLE `local_officials`
	DROP COLUMN `leo_title`,
	DROP COLUMN `leo_first_name`,
	DROP COLUMN `leo_initial`,
	DROP COLUMN `leo_last_name`,
	DROP COLUMN `leo_suffix`,
	DROP COLUMN `lovc_title`,
	DROP COLUMN `lovc_first_name`,
	DROP COLUMN `lovc_initial`,
	DROP COLUMN `lovc_last_name`,
	DROP COLUMN `lovc_suffix`,
	DROP COLUMN `leo_phone`,
	DROP COLUMN `leo_fax`,
	DROP COLUMN `leo_email`,
	DROP COLUMN `lovc_phone`,
	DROP COLUMN `lovc_fax`,
	DROP COLUMN `lovc_email`,
	DROP COLUMN `addc_title`,
	DROP COLUMN `addc_first_name`,
	DROP COLUMN `addc_initial`,
	DROP COLUMN `addc_last_name`,
	DROP COLUMN `addc_suffix`,
	DROP COLUMN `addc_phone`,
	DROP COLUMN `addc_email`;

ALTER TABLE `leo_corrections`
	DROP COLUMN `leo_title`,
	DROP COLUMN `leo_first_name`,
	DROP COLUMN `leo_initial`,
	DROP COLUMN `leo_last_name`,
	DROP COLUMN `leo_suffix`,
	DROP COLUMN `lovc_title`,
	DROP COLUMN `lovc_first_name`,
	DROP COLUMN `lovc_initial`,
	DROP COLUMN `lovc_last_name`,
	DROP COLUMN `lovc_suffix`,
	DROP COLUMN `leo_phone`,
	DROP COLUMN `leo_fax`,
	DROP COLUMN `leo_email`,
	DROP COLUMN `lovc_phone`,
	DROP COLUMN `lovc_fax`,
	DROP COLUMN `lovc_email`,
	DROP COLUMN `addc_title`,
	DROP COLUMN `addc_first_name`,
	DROP COLUMN `addc_initial`,
	DROP COLUMN `addc_last_name`,
	DROP COLUMN `addc_suffix`,
	DROP COLUMN `addc_phone`,
	DROP COLUMN `addc_email`;

ALTER TABLE `local_officials_to_officers`
	ALTER `local_official_id` DROP DEFAULT,
	ALTER `officer_id` DROP DEFAULT;
-- ALTER TABLE `local_officials_to_officers`
--	CHANGE COLUMN `local_official_id` `local_official_id` INT(11) NOT NULL FIRST,
-- 	CHANGE COLUMN `officer_id` `officer_id` INT(11) UNSIGNED NOT NULL AFTER `local_official_id`;
