package hello.hello_spring.service;

import hello.hello_spring.domain.Member;
import hello.hello_spring.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// 메서드 호출 시 트랜젝션을 켜고 (Lock) 정상 실행되면 Commit(DB에 저장 완료, Unlock)
// 작업 도중 에러 발생시 자동 Rollback (DB를 기존상태로 되돌림, Unlock)
// JPA : 데이터를 DB에 연결해서 작업x, 한꺼번에 DB에 반영 (Flush)하므로 시작 신호가 필요함
@Transactional // 데이터베이스 연동 오류 방지
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    // MemberService에서 memberRepository 직접 new를 사용하여 생성 X
    // 외부에서 MemberService 생성자에 memberRepository 전달
    // MemberService입장에서 보면 dependency injection(DI) 의존성 주입
//    public MemberService(@Qualifier("jdbcTemplateMemberRepository") MemberRepository memberRepository) {
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 회원 가입
    public Long join(Member member) {
        // 중복 회원 검증
        validateDuplicateMember(member);
        // 중복회원이 아니면 회원 가입
        memberRepository.save(member);
        return member.getId();
    }

    // 중복 회원 검증 메서드
    // isPresent() 메서드는 Optional 객체가 값을 가지고 있는 경우 true, 없다면 false 반환
    // ifPresent() 메서드는 Optional 객체가 값을 가지고 있는 경우 실행, 없다면 구문을 넘어감.
    private void validateDuplicateMember(Member member) {
        Optional<Member> result = memberRepository.findByName(member.getName());
        result.ifPresent(m -> {
            throw new IllegalThreadStateException("이미 존재하는 회원");
        });
    }

    // 전체 회원 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // id로 회원 조회
    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
