SET FOREIGN_KEY_CHECKS = 0;

DELETE sr FROM `rd_scheduled_reports` AS sr
    INNER JOIN `rd_reports` AS r
    WHERE sr.report_id=r.id
      AND r.standard=1;

DELETE ra FROM `rd_report_answers` AS ra
    INNER JOIN `rd_report_fields` AS rf
    INNER JOIN `rd_report_columns` AS rc
    INNER JOIN `rd_reports` AS r
    WHERE ra.report_field_id=rf.id
      AND rf.column_id=rc.id
      AND rc.report_id=r.id
      AND r.standard=1;

DELETE rf FROM `rd_report_fields` AS rf
    INNER JOIN `rd_report_columns` AS rc
    INNER JOIN `rd_reports` AS r
    WHERE rf.column_id=rc.id
      AND rc.report_id=r.id
      AND r.standard=1;

DELETE rc FROM `rd_report_columns` AS rc
    INNER JOIN `rd_reports` AS r
    WHERE rc.report_id=r.id
      AND r.standard=1;

DELETE r FROM `rd_reports` AS r
    WHERE r.standard=1;

INSERT INTO `rd_reports` (`title`, `standard`) VALUES ('Usage by Request Type', 1);
SET @report_id = last_insert_id();
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES (@report_id, 'Request Type', 0);
SET @column_id = last_insert_id();
INSERT INTO `rd_report_fields` (`column_id`, `user_field_name`) VALUES (@column_id, 'flow_type');
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES (@report_id, 'Number', 1);
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES (@report_id, 'Percentage', 2);

INSERT INTO `rd_reports` (`title`, `standard`) VALUES ('Completed by Request Type', 1);
SET @report_id = last_insert_id();
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES (@report_id, 'Request Type', 0);
SET @column_id = last_insert_id();
INSERT INTO `rd_report_fields` (`column_id`, `user_field_name`) VALUES (@column_id, 'flow_type');
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES (@report_id, 'Completed', 1);
SET @column_id = last_insert_id();
INSERT INTO `rd_report_fields` (`column_id`, `user_field_name`) VALUES (@column_id, 'downloaded');
SET @report_field_id = last_insert_id();
INSERT INTO `rd_report_answers` (`report_field_id`, `answer`) VALUES (@report_field_id, '1');
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES(@report_id, 'Number', 2);
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES(@report_id, 'Percentage', 3);

INSERT INTO `rd_reports` (`title`, `standard`) VALUES ('By Age Group', 1);
SET @report_id = last_insert_id();
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES (@report_id, 'Age Group', 0);
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES (@report_id, 'Completed', 1);
SET @column_id = last_insert_id();
INSERT INTO `rd_report_fields` (`column_id`, `user_field_name`) VALUES (@column_id, 'downloaded');
SET @report_field_id = last_insert_id();
INSERT INTO `rd_report_answers` (`report_field_id`, `answer`) VALUES (@report_field_id, '1');
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES(@report_id, 'Number', 2);
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES(@report_id, 'Percentage', 3);

INSERT INTO `rd_reports` (`title`, `standard`) VALUES ('By Voting Region & Voter Type/Military', 1);
SET @report_id = last_insert_id();
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES (@report_id, 'Voter Type', 0);
SET @column_id = last_insert_id();
INSERT INTO `rd_report_fields` (`column_id`, `user_field_name`) VALUES (@column_id, 'voter_type');
SET @report_field_id = last_insert_id();
INSERT INTO `rd_report_answers` (`report_field_id`, `answer`) VALUES (@report_field_id, 'UNIFORMED_SERVICE_MEMBER');
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES(@report_id, 'Voting Region', 2);
SET @column_id = last_insert_id();
INSERT INTO `rd_report_fields` (`column_id`, `user_field_name`) VALUES (@column_id, 'voting_region_name');
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES(@report_id, 'Voting State', 1);
SET @column_id = last_insert_id();
INSERT INTO `rd_report_fields` (`column_id`, `user_field_name`) VALUES (@column_id, 'voting_region_state');
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES (@report_id, 'Completed', 3);
SET @column_id = last_insert_id();
INSERT INTO `rd_report_fields` (`column_id`, `user_field_name`) VALUES (@column_id, 'downloaded');
SET @report_field_id = last_insert_id();
INSERT INTO `rd_report_answers` (`report_field_id`, `answer`) VALUES (@report_field_id, '1');
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES(@report_id, 'Number', 4);
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES(@report_id, 'Percentage', 5);

INSERT INTO `rd_reports` ( `title`, `standard`) VALUES ('By Voting Region & Voter Type/Temporary Overseas', 1);
SET @report_id = last_insert_id();
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES (@report_id, 'Voter Type', 0);
SET @column_id = last_insert_id();
INSERT INTO `rd_report_fields` (`column_id`, `user_field_name`) VALUES (@column_id, 'voter_type');
SET @report_field_id = last_insert_id();
INSERT INTO `rd_report_answers` (`report_field_id`, `answer`) VALUES (@report_field_id, 'CITIZEN_OVERSEAS_TEMPORARILY');
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES(@report_id, 'Voting Region', 2);
SET @column_id = last_insert_id();
INSERT INTO `rd_report_fields` (`column_id`, `user_field_name`) VALUES (@column_id, 'voting_region_name');
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES(@report_id, 'Voting State', 1);
SET @column_id = last_insert_id();
INSERT INTO `rd_report_fields` (`column_id`, `user_field_name`) VALUES (@column_id, 'voting_region_state');
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES (@report_id, 'Completed', 3);
SET @column_id = last_insert_id();
INSERT INTO `rd_report_fields` (`column_id`, `user_field_name`) VALUES (@column_id, 'downloaded');
SET @report_field_id = last_insert_id();
INSERT INTO `rd_report_answers` (`report_field_id`, `answer`) VALUES (@report_field_id, '1');
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES(@report_id, 'Number', 4);
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES(@report_id, 'Percentage', 5);

INSERT INTO `rd_reports` (`title`, `standard`) VALUES ('By Voting Region & Voter Type/Permanent Overseas', 1);
SET @report_id = last_insert_id();
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES (@report_id, 'Voter Type', 0);
SET @column_id = last_insert_id();
INSERT INTO `rd_report_fields` (`column_id`, `user_field_name`) VALUES (@column_id, 'voter_type');
SET @report_field_id = last_insert_id();
INSERT INTO `rd_report_answers` (`report_field_id`, `answer`) VALUES (@report_field_id, 'CITIZEN_OVERSEAS_INDEFINITELY');
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES(@report_id, 'Voting Region', 2);
SET @column_id = last_insert_id();
INSERT INTO `rd_report_fields` (`column_id`, `user_field_name`) VALUES (@column_id, 'voting_region_name');
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES(@report_id, 'Voting State', 1);
SET @column_id = last_insert_id();
INSERT INTO `rd_report_fields` (`column_id`, `user_field_name`) VALUES (@column_id, 'voting_region_state');
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES (@report_id, 'Completed', 3);
SET @column_id = last_insert_id();
INSERT INTO `rd_report_fields` (`column_id`, `user_field_name`) VALUES (@column_id, 'downloaded');
SET @report_field_id = last_insert_id();
INSERT INTO `rd_report_answers` (`report_field_id`, `answer`) VALUES (@report_field_id, '1');
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES(@report_id, 'Number', 4);
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES(@report_id, 'Percentage', 5);

INSERT INTO `rd_reports` (`title`, `standard`, `flow_type`) VALUES ('By Voting Region & Voter Type/US Registration', 1, 'DOMESTIC_REGISTRATION');
SET @report_id = last_insert_id();
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES (@report_id, 'Voter Type', 0);
SET @column_id = last_insert_id();
INSERT INTO `rd_report_fields` (`column_id`, `user_field_name`) VALUES (@column_id, 'flow_type');
SET @report_field_id = last_insert_id();
INSERT INTO `rd_report_answers` (`report_field_id`, `answer`) VALUES (@report_field_id, 'DOMESTIC_REGISTRATION');
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES(@report_id, 'Voting Region', 2);
SET @column_id = last_insert_id();
INSERT INTO `rd_report_fields` (`column_id`, `user_field_name`) VALUES (@column_id, 'voting_region_name');
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES(@report_id, 'Voting State', 1);
SET @column_id = last_insert_id();
INSERT INTO `rd_report_fields` (`column_id`, `user_field_name`) VALUES (@column_id, 'voting_region_state');
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES (@report_id, 'Completed', 3);
SET @column_id = last_insert_id();
INSERT INTO `rd_report_fields` (`column_id`, `user_field_name`) VALUES (@column_id, 'downloaded');
SET @report_field_id = last_insert_id();
INSERT INTO `rd_report_answers` (`report_field_id`, `answer`) VALUES (@report_field_id, '1');
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES(@report_id, 'Number', 4);
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES(@report_id, 'Percentage', 5);

INSERT INTO `rd_reports` (`title`, `standard`, `flow_type`) VALUES ('By Voting Region & Voter Type/US Absentee', 1, 'DOMESTIC_ABSENTEE');
SET @report_id = last_insert_id();
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES (@report_id, 'Voter Type', 0);
SET @column_id = last_insert_id();
INSERT INTO `rd_report_fields` (`column_id`, `user_field_name`) VALUES (@column_id, 'flow_type');
SET @report_field_id = last_insert_id();
INSERT INTO `rd_report_answers` (`report_field_id`, `answer`) VALUES (@report_field_id, 'DOMESTIC_ABSENTEE');
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES(@report_id, 'Voting Region', 2);
SET @column_id = last_insert_id();
INSERT INTO `rd_report_fields` (`column_id`, `user_field_name`) VALUES (@column_id, 'voting_region_name');
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES(@report_id, 'Voting State', 1);
SET @column_id = last_insert_id();
INSERT INTO `rd_report_fields` (`column_id`, `user_field_name`) VALUES (@column_id, 'voting_region_state');
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES (@report_id, 'Completed', 3);
SET @column_id = last_insert_id();
INSERT INTO `rd_report_fields` (`column_id`, `user_field_name`) VALUES (@column_id, 'downloaded');
SET @report_field_id = last_insert_id();
INSERT INTO `rd_report_answers` (`report_field_id`, `answer`) VALUES (@report_field_id, '1');
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES(@report_id, 'Number', 4);
INSERT INTO `rd_report_columns` (`report_id`, `name`, `column_number`) VALUES(@report_id, 'Percentage', 5);

SET FOREIGN_KEY_CHECKS = 1;