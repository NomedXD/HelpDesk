-- liquibase formatted sql

-- changeset vlad:1711668201305-1
DROP TABLE tokens CASCADE;

-- changeset vlad:1711668201305-2
CREATE TABLE blocked_tokens (
    id uuid primary key,
    expires_at timestamp check ( expires_at > now() )
)