package org.example.cinemapjt.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "회원")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Member {

    @Id
    @Column(name = "회원번호", length = 10, nullable = false)
    private String memberId;

    @Column(name = "비밀번호", length = 64, nullable = false)
    private String password;

    @Column(name = "회원이름", length = 20, nullable = false)
    private String name;

    @Column(name = "휴대전화", length = 16, nullable = false)
    private String phoneNumber;

    @Column(name = "등급", length = 10, nullable = false)
    private String grade;

    @Column(name = "카드번호", length = 26, nullable = false)
    private String cardNumber;

    @Column(name = "승인여부")
    private int isApproved;
}
