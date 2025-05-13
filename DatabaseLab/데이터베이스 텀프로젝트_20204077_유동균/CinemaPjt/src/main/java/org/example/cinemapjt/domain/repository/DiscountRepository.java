package org.example.cinemapjt.domain.repository;

import org.example.cinemapjt.domain.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.*;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, String> {
    Discount findByGrade(String grade);
}
