package src.main.test;

/**순서
   1.BCryptPasswordEncoder란?
   2.BCryptPasswordEncoder메서드 구성
   3.BCryptPasswordEncoder사용방법

   1.BCryptPasswordEncoder란?
   스프링 시큐리티(Spring Security) 프레임워크에서 제공하는 클래스 중 하나로 비밀번호를 
   암호화하는 데 사용할 수 있는 메서드를 가진 클래스

   스프링 기큐리티란 자바 서버 개바을 위해 필요로 한 인증, 권한 부여 및 기타 보안 기능을 제공하는 프레임워크(클래스와 인터페이스 모음)
   -BCryptPasswordEncoder는 BCrypt 해싱 함수 (BCrypt hashing function)를 사용해서 비밀번호를 인코딩해주는 메서드를 제공해준다.
   -PasswordEncoder 인터페이스를 구현한 클래스이다.
   -생성자의 인자 값(version, strength, SecureRandom instance)을 통해서 해시의 강도를 조절할 수 있따.

   BCryptPasswordEncoder는 위에서 언급했듯이 비밀번호를 암호화하는 데 사용할 수 있는 메서드를 제공한다.
   기본적으로 웹 개발함에 있어서 사용자의 비밀번호 데이터베이스에 저장하게 된다. 허가되지 않은 사용자가
   접근하지 못하도록 기본적인 보안이 되어 있을 것이다.

   메서드 구성
   BCryptPasswordEncoder는 스프링 시큐리티 5.4.2부터는 3개의 메서드를 가지고
   공통적으로 encode(), matchers(), 메서드에 upgradeEncoding() 메서드가 추가되었다.

   encode(java.lang.CharSequence rawPassword)
   -패스워드를 암호화해주는 메서드이다.
   -반환 타입은 String 타입이다.

   matchers(java.lang.CharSequence rawPassword, encodePassword)
   -제출된 인코딩 되지 않은 패스워드(일치 여부를 확인하고자 하는 패스워드)와 인코딩 된 패스워드의 일치 여부를 확인해준다.
   -첫 번째 매개변수는 일치 여부를 확인하고자 하는 인코딩 되지 않은 패스워드를 두 번째 매개변수는 인코딩 된 패스워드를 입력한다.
   -반환 타입을 boolean 이다.

    스프링 시큐리티 pom.xml

   <!-- security -->
        <!-- https://mvnrepository.com/artifact/org.springframework.security/spring-security-core -->
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-core</artifactId>
            <version>5.4.2</version>
        </dependency>
        
        <!-- https://mvnrepository.com/artifact/org.springframework.security/spring-security-web -->
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-web</artifactId>
            <version>5.4.2</version>
        </dependency>
        
        <!-- https://mvnrepository.com/artifact/org.springframework.security/spring-security-config -->
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-config</artifactId>
            <version>5.4.2</version>
        </dependency>    
 */

 @RequestMapping(value = "/secuTest", method = RequestMethod.GET)
 public void secuTest() {

  String rawPassword = "VAM123";              // 인코딩 전 메서드
  String encodePassword1;                     // 인코딩된 메서드
  String encodePassword2;                     // 똑같은 비밀번호 데이터를 encode() 메서드를 사용했을 때 동일한 인코딩된 값이 나오는지 확인하기 위해 추가

  encodePassword1 = passwordEncoder.encode(rawPassword);
  encodePassword2 = passwordEncoder.encode(rawPassword);

  // 인코딩된 패스워드 출력
  System.out.println("encodePassword : " + encodePassword1);
  System.out.println("  encodePassword : " + encodePassword2);

  String truePassword = "VAM123";
  String falsePassword = "asdfjlasf";

  System.out.println("truePassword verify : " + passwordEncoder.matchers(truePassword, encodePassword1));
  System.out.println("falsePassword verify : " + passwordEncoder.matchers(falsePassword, encodePassword2));
 
}

/**
 * 결과를 보면 같은 rawPassword 변수의 값을 encode() 메서드를 사용하여 인코딩하였음에도 서로 다은 값이 변환 된 것을 볼 수 있다.
   (encodePassword1 != encodePassword2)
 
 * truePassword 변수에는 일치하는 값을 대입하였고, falsePassword 변수에는 일치하지 않는 값을 대입하였다.
 * 해당 변수들을 사용해서 인코딩 된 비밀번호와의 일치 여부를 확인하기 위해서 matchers() 메서드를 활용하였고 그 결과로 일치하는
 * 값을 가진 truePassword 변수를 사용했을 경우에는 true를, 일치하지 않는 값을 가진 falsePassowrd 변수를 사용했을 경우는 false를 반환 
 */

// 로그인 
@RequestMapping(value="login", method=RequestMethod.POST)
public String loginPOST(HttpServletRequest request, MemberVO member, RedirectAttributes rttr) throws Exception{

  //System.out.println("login 메서드 진입");
  //System.out.println("전달된 데이터 : " + member);

  /**
   * 웹 클라이언트가 서버에게 요청을 보내면 서버는 클라이언트를 식별하는 session id를 생성한다.
     서버는 session id로 key와 value를 저장하는 HttpSession을 생성하고, session id를 저장하고 있는 쿠키를 생성항 클라이언트에 전송
     클라이언트는 서버 측에 요청을 보낼 때, session id를 가지고 있는 쿠키를 전송한다.
     서버는 쿠키의 session id로 HttpSession을 찾는다.

     session 얻기
     HttpSession session = request.getSession(); 
     request의 getSession() 메서드는 서버에 생성된 세션이 있다면, 세션을 반환하고, 없다면 새 세션을 생성하여 반환한다. (default가 true)

     HttpSession session = request.getSession(false);
     request의 getSession() 메서드의 파라미터로 false를 전달하면, 이미 생성된 세션이 있을 때, 그 세션을 반환하고, 없으면 null을 반환한다.

     setAttribute(String name, Object value)
     setAttribute는 name, value 쌍으로 객체 Object를 저장하는 메서드이다.
     session.setAttribute(이름, 값) // 이렇게 사용한다.

     session 제거 removeAttribute(String name), invalidate()

     세션은 기본적으로 30분 유지된다. 세션 유지 시간은 서버에 접속한 후 서버에 요청을 하지 않는 최대 시간을 말한다.
     30분 이상 서버에 전혀 반응을 보이지 않으면, 세션이 자동으로 끊어진다.
     이 세션 유지 시간은 web.xml 파일에서 설정할 수 있다.
   */

  HttpSession session = request.getSession();
  MemberVO lvo = memberservice.memberLogin(member);

  if(lvo == null) {                     // 일치하지 않는 아이디, 비밀번호 입력 경우

      int result = 0;
      rttr.addFlashAttribute("result", result);
      return "redirect:/member/login";

  }

  session.setAttribute("member", lvo);  // 잂치하는 아이디, 비밀번호 경우 (로그인 성공)

  return "redirectL:/main"; 
}

/**
 * memberLogin(member) 호출하여 반환받은 정보를 lvo 변수에 저장
    =>(lvo == null) 작성
    *true(lvo가 null인 경우) - 로그인 실패
      => 리플렉션에 실패를 의미하는 데이터 저장
      => 로그인 페이지 리다이렉트
    *false(lvo가 null이 아닌 경우) - 로그인 성공
      => 세션에 사용자의 정보 저장
      => 메인 페이지로 리다이렉트  

      여기서 핵싱은 memberLogin() 메서드이다. 해당 메서드를 통해 select 쿼리가 실행이 되는데,
      사용자로 부터 제출받은 아이디와 비밀번호가 일치하는 사용자를 찾게 되면 해당 사용자의 정보를 반환,
      일치하는 사용자를 찾지 못한다면 null이 반환되게 된다.
 */

/**새로 작성할 코드의 흐름
 * 기존의 코드 경우 memberLogin() 메서드를 호출하여 쿼리문을 실행하기 위해서 사용자가 제출한 아이디, 비밀번호 데이터를 넘겨주었다.
   하지만 현재 데이터베이스에 저장된 비밀번호는 인코딩 된 데이터이다. 따라서 아무리 사용자가 올바른 아이디와 비밀번호를 제출하더라도
   memberLogin() 메서드는 null을 반환할 것이다.

   데이터베이스 자체에서 인코딩된 비밀번호와 사용자가 제출한 비밀번호가 일치하는지를 판단해준다면 좋겠지만 이는 불가능
   그렇기 때문에 데이터베이스로부터 사용자의 비밀번호를 꺼내와서 BCryptPasswordEncoder 클래스의 matchers() 메서드를 활용하여 사용자에 의해 
   제출된 비밀번호와 일치 여부를 왁인해야한다. 일치 여부에 따라 로그인 성공 시 실행되어야 할 코드를 적용하던지,
   로그인 실패 시 실행 되어야 할 코드를 적용해야 할 것이다. 따라서 아래와 같은 흐름으로 코드가 진행되도록 설계하였다.

   회원의 정보를 반환하는 쿼리문 실행(사용자의 아이디를 조건으로 함)
   => 회원의 정보가 null인지판단
   *null인 경우 - 로그인 실패
    =>로그인 실패 실행 코드
   *null이 아닌 경우
    =>matchers() 문을 통해 데이터베이스 저장된 비밀번호와 제출된 비밀번호 일치 여부 확인
    
      true인 경우 - 비밀번호 일치
        로그인 성공 실행 코드
      false인 경우  - 비밀번호 불일치
        로그인 실패 실행 코드
 */
@RequestMapping(value="login", method=RequestMethod.POST)
public String loginPOST(HttpServletRequest request, MemberVO member, RedirectAttributes rttr) throws Exception{

  /* HttpSession 클래스 타입의 참조변수 session을 선언 및 초기화하고, String 타입의 rawPw, encodePw 변수를 선언 및 초기화
     rawPw, encodePw는 제출받은 비밀번호와 인코딩 된 비밀번호를 잠시 저장하기 위한 용도로 사용할 것이다.
  */
  HttpSession session = request.getSession();
  String rawPw = "";
  String encodePw = "";

  /* memberLogin() 메서드를 호출하여 위에서 수정한 쿼리가 실행이 되도록 한다.
      실행된 결과로 반환받은 MemberVO 인스턴스의 주소를 MemberVO 타입의 lvo 변수에 저장한다.
  */
  MemberVO lvo = memberserivce.memberLogin(member); 

  /* if의 조건문이 false인 경우는 lvo의 저장된 값이 null 이라는 의미이므로, else의 구현부는 로그인 실패 시 실행되어야 할 코드
  */
  if(lvo != null) {                  // 일치하는 아이디 존재 시

    rawPw = member.getMemberPw();    // 사용자가 제출한 비밀번호
    encodePw = lvo.getMemberPw();    // 데이터베이스에 저장한 인코딩된 비밀번호


  //////////////////////////////////////[일치하는 아이디 존재 시 1차 통과]//////////////////////////////////////////////


    // matchers(인코딩 전 비밀번호, 인코딩 후 비밀번호) 메서드가 true를 반환하는지 하지 않는지를 조건문으로하는 if문을 작성한다.
    if(true == pwEncoder.matchers(rawPw, encoderPw)); { // 비밀번호 일치여부 판단

      lvo.setMemberPw("");           // 인코딩된 비밀번호 정보 지움
      session.setAttribute("member", lvo); // session에 사용자의 정보 저장
      return "redirect:/main";       // 메인페이지 이동

    } else {

      rttr.addFlashAttribute("result", 0);
      return "redirect:/member/login";  // 로그인 페이지로 이동

    }
    
  ///////////////////////////////////////[1차 통과 후 인코딩 전, 후 비밀번호 일치 시 2차 통과]///////////////////////////
  

  } else {                           // 일치하는 아이디가 존재하지 않을 시 (로그인 실패)

    rttr.addFlashAttribute("result", 0);
    return "redirect:/member/login"; // 로그인 페이지로 이동

  }

  session.setAttribute("member", lvo); // 일치하는 아이디, 비밀번호 경우 (로그인 성공)

  return "redirect:/main";
}