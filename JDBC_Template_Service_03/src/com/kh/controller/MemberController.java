package com.kh.controller;

import java.util.ArrayList;

import com.kh.model.dap.MemberDao;
import com.kh.model.service.MemberService;
import com.kh.model.vo.Member;
import com.kh.view.Membermenu;

// Controller : View를 통해서 사용자가 요청한 기능에 대해서 처리하는 담당
//				해당 메소드로 전달된 데이터 [가공처리부] dao로 전달하여 호출
//				dao로부터 반환받은 결과에 따라서 성공인지 실패인지 판단 후 응답하면 결정(View 메소드 호출)
public class MemberController {
	
	/**
	 * 사용자의 추가요청을 처리해주는 메소드
	 * @param userId ~ hobby : 사용자가 입력했던 정보들이 담겨있는 매개변수
	 */
	
	public void insertMember(String userId, String userPwd, String userName, String gender, String age,
			String email, String phone, String address, String hobby) {
		
		Member m = new Member(userId, userPwd, userName, gender,
							Integer.parseInt(age), email, phone, address, hobby );
		int result = new MemberService().insertMember(m);
		
		if (result > 0) {
			new Membermenu().displaySuccess("성공적으로 회원이 추가되었습니다");
		} else {
			new Membermenu().displayFail("회원 추가에 실패하였습니다.");
		}
	}
	
	/**
	 * 사용자의 회원 전체조회 요청을 처리해주는 메소드
	 */
	public void selectList() {
		ArrayList<Member> list = new MemberService().selectList();
		
		if(list.isEmpty()) {
			new Membermenu().displayNoData("전체 조회 결과가 없습니다.");
		} else {
			new Membermenu().displayMemberList(list);
		}
	}
	
	/**
	 * 사용자의 아이디로 회원 검색 요청을 처리해주는 메소드
	 * @param userId : 사용자가 입력한 검색하고자 하는 회원 아이디
	 */
	
	public void selectByUserId(String userId) {
		Member m = new MemberService().selectByUserId(userId);
		
		if(m == null) {
			new Membermenu().displayNoData(userId + "가 조회되지 않습니다"); 
		} else {
			new Membermenu().displayMember(m);
		}
		
	}
		
	public void selectByUserName(String keyword) {
		ArrayList<Member> list = new MemberService().selectByUserName(keyword);
		
		if(list.isEmpty()) {
			new Membermenu().displayNoData(keyword + "에 해당하는 검색 결과가 없습니다.");
		} else {
			new Membermenu().displayMemberList(list);
		}
	}
	
	public void updateMember(String userId, String userPwd, 
							String email, String phone, String address) {
		Member m = new Member();
		m.setUserId(userId);
		m.setUserPwd(userPwd);
		m.setEmail(email);
		m.setPhone(phone);
		m.setAddress(address);
		
	    int result = new MemberService().updateMember(m);
	    
	    if(result > 0) {
	    	new Membermenu().displaySuccess("성공적으로 수정 하였습니다.");
	    } else {
	    	new Membermenu().displayFail("회원 정보 변경에 실패하였습니다.");
	    }
		
	}
	
	public void deleteMember(String userId) {
		int result = new MemberService().deleteMember(userId);

		if (result > 0) {
			new Membermenu().displaySuccess(userId + "회원을 삭제처리 하였습니다");
		} else {
			new Membermenu().displayFail(userId + "회원 삭제 실패 하였습니다.");
		}
	}

}
