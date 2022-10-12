package src.main.practice;

import java.util.Random;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vam.model.MemberVO;
import com.vam.service.MemberService;

@Controller
@RequestMapping(value="/member")
public class MemberController {

    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    private MemberService memberservice;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private BCryptPasswordEncoder pwEncoder;

    // 회원가입
    @RequestMapping(value = "/join", method = RequestMethod.POST)
    public String joinPOST(MemberVO member) throws Exception {

        // String 타입은 잠시 저장시킬 용
        String rawPw = "";      // 인코딩 전 비밀번호
        String encodePw = "";   // 인코딩 후 비밀번호

        // member 객체로부터 비밀번호 데이터를 얻어내서 해당 비밀번호 데이터를 인코딩한 뒤 다시 member 객체에 저장시키는 코드
        rawPw = member.getMemberPw();               // 비밀번호 데이터 얻음
        encodePw = pwEncoder.encode(rawPw);         // 비밀번호 인코딩
        member.setMemberPw(encodePw);               // 인코딩되니 비밀번호 member 객체에 다시 저장

        /* 회원가입 쿼리 실행 */
        memberservice.memberJoin(member);

        return "redirect:/main";                    // 메인 페이지로 이동
    }

    // 로그인 페이지 이동
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public void loginGET() {

        logger.info("로그인 페이지 진입");

    }

    // 아이디 중복 검사
    @RequestMapping(value = "/memberIdChk", method = RequestMethod.POST)
    @ResponseBody
    public void memberIdChk(String memberId) throws Exception{

        logger.info("memberIdChk() 진입");

        int result = memberservice.idCheck(memberId);   // idCheck가 만약 빨간색 에러 표시가 나오면 아직 memberservice에서 해당 메서드를 만들지 않아서이다.

        logger.info("결과값 = " + result);

        if(result != 0) {

            return "fail"; // 중복 아이디가 존재

        } else {

            return "success"; // 중복 아이디 존재 x

        }
    }

    // 이메일 인증
    @RequestMapping(value = "/mailCheck", method = RequestMethod.GET)
    @ResponseBody
    public String mailCheckGET(String email) throws Exception{

        // 뷰(view)로부터 넘어온 데이터 확인
        Logger.info("이메일 데이터 전송 확인");
        Logger.info("인증번호 : " + email);

        // 인증번호(난수) 생성
        Random random = new Random();

        // 111111 ~ 999999 범위의 숫자를 얻기 위해서 nextInt(888888) + 111111를 사용
        int checkNum = random.nextInt(888888) + 111111;

        // 정상 작동 확인 위해 logger 코드 추가
        logger.info("인증번호" + checkNum);

        // 이메일 보내기
        String setForm = "hddong728@naver.com";         // root-context.xml에 삽입한 자신의 이메일 계정의 이메일 주소.
        String toMail = email; 							// 수신받을 이메일입니다. 뷰로부터 받은 이메일 주소인 변수 email을 사용.
		String title = "회원가입 인증 이메일 입니다.";    // 자신이 보낼 이메일 제목을 작성
		String content = 								// 자신이 보낼 이메일 내용
					"홈페이지를 방문해주셔서 감사합니다.";
					"<br><br>" + 						// <br>은 줄 바꿈을 위해 삽입한 태그
					"인증 번호는 " + checkNum + "입니다." + 
					"<br>" + 
					"해당 인증번호를 인증번호 확인란에 기입하여 주세요";
        
        try {

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setForm(setForm);
            helper.setTo(toMail);
            helper.setSubject(title);
            helper.setText(content,true);
            mailSender.send(message);

        }catch(Exception e) {
            e.pringStackTrace();
        }

        /** 우리가 생성한 인증번호 변수를 뷰로 반환하기만 하면 된다. 하지만 생성한 방식의 경우 int 타입이다.
            ajax를 통한 요청으로 인해 뷰로 다시 반환할 때 데이터 타입은 String 타입만 가능하다.
        */
        String num = Integer.toString(checkNum);

        return num;
            
    }

    // 로그인
    @RequestMapping(value = "login", method=RequestMethod.POST)
    public String loginPOST(HttpServletRequest request, MemberVO member, RedirectAttribute rttr) throws Exception{

        //System.out.println("login 메서드 진입");
        //System.out.println("전달된 데이터 : " + member);

        HttpSession session = request.getSession(); // session을 사용하기 위해 session 변수를 선언하고
        String rawPw = "";                                      // 인코딩 전 비밀번호
        String encodePw = "";                                   // 인코딩 후 비밀번호
        
        MemberVO lvo = memberservice.memberLogin(member);       // 제출한 아이디와 일치하는 아이디 있는지 확인

        if(lvo != null) {                                       // 일치하는 아이디 존재 시

            rawPw = member.getMemberPw();                       // 사용자가 제출한 비밀번호
            encodePw = lvo.getMemberPw();                       // 데이터베이스에 저장한 인코딩된 비밀번호

            if(true == pwEncoder.matchers(rawPw, encodePw)) {   // 비밀번호 일치여부 판단

                lvo.setMemberPw("");                            // 인코딩된 비밀번호 정보 지움
                session.setAttribute("member", lvo);            // session에 사용자의 정보 저장
                return "redirect:/main";                        // 메인 페이지로 이동

            } else {                                            // rawPw 와 encodePw 가 일치하지 않는 경우 (로그인 실패1)

                rttr.addFlashAttribute("result", 0);
                return "redirect:/member/login";                // 로그인 페이지로 이동
            }

        }   else {                                              // 일치하는 아이디가 존재하지 않을 시 (로그인 실패2)

            rttr.addFlashAttribute("result", 0);
            return "redirect:/member/login";                    // 로그인 페이지로 이동

        }

        session.setAttribute("member", lvo);                    // 일치하는 아이디, 비밀번호일 경우 (로그인 성공)

        return "redirect:/main";
    }

    // 메인페이지 로그아웃
    @RequestMapping(value="logout.do", method=RequestMethod.GET)
    public  String logoutMainGET(HttpServletRequest request) throws Exception {

        logger.info("logoutMainGET 메서드 진입");

        HttpSession session = request.getSession();

        session.invalidate();

        return "redirect:/main";

    }

    // 비동기식 로그아웃 메서드
    @RequestMapping(value-"logout.do", method=RequestMethod.POST)
    @ResponseBody // ajax를 통해서 서버에 요청을 하는 방식이기 때문에 선언
    public  void logoutPOST(HttpServletRequest request) throws Exception{

        logger.info("비동기식 로그아웃 메서드 진입");

        HttpSession session = request.getSession();

        session.invalidate();
        
    }
}