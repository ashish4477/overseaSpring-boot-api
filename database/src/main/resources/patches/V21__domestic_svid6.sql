ALTER TABLE `state_voter_info`
   DROP COLUMN `new_voter_witnesses`,
   DROP COLUMN `new_voter_notarization`,
   DROP COLUMN `absentee_ballot_witnesses`,
   DROP COLUMN `absentee_ballot_notarization`,
   DROP COLUMN `early_voting_witnesses`,
   DROP COLUMN `early_voting_notarization`;