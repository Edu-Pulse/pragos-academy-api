package org.binar.pragosacademyapi.repository;

import org.binar.pragosacademyapi.entity.Course;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    Course findByCode(String code);

    @Query("SELECT C FROM Course C WHERE C.discount =: discount OR C.category =: category.id OR C.level =: level OR C.type =: type ")
    List<Course> filter(@Param("discount") Boolean discount, @Param("category")Long category, @Param("level")String level, @Param("type")String type);
}
