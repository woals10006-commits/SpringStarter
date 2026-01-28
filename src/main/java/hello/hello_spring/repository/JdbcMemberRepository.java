package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
// jsp&servlet에서 사용하는 방식
public class JdbcMemberRepository implements MemberRepository {
    // DB에 연결하기 위한정보를 담고있는 객체 주입
    @Autowired
    private DataSource dataSource;

    // 생성자 의존성 주입
    public JdbcMemberRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Member save(Member member) {
        // ? : 바인딩 변수 -> 나중에 실제 데이터가 들어갈 자리 표시
        String sql = "INSERT INTO member(name) VALUES(?)";

        Connection conn = null; // DB와 연결하는 객체
        PreparedStatement pstmt = null;  // SQL을 실행하는 객체
        ResultSet rs = null; // SQL문의 결과값을 돌려받는 객체

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, member.getName());

            pstmt.executeUpdate(); // 실제로 DB에 쿼리를 전달(실행)
            rs = pstmt.getGeneratedKeys(); // 방금 생성된 ID(PK)를 가져옴

            if (rs.next()) { // 쿼리 결과 값이 존재하면
                member.setId(rs.getLong(1));
            }else{
                throw new SQLException("id 조회 실패");
            }
            return member;
        } catch (Exception e) {
            e.printStackTrace(); // 에러 발생시 어떤 에러가 발생했는지 출력 후 다음 코드 진행
            throw new IllegalStateException(e); // 에러 발생시 더 이상의 코드는 진행 불가
        }finally{
            // 사용한 자원은 반드시 반납(즉, open -> close)
            close(conn, pstmt, rs);
        }
    }

    @Override
    public Optional<Member> findById(Long id) {
        String sql = "SELECT * FROM member WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

            // 데이터 조회시 executeQuery() 사용하고 ResultSet에 결과 저장
            rs = pstmt.executeQuery();

            if (rs.next()) { // 결과가 존재한다면
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));

                // Optional이용시 NullPointerException 발생하지 않는다.
                return Optional.of(member); // 결과를 Optional로 감싸서 반환
            } else {
                return Optional.empty(); // 결과가 없으면 빈 Optional 반환
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public Optional<Member> findByName(String name) {
        String sql = "SELECT * FROM member WHERE name = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));

                return Optional.of(member);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public List<Member> findAll() {
        String sql = "SELECT * FROM member";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            List<Member> members = new ArrayList<>();

            while (rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                members.add(member);
            }
            return members;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    private Connection getConnection() {
        // 스프링 프레임워크를 사용할 때 DataSourceUtils을 사용해야
        // 트랜잭션(작업 단위 관리)이 꼬이지 않고 Connection 객체 얻어올수 있다.
        return DataSourceUtils.getConnection(dataSource);
    }

    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs){
        try {
            if(rs != null){
                rs.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        try {
            if(pstmt != null){
                pstmt.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        try {
            if(conn != null){
                conn.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    private  void close(Connection conn){
        DataSourceUtils.releaseConnection(conn, dataSource);
    }
}
