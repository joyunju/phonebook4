package com.javaex.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.javaex.vo.PersonVo;

@Repository
public class PhoneDao {

	@Autowired
	private SqlSession sqlSession;

	@Autowired
	private DataSource dataSource;

	// 0. import java.sql.*;
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

//	private String driver = "oracle.jdbc.driver.OracleDriver";
//	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
//	private String url = "jdbc:oracle:thin:@webdb_high?TNS_ADMIN=/Users/joyunju/Downloads/Wallet_webdb";
//	private String id = "admin";
//	private String pw = "Zooland9412!";

	private void getConnection() {
		try {
			// 1. JDBC 드라이버 (Oracle) 로딩
			// Class.forName(driver);

			// 2. Connection 얻어오기
			// conn = DriverManager.getConnection(url, id, pw);
			// System.out.println("접속성공");
			conn = dataSource.getConnection();

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	private void close() {
		// 5. 자원정리
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	// 전체리스트 가져오기
	public List<PersonVo> getPersonList() {
		/*
		 * System.out.println("PhoneDao>getPersonList");
		 * 
		 * List<PersonVo> personList = sqlSession.selectList("phonebook.selectList");
		 * System.out.println(personList);
		 */

		return sqlSession.selectList("phonebook.selectList");
	}

	// 사람추가
	public int personInsert(PersonVo vo) {
		System.out.println("PhoneDao>personInsert");

		int count = -1;
		count = sqlSession.insert("phonebook.personInsert", vo);

		System.out.println(count + "건 등록되었습니다.");
		return count;

	}

	// 사람추가 : Map 을 사용하는 사강의 상황 : 문법 설명
	public int personInsert2(Map<String, String> pMap) {
		System.out.println("PhoneDao>personInsert2");

		int count = -1;
		count = sqlSession.insert("phonebook.personInsert2", pMap);

		System.out.println(count + "건 등록되었습니다.");
		return count;

	}

	
	// 사람 삭제
	public int personDelete(int no) {
		System.out.println("PhoneDao>personDelete");

		int count = -1;
		count = sqlSession.delete("phonebook.personDelete", no);
		System.out.println(count + "건 삭제되었습니다.");
		return count;
	}

	// 1명 정보 가져오기
	public PersonVo getPerson(int no) {
		System.out.println("PhoneDao>getPerson()");

		PersonVo personVo = sqlSession.selectOne("phonebook.getPerson", no);

		System.out.println(personVo);

		return personVo;
	}

	// 사람 수정
	public int personUpdate(PersonVo vo) {
		System.out.println("PhoneDao>personUpdate()");
		int count = sqlSession.update("phonebook.personUpdate", vo);

		return count;

	}

	// 사람 리스트(검색안할때)
	public List<PersonVo> getPersonList2() {
		return getPersonList2("");
	}

	// 사람 리스트(검색할때)
	public List<PersonVo> getPersonList2(String keword) {
		List<PersonVo> personList = new ArrayList<PersonVo>();

		getConnection();

		try {

			// 3. SQL문 준비 / 바인딩 / 실행 --> 완성된 sql문을 가져와서 작성할것
			String query = "";
			query += " select  person_id, ";
			query += "         name, ";
			query += "         hp, ";
			query += "         company ";
			query += " from person";

			pstmt = conn.prepareStatement(query); // 쿼리로 만들기

			rs = pstmt.executeQuery();

			// 4.결과처리
			while (rs.next()) {
				int personId = rs.getInt("person_id");
				String name = rs.getString("name");
				String hp = rs.getString("hp");
				String company = rs.getString("company");

				PersonVo personVo = new PersonVo(personId, name, hp, company);
				personList.add(personVo);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();

		return personList;

	}

	// 사람 추가
	public int personInsert2(PersonVo personVo) {
		int count = 0;
		getConnection();

		try {

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = ""; // 쿼리문 문자열만들기, ? 주의
			query += " INSERT INTO person ";
			query += " VALUES (seq_person_id.nextval, ?, ?, ?) ";
			// System.out.println(query);

			pstmt = conn.prepareStatement(query); // 쿼리로 만들기

			pstmt.setString(1, personVo.getName()); // ?(물음표) 중 1번째, 순서중요
			pstmt.setString(2, personVo.getHp()); // ?(물음표) 중 2번째, 순서중요
			pstmt.setString(3, personVo.getCompany()); // ?(물음표) 중 3번째, 순서중요

			count = pstmt.executeUpdate(); // 쿼리문 실행

			// 4.결과처리
			System.out.println("[" + count + "건 추가되었습니다.]");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
		return count;
	}

	// 사람 삭제
	public int personDelete2(int personId) {
		int count = 0;
		getConnection();

		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = ""; // 쿼리문 문자열만들기, ? 주의
			query += " delete from person ";
			query += " where person_id = ? ";
			pstmt = conn.prepareStatement(query); // 쿼리로 만들기

			pstmt.setInt(1, personId);// ?(물음표) 중 1번째, 순서중요

			count = pstmt.executeUpdate(); // 쿼리문 실행

			// 4.결과처리
			System.out.println(count + "건 삭제되었습니다.");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
		return count;
	}

	// 1명 정보 가져오기
	public PersonVo getPerson2(int personId) {
		PersonVo personVo = null;

		this.getConnection();

		try {

			// 3. SQL문 준비 / 바인딩 / 실행
			// SQL문 준비
			String query = "";
			query += " select  person_id, ";
			query += "         name, ";
			query += "         hp, ";
			query += "         company ";
			query += " from person ";
			query += " where person_id = ? ";

			// 바인딩
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, personId);

			// 실행
			rs = pstmt.executeQuery();

			// 4.결과처리
			while (rs.next()) {

				int id = rs.getInt("person_id");
				String name = rs.getString("name");
				String hp = rs.getString("hp");
				String company = rs.getString("company");

				personVo = new PersonVo(id, name, hp, company);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		this.close();

		return personVo;
	}

	// 사람 수정
	public int personUpdate2(PersonVo personVo) {
		int count = 0;
		getConnection();

		try {

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = ""; // 쿼리문 문자열만들기, ? 주의
			query += " update person ";
			query += " set name = ? , ";
			query += "     hp = ? , ";
			query += "     company = ? ";
			query += " where person_id = ? ";

			pstmt = conn.prepareStatement(query); // 쿼리로 만들기

			pstmt.setString(1, personVo.getName()); // ?(물음표) 중 1번째, 순서중요
			pstmt.setString(2, personVo.getHp()); // ?(물음표) 중 2번째, 순서중요
			pstmt.setString(3, personVo.getCompany()); // ?(물음표) 중 3번째, 순서중요
			pstmt.setInt(4, personVo.getPersonId()); // ?(물음표) 중 4번째, 순서중요

			count = pstmt.executeUpdate(); // 쿼리문 실행

			// 4.결과처리
			System.out.println(count + "건 수정되었습니다.");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
		return count;
	}

}