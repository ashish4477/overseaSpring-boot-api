SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE `rd_scheduled_reports`;
TRUNCATE TABLE `rd_report_answers`;
TRUNCATE TABLE `rd_report_fields`;
TRUNCATE TABLE `rd_report_columns`;
TRUNCATE TABLE `rd_report_faces`;
TRUNCATE TABLE `rd_reports`;

INSERT INTO `rd_reports` (`id`, `title`, `standard`) VALUES (1, 'Usage by Request Type', 1);
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES (1, 1, 'Request Type', 0);
INSERT INTO `rd_report_fields` (`id`, `column_id`, `user_field_name`) VALUES (1, 1, 'flow_type');
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES(2, 1, 'Number', 1);
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES(3, 1, 'Percentage', 2);

INSERT INTO `rd_reports` (`id`, `title`, `standard`) VALUES (2, 'Completed by Request Type', 1);
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES (21, 2, 'Request Type', 0);
INSERT INTO `rd_report_fields` (`id`, `column_id`, `user_field_name`) VALUES (21, 21, 'flow_type');
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES (22, 2, 'Completed', 1);
INSERT INTO `rd_report_fields` (`id`, `column_id`, `user_field_name`) VALUES (22, 22, 'downloaded');
INSERT INTO `rd_report_answers` (`id`, `report_field_id`, `answer`) VALUES (22, 22, '1');
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES(23, 2, 'Number', 2);
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES(24, 2, 'Percentage', 3);

INSERT INTO `rd_reports` (`id`, `title`, `standard`) VALUES (3, 'By Age Group', 1);
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES (31, 3, 'Age Group', 0);
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES (32, 3, 'Completed', 1);
INSERT INTO `rd_report_fields` (`id`, `column_id`, `user_field_name`) VALUES (32, 32, 'downloaded');
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES(33, 3, 'Number', 2);
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES(34, 3, 'Percentage', 3);

INSERT INTO `rd_reports` (`id`, `title`, `standard`) VALUES (4, 'By Voting Region & Voter Type/Military', 1);
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES (41, 4, 'Voter Type', 0);
INSERT INTO `rd_report_fields` (`id`, `column_id`, `user_field_name`) VALUES (41, 41, 'voter_type');
INSERT INTO `rd_report_answers` (`id`, `report_field_id`, `answer`) VALUES (41, 41, 'UNIFORMED_SERVICE_MEMBER');
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES(42, 4, 'Voting Region', 2);
INSERT INTO `rd_report_fields` (`id`, `column_id`, `user_field_name`) VALUES (42, 42, 'voting_region_name');
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES(43, 4, 'Voting State', 1);
INSERT INTO `rd_report_fields` (`id`, `column_id`, `user_field_name`) VALUES (43, 43, 'voting_region_state');
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES (44, 4, 'Completed', 3);
INSERT INTO `rd_report_fields` (`id`, `column_id`, `user_field_name`) VALUES (44, 44, 'downloaded');
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES(45, 4, 'Number', 4);
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES(46, 4, 'Percentage', 5);

INSERT INTO `rd_reports` (`id`, `title`, `standard`) VALUES (5, 'By Voting Region & Voter Type/Temporary Overseas', 1);
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES (51, 5, 'Voter Type', 0);
INSERT INTO `rd_report_fields` (`id`, `column_id`, `user_field_name`) VALUES (51, 51, 'voter_type');
INSERT INTO `rd_report_answers` (`id`, `report_field_id`, `answer`) VALUES (51, 51, 'CITIZEN_OVERSEAS_TEMPORARILY');
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES(52, 5, 'Voting Region', 2);
INSERT INTO `rd_report_fields` (`id`, `column_id`, `user_field_name`) VALUES (52, 52, 'voting_region_name');
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES(53, 5, 'Voting State', 1);
INSERT INTO `rd_report_fields` (`id`, `column_id`, `user_field_name`) VALUES (53, 53, 'voting_region_state');
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES (54, 5, 'Completed', 3);
INSERT INTO `rd_report_fields` (`id`, `column_id`, `user_field_name`) VALUES (54, 54, 'downloaded');
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES(55, 5, 'Number', 4);
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES(56, 5, 'Percentage', 5);

INSERT INTO `rd_reports` (`id`, `title`, `standard`) VALUES (6, 'By Voting Region & Voter Type/Permanent Overseas', 1);
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES (61, 6, 'Voter Type', 0);
INSERT INTO `rd_report_fields` (`id`, `column_id`, `user_field_name`) VALUES (61, 61, 'voter_type');
INSERT INTO `rd_report_answers` (`id`, `report_field_id`, `answer`) VALUES (61, 61, 'CITIZEN_OVERSEAS_INDEFINITELY');
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES(62, 6, 'Voting Region', 2);
INSERT INTO `rd_report_fields` (`id`, `column_id`, `user_field_name`) VALUES (62, 62, 'voting_region_name');
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES(63, 6, 'Voting State', 1);
INSERT INTO `rd_report_fields` (`id`, `column_id`, `user_field_name`) VALUES (63, 63, 'voting_region_state');
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES (64, 6, 'Completed', 3);
INSERT INTO `rd_report_fields` (`id`, `column_id`, `user_field_name`) VALUES (64, 64, 'downloaded');
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES(65, 6, 'Number', 4);
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES(66, 6, 'Percentage', 5);

INSERT INTO `rd_reports` (`id`, `title`, `standard`, `flow_type`) VALUES (7, 'By Voting Region & Voter Type/US Registration', 1, 'DOMESTIC_REGISTRATION');
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES (71, 7, 'Voter Type', 0);
INSERT INTO `rd_report_fields` (`id`, `column_id`, `user_field_name`) VALUES (71, 71, 'flow_type');
INSERT INTO `rd_report_answers` (`id`, `report_field_id`, `answer`) VALUES (71, 71, 'DOMESTIC_REGISTRATION');
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES(72, 7, 'Voting Region', 2);
INSERT INTO `rd_report_fields` (`id`, `column_id`, `user_field_name`) VALUES (72, 72, 'voting_region_name');
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES(73, 7, 'Voting State', 1);
INSERT INTO `rd_report_fields` (`id`, `column_id`, `user_field_name`) VALUES (73, 73, 'voting_region_state');
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES (74, 7, 'Completed', 3);
INSERT INTO `rd_report_fields` (`id`, `column_id`, `user_field_name`) VALUES (74, 74, 'downloaded');
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES(75, 7, 'Number', 4);
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES(76, 7, 'Percentage', 5);

INSERT INTO `rd_reports` (`id`, `title`, `standard`, `flow_type`) VALUES (8, 'By Voting Region & Voter Type/US Absentee', 1, 'DOMESTIC_ABSENTEE');
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES (81, 8, 'Voter Type', 0);
INSERT INTO `rd_report_fields` (`id`, `column_id`, `user_field_name`) VALUES (81, 81, 'flow_type');
INSERT INTO `rd_report_answers` (`id`, `report_field_id`, `answer`) VALUES (81, 81, 'DOMESTIC_ABSENTEE');
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES(82, 8, 'Voting Region', 2);
INSERT INTO `rd_report_fields` (`id`, `column_id`, `user_field_name`) VALUES (82, 82, 'voting_region_name');
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES(83, 8, 'Voting State', 1);
INSERT INTO `rd_report_fields` (`id`, `column_id`, `user_field_name`) VALUES (83, 83, 'voting_region_state');
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES (84, 8, 'Completed', 3);
INSERT INTO `rd_report_fields` (`id`, `column_id`, `user_field_name`) VALUES (84, 84, 'downloaded');
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES(85, 8, 'Number', 4);
INSERT INTO `rd_report_columns` (`id`, `report_id`, `name`, `column_number`) VALUES(86, 8, 'Percentage', 5);

SET FOREIGN_KEY_CHECKS = 1;