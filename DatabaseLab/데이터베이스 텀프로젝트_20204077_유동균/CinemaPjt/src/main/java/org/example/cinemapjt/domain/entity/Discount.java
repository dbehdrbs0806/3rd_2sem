package org.example.cinemapjt.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "할인율")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Discount {

    @Id
    @Column(name = "등급", length = 10, nullable = false)
    private String grade;

    @Column(name = "할인율", nullable = false, precision = 5)
    private Double discountRate;
}
