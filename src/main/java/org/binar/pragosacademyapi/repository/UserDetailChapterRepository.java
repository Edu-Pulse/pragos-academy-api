package org.binar.pragosacademyapi.repository;

import org.binar.pragosacademyapi.entity.UserDetailChapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailChapterRepository extends JpaRepository<UserDetailChapter, Long> {
    Boolean existsByUserIdAndDetailChapter_IdAndAndIsDone(Long userId, Long detailChapterId, Boolean isDone);
}
