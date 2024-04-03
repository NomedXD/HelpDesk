-- liquibase formatted sql

-- changeset vlad:1711703900704-2
ALTER TABLE users
    DROP CONSTRAINT fk_users_on_role;

-- changeset vlad:1711703900704-1
ALTER TABLE users
    ADD user_role SMALLINT;

-- changeset vlad:1711703900704-3
DROP TABLE user_role CASCADE;

-- changeset vlad:1711703900704-4
ALTER TABLE users
    DROP COLUMN role_id;

