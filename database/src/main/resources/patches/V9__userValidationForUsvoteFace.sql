UPDATE `faces_config`
  SET `user_validation_skip_fields` = 'voterHistory,voterType,currentAddress' where path_prefix = 'faces/usvote';
  