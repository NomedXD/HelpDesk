-- liquibase formatted sql


ALTER TABLE public.feedbacks
    DROP COLUMN id;

ALTER TABLE public.feedbacks
    ADD COLUMN
        id integer primary key;