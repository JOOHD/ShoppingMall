package src.main.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vam.model.MemberVO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file.src/main/webapp/WEB-INF/spring/root-context.xml")
public class MemberMapperTests {

    @Autowired
    private MemberMapper membermapper;
    
    /*
    // 회원가입 쿼리 테스트 메서드
    @Test
    public void memberJoin() throws Exception{
        MemberVO memeber = new MemberVO();
        
		member.setMemberId("test");			//회원 id
		member.setMemberPw("test");			//회원 비밀번호
		member.setMemberName("test");		//회원 이름
		member.setMemberMail("test");		//회원 메일
		member.setMemberAddr1("test");		//회원 우편번호
		member.setMemberAddr2("test");		//회원 주소
		member.setMemberAddr3("test");		//회원 상세주소

        membermapper.memberJoin(member); // 쿼리 메서드 실행
    }
    */
    
    /*
    // 아이디 중복 검사
    @Test
    public void memberIdChk() throws Exception{
        String id = "admin"; // 존재하는 아이디
        String id2 = "test123"; // 존재하지 않는 아이디
        membermapper.idCheck(id);
        membermapper.idCheck(id2);
    }
    */

    // 로그인 쿼리 mapper 메서드 테스트
    @Test
    public void memberLogin() throws Exception{

        MemberVO member = new MemberVO(); // MemberVO 변수 선언 및 초기화

        // 올바른 아이디 비번 입력경우
        member.setMemberId("test1");
        member.setMemberPw("test1");

        // 올바르지 않은 아이디 비번 입력경우
        member.setMemberId("test1123");
        member.setMemberPw("test123123");

        membermapper.memberLogin(member);
        System.out.println("결과 값 : " + membermapper.memberLogin(member));
    }
}

// 데이터베이스 SELECT * FROM book_member; 명령문을 통해 테스트가 정상적으로 진행되었는지 확인한다.