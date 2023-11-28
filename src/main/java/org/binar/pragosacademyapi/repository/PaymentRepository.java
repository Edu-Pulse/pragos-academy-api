package org.binar.pragosacademyapi.repository;

import org.binar.pragosacademyapi.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
