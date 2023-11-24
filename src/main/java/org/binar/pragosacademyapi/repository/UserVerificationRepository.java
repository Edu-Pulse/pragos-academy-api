package org.binar.pragosacademyapi.repository;

import org.binar.pragosacademyapi.entity.UserVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserVerificationRepository extends JpaRepository<UserVerification, Long> {
    UserVerification findByUser_Id(Long userId);
}
