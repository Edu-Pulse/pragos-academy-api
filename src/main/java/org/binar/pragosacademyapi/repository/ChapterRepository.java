package org.binar.pragosacademyapi.repository;

import org.binar.pragosacademyapi.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    Chapter findByCapther(String chapter);
    @Query("select ch.capther from Chapter ch where ch.course.code =:courseCode")
    List<String> listChapterByCourse(@Param("courseCode") String courseCode);
}
