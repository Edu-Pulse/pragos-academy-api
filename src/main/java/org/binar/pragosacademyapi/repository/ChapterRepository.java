package org.binar.pragosacademyapi.repository;

import org.binar.pragosacademyapi.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {

    @Query("select ch from Chapter ch where ch.course.code = :courseCode and ch.capther = :chapterName")
    Optional<Chapter> chapterByCourseAndName(@Param("courseCode") String courseCode, @Param("chapterName") String chapterName);
    @Query("select ch.capther from Chapter ch where ch.course.code =:courseCode")
    List<String> listChapterByCourse(@Param("courseCode") String courseCode);
}
