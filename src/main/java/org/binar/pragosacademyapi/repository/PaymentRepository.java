package org.binar.pragosacademyapi.repository;

import org.binar.pragosacademyapi.entity.Course;
import org.binar.pragosacademyapi.entity.Payment;
import org.binar.pragosacademyapi.entity.dto.CourseDto;
import org.binar.pragosacademyapi.entity.dto.PaymentDto;
import org.binar.pragosacademyapi.entity.dto.PaymentUserDto;
import org.binar.pragosacademyapi.enumeration.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("select c from Payment p inner join Course c on p.course.code =:courseCode where p.user.email =:email and c.code =:courseCode")
    Course detailCourse(@Param("courseCode") String courseCOde, @Param("email") String email);
    @Query("select p.rating from Payment p where p.course.code =:courseCode and p.rating != null ")
    List<Integer> getListRating(@Param("courseCode") String courseCode);
    Payment findByUser_IdAndCourse_Code(Long userId, String courseCode);
    @Query("select p from Payment p where p.user.email =:email and p.status = true ")
    List<Payment> paymentByUserAndStatus(@Param("email") String email);
    @Query("select new org.binar.pragosacademyapi.entity.dto.CourseDto(p.course.code, p.course.category.image, p.course.category.name, p.course.name, p.course.description, p.course.lecturer, p.course.level, p.course.type, p.course.price, p.course.discount, p.course.createdAt) from Payment p where p.user.email = :email and p.status = true ")
    Page<CourseDto> paymentByUserAndStatusTrue(@Param("email") String email, Pageable pageable);
    @Query("select new org.binar.pragosacademyapi.entity.dto.PaymentDto(p.user.id, p.course.category.name, p.course.name, p.status, p.paymentMethod, p.paymentDate) from Payment p where p.course.type = :type")
    Page<PaymentDto> findByType(@Param("type") Type type, Pageable pageable);
    @Query("SELECT new org.binar.pragosacademyapi.entity.dto.PaymentDto(p.user.id, p.course.category.name, p.course.name, p.status, p.paymentMethod, p.paymentDate) FROM Payment p WHERE lower(p.course.name) like lower(:courseName) ")
    Page<PaymentDto> findByCourseName(@Param("courseName") String courseName, Pageable pageable);
    @Query("select new org.binar.pragosacademyapi.entity.dto.PaymentUserDto(p.course.category.image,p.course.category.name, p.course.name, p.course.lecturer, p.course.level, p.status, p.paymentMethod, p.amount, p.paymentDate) from Payment p where p.user.email = :email")
    Page<PaymentUserDto> paymentByUser(@Param("email") String email, Pageable pageable);

    Payment findByTransactionId(String transactionId);
}
