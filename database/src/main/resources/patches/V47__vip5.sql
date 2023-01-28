ALTER TABLE `vip_ballot_responses`
  MODIFY `vip_id` BIGINT NOT NULL;

ALTER TABLE `vip_ballots`
  MODIFY `vip_id` BIGINT NOT NULL;

ALTER TABLE `vip_candidates`
  MODIFY `vip_id` BIGINT NOT NULL;

ALTER TABLE `vip_contests`
  MODIFY `vip_id` BIGINT NOT NULL;

ALTER TABLE `vip_custom_ballots`
  MODIFY `vip_id` BIGINT NOT NULL;

ALTER TABLE `vip_elections`
  MODIFY `vip_id` BIGINT NOT NULL;

ALTER TABLE `vip_electoral_districts`
  MODIFY `vip_id` BIGINT NOT NULL;

ALTER TABLE `vip_localities`
  MODIFY `vip_id` BIGINT NOT NULL;

ALTER TABLE `vip_precinct_splits`
  MODIFY `vip_id` BIGINT NOT NULL;

ALTER TABLE `vip_precincts`
  MODIFY `vip_id` BIGINT NOT NULL;

ALTER TABLE `vip_referenda`
  MODIFY `vip_id` BIGINT NOT NULL;

ALTER TABLE `vip_sources`
  MODIFY `source_id` BIGINT NOT NULL,
  MODIFY `vip_id` BIGINT NOT NULL;
 
ALTER TABLE `vip_states`
  MODIFY `vip_id` BIGINT NOT NULL;
  
ALTER TABLE `vip_street_segments`
  MODIFY `vip_id` BIGINT NOT NULL;
