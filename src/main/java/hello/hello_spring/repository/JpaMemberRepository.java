package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class JpaMemberRepository implements MemberRepository {

    // JPA 라이브러리를 받으면 스프링은 자동으로 EntityManager 객체 생성
    // JPA는 EntityManager 객체로 모든 동작을 수행
    private EntityManager em;

    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Member save(Member member) {
        // persist() : 영속성 컨텍스트에 객체를 저장
        // 즉, 메서드 호출시 실제 INSERT SQL문 수행
        em.persist(member);
        return member;
    }

    //ID(PK)로 조회
    @Override
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    //PK가 아닌 조건으로 여러 건 조회시 JPQL 사용
    @Override
    public List<Member> findAll() {
        return em.createQuery(
                "select m from Member m", // Member 엔티티 기준 쿼리 작성
                Member.class
        ).getResultList();
    }

    // :변수명 - 파라미터 바인딩 사용
    @Override
    public Optional<Member> findByName(String name) {
        List<Member> result = em.createQuery(
                "select m  from Member m where m.name = :name",
                Member.class
        ).setParameter("name", name).getResultList();
        // 여러 결과 중 하나만 반환 (중복 이름 고려)
        return result.stream().findAny();
    }
}
