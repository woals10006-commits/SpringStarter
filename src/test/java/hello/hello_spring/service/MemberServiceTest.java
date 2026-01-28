package hello.hello_spring.service;

import hello.hello_spring.domain.Member;
import hello.hello_spring.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MemberServiceTest {
    MemoryMemberRepository memoryMemberRepository;
    MemberService memberService;

    // @BeforeEach : 테스트 메서드 실행이 되기 전에 한번씩 동작하는 메서드
    @BeforeEach
    public void beforeEach(){
        memoryMemberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memoryMemberRepository);
    }

    // clearStore() : Map에 저장된 값들을 비우는 메서드
    // @AfterEach : 테스트 메서드 실행이 끝날때 마다 한 번씩 동작하는 콜백 메서드
    // 테스트 후 repository에 객체가 남아 다음 테스트 진행시 발생하는 에러 방지
    @AfterEach
    public void afterEach() {
        memoryMemberRepository.clearStore();
    }

    // 회원 가입 테스트
    @Test
    void join() {
        Member member = new Member();
        member.setName("user1");

        Long saveId = memberService.join(member);

        Member findeMember = memberService.findOne(saveId).get();
        assertThat(member.getName()).isEqualTo(findeMember.getName());
    }

    @Test
    public void validateDuplicateMember(){
        Member member1 = new Member();
        member1.setName("spring1");

        Member member2 = new Member();
        member2.setName("spring1");

        memberService.join(member1);
        IllegalThreadStateException e =
                assertThrows(IllegalThreadStateException.class, () -> memberService.join(member2));

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원");
    }

}
