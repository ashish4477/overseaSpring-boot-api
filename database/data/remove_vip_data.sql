-- Removes data from the VIP data for a single identifier. For now, the identifier needs to be explicitly
-- set in the file.
USE overseas_foundation;
SET FOREIGN_KEY_CHECKS=0;

DELETE FROM vip_referendum_ballot_responses
WHERE EXISTS (SELECT r.id
              FROM vip_referenda r
              WHERE r.id=referendum_id
                AND r.source_id=4)
  AND id>0;

DELETE FROM vip_custom_ballot_responses
WHERE EXISTS (SELECT cb.id
              FROM vip_custom_ballots cb 
              WHERE cb.id=custom_ballot_id
                AND cb.source_id=4)
  AND id>0;
                
DELETE FROM vip_precinct_split_districts
WHERE EXISTS (SELECT ed.id
              FROM vip_electoral_districts ed
              WHERE ed.id=electoral_district_id
                AND ed.source_id=4)
  AND electoral_district_id>0;
                
DELETE FROM vip_precinct_districts
WHERE EXISTS (SELECT ed.id
              FROM vip_electoral_districts ed
              WHERE ed.id=electoral_district_id
                AND ed.source_id=4)
  AND electoral_district_id>0;

DELETE FROM vip_ballot_candidates
WHERE EXISTS (SELECT b.id
              FROM vip_ballots b
              WHERE b.id=ballot_id
                AND b.source_id=4)
  AND id>0;

DELETE FROM vip_ballot_responses
WHERE source_id=4
  AND id>0;

DELETE FROM vip_ballots
WHERE source_id=4
  AND id>0;

DELETE FROM vip_candidate_bios
WHERE EXISTS (SELECT c.id
              FROM vip_candidates c
              WHERE c.id=candidate_id
                AND c.source_id=4)
AND id>0;

DELETE FROM vip_candidates
WHERE source_id=4
  AND id>0;

DELETE FROM vip_custom_ballots
WHERE source_id=4
  AND id>0;

DELETE FROM vip_detail_addresses
WHERE EXISTS (SELECT ss.id
              FROM vip_street_segments ss
              WHERE ss.non_house_address_id=id
                AND ss.source_id=4)
  AND id>0;

DELETE FROM vip_localities
WHERE source_id=4
  AND id>0;

DELETE FROM vip_precinct_splits
WHERE source_id=4
  AND id>0;

DELETE FROM vip_precincts
WHERE source_id=4
  AND id>0;

DELETE FROM vip_referendum_details
WHERE EXISTS (SELECT r.id
              FROM vip_referenda r
              WHERE r.id=referendum_id
                AND r.source_id=4)
  AND id>0;

DELETE FROM vip_referenda
WHERE source_id=4
  AND id>0;

DELETE FROM vip_street_segments
WHERE source_id=4
  AND id>0;

DELETE FROM vip_contests
WHERE source_id=4
  AND id>0;

DELETE FROM vip_electoral_districts
WHERE source_id=4
  AND id>0;

DELETE FROM vip_elections
WHERE source_id=4
  AND id>0;

DELETE FROM vip_states
WHERE source_id=4
  AND id>0;

DELETE FROM vip_sources
WHERE id=4;

SET FOREIGN_KEY_CHECKS=1;