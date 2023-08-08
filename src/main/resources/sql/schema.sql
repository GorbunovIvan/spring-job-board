create table if not exists `job-categories`
(
    id   int auto_increment
        primary key,
    name varchar(99) not null
);

create table if not exists skills
(
    id   int auto_increment
        primary key,
    name varchar(99) not null
);

create table if not exists users
(
    id         bigint auto_increment
        primary key,
    created_at datetime(6)  null,
    email      varchar(255) not null,
    name       varchar(255) null,
    is_active  bit          null,
    password   varchar(255) null,
    constraint users_pk_email
        unique (email)
);

create table if not exists applicants
(
    id          bigint auto_increment
        primary key,
    description varchar(999) not null,
    first_name  varchar(99)  not null,
    last_name   varchar(99)  not null,
    user_id     bigint       null,
    constraint UK_8dcjpq7ywwkgrsen3kr8yfj57
        unique (user_id),
    constraint FKs48x9ywgx7l6mg1egokmys0gp
        foreign key (user_id) references users (id)
);

create table if not exists employers
(
    id      bigint auto_increment
        primary key,
    name    varchar(99) not null,
    user_id bigint      null,
    constraint UK_lf0p1iaulwu7g1cvvyu2vfn2j
        unique (user_id),
    constraint employers_pk_name
        unique (name),
    constraint FK6abfx371o1dsomsi0jstr0utl
        foreign key (user_id) references users (id)
);

create table if not exists skills_applicants
(
    applicant_id bigint not null,
    skill_id     int    not null,
    primary key (applicant_id, skill_id),
    constraint FK6mgp3we657sk2ag4okx1gx13a
        foreign key (applicant_id) references applicants (id),
    constraint FKblmtp21a7dwmdfa2xu0jyy5is
        foreign key (skill_id) references skills (id)
);

create table if not exists users_roles
(
    user_id bigint                 not null,
    roles   enum ('ADMIN', 'USER') null,
    constraint FK2o0jvgh89lemvvo17cbqvdxaa
        foreign key (user_id) references users (id)
);

create table if not exists vacancies
(
    id          bigint auto_increment
        primary key,
    created_at  datetime(6)                         null,
    description varchar(200)                        not null,
    title       varchar(200)                        not null,
    employer_id bigint                              not null,
    status      enum ('CLOSED', 'HIDDEN', 'OPENED') null,
    constraint FKoitkfsx04o9py3xgyvbtuxbqf
        foreign key (employer_id) references employers (id)
);

create table if not exists `response-to-vacancies`
(
    id           bigint auto_increment
        primary key,
    applicant_id bigint      not null,
    vacancy_id   bigint      not null,
    created_at   datetime(6) null,
    constraint FK4sdl8n7y73m2i9yiwinoywmrd
        foreign key (vacancy_id) references vacancies (id),
    constraint FKf06qmu4m93pi9j50qmqgv788i
        foreign key (applicant_id) references applicants (id)
);

create table if not exists skills_vacancies
(
    vacancy_id bigint not null,
    skill_id   int    not null,
    primary key (vacancy_id, skill_id),
    constraint FKdyp50hhmt5ircm0bm9exrwva3
        foreign key (vacancy_id) references vacancies (id),
    constraint FKhroqqwg2j8v4leghiimp8ih32
        foreign key (skill_id) references skills (id)
);

create table if not exists vacancies_categories
(
    vacancy_id bigint not null,
    category   int    not null,
    primary key (vacancy_id, category),
    constraint FKitfatgvq45974thy8kew5tcby
        foreign key (vacancy_id) references vacancies (id),
    constraint FKowsh7xcjpalatohvs0qtuqld
        foreign key (category) references `job-categories` (id)
);

create table if not exists vacancies_modes
(
    vacancy_id bigint                       not null,
    modes      enum ('IN_OFFICE', 'REMOTE') null,
    constraint FK57hchh3nlpj5jfg6swx8dbh92
        foreign key (vacancy_id) references vacancies (id)
);

create table if not exists vacancies_types
(
    vacancy_id bigint                                                                not null,
    types      enum ('CONTRACT', 'FULL_TIME', 'PART_TIME', 'PERMANENT', 'TEMPORARY') null,
    constraint FK596inyebrcj5yrw5dv0m90a2l
        foreign key (vacancy_id) references vacancies (id)
);

