CREATE TABLE hbt.message_answered
(
    message_id integer  primary key GENERATED ALWAYS AS IDENTITY,
    message_text text not null,
    message_date_answered timestamp not null,
    owner_id integer,
    FOREIGN KEY (owner_id) REFERENCES hbt.user (user_id)
);