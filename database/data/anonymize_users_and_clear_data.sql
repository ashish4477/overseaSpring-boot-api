# Script removes all users except bearcoders, remove all old info from auxiliary tables.
# Removes emails log, wizard context log (users) and pending voter registrations


# Remove wizard context log
delete from answers where users_id in (
  select pdf.id from users pdf join users u on u.id = pdf.user_id where u.username not like '%@bear-code.com'
);

delete from users where user_id in (select id from users where username not like '%@bear-code.com');

delete from wizard_result_names
  where id not in (select pdf.name_id from users pdf)
  and id not in (select pdf.previous_name_id from users pdf);

delete from wizard_result_addresses
where id not in (select voting_address_id from users)
and id not in (select current_address_id from users)
and id not in (select forwarding_address_id from users)
and id not in (select previous_address_id from users);

# Remove emails
delete from emails_log;
delete from email;

# Remove mailing lists
delete from mailing_list_link;
delete from mailing_list_address;

# Remove pending voter registration
delete from pending_voter_registration_statuses;
delete from pending_voter_answers;
delete from pending_voter_registrations;
delete from pending_voter_names;
delete from pending_voter_addresses;
delete from encryption_key_statuses;

# Removes reporting dashboard
delete from rd_scheduled_reports where user_id in (select id from users where username not like '%@bear-code.com');
update rd_reports set owner_id = NULL ;

# Update corrections
update leo_corrections set editor_id = null
where editor_id in (select id from users where username not like '%@bear-code.com');

# Remove users
delete from users_roles where user_id in (select id from users where username not like '%@bear-code.com');
delete from users_admin_faces where user_id in (select id from users where username not like '%@bear-code.com');
delete from extended_profile where user_id in (select id from users where username not like '%@bear-code.com');

delete from users where username not like '%@bear-code.com';

delete from user_names
  where id not in (select name_id from users)
  and id not in (select previous_name_id from users);

delete from wizard_result_addresses
where id not in (select voting_address_id from users)
and id not in (select current_address_id from users)
and id not in (select forwarding_address_id from users)
and id not in (select previous_address_id from users);
