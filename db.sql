Create table users(
    id BIGSERIAL PRIMARY KEY ,
    name VARCHAR(200) NOT NULL ,
    email VARCHAR(200) UNIQUE NOT NULL ,
    phone VARCHAR(200) UNIQUE NOT NULL ,
    password VARCHAR NOT NULL ,
    image_profile VARCHAR ,
    role VARCHAR NOT NULL
);

SELECT * FROM users;

CREATE TABLE courses(
    code VARCHAR(20) PRIMARY KEY ,
    name VARCHAR(100) NOT NULL ,
    description TEXT NOT NULL ,
    level VARCHAR NOT NULL ,
    category VARCHAR NOT NULL ,
    type VARCHAR NOT NULL ,
    price INT NOT NULL ,
    discount INT
);

SELECT * FROM courses;

CREATE TABLE chapters(
    id BIGSERIAL PRIMARY KEY ,
    course_code VARCHAR NOT NULL,
    name VARCHAR(100) NOT NULL ,
    video VARCHAR NOT NULL ,
    material TEXT NOT NULL ,
    chapter VARCHAR NOT NULL ,
    constraint fk_chapters_courses foreign key (course_code) references courses(code)
);

CREATE TABLE payments(
    id BIGSERIAL PRIMARY KEY ,
    user_id BIGINT NOT NULL ,
    course_code VARCHAR NOT NULL ,
    amount BIGINT NOT NULL ,
    payment_method VARCHAR NOT NULL ,
    status boolean NOT NULL ,
    payment_date DATE ,
    constraint fk_payments_users foreign key (user_id) references users(id),
    constraint fk_payments_courses foreign key (course_code) references courses(code)
);

CREATE TABLE user_chapters(
    id BIGSERIAL PRIMARY KEY ,
    user_id BIGINT NOT NULL ,
    chapter_id BIGINT NOT NULL ,
    is_done BOOLEAN NOT NULL ,
    constraint fk_user_chapters_users foreign key (user_id) references users(id),
    constraint fk_user_chapters_chapters foreign key (chapter_id) references chapters(id)
);