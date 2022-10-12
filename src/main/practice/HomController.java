package src.main.practice;

/**  일자와 시간을 표기하는 포맷 양식뿐만 아니라 보통 문자열을 일자나 시간으로 또는 아래와 같이 일자나 시간을 문자열로 바꾸는 기능을 제공
     java.text.DateFormat 클래스는 추상클래스이므로 보통 SimpleDateFormat와 같은 서브 클래스를 사용하여 날짜/시간 형식화(날짜를 문자로 변환)
     구문 분석(문자를 날짜로 변환)등의 목적으로 주로 사용한다. 
 */
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereoptype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotaion.RequestMapping;
import org.springframework.web.bind.annotaion.RequstMethod;

/**
 * Handles request for the application home page.
 */
@Controller /**  -Controller의 역할을 수행 한다고 명시(해당 클래스를 Controller로 사용한다고 Spring FrameWork에 알린다.)
                 -필요한 비즈니스 로직을 호출하여 전달할 모델(Model)과 이동할 뷰(View) 정보를 DispacherServlet에 반환 한다.
                 -Bean으로 등록
                 -@Component의 구체화 된 어노테이션
            */
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    /**
     *  simply selects the home view to render by returning its name.
     */
    @RequestMapping(value = "/", method = RequestMethod.GET) // value = URL 값, method = Http Request 메소드 값
    /**@RequestMapping
     *  -요청에 대해 어떤 Controller, 어떤 메소드가 처리할지 맵핑하기 위한 어노테이션
     *  -클래스나 메서드 선언부에 @RequestMapping과 함께 URL을 명시하여 사용한다.
     *  -viewName 생략시 @RequestMapping의 path로 설정한 URL이 default viewName
     */
    public String home(Locale locale, Model model) {
        logger.info("Welcome home! The client locale is {}", locale);

        Date date = new Date();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

        String formattedDate = dateFormat.format(date);

        model.addFlashAttribute("servletTime",, formattedDate);

        return "home";
    }

    /**@RequestParam
     * 1.사용자가 원하는 매개변수에 매핑하기위해 사용한다.
            ex1)
                @PostMapping("/member")
                public String member(@RequestParam String name, @RequestParam int age)

                여기서 @RequestParam은 생략 가능하다. 사용자가 입력한 key값과 매개변수의 이름을 
                비교하여 값을 넣어주기 때문이다.

            ex2)
                @PostMapping("/member)
                public String member(String name, int age)

                걀국 다음의 ex2)와 ex1)은 동일하다.
     *  @PathVariable
     *  2.url 경로를 변수화하여 사용할 수 있도록 해준다.
            ex)
                @RequestMapping("/member/{name}/{age}")
                public String member(@PathVariable("name") String name, @PathVariable("age") String age)
                
                => RequsetMapping의 {name}과 PathVarable의 String name을 매핑 하여 준다.
        *@RequestMethod.GET, POST
         Controller의 @RequestMapping을 아래와 같이 설정한다.
         @RequestMapping(value="경로", method=RequestMethod.GET)
            ex1)
            @RequestMapping(value="/student/studentForm", method=RequestMethod.GET)
            public String studentForm() {
                // get 방식만 가능
                return "student/studentForm"
            }
            이와같이 설정을 하면, GET방식으로 왔을때만 로직을 수행한다.

            ex2)
            @RequestMapping(value="/student/studentPro", method=RequestMethod.POST)
            public String studentPro(StudentVO studentVO) {
                    // post 방식만 가능
                    System.out.println(studentVO.toString());
                    return "student/studentPro";
            }

            ex3)
            만약 GET, POST 상관없이 Controller의 로직을 수행하게 하고 싶다면,
            @RequestMapping의 method 부분을 지우면 된다.
            @RequestMapping(value = "index")
            public String index() {
                // get, post 방식 모두 가능
                return "index";
            }

            만약 내가 로그인을 했는데, 주소창에 내 ID와 비밀번호가 노출되면..
            말 안해도 보안적인 문제가 크다는 것을 알 수 있다.
            숨겨서 보낼 데이터는 POST 방식으로 해야한다.
      */
     */
}            