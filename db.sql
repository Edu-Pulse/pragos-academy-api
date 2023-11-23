Create table users(
    id BIGSERIAL PRIMARY KEY ,
    name VARCHAR(200) NOT NULL ,
    email VARCHAR(200) UNIQUE NOT NULL ,
    phone VARCHAR(200) UNIQUE NOT NULL ,
    password VARCHAR NOT NULL ,
    city VARCHAR NOT NULL ,
    country VARCHAR NOT NULL ,
    image_profile VARCHAR ,
    role VARCHAR NOT NULL
);
SELECT * FROM users;

CREATE TABLE courses(
    code VARCHAR(20) PRIMARY KEY ,
    category_id INT NOT NULL ,
    name VARCHAR(100) NOT NULL ,
    description TEXT NOT NULL ,
    level VARCHAR NOT NULL ,
    type VARCHAR NOT NULL ,
    price INT NOT NULL ,
    discount INT,
    rating FLOAT NOT NULL ,
    constraint fk_courses_category foreign key (category_id) references categories(id)
);
ALTER TABLE courses ADD COLUMN lecturer VARCHAR(100) NOT NULL default 'Admin';
ALTER TABLE courses ADD COLUMN intended_for TEXT NOT NULL default '';
SELECT * FROM courses;

CREATE TABLE categories(
    id INT PRIMARY KEY ,
    name VARCHAR(50) NOT NULL ,
    image VARCHAR(100) NOT NULL
);

SELECT * from categories;


CREATE TABLE chapters(
    id BIGSERIAL PRIMARY KEY ,
    course_code VARCHAR NOT NULL,
    name VARCHAR(100) NOT NULL ,
    video VARCHAR NOT NULL ,
    material TEXT NOT NULL ,
    chapter VARCHAR NOT NULL ,
    constraint fk_chapters_courses foreign key (course_code) references courses(code)
);

SELECT * FROM chapters;

CREATE TABLE payments(
    id BIGSERIAL PRIMARY KEY ,
    user_id BIGINT NOT NULL ,
    course_code VARCHAR NOT NULL ,
    amount BIGINT NOT NULL ,
    payment_method VARCHAR NOT NULL ,
    status boolean NOT NULL ,
    payment_date DATE ,
    rating INT,
    constraint fk_payments_users foreign key (user_id) references users(id),
    constraint fk_payments_courses foreign key (course_code) references courses(code)
);


SELECT * FROM payments;

CREATE TABLE user_chapters(
    id BIGSERIAL PRIMARY KEY ,
    user_id BIGINT NOT NULL ,
    chapter_id BIGINT NOT NULL ,
    is_done BOOLEAN NOT NULL ,
    constraint fk_user_chapters_users foreign key (user_id) references users(id),
    constraint fk_user_chapters_chapters foreign key (chapter_id) references chapters(id)
);

SELECT * FROM user_chapters;