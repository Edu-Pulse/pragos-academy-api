package org.binar.pragosacademyapi.repository;

import org.binar.pragosacademyapi.entity.Course;
import org.binar.pragosacademyapi.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("select c from Payment p inner join Course c on p.course.code =:courseCode where p.user.email =:email")
    Course detailCourse(@Param("courseCode") String courseCOde, @Param("email") String email);
}
