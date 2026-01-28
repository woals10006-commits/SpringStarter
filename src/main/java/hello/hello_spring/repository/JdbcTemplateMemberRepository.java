package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
//@Primary // 여러 후보 중 이 객체가 1순위로 주입
public class JdbcTemplateMemberRepository implements  MemberRepository{
    // 스프링 프레임워크가 제공하는 JDBC 이용을 위한 핼퍼
    // 즉, DB 사용 코드를 줄이는 기능을 가진 객체
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateMemberRepository(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Member save(Member member) {
        // SimpleJdbcInsert : SQL을 직접 작성하지 않고 데이터를 넣을 수 있도록 도와주는 객체
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("member").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", member.getName()); // 컬럼명과 넣을 값 매핑

        // DB가 자동으로 생성한 ID 받기
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        // member 객체에 ID 저장
        member.setId(key.longValue());

        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        // query() 가 조회, 연결, 반납을 모두 자동으로 처리
        List<Member> result = jdbcTemplate.query("SELECT * FROM member WHERE id = ?", memberRowMapper(), id);
        // 결과가 리스트이므로 stream()을 사용하여 findAny()를 통해 리스트에 저장된 첫번째 값을 찾아 반환
        return result.stream().findAny();
    }

    @Override
    public Optional<Member> findByName(String name) {
        // query() 가 조회, 연결, 반납을 모두 자동으로 처리
        List<Member> result = jdbcTemplate.query("SELECT * FROM member WHERE name = ?", memberRowMapper(), name);
        // 결과가 리스트이므로 stream()을 사용하여 findAny()를 통해 리스트에 저장된 첫번째 값을 찾아 반환
        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        return jdbcTemplate.query("SELECT * FROM member", memberRowMapper());
    }

    public void updateName(Long id, String newName){
        jdbcTemplate.update("UPDATE member SET name = ? WHERE id = ?", newName, id);
    }

    /*
    RowMapper: DB에서 꺼내온 한 줄(Row)의 데이터를 자바 객체로 어떻게 저장할지 정의
     */
    private RowMapper<Member> memberRowMapper(){
        return (rs, rowNum) -> {
          Member member = new Member();
          member.setId(rs.getLong("id"));
          member.setName(rs.getString("name"));
          return member;
        };
    }
}
