ALTER TABLE `state_voter_info`
   MODIFY COLUMN `new_voter_witnesses` VARCHAR(32) DEFAULT '',
   MODIFY COLUMN `new_voter_notarization` VARCHAR(32) DEFAULT '',
   ADD COLUMN `new_voter_notwit_reqs` TEXT AFTER `new_voter_notarization`,
   MODIFY COLUMN `absentee_ballot_witnesses` VARCHAR(32) DEFAULT '',
   MODIFY COLUMN `absentee_ballot_notarization` VARCHAR(32) DEFAULT '',
   ADD COLUMN `absentee_ballot_notwit_reqs` TEXT AFTER `absentee_ballot_notarization`,
   MODIFY COLUMN `early_voting_witnesses` VARCHAR(32) DEFAULT '',
   MODIFY COLUMN `early_voting_notarization` VARCHAR(32) DEFAULT '',
   ADD COLUMN `early_voting_notwit_reqs` TEXT AFTER `early_voting_notarization`;