package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MemoryMemberRepository implements MemberRepository {
    // 자바에서 임시 저장공간 Map으로 할당(why? DB 선택이 안되어있으므로)
    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L; // Map의 key 사용할 변수

    @Override
    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        // 결과가 null이여도, 클라이언트에서 처리가능
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream()
                .filter(member -> member.getName().equals(name))
                .findAny();
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    // (테스트용: 하나의 테스트 진행 후 repository를 비우는 메서드)
    public void clearStore() {
        store.clear();
    }
}
