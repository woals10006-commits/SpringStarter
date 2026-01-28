package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest // 스프링컨테이너를 실행해서 테스트(DB 접속 정보 등을 가져오기 위함)
//@Transactional // 테스트 완료 후 DB에서 변경된 데이터를 자동으로 롤백
class JdbcTemplateMemberRepositoryTest {

    @Autowired
    JdbcTemplateMemberRepository jdbcTemplateMemberRepository;

    @Autowired
    org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    @BeforeEach // 각 테스트 메서드가 실행되기 직전에 매번 실행되도록 설정
    void sequenceReset() {
        // 1. 현재 테이블에서 ID 최대값 찾기 (데이터가 없으면 0)
        Long maxId = jdbcTemplate.queryForObject("SELECT COALESCE(MAX(ID), 0) FROM MEMBER", Long.class);

        // 2. 다음 번호를 (최대값 + 1)로 재설정합니다.
        String sql = "ALTER TABLE MEMBER ALTER COLUMN ID RESTART WITH " + (maxId + 1);
        jdbcTemplate.execute(sql);
    }


    @Test
    void save() {
        // 준비
        Member member = new Member();
        member.setName("재민");
        Member savedMember = jdbcTemplateMemberRepository.save(member);

        // 실행
        String newName = "스프링 고수";
        jdbcTemplateMemberRepository.updateName(member.getId(), newName);

        // 검증
        Member findMember = jdbcTemplateMemberRepository.findById(savedMember.getId()).get();
        assertThat(findMember.getName()).isEqualTo(newName);
        System.out.println("변경 후 이름: " + findMember.getName());

    }

    @Test
    void findById() {
    }

    @Test
    void findByName() {
    }

    @Test
    void findAll() {
    }
}