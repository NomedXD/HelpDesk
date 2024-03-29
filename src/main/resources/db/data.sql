-- Categories
INSERT INTO public.categories (id, name) VALUES (DEFAULT, 'Application & Services');
INSERT INTO public.categories (id, name) VALUES (DEFAULT, 'Benefits & Paper Work');
INSERT INTO public.categories (id, name) VALUES (DEFAULT, 'Hardware & Software');
INSERT INTO public.categories (id, name) VALUES (DEFAULT, 'People Management');
INSERT INTO public.categories (id, name) VALUES (DEFAULT, 'Security & Access');
INSERT INTO public.categories (id, name) VALUES (DEFAULT, 'Workplaces &amp; Facilities');

-- Users
INSERT INTO public.users (id, first_name, last_name, email, user_role, password)
VALUES (DEFAULT, 'Ryan', 'Gosling', 'user1_minsk@yopmail.com', 1, e'$2a$12$0ky1XYHR.0cjp87QgS1SDuyw9zBnSNEMCyYhuvKkXzcDBQqeK.Ehe');

INSERT INTO public.users (id, first_name, last_name, email, user_role, password)
VALUES (DEFAULT, 'Paul', 'Atreides', 'user2_minsk@yopmail.com', 1, e'$2a$12$0ky1XYHR.0cjp87QgS1SDuyw9zBnSNEMCyYhuvKkXzcDBQqeK.Ehe');

INSERT INTO public.users (id, first_name, last_name, email, user_role, password)
VALUES (DEFAULT, 'Manager1', 'Manger1', 'manager1_minsk@yopmail.com', 0, e'$2a$12$0ky1XYHR.0cjp87QgS1SDuyw9zBnSNEMCyYhuvKkXzcDBQqeK.Ehe');

INSERT INTO public.users (id, first_name, last_name, email, user_role, password)
VALUES (DEFAULT, 'Manager2', 'Manger2', 'manager2_minsk@yopmail.com', 0, e'$2a$12$0ky1XYHR.0cjp87QgS1SDuyw9zBnSNEMCyYhuvKkXzcDBQqeK.Ehe');

INSERT INTO public.users (id, first_name, last_name, email, user_role, password)
VALUES (DEFAULT, 'Engineer1', 'Engineer1', 'engineer1_minsk@yopmail.com', 2, e'$2a$12$0ky1XYHR.0cjp87QgS1SDuyw9zBnSNEMCyYhuvKkXzcDBQqeK.Ehe');

INSERT INTO public.users (id, first_name, last_name, email, user_role, password)
VALUES (DEFAULT, 'Engineer2', 'Engineer2', 'engineer2_minsk@yopmail.com', 2, e'$2a$12$0ky1XYHR.0cjp87QgS1SDuyw9zBnSNEMCyYhuvKkXzcDBQqeK.Ehe');
