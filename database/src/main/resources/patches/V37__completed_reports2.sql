SET FOREIGN_KEY_CHECKS = 0;

DELETE ra FROM `rd_report_answers` AS ra
    INNER JOIN `rd_report_fields` AS rf
    INNER JOIN `rd_report_columns` AS rc
    INNER JOIN `rd_reports` AS r
    WHERE ra.report_field_id=rf.id
      AND rf.column_id=rc.id
      AND rc.name = 'Completed'
      AND rc.report_id=r.id
      AND r.title IN ('By Age Group', 
      				  'By Voting Region & Voter Type/Military',
      				  'By Voting Region & Voter Type/Temporary Overseas',
      				  'By Voting Region & Voter Type/Permanent Overseas',
      				  'By Voting Region & Voter Type/US Registration',
      				  'By Voting Region & Voter Type/US Absentee')
      AND r.standard=1;

SET FOREIGN_KEY_CHECKS = 1;