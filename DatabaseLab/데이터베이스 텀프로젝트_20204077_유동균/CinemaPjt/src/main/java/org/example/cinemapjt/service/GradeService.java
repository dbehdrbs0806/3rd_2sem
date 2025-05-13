package org.example.cinemapjt.service;

import org.example.cinemapjt.domain.dto.DiscountDto;
import org.example.cinemapjt.domain.entity.Discount;
import org.example.cinemapjt.domain.repository.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GradeService {

    private final DiscountRepository discountRepository;

    @Autowired
    public GradeService(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    // 모든 할인율 조회
    public List<DiscountDto> getAllDiscounts() {
        List<Discount> discounts = discountRepository.findAll();
        return discounts.stream()
                .map(discount -> new DiscountDto(discount.getGrade(), discount.getDiscountRate()))
                .collect(Collectors.toList());
    }

    // 특정 등급 할인율 조회
    public DiscountDto getDiscountByGrade(String grade) {
        Discount discount = discountRepository.findByGrade(grade);
        if (discount == null) {
            throw new IllegalArgumentException("해당 등급의 할인율이 존재하지 않습니다: " + grade);
        }
        return new DiscountDto(discount.getGrade(), discount.getDiscountRate());
    }

    // 할인율 업데이트
    public void updateDiscounts(Map<String, Double> discountUpdates) {
        discountUpdates.forEach((grade, discountRate) -> {
            Discount discount = discountRepository.findByGrade(grade);
            if (discount == null) {
                throw new IllegalArgumentException("해당 등급의 할인율이 존재하지 않습니다: " + grade);
            }
            discount.setDiscountRate(discountRate);
            discountRepository.save(discount);
        });
    }
}
