SELECT now() AS "Time", 'Load temporary table from regions data' AS "Action";
CREATE TEMPORARY TABLE `ldv_regions` (
  `region_id` int(11) NULL,
  `region_name` varchar(255) NOT NULL,
  `county_name` varchar(255) NOT NULL,
  `municipality_name` varchar(255) NOT NULL,
  `municipality_type` varchar(255) NOT NULL,
  `state_name` varchar(255) NOT NULL,
  `state_abbr` varchar(255) NOT NULL,
  PRIMARY KEY (`region_id`),
  INDEX ldv_county (`county_name`),
  INDEX ldv_municipality (`municipality_name`, `municipality_type`),
  INDEX ldv_state (`state_abbr`, `state_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
LOAD DATA LOCAL INFILE '/tmp/voting-regions.csv' 
  INTO TABLE `ldv_regions`
  FIELDS TERMINATED BY ','
  ENCLOSED BY '"'
  LINES TERMINATED BY '\n'
  IGNORE 1 ROWS;
CREATE TEMPORARY TABLE `ldv_regions_2` LIKE `ldv_regions`;
INSERT INTO `ldv_regions_2` SELECT * FROM `ldv_regions`;

SELECT now() AS "Time", 'Update counties from temporary table' AS "Action";
INSERT INTO `counties`
  (`name`, `county_type`, `state_id`)
  SELECT ldv.`county_name`,
         CASE ldv.`state_abbr`
           WHEN 'LA' THEN 'Parish'
           ELSE 'County'
         END,
         MIN(s.id)
    FROM `ldv_regions` ldv
    JOIN `states` s ON s.`abbr` = ldv.`state_abbr`
   WHERE ldv.`county_name` != ''
     AND NOT EXISTS (SELECT ldv2.`region_id`
                       FROM `ldv_regions_2` ldv2
                      WHERE ldv2.`region_id` < ldv.`region_id`
                        AND ldv2.`state_abbr` = ldv.`state_abbr`
                        AND ldv2.`county_name` = ldv.`county_name`)
     AND NOT EXISTS (SELECT c2.`id`
                       FROM `counties` c2
                      WHERE c2.`name` = ldv.`county_name`
                        AND c2.state_id = s.`id`)
   GROUP BY ldv.`county_name`, ldv.`state_abbr`;
                        
SELECT now() AS "Time", 'Ensure that counties only exist once' AS "Action";
SELECT c.*
  FROM `counties` c
  JOIN `counties` c2 ON c2.`id` != c.`id`
                    AND c2.`name` = c.`name`
                    AND c2.`state_id` = c.`state_id`
 LIMIT 10;

SELECT now() AS "Time", 'Update municipalities from temporary table' AS "Action";
INSERT INTO `municipalities`
  (`name`, `municipality_type`, `state_id`)
  SELECT ldv.`municipality_name`,
         ldv.`municipality_type`,
         s.`id`
    FROM `ldv_regions` ldv
    JOIN `states` s ON s.`abbr` = ldv.`state_abbr`
   WHERE ldv.`municipality_name` != ''
     AND ldv.`county_name` = ''
     AND NOT EXISTS (SELECT ldv2.`region_id`
                       FROM `ldv_regions_2` ldv2
                      WHERE ldv2.`region_id` < ldv.`region_id`
                        AND ldv2.`state_abbr` = ldv.`state_abbr`
                        AND ldv2.`county_name` = ldv.`county_name`
                        AND ldv2.`municipality_name` = ldv.`municipality_name`
                        AND ldv2.`municipality_type` = ldv.`municipality_type`)
     AND NOT EXISTS (SELECT m2.`id`
                       FROM `municipalities` m2
                      WHERE m2.`name` = ldv.`municipality_name`
                        AND m2.`municipality_type` = ldv.`municipality_type`
                        AND m2.`state_id` = s.`id`
                        AND m2.`county_id` IS NULL);
INSERT INTO `municipalities`
  (`name`, `municipality_type`, `county_id`, `state_id`)
  SELECT ldv.`municipality_name`,
         ldv.`municipality_type`,
         c.`id`,
         s.`id`
    FROM `ldv_regions` ldv
    JOIN `states` s ON s.`abbr` = ldv.`state_abbr`
    JOIN `counties` c ON c.`name` = ldv.`county_name` AND c.`state_id` = s.`id`
   WHERE ldv.`municipality_name` != ''
     AND ldv.`county_name` != ''
     AND NOT EXISTS (SELECT ldv2.`region_id`
                       FROM `ldv_regions_2` ldv2
                      WHERE ldv2.`region_id` < ldv.`region_id`
                        AND ldv2.`state_abbr` = ldv.`state_abbr`
                        AND ldv2.`county_name` = ldv.`county_name`
                        AND ldv2.`municipality_name` = ldv.`municipality_name`
                        AND ldv2.`municipality_type` = ldv.`municipality_type`)
     AND NOT EXISTS (SELECT m2.`id`
                       FROM `municipalities` m2
                      WHERE m2.`name` = ldv.`municipality_name`
                        AND m2.`municipality_type` = ldv.`municipality_type`
                        AND m2.`state_id` = s.`id`
                        AND m2.`county_id` = c.`id`);

SELECT now() AS "Time", 'Ensure that municipalities only exist once' AS "Action";
SELECT m.*
  FROM `municipalities` m
  JOIN `municipalities` m2 ON m2.`id` < m.`id`
                          AND m2.`name` = m.`name`
                          AND m2.`municipality_type` = m.`municipality_type`
                          AND CASE
						        WHEN m.`county_id` IS NULL THEN m2.`county_id` IS NULL
							    ELSE m2.`county_id` = m.`county_id`
						      END
                          AND m2.`state_id` = m.`state_id`
 LIMIT 10;

SELECT now() AS "Time", 'Update voting regions from temporary table' AS "Action";
UPDATE `voting_regions` vr
  JOIN `states` s ON s.`id` = vr.`state_id`
  JOIN `ldv_regions` ldv ON ldv.`region_id` = vr.`id`
  SET vr.`name` = ldv.`region_name`
  WHERE ldv.`region_id` IS NOT NULL AND ldv.`region_id` != 0;

SELECT now() AS "Time", 'Update voting region counties from temporary table' AS "Action";
UPDATE `voting_regions` vr
  JOIN `states` s ON s.`id` = vr.`state_id`
  JOIN `ldv_regions` ldv ON ldv.`region_id` = vr.`id`
  JOIN `counties` c ON c.`state_id` = vr.`state_id`
                    AND c.`name` = ldv.`county_name`
  SET vr.`county_id` = c.`id`
  WHERE ldv.`county_name` != '';

SELECT now() AS "Time", 'Update voting region municipalities from temporary table' AS "Action";
UPDATE `voting_regions` vr
  JOIN `states` s ON s.`id` = vr.`state_id`
  JOIN `ldv_regions` ldv ON ldv.`region_id` = vr.`id`
  JOIN `municipalities` m ON m.`state_id` = vr.`state_id`
                         AND m.`county_id` = vr.`county_id`
                         AND m.`name` = ldv.`municipality_name`
                         AND m.`municipality_type` = ldv.`municipality_type`
  SET vr.`municipality_id` = m.`id`
  WHERE ldv.`municipality_name` != '' AND ldv.`county_name` != '';
UPDATE `voting_regions` vr
  JOIN `states` s ON s.`id` = vr.`state_id`
  JOIN `ldv_regions` ldv ON ldv.`region_id` = vr.`id`
  JOIN `municipalities` m ON m.`state_id` = vr.`state_id`
                         AND m.`county_id` IS NULL
                         AND m.`name` = ldv.`municipality_name`
                         AND m.`municipality_type` = ldv.`municipality_type`
  SET vr.`municipality_id` = m.`id`
  WHERE ldv.`municipality_name` != '' AND ldv.`county_name` = '';

SELECT now() AS "Time", 'Create new voting regions from temporary table' AS "Action";
INSERT INTO `voting_regions`
  (`name`, `state_id`, `county_id`, `municipality_id`)
  SELECT ldv.`region_name`,
         s.`id`,
         c.`id`,
         m.`id`
    FROM `ldv_regions` ldv
    JOIN `states` s ON s.`abbr` = ldv.`state_abbr`
    LEFT JOIN `counties` c ON c.`name` = ldv.`county_name`
                          AND c.`state_id` = s.`id`
    LEFT JOIN `municipalities` m ON m.`name` = ldv.`municipality_name`
                                AND m.`municipality_type` = ldv.`municipality_type`
                                AND CASE
                                      WHEN ldv.`county_name` = '' THEN m.`county_id` IS NULL
                                      ELSE m.`county_id` = c.`id`
                                    END
                                AND m.`state_id` = s.`id`
    WHERE (ldv.`region_id` IS NULL OR ldv.`region_id` = 0)
      AND NOT EXISTS (SELECT vr2.`id`
                        FROM `voting_regions` vr2
                       WHERE vr2.`state_id` = s.`id`
                         AND vr2.`name` = ldv.`region_name`
                         AND CASE
                               WHEN ldv.`county_name` = '' THEN vr2.`county_id` IS NULL
                               ELSE vr2.`county_id` = c.`id`
                             END
                         AND CASE
                               WHEN ldv.`municipality_name` = '' THEN vr2.`municipality_id` IS NULL
                               ELSE vr2.`municipality_id` = m.`id`
                             END);

SELECT now() AS "Time", 'Look for missing county or municipality links' AS "Action";
SELECT CASE
         WHEN vr.`id` IS NULL THEN '-'
         ELSE vr.`id`
       END AS 'Region ID',
       CASE
         WHEN vr.`name` IS NULL THEN '-'
         ELSE vr.`name`
       END AS 'Region Name',
       CASE
         WHEN s.`id` IS NULL THEN '-'
         ELSE s.`abbr`
       END AS 'State',
       ldv.`state_abbr` AS 'LDV State', 
       CASE
         WHEN vr.`county_id` IS NULL THEN '-'
         ELSE c.`name`
       END AS 'County',
       CASE
         WHEN ldv.`county_name` = '' THEN '-'
         ELSE ldv.`county_name`
       END AS 'LDV County',
       CASE
         WHEN vr.`municipality_id` IS NULL THEN '-'
         ELSE concat_ws(' ', m.`name`, m.`municipality_type`)
       END AS 'Municipality',
       CASE
         WHEN ldv.`municipality_name` = '' THEN '-'
         ELSE concat_ws(' ', ldv.`municipality_name`, ldv.`municipality_type`)
       END AS 'LDV Municipality'
  FROM `voting_regions` vr
  JOIN `states` s ON s.`id` = vr.`state_id`
  JOIN `ldv_regions` ldv ON ldv.`region_id` = vr.`id`
  LEFT JOIN `counties` c ON c.`id` = vr.`county_id`
  LEFT JOIN `municipalities` m ON m.`id` = vr.`municipality_id`
 WHERE (ldv.`municipality_name` != '' AND vr.`municipality_id` IS NULL)
    OR (ldv.`municipality_name` = '' AND vr.`municipality_id` IS NOT NULL)
    OR (ldv.`county_name` != '' AND vr.`county_id` IS NULL)
    OR (ldv.`county_name` = '' AND vr.`county_id` IS NOT NULL)
 ORDER BY vr.`id`
 LIMIT 10;

SELECT now() AS "Time", 'Look for missing voting regions with IDs in voting regions table' AS "Action";
SELECT ldv.*
  FROM `ldv_regions` ldv
 WHERE ldv.`region_id` IS NOT NULL
   AND ldv.`region_id` != 0
   AND NOT EXISTS (SELECT vr.`id`
                     FROM `voting_regions` vr
                    WHERE vr.`id` = ldv.`region_id`)
 ORDER BY ldv.`region_id`
 LIMIT 10;
                  
SELECT now() AS "Time", 'Look for missing voting regions without IDs in voting regions table' AS "Action";
SELECT ldv.*
  FROM `ldv_regions` ldv
  JOIN `states` s ON s.`abbr` = ldv.`state_abbr`
 WHERE (ldv.`region_id` IS NULL OR ldv.`region_id` = 0)
   AND NOT EXISTS (SELECT vr.`id`
                     FROM `voting_regions` vr
                     LEFT JOIN `counties` c ON c.`id` = vr.`county_id`
                     LEFT JOIN `municipalities` m ON m.`id` = vr.`municipality_id`
                    WHERE vr.`name` = ldv.`region_name`
                      AND vr.`state_id` = s.`id`
                      AND CASE
                            WHEN ldv.`county_name` = '' THEN vr.`county_id` IS NULL
                            ELSE c.`name` = ldv.`county_name`
                          END
                      AND CASE
                             WHEN ldv.`municipality_name` = '' THEN vr.`municipality_id` IS NULL
                             ELSE m.`name` = ldv.`municipality_name` && m.`municipality_type` = ldv.`municipality_type`
                          END)
 ORDER BY s.`id`, ldv.`region_name`
 LIMIT 10;
 
 SELECT now() AS "Time", 'Delete municipalities that are no longer referenced by voting regions' AS "Action";
 DELETE FROM `municipalities`
  WHERE NOT EXISTS (SELECT `id`
                      FROM `voting_regions`
                     WHERE `voting_regions`.`municipality_id` = `municipalities`.`id`);

SELECT now() AS "Time", 'Delete counties that are no longer referenced by municipalities or voting regions' AS "Action";
DELETE FROM `counties`
 WHERE NOT EXISTS (SELECT `id`
                     FROM `voting_regions`
                    WHERE `voting_regions`.`county_id` = `counties`.`id`)
   AND NOT EXISTS (SELECT `id`
                     FROM `municipalities`
                    WHERE `municipalities`.`county_id` = `counties`.`id`);