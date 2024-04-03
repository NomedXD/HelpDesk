-- liquibase formatted sql


-- changeset vlad:1712066106624-1
CREATE TABLE refresh_tokens
(
    id         UUID NOT NULL,
    user_id    INTEGER,
    expires_at TIMESTAMP check ( expires_at > now() ),
    CONSTRAINT pk_refresh_tokens PRIMARY KEY (id)
);

-- changeset vlad:1712066106624-2
ALTER TABLE refresh_tokens
    ADD CONSTRAINT uc_refresh_tokens_user UNIQUE (user_id);

-- changeset vlad:1712066106624-3
DROP TABLE blocked_tokens CASCADE;

