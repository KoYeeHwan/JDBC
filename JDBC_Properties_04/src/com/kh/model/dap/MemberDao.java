package com.kh.model.dap;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import com.kh.commom.JDBCTemplate;
import com.kh.model.vo.Member;

public class MemberDao {
	
	/*
	 * 기존방식 : DAO 클래스에 사용자가 요청할때마다 실행해야하는 sql문을 자바소스코드내에 명시적으로 작성 => 정적코딩 방식
	 *
	 *  > 문제점 : sql문을 수정해야될 경우 자바소스코드를 수정해야됨 => 수정된 내용을 반영시키고자 한다면 프로그램을 종류 후 재구동 해야됨
	 *  
	 *  > 해결방식 : sql문들을 별도로 관리하는 외부 파일(.xml)로 만들어서 실시간으로 그 파일에 기록한 sql문을 읽어들여서 실행 => 동적코딩방식  
	 * 
	 * */

	private Properties prop = new Properties();
	
	public MemberDao() {
		try {
			prop.loadFromXML(new FileInputStream("resources/query.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 사용자가 입력한 정보들을 db에 추가시켜주는 메소드
	 * 
	 * @param m : 사용자가 입력한 값들이 담겨있는 member객체
	 * @return : insert문 실행 후 처리된 함수
	 */
	public int insertMember(Connection conn, Member m) {
		// insert => 처리된 행 수

		int result = 0;

		PreparedStatement pstmt = null;
		String sql = prop.getProperty("insertMember");

		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, m.getUserId());
			pstmt.setString(2, m.getUserPwd());
			pstmt.setString(3, m.getUserName());
			pstmt.setString(4, m.getGender());
			pstmt.setInt(5, m.getAge());
			pstmt.setString(6, m.getEmail());
			pstmt.setString(7, m.getPhone());
			pstmt.setString(8, m.getAddress());
			pstmt.setString(9, m.getHobby());

			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}

		return result;
	}

	public ArrayList<Member> selectList(Connection conn) {
		// select문(여러 행 조회) => ResultSet객체 => ArrayList에 담아 넘기기

		ArrayList<Member> list = new ArrayList<>(); // 비어있는 상태

		PreparedStatement pstmt = null;
		ResultSet rset = null;

		String sql = prop.getProperty("selectList");
		
		try {
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();

			while (rset.next()) {
						
				Member m = new Member();
				m.setUserNo(rset.getInt("userno"));
				m.setUserId(rset.getString("userid"));
				m.setUserPwd(rset.getString("userpwd"));
				m.setUserName(rset.getString("username"));
				m.setGender(rset.getString("gender"));
				m.setAge(rset.getInt("age"));
				m.setEmail(rset.getString("email"));
				m.setPhone(rset.getString("phone"));
				m.setAddress(rset.getString("address"));
				m.setHobby(rset.getString("hobby"));
				m.setEnrollDate(rset.getDate("enrolldate"));
				
				list.add(m);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(conn);
		}
		
		return list;
	}

	public Member selectByUserId(Connection conn, String userId) {
		// select문(한행) => ResultSet 객체 => Member 객체

		Member m = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;

		String sql = prop.getProperty("selectByUserId");
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			rset = pstmt.executeQuery();

			
			if(rset.next()) {
				m = new Member(rset.getInt("userno"),
				rset.getString("userid"),
				rset.getString("userpwd"),
				rset.getString("username"),
				rset.getString("gender"),
				rset.getInt("age"),
				rset.getString("email"),
				rset.getString("phone"),
				rset.getString("address"),
				rset.getString("hobby"),
				rset.getDate("enrolldate"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return m;
	}

	public ArrayList<Member> selectByUserName(Connection conn, String keyword) {
		// select문 (여러행) => ResultSet 객체 => ArrayList 객체

		ArrayList<Member> list = new ArrayList<>();

		PreparedStatement pstmt = null;
		ResultSet rset = null;

		String sql = prop.getProperty("selectByUserName");
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, keyword);
			rset = pstmt.executeQuery();

			while (rset.next()) {

				Member m = new Member();
				m.setUserNo(rset.getInt("userno"));
				m.setUserId(rset.getString("userid"));
				m.setUserPwd(rset.getString("userpwd"));
				m.setUserName(rset.getString("username"));
				m.setGender(rset.getString("gender"));
				m.setAge(rset.getInt("age"));
				m.setEmail(rset.getString("email"));
				m.setPhone(rset.getString("phone"));
				m.setAddress(rset.getString("address"));
				m.setHobby(rset.getString("hobby"));
				m.setEnrollDate(rset.getDate("enrolldate"));

				list.add(m);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}

		return list;
	}

	public int updateMember(Connection conn, Member m) {
		int result = 0;

		PreparedStatement pstmt = null;
		String sql = prop.getProperty("updateMember");
		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, m.getUserPwd());
			pstmt.setString(2, m.getEmail());
			pstmt.setString(3, m.getPhone());
			pstmt.setString(4, m.getAddress());
			pstmt.setString(5, m.getUserId());

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}
		
		return result;
	}

	public int deleteMember(Connection conn, String userId) {
		// delete문 (처리된 행 수) =>
		int result = 0;
		
		PreparedStatement pstmt = null;
		
		String sql = prop.getProperty("deleteMember");
	try {
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, userId);
	
		result = pstmt.executeUpdate();
	} catch (SQLException e) {
		e.printStackTrace();
	} finally {
		JDBCTemplate.close(conn);
	}
		return result;
	}
}
