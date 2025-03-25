create table if not exists universities(
                                           id bigint primary key,
                                           name varchar(255),
    description text,
    rating numeric(3,2),
    base_cost BIGINT,
    location varchar(255),
    website varchar(255),
    accreditation varchar(255),
                                           rating_count bigint,
    contact_email varchar(255),
    logo_url varchar(255),
    created_by varchar(50),
    created_date timestamp,
    last_modified_by varchar(50),
    last_modified_date timestamp,
                                           military_department boolean,
                                           dormitory boolean,
    active boolean
    );

CREATE SEQUENCE universities_address_id_seq START WITH 1 INCREMENT BY 1;
ALTER TABLE university_address ALTER COLUMN id SET DEFAULT nextval('universities_address_id_seq');
SELECT last_value, is_called FROM universities_id_seq;
ALTER SEQUENCE universities_id_seq RESTART WITH 1;

CREATE SEQUENCE faculty_id_seq START WITH 1 INCREMENT BY 1;
ALTER TABLE faculties ALTER COLUMN id SET DEFAULT nextval('faculty_id_seq');

CREATE SEQUENCE universities_id_seq START WITH 1 INCREMENT BY 1;
ALTER TABLE universities ALTER COLUMN id SET DEFAULT nextval('universities_id_seq');

CREATE SEQUENCE courses_id_seq START WITH 1 INCREMENT BY 1;
ALTER TABLE courses ALTER COLUMN id SET DEFAULT nextval('courses_id_seq');

CREATE SEQUENCE universities_id_seq START WITH 1 INCREMENT BY 1;
ALTER TABLE universities ALTER COLUMN id SET DEFAULT nextval('universities_id_seq');

CREATE SEQUENCE review_id_seq START WITH 1 INCREMENT BY 1;
ALTER TABLE reviews ALTER COLUMN id SET DEFAULT nextval('review_id_seq');

CREATE SEQUENCE review_comments_id_seq START WITH 1 INCREMENT BY 1;
ALTER TABLE review_comments ALTER COLUMN id SET DEFAULT nextval('review_comments_id_seq');

CREATE SEQUENCE specialties_comments_id_seq START WITH 1 INCREMENT BY 1;
ALTER TABLE specialties ALTER COLUMN id SET DEFAULT nextval('specialties_comments_id_seq');

create table if not exists courses(
                                      id bigint primary key,
                                      university_id bigint REFERENCES universities(id),
    name varchar(255),
    description text,
    duration_years integer,
    tuition_fee bigint,
    requirements text,
    study_mode varchar(50),
    language varchar(50),
    created_by varchar(50),
    created_date timestamp,
    last_modified_by varchar(50),
    last_modified_date timestamp,
    active boolean
    );

create table if not exists faculties(
    id bigint primary key,
    university_id bigint REFERENCES universities(id),
    name varchar(255),
    description text,
    contact_email varchar,
    contact_phone varchar,
    created_by varchar(50),
    base_cost bigint,
    created_date timestamp,
    last_modified_by varchar(50),
    last_modified_date timestamp,
    active boolean
);

create table if not exists specialties(
  id bigint primary key,
  name varchar(255),
  faculty_id bigint REFERENCES faculties(id),
    description text,
    created_by varchar(50),
    created_date timestamp,
    last_modified_by varchar(50),
    last_modified_date timestamp,
    active boolean
    );

create table if not exists course_specialties(
                                                 course_id bigint references courses(id) not null ,
    specialty_id bigint references specialties(id) not null,
    primary key (course_id,specialty_id)
    );

create table if not exists reviews(
    id bigint primary key,
    university_id bigint references universities(id) not null,
    user_id BIGINT NOT NULL,
    comment TEXT,
    rating SMALLINT,
    status VARCHAR(20) DEFAULT 'PENDING',
    likes INT DEFAULT 0,
    dislikes INT DEFAULT 0,
    created_by varchar(50),
    created_date timestamp,
    last_modified_by varchar(50),
    last_modified_date timestamp,
    active boolean
);

CREATE TABLE review_comments (
     id BIGINT PRIMARY KEY,
     review_id BIGINT NOT NULL,
     user_id BIGINT NOT NULL,
     comment text,
     created_by varchar(50),
     created_date timestamp,
     last_modified_by varchar(50),
     last_modified_date timestamp,
     active boolean,
     CONSTRAINT fk_review
         FOREIGN KEY (review_id) REFERENCES reviews(id)
);

create table if not exists comparison_histories(
                                                   id      bigint primary key,
                                                   user_id bigint,
                                                   specialty_id bigint references specialties(id),
    university_ids jsonb,
    criteria jsonb,
    created_by varchar(50),
    created_date timestamp,
    last_modified_by varchar(50),
    last_modified_date timestamp,
    active boolean
);


create table if not exists university_address(
id bigint primary key,
city varchar,
region varchar,
university_id bigint references universities(id) not null,
full_address varchar,
created_by varchar(50),
created_date timestamp,
last_modified_by varchar(50),
last_modified_date timestamp,
active boolean
);