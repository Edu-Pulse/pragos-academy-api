package org.binar.pragosacademyapi.repository;

import org.binar.pragosacademyapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
