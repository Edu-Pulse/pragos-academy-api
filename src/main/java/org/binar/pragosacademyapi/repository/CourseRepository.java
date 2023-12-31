package org.binar.pragosacademyapi.repository;

import org.binar.pragosacademyapi.entity.Course;
import org.binar.pragosacademyapi.entity.dto.CourseDto;
import org.binar.pragosacademyapi.enumeration.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    Course findByCode(String code);
    @Query("select count(dc.id) " +
            "from Course c " +
            "left join Chapter ch on c.code = ch.course.code " +
            "left join DetailChapter dc on ch.id = dc.chapter.id where c.code =:courseCode" )
    Integer getCountDetailChapter(@Param("courseCode") String courseCode);
    @Query("select count(udc.id) " +
            "from Course c " +
            "left join Chapter ch on c.code = ch.course.code " +
            "left join DetailChapter dc on ch.id = dc.chapter.id " +
            "left join UserDetailChapter udc on dc.id = udc.detailChapter.id " +
            "where c.code =:courseCode " +
            "and " +
            "udc.user.email =:email " +
            "and " +
            "udc.isDone = true ")
    Integer getCountDetailChapterDone(@Param("courseCode") String courseCode, @Param("email") String email);

    @Query("select " +
            "new org.binar.pragosacademyapi.entity.dto.CourseDto(c.code, c.category.image, c.category.name, c.name, c.description, c.lecturer, c.level, c.type, c.price, c.discount, c.createdAt) " +
            "from Course c " +
            "where c.type =:type")
    Page<CourseDto> filterByType(@Param("type") Type type, Pageable pageable);
    @Query("select " +
            "new org.binar.pragosacademyapi.entity.dto.CourseDto(c.code, c.category.image, c.category.name, c.name, c.description, c.lecturer, c.level, c.type, c.price, c.discount, c.createdAt) " +
            "from Course c " +
            "where lower(c.name) like lower(:courseName) ")
    List<CourseDto> searchByCourseName(@Param("courseName") String courseName);

    @Query("select " +
            "new org.binar.pragosacademyapi.entity.dto.CourseDto(c.code, c.category.image, c.category.name, c.name, c.description, c.lecturer, c.level, c.type, c.price, c.discount, c.createdAt) " +
            "from Course c " +
            "where c.category.id = :category")
    List<CourseDto> searchByCategory(@Param("category") Integer category);
    @Query("select count(c.code) from Course c")
    Integer getTotalCourse();
    @Query("select count(c.code) from Course c where c.type = :type")
    Integer getTotalCoursePremium(@Param("type") Type type);
}
