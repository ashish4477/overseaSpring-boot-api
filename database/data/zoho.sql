CREATE TABLE ZOHO_TOKEN (
    id varchar(255) NOT NULL,
    user_mail varchar(255) NOT NULL,
    client_id varchar(255),
    client_secret varchar(255),
    refresh_token varchar(255),
    access_token varchar(255),
    grant_token varchar(255),
    expiry_time varchar(20),
    redirect_url varchar(255),
    primary key (id)
);