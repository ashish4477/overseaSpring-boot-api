ALTER TABLE vip_sources
   ADD `last_modified` TIMESTAMP;
     
ALTER TABLE vip_contests
   ADD `ballot_placement` INT(11);