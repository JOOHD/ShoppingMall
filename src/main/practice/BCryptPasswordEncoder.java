package src.main.practice;

@RequestMapping(value = "/secuTest", method = RequestMethod.GET)
public void secuTest() {

    String rawPassword = "VAM123;"                      // 인코딩 전 메서드
    String encodePassword1;                             // 인코딩도니 메서드
    String encodePassword2;                             // 똑같은 비밀번호 데이터를 encode() 메서드를 사용했을 때 동일한 값이 나오는지 확인하기 위해 추가

    encodePassword1 = passwordEncoder.encode(rawPassword);
    encodePassword2 = passwordEncoder.encode(rawPassword);

    // 인코딩된 패스워드 출력
    System.out.println("encodePassword : " + encodePassword1);
    System.out.println("  encodePassword : " + encodePassword2);

    String truePassword = "VAM123";
    String falsePassword = " asdfjlasf";

    System.out.println("truePssword verify : " + passwordEncoder.matchers(truePassword, encodePassword1));
    System.out.println("falsePssword verify : " + passwordEncoder.matchers(falsePassword, encodePassword2));


}

/    // 로그인
    @RequestMapping(value = "login", method=RequestMethod.POST)
    public String loginPOST(HttpServletRequest request, MemberVO member, RedirectAttribute rttr) throws Exception{

        //System.out.println("login 메서드 진입");
        //System.out.println("전달된 데이터 : " + member);

        HttpSession session = request.getSession(); // session을 사용하기 위해 session 변수를 선언하고
        String rawPw = "";          // 인코딩 전 비밀번호
        String encodePw = "";       // 인코딩 후 비밀번호
        
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

        }  else {                                              // 일치하는 아이디가 존재하지 않을 시 (로그인 실패2)

           rttr.addFlashAttribute("result", 0);
           return "redirect:/member/login";                    // 로그인 페이지로 이동

        }

        session.setAttribute("member", lvo);                    // 일치하는 아이디, 비밀번호일 경우 (로그인 성공)

        return "redirect:/main";
    }
}