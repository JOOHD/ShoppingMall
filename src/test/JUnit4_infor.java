단위 테스트(Unit Test)는 프로그램의 기본 단위인 모듈(Module)을 테스트하는 것이다.
'구현 단계에서 각 모듈의 개발을 완료한 후 명세서의 내용대로 정확히 구현되었는지를 테스트하는 것이다.'
테스트가 가능한 최소 단위로 나눠서 테스트를 수행하며 개발 수명주기의 정황과 시스템에 의존적이면서도
시스템의 다른 부분에서 격리하여 독립적으로 수행해야 하는 테스트이다. 단위테스트를 하기 위해서는
가짜 프로그램, 객체(Mock Object)를 만들어서 활용할 수 있으며, 정교하게 테스트 하기 위해서는 
테스트 케이스 작성은 필수라 할 수 있다.

단위테스트의 장점
1.개발단계 초기에 문제를 발견할 수 있게 도와줌
2.리팩토링 또는 라이브러리 업그레이드 등에서 기능 확인을 도와줌(희귀 테스트)
3.기능에 대한 불확실성 감소
4.시스템에 대한 실제 문서 또는 예제로써 사용가능
5.빠른 피드백과 기능을 안전하게 보호

JUnit이란?
Java의 단위테스트를 수행해주는 대표적인 Testing Framework로써 JUnit4와 그 다음 버전인 JUnit5를 대게 사용한다.

ex) JUnit 간단한 실습 예제
    @RestController
    @RequestMapping("/api")
    public class ApiController {
        @GetMapping
        public ResponseEntity<String> hello() {
            return new ResponseEntity<String>(body : "Hello World", HttpStatus.OK);
        }
    }

    // 위와 같은 RestController의 API를 테스트 하기 위해 JUnit5를 사용하여 아래와 같은 테스트 코드를 작성해보았다.

    @ExtendWith(SpringExtension.class)
    @SpringBootTest(webEnviroment = SpringBootTest.webEnviroment.MOCK)
    @AutoConfigureMockMvc
    class ApiControllerTest {
        @Autowired
        MockMvc mockMvc;

        @Test
        void getHelloTest() throws Exception {
            mockMvc.perform(get(urlTemplate : "/api"))
                    .andExpect(status().isOkP())
                    .andExpect(content().string(expectedContent "Hello World"));
        }
    }

    API를 호출하여 테스트하기 위해 Mockito의 mock을 이용하여 환경을 구성 후 기능단위 테스트를 실행해보았다. 원했던 200의 상태코드와 "Hello World"를 얻은 것을 잘 확인할 수 있었다. 위의 코드상에서 사용한 어노테이션과 JUnit에서 사용하는 어노테이션은 다음과 같은 것들이 있다. (JUnit5기준)

    @ExtendWtih(SpringExtenstion.class) : JUnit4의 @RunWith(SpringRunner.class) 와 같은 어노테이션으로 JUnit5부터 사용.
    @SpringBootTest : SpringBoot의 테스트 환경에서 의존성을 주입해주는 중요한 어노테이션으로써 위 코드는 Mock의 의존성을 주입해 주었음. classes라는 parameter를 통해 Bean을 주입할 수 있음.
    @Test : 해당 메소드가 Test대상임을 알려줌.
    @BeforeAll : 해당 클래스의 모든 Test가 수행되기 전에 딱 한번 호출됨. static 메소드의 형태. 보통 설정등에 활용. 
    @AfterAll : 해당 클래스의 모든 Test가 수행된 후에 딱 한번 호출됨. static 메소드의 형태.
    @BeforeEach : 각 Test가 수행되기 전에 매번 호출됨. 로깅등에 활용.
    @AfterEach : 각 Test가 수행된 후에 매번 호출됨.
    위의 mock을 사용한 단위 테스트 말고도 Bean을 주입하여 service 및 respository를 assert함수들을 이용하여 테스트하는 것이 일반적이다.
    
     