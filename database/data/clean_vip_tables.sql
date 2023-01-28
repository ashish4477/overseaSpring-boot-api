-- Cleans out the VIP database tables.
-- Warning: this script truncates all of the tables, eliminating all data.
--          If you need to delete data for a single source, you will need a different method.
USE overseas_foundation;
SET FOREIGN_KEY_CHECKS=0;

TRUNCATE TABLE vip_referendum_ballot_responses;
TRUNCATE TABLE vip_custom_ballot_responses;
TRUNCATE TABLE vip_precinct_split_districts;
TRUNCATE TABLE vip_precinct_districts;
TRUNCATE TABLE vip_ballot_candidates;
TRUNCATE TABLE vip_ballot_responses;
TRUNCATE TABLE vip_ballots;
TRUNCATE TABLE vip_candidate_bios;
TRUNCATE TABLE vip_candidates;
TRUNCATE TABLE vip_custom_ballots;
TRUNCATE TABLE vip_detail_addresses;
TRUNCATE TABLE vip_localities;
TRUNCATE TABLE vip_precinct_splits;
TRUNCATE TABLE vip_precincts;
TRUNCATE TABLE vip_referenda;
TRUNCATE TABLE vip_referendum_details;
TRUNCATE TABLE vip_street_segments;
TRUNCATE TABLE vip_contests;
TRUNCATE TABLE vip_electoral_districts;
TRUNCATE TABLE vip_elections;
TRUNCATE TABLE vip_states;
TRUNCATE TABLE vip_sources;

SET FOREIGN_KEY_CHECKS=1;