package org.example.cinemapjt.config;


import org.example.cinemapjt.service.GradeService;
import org.example.cinemapjt.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.context.annotation.*;
import org.example.cinemapjt.domain.repository.*;

/*
    Configuration class
    DI(의존성 주입을 받기위해 @Bean 처리하는 부분)
    @Configuration: config 파일로 사용하기 위한 어노테이션
    @Bean: Bean으로 처리하기 위한 어노테이션
 */
@Configuration
public class AppConfig {

    /*  DiscountRepository 사용에 @Bean을 붙이지 않아도 되는 이유
        JPA로 생성한 Repository이기 때문에 자동으로 Bean으로 등록함
     */
    private final DiscountRepository discountRepository;
    private final MovieRepository movieRepository;

    public AppConfig(DiscountRepository discountRepository, MovieRepository movieRepository) {
        this.discountRepository = discountRepository;
        this.movieRepository = movieRepository;
    }

    @Bean                                                   // @Bean 처리
    public MemberService memberService() {
        return new MemberService();
    }

    @Bean
    public GradeService gradeService() {
        return new GradeService(discountRepository);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
