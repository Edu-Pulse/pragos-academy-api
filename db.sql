CREATE TABLE users(
      id BIGSERIAL PRIMARY KEY ,
      name VARCHAR(200) NOT NULL ,
      email VARCHAR(200) UNIQUE NOT NULL ,
      phone VARCHAR(200) UNIQUE NOT NULL ,
      password VARCHAR NOT NULL ,
      city VARCHAR NOT NULL ,
      country VARCHAR NOT NULL ,
      image_profile VARCHAR ,
      role VARCHAR NOT NULL ,
      is_enable BOOLEAN NOT NULL
);

SELECT * FROM users;

CREATE TABLE user_verifications(
    id BIGSERIAL PRIMARY KEY ,
    user_id BIGINT UNIQUE NOT NULL ,
    verification_code INT NOT NULL ,
    expired_at timestamp NOT NULL ,
    constraint fk_user_verifications_users foreign key (user_id) references users(id)
);

SELECT * FROM user_verifications;

CREATE TABLE courses(
    code VARCHAR(20) PRIMARY KEY ,
    category_id INT NOT NULL ,
    name VARCHAR(100) NOT NULL ,
    lecturer VARCHAR(100) NOT NULL ,
    description TEXT NOT NULL ,
    intended_for TEXT NOT NULL ,
    level VARCHAR NOT NULL ,
    type VARCHAR NOT NULL ,
    price INT NOT NULL ,
    discount INT,
    constraint fk_courses_category foreign key (category_id) references categories(id)
);

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
     chapter VARCHAR NOT NULL UNIQUE ,
     constraint fk_chapters_courses foreign key (course_code) references courses(code)
);

Alter table chapters drop column chapter;
Alter table chapters add column chapter VARCHAR NOT NULL default 'Chapter 1 - Pendahuluan' ;

SELECT * FROM chapters;

CREATE TABLE detail_chapters(
    id BIGSERIAL PRIMARY KEY ,
    chapter_id BIGINT NOT NULL ,
    name VARCHAR(100) NOT NULL ,
    video VARCHAR NOT NULL ,
    material TEXT NOT NULL ,
    constraint fk_detail_chapters_chapters foreign key (chapter_id) references chapters(id)
);

SELECT * FROM detail_chapters;

CREATE TABLE user_detail_chapters(
  id BIGSERIAL PRIMARY KEY ,
  user_id BIGINT NOT NULL ,
  detail_chapter_id BIGINT NOT NULL ,
  is_done BOOLEAN NOT NULL ,
  constraint user_detail_chapters_users foreign key (user_id) references users(id),
  constraint user_detail_chapters_detail_chapters foreign key (detail_chapter_id) references detail_chapters(id)
);

select * from user_detail_chapters;

CREATE TABLE payments(
     id BIGSERIAL PRIMARY KEY ,
     user_id BIGINT NOT NULL ,
     course_code VARCHAR NOT NULL ,
     amount BIGINT NOT NULL ,
     payment_method VARCHAR NOT NULL ,
     card_number VARCHAR NOT NULL ,
     card_holder_name VARCHAR NOT NULL ,
     cvv VARCHAR NOT NULL ,
     expiry_date VARCHAR NOT NULL ,
     status boolean NOT NULL ,
     payment_date DATE ,
     rating INT,
     constraint fk_payments_users foreign key (user_id) references users(id),
     constraint fk_payments_courses foreign key (course_code) references courses(code)
);

SELECT * FROM chapters;

SELECT * FROM payments;

select c.name, c.code from payments p inner join courses c on p.course_code = 'UIUX0123' where p.user_id = 1 and c.code = 'UIUX0123';
