package com.vam.controller;

import java.util.Random;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

/*  MimeMessage
mailSender.send(새로운 MimeMessagePreparator() {
    public void prepare(MimeMessage mimeMessage)에서 MessagingException이 발생합니다. {
      MimeMessageHelper 메시지 = 새로운 MimeMessageHelper(mimeMessage, true, "UTF-8");
      message.setFrom(" me@mail.com ");
      message.setTo(" you@mail.com ");
      message.setSubject("내 제목");
      message.setText("내 텍스트 <img src='cid:myLogo'>", true);
      message.addInline("myLogo", new ClassPathResource("img/mylogo.gif"));
      message.addAttachment("myDocument.pdf", new ClassPathResource("doc/myDocument.pdf"));
    }
  });
*/

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vam.model.MemberVO;
import com.vam.service.MemberService;

@Controller
@RequestMapping(value = "/member")
public class MemberController {

	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	/**회원가입 메서드를 추가하기 앞서 @Autowired를 통해서 MemberService.java가 
	   MemberController.java에 자동 주입되도록 코드를 추가해준다.
	   한마디로, @Autowired 어노테이션으로 인해 스프링이 컨테이너에 있는 MemberService를 가져다가 연결 시켜줌 (연관된 객체를 찾아 넣어줌)
	   객체 의존관계를 외부에서 넣어주는 것을 DI(Dependancy Injection), 의존성 주입

	   private final MemberService memberService = new MemberService();
	   위처럼 객체를 new 해서 사용하지 않고, 하나만 생성해서 같이 쓰도록 하기 위해 
	   스프링 컨테이너로부터 MemberService를 가져다 쓰도록 코드 변경 (스프링 컨테이너에게 등록을 하고 사용)
	   */   
	@Autowired 
	private MemberService memberservice;

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
    private BCryptPasswordEncoder pwEncoder;

	// 회원가입 페이지 이동
	@RequestMapping(value = "join", method = RequestMethod.GET)
	public void joinGET() {
		
		logger.info("회원가입 페이지 진입");
				
	}
	
	// 회원가입
	@RequestMapping(value = "/join", method = RequestMethod.POST)
	public String joinPOST(MemberVO member) throws Exception{ 
		// MemberVO(타입) : 데이터를 전달받기 위해 MemberVO를 타입을 사용, 사용자의 정보를 "member"라고 네이밍, 밑에 MemberVO lvo와 같은 맥락, lvo는 BCrypt된 변수

		/** ------------------회원가입 로직-----------------------
	 
		logger.info("join 진입");

		// 회원가입 서비스 실행
		memberservice.memberJoin(member); // 전달받은 데이터를 회원DB에 등록시키는 처리를 하는 Service단계의 memberJoin() 메서드 호출

		logger.info("join Service 성공");

			-----------------------------------------------------*/

		// String 타입은 잠시 저장시킬 용
		String rawPw = "";    // 인코딩 전 비밀번호
		String encodePw = ""; // 인코딩 후 비밀번호

		// member 객체로부터 비밀번호 데이터를 얻어내서 해당 비밀번호 데이터를 인코딩한 뒤 다시 member 객체에 저장시키는 코드
		rawPw = member.getMemberPw(); 			// 비밀번호 데이터 얻음
		encodePw = pwEncoder.encode(rawPw);		// 비밀번호 인코딩
		member.setMemberPw(encodePw);			// 인코딩되니 비밀번호 member 객체에 다시 저장

		// 회원 정보가 담긴 member 인스턴스를 인자 값으로 하는 memberJoin() 메서드를 호출하여 회원가입 쿼리가 실행되도록 코드를 작성
		/* 회원가입 쿼리 실행 */
		memberservice.memberJoin(member);
		
		// 반환형식을 String으로 하여 return에 main 페이지로 이동하도록 작성. public (String) joinPOST()
		// 회원가입 완료 시 main 페이지로 이동 = "redirect:/main"
		return "redirect:/main";
	}

	// 로그인 페이지 이동
	@RequestMapping(value = "login", method = RequestMethod.GET)
	public void loginGET() {
		
		logger.info("로그인 페이지 진입");
		
	}

	// 아이디 중복 검사
	/** MemberController.java에 아이디 중복검사 실행하는 메서드(memberIdChkPOST)를 작성
	 *  해당 메서드의 반환 타입은 void로 해준다.(테스트 진행 후 String 타입으로 변경)
	 *  여기서 주의해야 할 점은 @ResponseBody 어노테이션이다. 해당 코드를 추가해주지 않으면, join.jsp로 메서드의 결과가 반환되지 않는다.
	 *  join.jsp에서 작성한 ajax의 요청을 memberIdChkPOST 메서드가 수신받는지 확인하기 위해 logger.info()를 추가
	 */

	@RequestMapping(value = "/memberIdChk", method = RequestMethod.POST)
	@ResponseBody
	public void memberIdChk(String memberId) throws Exceptioin{

		logger.info("memberIdChk() 진입");
		
		/**
		 * 테스트를 정상적으로 통과하였다면 테스트를 위해 작성하였던 logger.info()를 지우거나 주석 처리한다.
		 * 메서드의 반환 타입을 void에서 String으로 변경해준다.
		 * memberIdChkPOST 메서드에 아래의 코드를 추가해준다.
		 * memberservice.idCheck(memberId)의 결과를 int형 변수 result에 저장한다.
		 	(존재한다면 '1', 존재하지 않는다면 '0'을 반환)
		 * result의 결과가 0이 아니면 "fail"을 반환하고, result의 결과가 1이 아니면 "success"를 반환한다.	 
		 */

		 int result = memberservice.idCheck(memberId); // idCheck가 만약 빨간색 에러 표시가 나오면 아직 memberservice에서 해당 메서드를 만들지 않아서이다.

		 logger.info("결과값 = " + result);

		 if(result != 0) {

			return "fail"; // 중복 아이디가 존재

		 } else {

			return "success"; // 중복 아이디 x
		 }

	}// memberIdChkPOST() 종료 
	
	// 이메일 인증
	@RequestMapping(value = "/mailChek", method = RequesetMethod.GET)
	@ResponseBody
	
	/**  반환타입을 임시로 void로 설정, 추후에 String 변경
		ajax를 통한 요청으로 인해 뷰로 다시 반환할 때 데이터 타입은 String 타입만 가능 */
	public String mailCheckGET(String email) throws Exception{ 

		// 뷰(View)로부터 넘어온 데이터 확인
		Logger.info("이메일 데이터 전송 확인");
		Looger.info("인증번호 : " + email);
		
		// 인증번호(난수) 생성
		Random random = new Random();

		// 111111 ~ 999999 범위의 숫자를 얻기 위해서 nextInt(888888) + 111111를 사용
		int checkNum = random.nextInt(888888) + 111111; 

		// 정상 작동 확인 위해 logger 코드 추가
		logger.info("인증번호" + checkNum);

		// 이메일 보내기
		String setForm = "hddong728@naver.com"; 		// root-context.xml에 삽입한 자신의 이메일 계정의 이메일 주소.
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

	/**
	 * MemberController.java에 로그인 기능을 수행하는 메서드를 작성한다. 파라미터로는 
	    MemberVO, HttpServletRequest, RedirectAttribute를 사용한다.
		-MemberVO : 데이터를 전달받기 위해
		-HttpServletRequest : 로그인 성공 시 session에 회원 정보를 저장하기 위해
		-RedirectAttribute : 로그인 실패 시 리다이렉트 된 로그인 페이지에 실패를 의미하는 데이터를 전송하기 위해
	 * @param request
	 * @param member
	 * @param rttr
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "login", method=RequestMethod.POST)
	public String loginPOST(HttpServletRequest request, MemberVO member, RedirectAttribute rttr) throws Exception{

		HttpSession session = request.getSession(); // session을 사용하기 위해 session 변수를 선언하고 request.getSession()으로 초기화한다.

		// rawPw, encodePw는 제출받은 비밀번호와 인코딩 된 비밀번호를 잠시 저장하기 위한 용도로 사용할 것입니다.
        String rawPw = "";
        String encodePw = "";
    
		// memberLogin() 메서드를 호출하여 위에서 수정한 쿼리가 실행이 되도록 합니다. 실행된 결과로 반환받은 MemverVO(타입)인스턴스의 주소를  MemverVO타입의 lvo 변수에 저장합니다. 
        MemberVO lvo = memberservice.memberLogin(member);        // 제출한아이디와 일치하는 아이디있는지 
        
        if(lvo != null) {            							 // 일치하는 아이디 존재시

			/**
			 * if(lvo != null)의 구현부는 비밀번호가 일치하는지를 확인해주는 코드를 작성해주면 된다.
			   먼저 rawPW 와 encodePw 변수에 각각 사용자가 제출한 비밀번호와 데이터베이스에 저장한 
			   인코딩 된 비밀번호를 저장해준다.
			 */

			rawPw = member.getMemberPw(); 						 // 사용자가 제출한 비밀번호
            encodePw = lvo.getMemberPw(); 						 // 데이터베이스에 저장한 인코딩된 비밀번호

			// matches(인코딩 전 비번, 인코딩 후 비번) 메서드가 true를 반환하는지 하지 않는지를 조건문으로 if문 작성
			if(true == pwEncoder.matches(rawPw, encodePw)) {	 // 비밀번호 일치여부 판단

				/**
				 * 조건문이 true인 경우 실행되는 if 구현부에는 로그인 성공 시 실행되어야 할 코드를 작성
				   session에 뷰에 보낼 사용자의 정보가 담긴 lvo를 저장하는 코드를 작성하기 전에 lvo에 저장된 사용자의
				   비밀번호 정보는 지워준다. 인코딩 되었더라도 굳이 노출시킬 필요는 없기 때문

				   setAttribute(name, value)
				   name : 값을 설정할 속성의 이름을 지정하는 문자열이다. setAttribute() 속성 이름은 HTML 문서의 HTML 요소에서
				   			호출 될 떄 자동으로 모두 소문자로 변환됩니다.

				   value : 속성에 할당할 값이 포함된 문자열이다. 지정된 비문자열 값은 자동으로 문자열로 변환된다.		   
				 */

				lvo.setMemberPw("");							 // 인코딩된 비밀번호 정보 지움
				session.setAttribute("member", lvo); 			 // session에 사용자의 정보 저장
				return "redirect:/main"; 						 // 메인페이지 이동

		} else {												 // 아이디가 가입이 안되어있는 경우 (로그인 실패)

				// 조건문이 false 경우에 실행되는 else 구현부에는 로그인 실패 시 실행되어야 할 코드
				rttr.addFlashAttribute("result", 0);
				return "redirect:/member/login";				 // 로그인 페이지로 이동

			}	

        } else {                    							 // 일치하는 아이디가 존재하지 않을 시 (로그인 실패)

			rttr.addFlashAttribute("result", 0);
			return "redirect:/member/login"; 					 // 로그인 페이지로 이동
            
        }


		// Systen.out.println("login 메서드 진입");
		// System.out.println("전달된 데이터 : " + member);

		// return null;

		/**
		 * 1.두 가지의 변수를 선언 및 초기화를 진행할 것이다. 먼저 session을 사용하기 위해 session 변수를 선언하고
		  		request.getSession()으로 초기화한다.
		 * 2.두 번째 변수로 MemberVO 타입의 변수 lvo를 선언하고 이전 포스팅에서 만든 
		 		MemberService.java의 memberLogin메서드로 초기화 한다.
				-인자는 서버로부터 전달받은 member 변수를 사용한다.
				-memberLogin 메서드를 요청하게 되면 MemberMapper.java를 거쳐서 로그인 쿼리가 실행이 되게 되고
					그 결과 값이 담긴 MemberVO객체를 반환 받아서 변수 lvo에 저장되게 된다.
					MemberService.java memberLogin() -> MemberMapper.java(memberLogin()메서드 DB 접근) -> MemberMapper.xml(memberLogin()메서드 db쿼리 실행)
		 
		HttpSession session = request.getSession();
		MemberVO lvo = memberservice.memberLogin(member);

		/**
		 * lvo 값은 아이디, 비밀번호가 존재할 경우  memberId, memberName, adminCk, money, point 데이터가 담긴 MemberVO
		   객체가 저장되게 된다. 아이디, 비밀번호가 존재하지 않을 경우 lvo값은 null이 저장된다. 따라서 이러한 상황을 이용해서 
		   if문을 통해 lvo 값이 null일 경우 로그인 실패이기 때문에 로그인 페이지로 리다이렉트 되도록, null이 아닌 경우
		   로그인 성공이기 때문에 메인 페이지로 리다이렉트 되도록 return 값을 설정해준다.
		   
		   -return문으로 가기 전 실패일 경우 RedirectAttributes에 실패를 의미하는 데이터를, 성공일 경우 session에 변수 lvo에 
		   담긴 데이터를 저장하는 코드를 추가해준다. 해당 데이터들은 페이지에 전달되어 각 상황에 맞게 활용될 것이다.
		   
		   -RedirectAttributes에 담긴 데이터는 자신이 임의로 아무 데이터나 작성해도 된다. 0은 거짓, 1은 참이라고 생각하여 0을 저장
		 
		if(lvo != null) {								// 일치하지 않는 아이디, 비밀번호 입력 경우

			int result = 0;
			rttr.addFlashAttribute("result", result);
			return "redirect:/member/login";

		}

		session.setAttribute("member", lvo); 			// 일치하는 아이디, 비밀번호 경우 (로그인 성공)

		return "redirect:/main";
		*/
	}
}

// 메인페이지 로그아웃
@RequestMapping(value="logout.do", method=RequestMethod.GET)
public String logoutMainGET(HttpServletRequest request) throws Exception {
	
	logger.info("logoutMainGET메서드 진입");

	// 세션을 제거하는 작업을 해야 하기 때문에 HttpSession타입의 session 변수 및 초기화를 한다.
	HttpSession session = request.getSession();

		/** HttpSession
		 * 둘 이상의 페이지 요청 또는 웹 사이트 방문에서 사용자를 식별하고 해당 사용자에 대한 정보를 저장하는 방법을 제공
		   서블릿 컨테이너는 이 인터페이스를 사용하여 HTTP 클라이언트와 HTTP 서버 간의 세션을 생성한다.
		   세션은 사용자의 둘 이상의 연결 또는 페이지 요청에 대해 지정된 기간 동안 지속된다. 세션은 일반적으로 사이트를 여러 번
		   방문할 수 있는 한 사용자에 해당한다. 서버는 쿠키를 사용하거나 URL을 다시 작성하는 등 다양한 방법으로 세션을 유지할 수 있다.
		 * 
		 */

	/**
	 * 우리는 사용자의 정보를 "member"라고 네이밍 한 session을 삭제해야 한다. HttpSession api문서를 보면 우리 목적에 맞는 session을 제거할 수 있는
	   메서드가 2개("invalidate()", "removeAttribute()")가 있다. "invalidate()" 메서드의 경우 세션 전체를 무효화하는 메서드이다.
	   "removeAttribute()"의 경우 특정 이름으로 네이밍 한 session 객체를 타겟팅하여 삭제하는 메서드이다.
	   로그아웃을 할 경우 사용될 세션이 없기 때문에 저는 "invalidate()" 메서드를 사용하였다. 아래와 같이 session을 제거하는 코드와
	   메서드 실행 후 main 페이지로 이동할 수 있도록 return문을 작성하였다.
	 */

	session.invalidate();
	
	return "redirect:/main";

}

/**서버 메서드 작성
 * MemberController.java에 뷰(View)에서 요청하는 url의 메서드를 작성한다. 주의할 점은 ajax를 통해서 서버에 요청을 하는 방식이기 때문에
   헤당 메서드에 반드시 @ResponseBody 어노테이션을 붙여주어야 한다. 뷰(View)에 특정 데이터를 전달해야 할 경우 변환 방식을 String으로 하면
   되지만 메서드의 작업만 수행하면 되기 때문에 메서드의 반환 방식은 void로 하였다. 메서드 구현부는 저번 포스팅에서의 로그아웃 메서드 구현부와
   같이 단순히 세션의 정보를 제거해주는 코드를 작성  

   로그아웃 메서드가 수행이 완료되면 다른 화면으로 이동 없이 요청한 페이지에서 새로고침 되어 로그아웃 되었을 때의 화면 구성으로 변환 되도록 작성할 것입니다.
 */
    /* 비동기방식 로그아웃 메서드 */
 @RequestMapping(value="logout.do", method=RequestMethod.POST)
 @ResponseBody // ajax를 통해서 서버에 요청을 하는 방식이기 때문에 선언
 public void logoutPOST(HttpServletRequest request) throws Exception{

	logger.info("비동기 로그아웃 메서드 진입");

	HttpSession session = request.getSessoin();

	session.invalidate();
 }

 /** 
  * 로그아웃을 POST방식으로 요청하는 ajax 코드를 아래와 같이 작성한다. 로그아웃을 요청하는 url 메서드는 다음 순서에서 작성한다.
  	[success] 옵션에서 해당 요청이 정상적으로 수행되었는지 확인하기 위해 alert 창이 뜨도록 작성하였고, 현재의 페이지가 새로고침 되도록
	작성하였다. (새로고침을 해주어야 세션의 변경사항이 화면에 반영이 되기 떄문이다.)
	<script>
	
		// gnb_area 로그아웃 버튼 작동 
		$("#gnb_logout_button").click(function(){
			//alert("버튼 작동");
			$.ajax({
				type:"POST",
				url:"/member/logout.do",
				success:function(data){
					alert("로그아웃 성공");
					document.location.reload();     
				} 
			}); // ajax 
		});
		
	</script>
*/