package com.vam.service;

import com.vam.model.MemberVO;

/**
 * MemberService.java 인터페이스와 MemberServiceImpl.java 클래스의 코드는 아래와 같이 추가해준다.
 * MemberServiceImpl.java 클래스는 MemberService.java 인터페이스로 상속받도록 설정해준다.
 */

public interface MemberService {
    
    // 회원가입
    public void memberJoin(MemberVO member) throws Exception;

    // 아이디 중복 검사
    public int idCheck(String memberId) throws Exception;

    // 로그인
    // (MemberVO를 파라미터, 반환 값으로 사용한다.)
    public MemberVO memberLogin(MemberVO member) throws Exception;

    // 주문자 정보
    public MemberVO getMemberInfo(String memberId);
}
