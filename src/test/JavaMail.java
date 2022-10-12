package sendMail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MineMessage;

/*목표
  1.스프링에서 SMTP 서버를 이용한 메일 전송을 할 수 있다.
  2.1의 목표 기능을 구현하기 위해 구글, 네이버, 다음에 어떠한 설정을 해야하는지 알아보자.
  3.단순한 -텍스트만 전송, -이미지를 포함한 메일전송, -업로드를 포함한 메일전송을 알아보자

  목차
  1.메일 계정 보안 설정
  2.pom.xml설정
  3.mailSender Bean 등록
  4.Controller 메서드 추가
  5.텍스트/이미지/업로드 전송
  6.메일 전송 테스트
*/

public class MainEntity {
    static final String FROM = "hddong728@naver.com";
    static final Stirng FROMNAME = "도니서비스";
    static final String TO = "hddong728@daum.net";
    static final String SMTP_USERNAME = "hddong728@naver.com";
    static final String SMTP_PASSWORD = "****";
    static final String HOST = "smtp.live.com";
    static final int PORT = 25;
    static final String SUBJECT = "메일 제목";
    static final String BODY = String.join(

        System.getProperty("line.separator"),
            "<h1>메일 내용<h1>",
            "<p>이 메일은 아름다운 사람이 보낸 아름다운 메일입니다!<p>."
        );
    
    public static void main(String[] args) throws Exception {
        
        properties props = System.getProperties();

        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.starttls.enalble", "true");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);

        MimeMessage msg = new MimeMessage(session);
        
        msg.setFrom(new InternetAddress(FROM, FROMNAME));
        msg.setSubject(SUBJECT);
        msg.setContent(BDOY, "text/html;charset=euc-kr");

        Transport transport = session.getTransport();

        try {
            System.out.println("Sending...");

            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
            transport.senMessage(msg, msg.getAllRecipients());
            
            System.out.println("Email sent!");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            transport.close();
        }
    }    
}

/** Controller 메서드 추가
        MimeMessage 객체를 직접 생성하여 메일을 발송하는 방법과 MimeMessagePreparator를 사용해서
        메일을 전송하는 방법이 있다. 두가지 코드 모두 알아보자.

        보내는 이 이메일, 받는 이 이메일, 메일 제목, 메일내용 4가지의 데이터를 입력해주어야하는데 해당 데이터들을 
        VO객체에 담아서도 가능하고 Controller 메서드 내에 직접 삽입 하는 것도 가능하다.
        이번 포스팅에선 직접 삽입하는 방식을 진행해보자.
 */

// 메일 전송을 위해 필요로한 정보를 세팅하였던 "mailSender" Bean을 인젝션(의존성 주입)하여 사용
    @Autowired
    private JavaMailSender mailSender;

/* MimeMessage 객체를 직접 생성하여 메일을 발송하는 방법
    import javax.mail.internet.MimeMessage;
    import org.springframework.mail.javamail.JavaMailSenderImpl;
    import org.springframework.mail.javamail.MimeMessageHelper;

    *MimeMessage 대신 SimpleMailMessage를 사용할 수도 있습니다. 둘의 차이점은 MimeMessage의 경우
    멀티파트 데이터를 처리 할 수 있고, SimpleMailMessage는 단순한 텍스트 데이터만 전송 가능하다.
*/    

// Controller 메서드
@RequestMapping(value="/sendMail", method = ReqeustMethod.GET)
    public void sendMailTest() throws Exception{

        String subject = "test메일";
        String content = "메일 테스트 내용";
        String from = "보내는 이 아이디 도메인 주소";
        String to = "받는 이 아이디@도메인 주소";

        try {
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper mailHelper = new MimeMessageHelper(mail,true,"UTF-8");
            // true는 멀티파트 메세지를 사용하겠다는 의미
                
            /**
             * 단순한 텍스트 메세지만 사용시엔 아래의 코드도 사용가능
             * MimeMessageHelper mailHelper = new MimeMessageHelper(mail,"UTF-8");
             */

            // 보내는이(from)은 반드시 있어야한다. mailSender 빈에서 아이디를 기입하였지만 이는 SMTP사용 권한을 얻어오는 역할을 수행
            mailHelper.setForm(from);
            // 빈에 아이디 설정한 것은 단순히 smtp 인증을 받기 위해 사용 따라서 보내는이(setForm()) 반드시 필요
            // 보내는이와 메일주소를 수신한는이가 볼떄 모두 표기 되게 원하신다면 아래의 코드를 사용하시면 된다.
            // mailHelper.setForm("보내는이 이름 <보내는이 아이디@도메일주소>");
            mailHelper.setTo(to);
            mailHelper.setSubject(subject);
            mailHelper.setText(content, true);
            // true는 html을 사용하겠다는 의미이다. html을 사용하게되면 이미지를 첨부 할 수 있는 <img>태그를 사용 할 수 있다.

            /**
             * 단순한 텍스트만 사용하신다면 다음의 코드를 사용해도 좋다.
             */

             mailSender.send(mail);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

     /*Multipart
    -HTTP 요청의 한 종류로 HTTP 서버로 파일 혹은 데이터를 보내기 위한 요청 방식이다.
    -주로 HTML Input 앨리먼트에서 enctype으로 이용된다.
        -기본 enctype은 application/x-www-form-urlencoded 이다.
        -text-plain도 있긴 한데 잘 쓰이진 않는다.
    -큰 용량의 바이너리 데이터를 전송하는데 적합하다ㅏ.
        -application/x-www-form-urlencoded enctype은 큰 용량의 데이터를 전송하기엔 적합하지 않다.    
    */

    /*Boundary란 뭘까?
    파일을 첨부할 때, 브라우저가 생성한 값이다.
    일반적으로 여러 개의 엔티티가 전송되었을 때 각각의 key/value 쌍을 구분하기 위해서 boundary를 이용한다.
    위의 HTTP 예제에서는 3개의 GIF 파일이 보내졌는데, 그들을 각각 구분하는 용도로 boundary가 사용되었다.
    boundary는 1~70자의 문자들로 --로 시작하여 --로 끝난다.
    서버에서는 boundary로 나누어 각 데이터들을 읽는다.
    boundary로 나뉘어 여러 엔티티가 전송되기 때문에 Multipart 라는 이름을 쓰는 것 같다. (추측)
    */  
    
/** MimeMessagePreparator를 사용해서 메일을 전송하는 방법
    해당 방식은 MimeMessage를 직접 객체화 하지 않고 MimeMessagePreparator를 클래스를 사용하는 방식이다.
    이방식을 사용하기 위해 import 되는 클래스는 다음과 같다.
    -4.1과 다른점은 import org.springframework.mail.javamail.MimeMessagePreparator가 추가적으로 추가된다는 점이다.
    - new MimeMessageHelper(mail,true,"UTF-8") / mailHelper.setText(content, true) 는 4.1 설명과 동일합니다.
 */ 

 // Controller 메서드 이미지 첨부 (preparator)
@ReqestMapping(value="/sendMail", method = RequestMethod.GET)
public void sendMailTest2() throws Exception{

    String subject = "test 메일";
    String content = "메일 테스트 내용";
    String from = "보내는 이 아이디@도메인주소";
    String to = "받는 이 아이디@도메인주소";
    // 이미지를 첨부하는 방법은
    String content = "메일 테스트 내용" + "<img src=\"이미지 경로\">";

    final MimeMessagePreparator preparator = new MimeMessagePreparator() {

        public void prepare(MimeMessage mimeMessage) throws Exception{
            // 멀티파트 메세지 허용
            final MimeMessageHelper mailHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            mailHelper.setFrom(from);
            mailHelper.setTo(to);
            mailHelper.setSubject(subject);
            // html, true 사용 <img> 태그로 이미지 첨부 가능
            mailHelper.setText(content, true);

        }

    };

    try {
        mailSender.send(preparator);

    }   catch(Exception e) {
        e.printStackTrace();
    }
}

/* Controller 메서드 업로드

    import org.springframework.core.io.FileSystemResource;
    import java.io.File;

    FileSystemResource file = new FileSystemResource(new File("경로\업로드할파일.형식"));
    helper.addAttribute("업로드파일.형식", file);
*/
@RequestMapping(value="/sendMail", Method = RequestMethod.GET)
public void sendMailTest3() throws Exception{

    String subject = "test 메일";
    String content = "메일 테스트 내용" + "<img src=\"https://t1.daumcdn.net/cfile/tistory/214DCD42594CC40625\">";
    String from = "보내는 이 아이디@도메인주소";
    String to = "받는이 아이디@도메인주소";

    try {
        MimeMessage mail = mailSender.createMimeMessage();
        MimeMessageHelper mailHelper = new MimeMessageHelper(mail,true,"UTF-8");
        
        mailHelper.setFrom(from);
        mailHelper.setTo(to);
        mailHelper.setSubject(subject);
        mailHelper.setText(content, true);
        
        FileSystemResource file = new FileSystemResource(new File("D:\\test.txt")); 
        mailHelper.addAttachment("업로드파일.형식", file);
        
        mailSender.send(mail);
        
    } catch(Exception e) {
        e.printStackTrace();
    }
    
}

/* 메일 전송 테스트
   테스트 클래스를 통해 Controller 메서드에 추가했던 코드들을 테스트 해보겠다.
   테스트 클래스를 생성 후 아래의 코드를 작성, mailSendTest(), mailSendTest2() 둘 중 아무거나 사용
*/

import java.io.File;

import javax.mail.internet.MimeMessage;

import org.junit.Test;
import org.junit.runner.RunWith;
/**RunWith 란?
    JUnit 프레임워크의 테스트 실행방법을 확장할 때 사용하는 어노테이션
    즉, ApplicationContext를 만들고 관리하는 작업을 @RunWith(SpringRunner.class)에 설정된 class로 이용하겠다.
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
/**FileSystemResource 란?
    절대경로라던지 파일시스템에서 리소스를 찾는 방식 (거의 사용 X)
    Resource resource = new FileSystemResource({절대경로});
 */
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.test.context.ContextConfiguration;
/**ContextConfiguration 란?
   자동으로 만들어줄 애플리케이션 컨텍스트의 설정파일위치를 지정한 것이다.
 */
import org.springframework.test.context.junit.SpringJUnit4classRunner;

@RunWith(SpringJUnit4classRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
public class MailTestController {

    @Autowired
    JavaMailSenderImpl mailSender;

    // MimeMessage 객체를 직접 생성하여 메일을 발송하는 방법
    @Test
    public void mailSenderTest() throws Exception{

        String subject = "test 메일";
        String content = "메일 테스트 내용" + "<img src=\"https://t1.daumcdn.net/cfile/tistory/214DCD42594CC40625\">";
        String from = "보내는 이 아이디@도메인주소";
        String to = "받는 이 아이디@도메인주소";

        try {
            MimeMessage mail = mailSendeer.createMimeMessage();
            MimeMessageHelper mailHelper = new MimeMessageHelper(mail, "UTF-8");

            mailHelper.setFrom(from);
            mailHelper.setTo(to);
            mailHelper.setSubject(subject);
            mailHelper.setText(content, true);

            FileSystemResource file = new FileSystemResource(new File("D:\\test.txt"));
            mailHelper.addAttachment("test.txt", file);

            mailSender.send(mail);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // MimeMessagePreparator 를 사용해서 메일을 전송하는 방법
    @Test
    public void mailSendTest2() throws Exception{
        
        String subject = "test 메일";
        String content = "메일 테스트 내용" + "<img src=\"https://t1.daumcdn.net/cfile/tistory/214DCD42594CC40625\">";
        String from = "보내는이 아이디@도메인주소";
        String to = "받는이 아이디@도메인주소";
        
        try {
            final MimeMessagePreparator preparator = new MimeMessagePreparator() {
                
                public void prepare(MimeMessage mimeMessage) throws Exception{
                    final MimeMessageHelper mailHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                    
                    mailHelper.setFrom(from);
                    mailHelper.setTo(to);
                    mailHelper.setSubject(subject);
                    mailHelper.setText(content, true);
                    
                    FileSystemResource file = new FileSystemResource(new File("D:\\test.txt")); 
                    mailHelper.addAttachment("test.txt", file);
                    
                }
                
            };
            
            mailSender.send(preparator);
            
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        
        
    }
    
    
}
