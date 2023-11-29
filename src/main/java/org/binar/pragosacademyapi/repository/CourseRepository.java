package org.binar.pragosacademyapi.repository;

import org.binar.pragosacademyapi.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}
