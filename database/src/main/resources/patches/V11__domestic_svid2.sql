ALTER TABLE `state_voter_info`
   ADD COLUMN `new_voter_witnesses` tinyint(4) DEFAULT "0" AFTER `domestic_return_tel`,
   ADD COLUMN `new_voter_notarization` tinyint(4) DEFAULT "0" AFTER `new_voter_witnesses`,
   ADD COLUMN `absentee_ballot_witnesses` tinyint(4) DEFAULT "0" AFTER `new_voter_notarization`,
   ADD COLUMN `absentee_ballot_notarization` tinyint(4) DEFAULT "0" AFTER `absentee_ballot_witnesses`,
   ADD COLUMN `early_voting_witnesses` tinyint(4) DEFAULT "0" AFTER `absentee_ballot_notarization`,
   ADD COLUMN `early_voting_notarization` tinyint(4) DEFAULT "0" AFTER `early_voting_witnesses`,
   ADD COLUMN `overseas_site` VARCHAR(255) NOT NULL DEFAULT '' AFTER `website`,
   ADD COLUMN `military_site` VARCHAR(255) NOT NULL DEFAULT '' AFTER `overseas_site`,
   ADD COLUMN `finder_site` VARCHAR(255) NOT NULL DEFAULT '' AFTER `military_site`,
   ADD COLUMN `registration_site` VARCHAR(255) NOT NULL DEFAULT '' AFTER `finder_site`,
   ADD COLUMN `ballot_site` VARCHAR(255) NOT NULL DEFAULT '' AFTER `registration_site`,
   ADD COLUMN `early_voting_site` VARCHAR(255) NOT NULL DEFAULT '' AFTER `ballot_site`;