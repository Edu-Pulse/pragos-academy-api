package org.binar.pragosacademyapi.repository;

import org.binar.pragosacademyapi.entity.DetailChapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailChapterRepository extends JpaRepository<DetailChapter, Long> {
}
