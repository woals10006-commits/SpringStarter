package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MemoryMemberRepositoryTest {
    MemoryMemberRepository repository = new MemoryMemberRepository();

    /*
    한번에 여러 테스트를 실행하면 메모리 DB에 직전 테스트 결과가 남을 수 있다.
    이렇게 되면 다음 테스트가 이전 테스트 결과 때문에 실패할 가능성이 있다.
    따라서 각 테스트가 종료될 때 마다 메모리(DB)에 저장된 데이터 삭제
    테스트는 각각 독립적으로 실행되야 한다. 테스트 순서에 의존관계가 있는 것은 좋은 테스트가 아니다.
     */
    @AfterEach
    public void afterEach() {
        repository.clearStore();
    }

    @Test
    public void save() {
        // given : 준비
        Member member = new Member();
        member.setName("spring");

        // when : 실행
        repository.save(member);

        // then : 검증
        Member result = repository.findById(member.getId()).get();
        // assertThat(실제값).isEqualTo(기대값)
        assertThat(result).isEqualTo(member);
    }

    @Test
    public void findByName() {
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        Member result = repository.findByName("spring1").get();
        assertThat(result).isEqualTo(member1);
    }

    @Test
    public void findAll() {
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        List<Member> result = repository.findAll();

        assertThat(result.size()).isEqualTo(2);
    }
}
