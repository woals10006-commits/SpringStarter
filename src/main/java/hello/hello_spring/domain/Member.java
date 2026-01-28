package hello.hello_spring.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
// @Component : 자바 빈 등록
@Entity // JPA의 개체 등록
public class Member {

    @Id // 기본키(PK) 설정
    // DB가 ID를 자동으로 생성
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id; // 시스템이 저장하는 id
    private String name;
}
