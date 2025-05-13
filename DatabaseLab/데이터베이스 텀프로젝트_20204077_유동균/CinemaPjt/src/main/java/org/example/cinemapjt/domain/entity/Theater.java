package org.example.cinemapjt.domain.entity;

import jakarta.persistence.*;                       // @Entity, @Data 등 data에 쓰는 라이브러리
import lombok.*;                                    // lombok 사용


/* 상영관 개체테이블 Entity
   pk = 상영관번호
*/

@Entity
@Table(name = "상영관")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Theater {

    @Id
    @Column(name = "상영관번호", length = 10, nullable = false)
    private String theaterId;

    @Column(name = "상영관형태", length = 10, nullable = false)
    private String theaterType;
}
