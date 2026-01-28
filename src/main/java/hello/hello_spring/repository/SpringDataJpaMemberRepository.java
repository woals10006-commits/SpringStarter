package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>,
        MemberRepository {
    @Override
    Optional<Member> findByName(String name);
    // selcet m from Member m where m.name = ? 형태로 스프링 데이터 JPA가 JPQL 구성
}
