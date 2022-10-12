package com.vam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vam.mapper.MemberMapper;
import com.vam.model.MemberVO;

@Service
public class MemberServiceImpl implements MemberService {
    
    @Autowired
    MemberMapper membermapper;

    // 회원가입
    // public void memberJoin(MemberVO memeber) throws Exception;
    @Override // service의 memberJoin 메서드를 override
    public void memberJoin(MemberVO member) throws Exception { // 회원가입 쿼리를 실행하는 Mapper 단계의 memberJoin() 메서드를 실행

        membermapper.memberjoin(member);
    }

    // 아이디 중복 검사
    // public int idCheck(String memberId);
    @Override
    public int idCheck(String memberId) throws Exception {

        return membermapper.idCheck(memberId);
    }

    // 로그인
    // public MemberVO memberLogin(MemberVO member) throws Exception;
    @Override
    public MemberVO memberLogin(MemberVO member)  throws Exception {

        return membermapper.memberLogin(member);
    }

    // 주문자 정보
    // public MemberVO getMemberInfo(String memberId);
    public MemberVO getMemberInfo(String memberId) {
        
        return membermapper.getMemberInfo(memberId);
    }
}



