package com.vam.mapper;

import com.vam.model.MemberVO;

// mapper는 DAO와 같은 기능
public interface MemberMapper {
    
    // 회원가입
    public void memberJoin(MemberVO member); // 회원등록 insert쿼리를 실행시켜 줄 메서드를 작성한다.

    // 아이디 중복 검사
    public int idCheck(String memberId);

    // 로그인
    public MemberVO memberLogin(MemberVO member);
}





