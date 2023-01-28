ALTER TABLE `wizard_result_addresses`
  ADD COLUMN tmp_description VARCHAR(255) NULL DEFAULT '';

UPDATE `wizard_result_addresses` SET tmp_description = `type`, `type`=`description`
WHERE `description` = 'STREET'
OR `description` = 'UNKNOWN'
OR `description` = 'MILITARY'
OR `description` = 'OVERSEAS'
OR `description` = 'DESCRIBED'
OR `description` = 'RURAL_ROUTE';

UPDATE `wizard_result_addresses` SET `description` = tmp_description
WHERE `description` = 'STREET'
OR `description` = 'UNKNOWN'
OR `description` = 'MILITARY'
OR `description` = 'OVERSEAS'
OR `description` = 'DESCRIBED'
OR `description` = 'RURAL_ROUTE';

ALTER TABLE `wizard_result_addresses`
  DROP COLUMN tmp_description;
