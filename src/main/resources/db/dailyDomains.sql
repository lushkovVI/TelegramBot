CREATE TABLE hbt.daily_domains
(
    id integer  primary key GENERATED ALWAYS AS IDENTITY,
    domain_name CHARACTER VARYING(255) not null,
    domain_price  bigint not null
);