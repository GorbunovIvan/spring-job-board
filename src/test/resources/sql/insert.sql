INSERT INTO `spring-job-board-test`.users (id, created_at, email, name, is_active, password) VALUES (1, '2023-07-28 08:50:59.000000', 'bob@mail.com', 'bob 2', true, '$2a$12$xa.OM7bak.DZhCYnw.zbbuAE0o/ciYHtsfP0jzeq.VJVxmTlMsiim');
INSERT INTO `spring-job-board-test`.users (id, created_at, email, name, is_active, password) VALUES (2, '2023-07-28 08:51:06.000000', 'mike@mail.com', 'Mike', true, '$2a$12$bgx1Q6WrZiaZmJK98eHwyOQTKvA92RD8Afm.qdPJdiYm8276ILd.S');
INSERT INTO `spring-job-board-test`.users (id, created_at, email, name, is_active, password) VALUES (3, '2023-08-02 10:22:29.724363', 'newUser@mail.com', 'newUser', true, '$2a$12$YoTWJQ2O/NfEj1LjZx8UEOc.A/sV0kMnBu1HakV0v5hmpgRHuegNa');

INSERT INTO `spring-job-board-test`.users_roles (user_id, roles) VALUES (1, 'USER');
INSERT INTO `spring-job-board-test`.users_roles (user_id, roles) VALUES (2, 'USER');
INSERT INTO `spring-job-board-test`.users_roles (user_id, roles) VALUES (3, 'USER');

INSERT INTO `spring-job-board-test`.employers (id, name, user_id) VALUES (1, 'new employer 23', 1);
INSERT INTO `spring-job-board-test`.employers (id, name, user_id) VALUES (2, 'uncle bob', 2);
INSERT INTO `spring-job-board-test`.employers (id, name, user_id) VALUES (4, 'employer newUser', 3);

INSERT INTO `spring-job-board-test`.applicants (id, description, first_name, last_name, user_id) VALUES (1, 'some description', 'bob', 'power', 1);
INSERT INTO `spring-job-board-test`.applicants (id, description, first_name, last_name, user_id) VALUES (2, 'about myself 5', 'Mike', 'bush 3', 2);
INSERT INTO `spring-job-board-test`.applicants (id, description, first_name, last_name, user_id) VALUES (10, 'applicant desc newUser', 'applicant firstName newUser', 'applicant lastName newUser', 3);

INSERT INTO `spring-job-board-test`.skills (id, name) VALUES (1, 'hard-working');
INSERT INTO `spring-job-board-test`.skills (id, name) VALUES (2, 'intelligent');
INSERT INTO `spring-job-board-test`.skills (id, name) VALUES (3, 'in-good-shape');
INSERT INTO `spring-job-board-test`.skills (id, name) VALUES (4, 'punctual');

INSERT INTO `spring-job-board-test`.skills_applicants (applicant_id, skill_id) VALUES (1, 1);
INSERT INTO `spring-job-board-test`.skills_applicants (applicant_id, skill_id) VALUES (1, 2);
INSERT INTO `spring-job-board-test`.skills_applicants (applicant_id, skill_id) VALUES (2, 2);
INSERT INTO `spring-job-board-test`.skills_applicants (applicant_id, skill_id) VALUES (10, 2);
INSERT INTO `spring-job-board-test`.skills_applicants (applicant_id, skill_id) VALUES (1, 3);
INSERT INTO `spring-job-board-test`.skills_applicants (applicant_id, skill_id) VALUES (2, 3);
INSERT INTO `spring-job-board-test`.skills_applicants (applicant_id, skill_id) VALUES (2, 4);
INSERT INTO `spring-job-board-test`.skills_applicants (applicant_id, skill_id) VALUES (10, 4);

INSERT INTO `spring-job-board-test`.vacancies (id, created_at, description, title, employer_id, status) VALUES (2, '2023-07-30 09:50:16.430990', 'new vacancy 2 desc 3', 'new vacancy 2 3', 1, 'OPENED');
INSERT INTO `spring-job-board-test`.vacancies (id, created_at, description, title, employer_id, status) VALUES (7, '2023-07-30 15:09:02.503048', 'asdfasfasdfasfasdfsaf desc', 'asdfasdfasdf', 1, 'OPENED');
INSERT INTO `spring-job-board-test`.vacancies (id, created_at, description, title, employer_id, status) VALUES (8, '2023-07-31 14:10:51.308955', 'sdfsdfsdfsdfsd', 'sdf by 2', 2, 'OPENED');
INSERT INTO `spring-job-board-test`.vacancies (id, created_at, description, title, employer_id, status) VALUES (9, '2023-08-01 10:49:15.312555', 'sdfsdfsdfsd', 'sdf by 1', 1, 'OPENED');
INSERT INTO `spring-job-board-test`.vacancies (id, created_at, description, title, employer_id, status) VALUES (10, '2023-08-01 14:53:54.413888', 'for intelligent desc', 'vacancy for intelligent ', 2, 'OPENED');
INSERT INTO `spring-job-board-test`.vacancies (id, created_at, description, title, employer_id, status) VALUES (12, '2023-08-06 14:03:59.479637', 'desc fsdfsdfd', 'vacancy sdfgsdsdfg', 1, 'OPENED');

INSERT INTO `spring-job-board-test`.`job-categories` (id, name) VALUES (1, 'Cars');
INSERT INTO `spring-job-board-test`.`job-categories` (id, name) VALUES (2, 'IT');
INSERT INTO `spring-job-board-test`.`job-categories` (id, name) VALUES (3, 'Management');
INSERT INTO `spring-job-board-test`.`job-categories` (id, name) VALUES (4, 'School');
INSERT INTO `spring-job-board-test`.`job-categories` (id, name) VALUES (5, 'Building');

INSERT INTO `spring-job-board-test`.skills_vacancies (vacancy_id, skill_id) VALUES (2, 1);
INSERT INTO `spring-job-board-test`.skills_vacancies (vacancy_id, skill_id) VALUES (8, 1);
INSERT INTO `spring-job-board-test`.skills_vacancies (vacancy_id, skill_id) VALUES (12, 1);
INSERT INTO `spring-job-board-test`.skills_vacancies (vacancy_id, skill_id) VALUES (10, 2);
INSERT INTO `spring-job-board-test`.skills_vacancies (vacancy_id, skill_id) VALUES (2, 3);
INSERT INTO `spring-job-board-test`.skills_vacancies (vacancy_id, skill_id) VALUES (7, 3);
INSERT INTO `spring-job-board-test`.skills_vacancies (vacancy_id, skill_id) VALUES (9, 3);
INSERT INTO `spring-job-board-test`.skills_vacancies (vacancy_id, skill_id) VALUES (12, 3);
INSERT INTO `spring-job-board-test`.skills_vacancies (vacancy_id, skill_id) VALUES (7, 4);
INSERT INTO `spring-job-board-test`.skills_vacancies (vacancy_id, skill_id) VALUES (12, 4);

INSERT INTO `spring-job-board-test`.vacancies_categories (vacancy_id, category) VALUES (2, 1);
INSERT INTO `spring-job-board-test`.vacancies_categories (vacancy_id, category) VALUES (7, 1);
INSERT INTO `spring-job-board-test`.vacancies_categories (vacancy_id, category) VALUES (12, 2);
INSERT INTO `spring-job-board-test`.vacancies_categories (vacancy_id, category) VALUES (2, 3);
INSERT INTO `spring-job-board-test`.vacancies_categories (vacancy_id, category) VALUES (7, 3);
INSERT INTO `spring-job-board-test`.vacancies_categories (vacancy_id, category) VALUES (10, 3);
INSERT INTO `spring-job-board-test`.vacancies_categories (vacancy_id, category) VALUES (2, 4);
INSERT INTO `spring-job-board-test`.vacancies_categories (vacancy_id, category) VALUES (12, 4);
INSERT INTO `spring-job-board-test`.vacancies_categories (vacancy_id, category) VALUES (2, 5);
INSERT INTO `spring-job-board-test`.vacancies_categories (vacancy_id, category) VALUES (9, 5);

INSERT INTO `spring-job-board-test`.vacancies_modes (vacancy_id, modes) VALUES (2, 'REMOTE');
INSERT INTO `spring-job-board-test`.vacancies_modes (vacancy_id, modes) VALUES (2, 'IN_OFFICE');
INSERT INTO `spring-job-board-test`.vacancies_modes (vacancy_id, modes) VALUES (7, 'REMOTE');
INSERT INTO `spring-job-board-test`.vacancies_modes (vacancy_id, modes) VALUES (7, 'IN_OFFICE');
INSERT INTO `spring-job-board-test`.vacancies_modes (vacancy_id, modes) VALUES (8, 'IN_OFFICE');
INSERT INTO `spring-job-board-test`.vacancies_modes (vacancy_id, modes) VALUES (8, 'REMOTE');
INSERT INTO `spring-job-board-test`.vacancies_modes (vacancy_id, modes) VALUES (9, 'IN_OFFICE');
INSERT INTO `spring-job-board-test`.vacancies_modes (vacancy_id, modes) VALUES (10, 'REMOTE');
INSERT INTO `spring-job-board-test`.vacancies_modes (vacancy_id, modes) VALUES (10, 'IN_OFFICE');
INSERT INTO `spring-job-board-test`.vacancies_modes (vacancy_id, modes) VALUES (12, 'IN_OFFICE');
INSERT INTO `spring-job-board-test`.vacancies_modes (vacancy_id, modes) VALUES (12, 'REMOTE');

INSERT INTO `spring-job-board-test`.vacancies_types (vacancy_id, types) VALUES (2, 'PERMANENT');
INSERT INTO `spring-job-board-test`.vacancies_types (vacancy_id, types) VALUES (2, 'PART_TIME');
INSERT INTO `spring-job-board-test`.vacancies_types (vacancy_id, types) VALUES (2, 'FULL_TIME');
INSERT INTO `spring-job-board-test`.vacancies_types (vacancy_id, types) VALUES (2, 'TEMPORARY');
INSERT INTO `spring-job-board-test`.vacancies_types (vacancy_id, types) VALUES (7, 'FULL_TIME');
INSERT INTO `spring-job-board-test`.vacancies_types (vacancy_id, types) VALUES (7, 'PART_TIME');
INSERT INTO `spring-job-board-test`.vacancies_types (vacancy_id, types) VALUES (7, 'PERMANENT');
INSERT INTO `spring-job-board-test`.vacancies_types (vacancy_id, types) VALUES (8, 'PART_TIME');
INSERT INTO `spring-job-board-test`.vacancies_types (vacancy_id, types) VALUES (8, 'CONTRACT');
INSERT INTO `spring-job-board-test`.vacancies_types (vacancy_id, types) VALUES (8, 'PERMANENT');
INSERT INTO `spring-job-board-test`.vacancies_types (vacancy_id, types) VALUES (9, 'PERMANENT');
INSERT INTO `spring-job-board-test`.vacancies_types (vacancy_id, types) VALUES (10, 'CONTRACT');
INSERT INTO `spring-job-board-test`.vacancies_types (vacancy_id, types) VALUES (10, 'TEMPORARY');
INSERT INTO `spring-job-board-test`.vacancies_types (vacancy_id, types) VALUES (12, 'CONTRACT');
INSERT INTO `spring-job-board-test`.vacancies_types (vacancy_id, types) VALUES (12, 'PERMANENT');
INSERT INTO `spring-job-board-test`.vacancies_types (vacancy_id, types) VALUES (12, 'TEMPORARY');

INSERT INTO `spring-job-board-test`.`response-to-vacancies` (id, applicant_id, vacancy_id, created_at) VALUES (1, 1, 7, '2023-07-31 09:17:36.595594');
INSERT INTO `spring-job-board-test`.`response-to-vacancies` (id, applicant_id, vacancy_id, created_at) VALUES (2, 1, 8, '2023-08-01 10:40:31.505108');
INSERT INTO `spring-job-board-test`.`response-to-vacancies` (id, applicant_id, vacancy_id, created_at) VALUES (3, 2, 7, '2023-08-01 10:41:14.260259');
INSERT INTO `spring-job-board-test`.`response-to-vacancies` (id, applicant_id, vacancy_id, created_at) VALUES (4, 2, 2, '2023-08-02 14:00:31.844885');

