package org.example.cinemapjt.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "영화")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Movie {

    @Id
    @Column(name = "영화번호", length = 200, nullable = false)
    private String movieId;

    @Column(name = "영화제목", length = 200, nullable = false)
    private String title;

    @Column(name = "장르", length = 20, nullable = false)
    private String genre;

    @Column(name = "영화감독", length = 20, nullable = false)
    private String director;

    @Column(name = "주연배우", length = 100, nullable = false)
    private String leadActor;

    @Column(name = "포스터경로", length = 255) // 파일 경로 저장
    private String posterPath;
}