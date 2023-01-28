UPDATE addresses
   SET zip = CONCAT('0', zip)
 WHERE LENGTH(zip) = 4;