CREATE TABLE hbt.user
(
    user_id integer primary key GENERATED ALWAYS AS IDENTITY,
    user_name CHARACTER VARYING(255) not null,
    last_message_at timestamp not null,
    user_chat_id integer not null
);