package controller;

import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vam.model.AttachImageVO;
import com.vam.model.AuthorVO;
import com.vam.model.BookVO;
import com.vam.model.Criteria;
import com.vam.model.OrderCancelDTO;
import com.vam.model.OrderDTO;
import com.vam.model.PageDTO;
import com.vam.service.AdminService;
import com.vam.service.AuthorService;
import com.vam.service.OrderService;

import net.coobird.thumbnailator.Thumbnails;

/* HTTP Method /    동작    /   URL 형태
    GET         조회(SEL*READ)  /user/{id}
    POST        생성(CREATE)    /user
    PUT         수정(UPDATE)    /user
    DELETE      삭제(DELETE)    /user/{1}

    RequestMapping은 클래스 레벨에서 사용
    PostMapping, GetMapping 은 메소드에만 사용
 */

@Controller
@RequestMapping("/admiin")
public class AdminController {
    
    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AuthorService authorService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private OrderService orderService;

    /* 관리자 메인 페이지 이동 */
    @RequestMapping(value="main", method=RequestMethod.GET)
    public void adminMainGET() throws Exception {

        logger.info("관리자 페이지 이동");
        
    }

    /* 상품 관리(상품목록) 페이지 접속 */
    @RequestMapping(value="goodManage", method=RequestMethod.GET)
    public void goodManageGET(Criteria cri, Model model) throws Exception {

        /* 상품 리스트 데이터 */
        List list = adminService.goodGetList(cri);
      
        if(!list.isEmpty()) {   // 상품 리스트가 존재 할 경우

            model.addFlashAttribute("list", list); // 리스트 페이지로
        } else {                // 상품 리스트가 존재하지 않을 경우

            model.addFlashAttribute("listCheck", "empty"); // "empty" 문구 표시

            return;             // 상품 관리(상품목록) 페이지로 이동
        }

        /* 페이지 인터페이스 데이터 */
        model.addAttribute("pageMaker", new PageDTO(cri, adminService.goodGetTotal(cri)));
    } 

    /* 상품 등록 페이지 접속 */
    @RequestMapping(value="goodEnroll", method=RequestMethod.GET)
    public void goodEnrollGET(Model model) throws Exception {

        logger.inof("상품 등록 페이지 접속");

        // static 메서드가 아니여서, 인스턴스화 시키고, ObjectMapper 타입의 "mapper" 변수를 선언 후, ObjectMapper 객체로 초기화
        ObjectMapper objm = new ObjectMapper();

        // 상품 카테고리 리스트 메소드 호출
        List list = adminService.cateList();

        /*
           / 카테고리 /
        let cateList = JSON.parse('${cateList}');
        let cate1Array = new Array();
        let cate2Array = new Array();
        let cate3Array = new Array();
        let cate1Obj = new Object();
        let cate2Obj = new Object();
        let cate3Obj = new Object();
        
        let cateSelect1 = $(".cate1");		
        let cateSelect2 = $(".cate2");
        let cateSelect3 = $(".cate3");
        
        / 카테고리 배열 초기화 메서드 /
        function makeCateArray(obj,array,cateList, tier){
            for(let i = 0; i < cateList.length; i++){
                if(cateList[i].tier === tier){
                    obj = new Object();
                    
                    obj.cateName = cateList[i].cateName;
                    obj.cateCode = cateList[i].cateCode;
                    obj.cateParent = cateList[i].cateParent;
                    
                    array.push(obj);				
                    
                }
            }
        }	
        */

        // writeValueAsString = Java 객체를 String 타입의 JSON형식 데이터로 변환
        String cateList = objm.writeValueAsString(list);

        model.addAttribute("cateList", cateList);
    }

    /* 상품 조회 페이지, 상품 수정 페이지 */
    @GetMapping({"/goodDetail", "/goodModify"})
    public void goodsGetInfoGET(int bookId, Criteria cri, Model model) throws JsonProcessingException {

        logger.info("goodGetInfo()........." + bookId);

        ObjectMapper mapper = new ObjectMapper();

        /*  
            AdminMapper.java에서 상품조회를 하는 메소드를 구현하고자 하면
                BookVO result = mapper.goodsGetDetail(bookId);

                : 처음 db에 상품 리스트를 담고 goodsMapper.xml에 쿼리로 상품 정보를 구현
                  mapper의 goodsGetDetail(bookId) 메소드 호출 (bookId) 상품 식별
                  result 변수에 담는다.

                = System.out.println("상품 조회 데이터 : " + result); 하면 상품 정보가 쭉 나온다.
         */

        /* 카테고리 리스트 데이터 */
        model.addAttribute("cateList", mapper.writeValueAsString(adminService.cateList()));

        /* 목록 페이지 조건 정보 */
        model.addAttribute("cri", cri);

        /* 조회 페이지 정보 */
        model.addAttribute("goodsInfo", adminService.goodGetDetail(bookId));
        
    }

    /* 상품 정보 수정 */
    @PostMapping("/goodsModify")
    public String goodsModifyPOST(BookVO vo, RedirectAttributes rttr) {

        logger.info("goodModifyPOST........." + vo);

        int result = adminService.goodsModify(vo);

        rttr.addFlashAttribute("modify_result", result);

        return "redirect:/admin/goodsManage";

    }

        /* 상품 정보 삭제 */
        @PostMapping("/goodsDelete")
        public String goodsDeletePOST(int bookId, RedirectAttiributes rttr, AttachImageVO vo) {
    
            log.info("goodsDeletePOST......");
    
            /*
              상품 정보 삭제는 'DB에 저장된 상품에 대한 정보, 이미지 정보', '서버에 저장된 이미지 파일'
                1.서버 저장 이미지 삭제
                2.DB 정보 삭제(상품, 이미지)      
            
              지정 상품 이미지 정보 얻기 
                파일을 삭제하기 위해선 이미지 파일에 대한 정보가 필요로 하다. 따라서 'bookId' 데이터를 활용하여 상품
                이미지 정보를 DB로부터 가져올 수 있도록 Mapper, Service 메서드를 작성
            */
    
            // 먼저 이미지 정보를 반환해주는 service 메서드를 호출하고 반환받은 값을 List 타입의 fileList 변수에 저장해준다.
            List<AttachImageVO> fileList = adminService.getAttachInfo(bookId);
    
            /*
              변수 fileList에는 상품에 대한 이미지가 존재한다면 AttachImageVO 객체를 요소로 가지는 List 객체가 저장되어 있을 것,
              이미지가 존재하지 않는다면 null일 것이다. 이미지가 없다면 굳이 서버 파일 삭제 코드들이 실행이 될 필요가 없기 때문에,
              이미지가 존재 시 코드가 실행이 될 수 있도록 if문을 작성해 준다.
             */
    
            if(fileList != null) {
    
                // 파일 존재 시, DB로부터 가져온 이미지 정보를 활용하여 Path 객체를 생성하고
                List<Path> pathList = new ArrayList();
    
                /*  
                    forEach는 List, Map과 같은 형태로 쓸 수 있다.
                    또한 람다식을 활용하여 사용한다.
                    ex)
                        List<String> items = new ArrayList<>();
                        items.add("Paris");
                        items.add("Seoul");
                        items.add("Tokyo");
                        items.add("Washington");                    

                        Lamda 사용
                        Lamda : A a = (매개값) -> {구현코드};
                        ex)
                            Runnable runnable = new Runnable() {
                                public void run() {
                                    System.out.println("Lamda");
                                }
                            }

                            Runnable runnable = () -> {
                                System.out.println("Lamda");
                            }
                        items.forEach(name -> System.out.println(name));
                        items.forEach(System.out::printnln);
                 */

                fileList.forEach(vo ->{
    
                    // 원본 이미지
                    Path path = Paths.get("C:\\upload", vo.getUploadPath(), vo.getUuid() + "_" + vo.getFileName());
                    pathList.add(path);
                    
                    // 섬네일 이미지
                    Path path = Paths.get("C:\\upload", vo.getUploadPath(), "s_" + vo.getUuid()+"_" + vo.getFileName());
                    pathList.add(path);
    
                    
                });
    
                // 해당 Path 객체를 File 객체로 변환하여 delete() 메서드를 호출하여 파일을 삭제.
                pathList.forEach(path ->{
    
                        // 1.서버 저장 이미지 삭제
                        path.toFile().delete();
                });
    
            }   
            /*
              이번엔 DB의 데이터를 삭제하는 코드를 추가해줄 차례이다. 기존 코드를 보면 '상품 정보 삭제'를 
              수행하는 Service 메서드가 있는데 해당 메서드에서 '이미지 정보 삭제'에 대한 작업도 수행하도록 만들어 줄 것이다.
              그렇다면 '이미지 정보 삭제'를 수행하는 Mapper 메서드가 필요로한데 이는 앞서 deleteImageAll() 메서드 사용
            */
    
            int result = adminService.goodsDelete(bookId);
    
            rttr.addFlashAttribute("delete_result", result);
    
            return "redirect:/admin/goodsManage";
        }

    /* 작가 등록 페이지 접속 */
    @RequestMapping(value="authorEnroll", method=RequestMethod.GET)
    public void authorEnrollGET() throws Exception {

        log.info("작가 등록 페이지 접속");

    }

    /* 작가 관리 페이지 접속 */
    @RequestMapping(value="authorManage", method=RequestMethod.GET)
    public void authorManageGET(Criteria cri, Model model) throws Exception {

        logger.info("작가 관리 페이지 접속........" + cri);

        /* 작가 목록 출력 데이터 */
        List list = authorService.authorGetList(cri);

        if(!list.isEmpty()) {

            model.addAttribute("list", list); // 작가 존재 경우

        } else {

            model.addAttribute("listCheck", "empty"); // 작가 존재하지 않을 경우

        }

        /* 페이지 이동 인터페이스 데이터 */
        model.addAttribute("pageMaker", new PageDTO(cri, authorService.authorGetTotal(cri)));

    }

    /* 작가 등록 */
    @RequestMapping(value="authorEnroll.do", method=RequestMethod.POST)
    public String authorEnrollPOST(AuthorVO author, RedirectAttributes rttr) throws Exception {

        // 작가 등록 로그확인
        logger.info("authorEnroll : " + author);

        // 작가 등록 쿼리 실행
        authorService.authorEnroll(author); 

        // 등록 성공 메시지(작가이름)
        rttr.addFlashAttribute("enroll_result", author.getAuthorName()); 

        return "redirect:/admin/authorManage";
        
    }

    /* 작가 상세, 수정 페이지 */
    @GetMapping({"/authorDetaiil", "/authorModify"})
    public void authorGetInfoGET(int authorId, Criteria cri, Model model) throws Exception {

        logger.info("authorDetail......" + authorId);

        /* 작가 관리 페이지 정보 */
        model.addAttribute("cri", cri);

        /* 선택 작가 정보 */
        model.addAttribute("authorInfo", authorService.authorGetDetail(authorId));

    }

    /* 작가 정보 삭제 */
    @PostMapping("/authorDelete")
    public String authorDeletePOST(int authorId, RedirectAttiributes rttr) {

        logger.info("authorDeletePOST.........");

        int result = 0;

        try {

            // result 변수에 작가 정보 삭제를 담는다.
            result = authorService.authorDelete(authorId);
        
        } catch (Exception e) {

        /*
            우리가 신경 써줘야 할 부분이 있따. 우리가 삭제하고자 하는 데이터는 작가 테이블
            (vam_author)의 데이터이다. 문제는 외래 키 조건으로 인해 작가 테이블을 참조(reference)하고 있는
            상품 테이블(vam_book)이 있다는 점이다. 참조되지 않고 있는 행을 지운다면 문제가 없지만 만약 참조되고
            있는 행을 지우려고 시도를 한다면 '무결성 제약조건을 위반'한다는 경고와 함께 예외가 발생한다.

            따라서 try catch문을 사용하여 참조되지 않는 행을 지울땐 삭제를 수행하고 '작가 목록' 페이지로 1을 전송하도록 하고,
            예외가 발생한 상황에서는 '작가 목록 페이지'로 2를 전송하도록 작성

            authorManage.jsp
            /* 삭제 결과 경고창 
            let delete_result = ${delete_result};

            if(delete_result == 1) {
                    alert("삭제 완료");
            } else if(delete_result ==  2) {
                    alert("해당 작가 데이터를 사용하고 있는 데이터가 있어서 삭제 할 수 없다.")
            }
            */

            e.printStackTrace();
            result = 2;
            rttr.addFlashAttribute("delete_result", result);

            return "redirect:/admin/authorManager";
        }
    }

    /* 상품 등록 */
    @PostMapping("/goodsEnroll")
    public String goodsEnrollPOST(BookVO book, RedirectAttributes rttr) {
        
        logger.info("goodsEnrollPOST......" + book);
        
        adminService.bookEnroll(book);
        
        rttr.addFlashAttribute("enroll_result", book.getBookName());
        
        return "redirect:/admin/goodsManage";
    }	

    
    /* 작가 검색 팝업창 */
    @GetMapping("/authorPop")
    public void authorPopGET(Criteria cri, Model model) throws Exception {

        logger.info("authorPopGET........");

        // 원래는 '팝업창'에 작가 목록 리스트를 전송하면 10개씩 전송이된다. 팝업창 크기가 작아 5개로 변경해준다.
        cri.setAmount(5);

        /* 게시물 출력 데이터 */
        List list = authorService.authorGetList(cri);

        if(!list.isEmpty()) {

            model.addAttribute("list", list);   // 작가 존재 경우
        } else {

            model.addAttribute("listCheck", "empty");   // 작가 존재하지 않을 경우
        }

        /* 페이지 이동 인터페이스 데이터 */
        model.addAttribute("pageMaker", new PageDTO(cri, authorService.authorGetTotal(cri)));	

    }

    /*  MultipartFile
        View에서 전송한 multipart 타입의 파일을 다룰 수 있도록 해주는 인터페이스이다.
        해당 인터페이스의 메서드들은 파일의 이름 반환, 파일의 사이즈 반환, 파일을 특정 경로에 저장 등을 수행

        // 변경 전
        public void uploadAjaxActionPOST(MultipartFile uploadFile)

        // 변경 후
        public void uploadAjaxActionPOST(MultipartFile[] uploadFile)

        MultiparFile 배열 타입의 uploadFile의 모든 요소의 데이터 정보를 출력시키고 싶다면 for문을 활용하면 됩니다.
		
        //기본 for
		for(int i = 0; i < uploadFile.length; i++) {		
		}

        
        // 향상된 for
		for(MultipartFile multipartFile : uploadFile) {		
		}
		
    */

    /* 첨부 파일 업로드 */
    @PostMapping(value="/uploadAjaxAction", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    // View에서 전송한 첨부파일 데이터를 전달받기 위해서 MultipartFile 타입의 "uploadFile"변수를 매개변수로 부여한다.
    public ResponseEntity<List<AttachImageVO>> uploadAjaxActionPOST(MultipartFile[] uploadFile) {

        logger.info("uploadAjaxActionPOST........");

        logger.info("파일 이름 : " + uploadFile.getOriginalFilename());
		logger.info("파일 타입 : " + uploadFile.getContentType());
		logger.info("파일 크기 : " + uploadFile.getSize());

        /* 이미지 파일 체크 
            이미 뷰에서 이미지 파일을 서버로 전송하기 전에 사용자가 업로드를 위해 선택한 파일이 
            이미지 파일인지 아닌지, 체크를 하도록 코드를 작성하였다. 하지만 엄연히 뷰 단계에서의 체크일 뿐,
            만약 이미지 파일 체크 코드가 작동을 하지 않거나 다른 경로를 통해 업로드 url매핑 메서드를 
            호출할 경우 이미지가 아닌 파일도 업로드될 가능성이 있다.
            
            따라서 좀 더 확실히 이미지 파일만 처리를 보장하는 메서드가 되도록하기 위해서 업로드를 수행하는
            url 매핑 메서드에도 전달 받은 파일이 이미지 파일인지 체크를 하는 코드를 추가해줄 것이다.
            파일을 체크하여 파일이 아닐 경우 에러 상태 코드를 전송을 하여, 뷰에서 그에 따른 경고창이 출력되도록
        */
        
        // 전달받은 모든 파일(uploadFile)의 유형을 체크해야기에 for문 작성
        for(MultipartFile MultipartFile : uploadFile) {
        // for(int i = 0, i < uploadFile.length; i++) {}, 여러개의 파일을 처리할 수 있도록 변경

            // 먼저 전달받은 파일(uploadFile)을 File 객체로 만들어주고 File 참조 변수에 대입한다.
            File checkFile = new File(multipartFile.getOriginalFilename());

            // MIME TYPE(이미지 타입 or 다른 것)을 저장할 String 타입의 type 변수를 선언하고 null로 초기화한다.
            String type = null;

            try {

                /*
                  probeContentType() 메서드를 호출하여 반환받은 Mime Type에 대한 데이터가 image 인지 체크를 하여 image인 경우
                  업로드 코드가 그대로 실행이 되도록 할 것이다. image가 아닌 경우 업로드에 관한 코드가 실행이 되지 않고 상태 코드 400을 뷰에 
                  반환하도록 할 것이다.

                  뷰에서는 ajax의 error 속성을 추가하여 상태코드 400을 전달받았을 때 실행이 되는 콜백 함수를 속성 값으로 부여,
                  잘못된 형식의 파일이라는 경고 문구를 뜨도록 추가할 것 이다.
                 */

                    /* 
                       Java에선 파일의 Mime Type을 반환해주는 메서드를 제공하는데 nio 패키지 File 클래스의 probeContentType() 메서드이다.
                       Files의 probeContentType() 메서드를 호출하여 반환하는 MIME TYPE 데이터를 type 변수에 대입한다.

                       probeContentType은 파라미터로 전달받은 파일의 MIME TYPE을 문자열(String) 반환해주는 메서드이다.
                       파라미터로는 Path 객체를 전달받아야 한다. 따라서 MIME TYPE 확인 대상이자 File 객체인 checkfile을 Path 객체로
                       만들어 주어야 하는데, 이를 위해 File 클래스의 toPath() 메서드를 사용한다.
                    */

                    // 파라미터로 전달받은 파일의 MIME TYPE을 문자열로 설정 위해, checkfile을 Path 객체로 만들어 주고, File 클래스의 toPath() 메서드 사용
                    type = Files.probeContentType(checkFile.toPath());

                    // 전달 받은 파일이 이미지 인지 아닌지 체크를 하기 위해서 파일의 MIME TYPE 속성을 활용 쉽게 말해 LABEL이라고 생각
                    logger.info("MIME TYPE : " + type);

            } catch (IOException e) {

                    e.printStackTrace();
            }

            /*
               image/gif, image/png, image/jpeg, image/bmp, image/webp

               MIME TYPE이 image 인 경우 보시다시피 첫 단어가 image 인 것을 알 수 있습니다. 
               따라서 첫 단어가 image 인지 아닌지를 통해서 image 파일 임을 판단하겠습니다. 
               이를 위해 String 클래스의 startsWith() 메서드를 사용할 것입니다. 
               startsWiath() 메서드는 String 타입의 데이터를 파라미터로 전달받는데, 
               체크 대상인 Stirng 데이터가 메서드의 파라미터로 전달받은 문자로 시작할 경우 true를 그렇지 않을 경우 false를 반환합니다

               구현부가 실행이 되었다는 것은 image가 아니라는 것이기 때문에 메서드가 끝나도록 할 것입니다. 
               전달받은 파일이 image가 아니기 때문에 파일에 대한 정보를 뷰에 전달해줄 필요는 없습니다. 
               하지만 명령이 잘못되었음을 전달하기 위해 response의 상태코드(status)를 400으로 반환 할 것입니다.
             */

            // startsWith() 메서드 선언시 첫 문자가 image가 아닐경우
            if(!type.startsWith("images")) {
                    
                // 전달 해줄 파일의 정보는 없지만 반환 타입이 ResponseEntity<List<AttachImageVO>>이기 때문에 ResponseEntity 객체에 첨부해줄 값 null인 List<AttachImageVO> 타입의 참조 변수 선언
                List<AttachImageVO> list = null;

                // 상태코드가 400인 ResponseEntity 객체를 인스턴스화 하여 이를 반홚해주는 코드
                return new ResponseEntity<>(list, HttpStatus.BAD_REQUEST);
                
                /* 
                    View : error400
                    $.ajax({
                        url: '/admin/uploadAjaxAction',
                        processData : false,
                        contentType : false,
                        data : formData,
                        type : 'POST',
                        dataType : 'json',
                        success : function(result) {
                            console.log(result);
                        },
                        error : function(result) {
                            // 콜백함수에 잘못된 파일 선택시, 알리는 문구
                            alert("이미지 파일이 아닙니다.");
                        }
                    });
                 */
            }
        } // for

        /*
          본격적으로 시작하기 앞서서 업로드를 수행하는 url 매핑 메서드(uploadAjaxActionPOST)에 
          파일을 저장할 기본적 경로를 저장하는 String 타입의 변수를 선언하고 초기화합니다. 
        */

        /* (uploadAjaxActionPOST)에 파일을 저장할 기본적 경로 초기화 */
        String uploadFolder = "C:\\upload";

        /* 날짜 폴더 경로 */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // 오늘의 날짜 데이터를 얻기 위해서 Date 클래스 타입의 변수를 선언 및 초기화
        Date date = new Date();

        /* 오늘의 날짜 데이터를 값을 가지고 있는 date 변수를 "yyyy-MM-dd" 형식의
           문자열로 변환을 해주기 위해서 SimpleDateFormat의 format 메서드 호출
           String 타입으로 변환된 값을 String 타입의 str 변수를 선언.
        */
        String str = sdf.format(date);

        /* 편리하게도 File 클래스에서 실행되는 환경에 따라 그에 맞는 
           경로 구분자를 반환하는 정적(static) 변수인 separator가 있습니다.
        */   
        String datePath = str.replace("-", File.separator);

        /* 폴더 생성 */
        // 첫 번째 인자는 부모 경로, 두 번째 인자는 자식 경로
        File uploadPath = new File(uploadFolder, datePath);

            // 이미 존재하는 폴더 재생성 방지(파일 존재 하면 생성x)
            if(uploadPath.exists() == false) {

                /*
                폴더를 생성을 수행하기 위해서 File 클래스의 mkdir() 혹은 mkdirs()를 사용할 수 있습니다. 
                두 메서드는 폴더를 생성한다는 것을 동일 하지만 한 개의 폴더를 생성할 수 있느냐 여러 개의 폴더를 생성할 수 있느냐의 차이가 있습니다.
                우리는 여러개의 폴더를 생성해야 하기 때문에 mkdirs() 메서드를 사용합니다.
                */
                uploadPath.mkdirs();
            }

            /* 이미지 정보 담는 객체 */
            List<AttachImageVO> list = new ArrayList();

            /* 향상된 for */
            for(MultipartFile multipartFile : uploadFile) {

                /* 이미지 정보 객체 */
                AttachImageVO vo = new AttachImageVO();

                /* 파일 이름 */
                // File 객체를 만들어 주기 전 먼저 파일의 이름을 사용하기 위해 아래와 같이 String 타입의 uploadfileName 변수를 선언하여 파일 이름을 저장
                String uploadFileName = multipartFile.getOriginalFilename();
                vo.setFileName(uploadFileName);
                vo.setUploadPath(datePath);

                /* uuid 
                위에서 업로드 구현을 완료하였습니다. 하지만 한 가지 문제가 있습니다. 동일한 이름을 가진 파일을 저장하게 되면 기존의 파일을 덮어씌워 버린다는 점입니다.
                이러한 문제점을 보완하기 위해서 각 파일이 저장될 때 고유한 이름을 가지도록 하면 해결이 될 것입니다.
                따라서 파일에 고유한 이름을 가지도록 하기 위해서 기존 파일 이름에 UUID가 포함되도록 할 것입니다. 
                UUID(범용 고유 식별자)는 쉽게 말해 국제기구에서 표준으로 정한 식별자(일련번호)라고 생각하시면 됩니다.
                UUID는 총 5개 버전이 있으며 각 버전에 따라 식별자 생성 방식이 다릅니다. 좀 더 자세히 알고 싶으신 분은 UUID 위키 백과를 참고하시면 됩니다
                이를 Java에서도 UUID라는 클래스를 통해 구제기구에서 표준으로 정한 식별자를 사용할 수 있도록 하고 있습니다.
                Java에서도 5개의 버전 방식으로 제공을 하고 있는데 그중 가장 쉽게 사용할 수 있는 버전 4(랜덤) 방식의 radomUUID() 메서드를 사용할 것입니다.
                해당 메서드는 정적(static) 메서드 이기 때문에 UUID를 인스턴스화 하지 않고도 사용할 수 있습니다.
                주의할 점은 UUID.randomUUID()를 통해 생성된 '식별자'는 UUID 타입의 데이터 이기 때문에 toString() 메서들 사용하여 String 타입으로 변경해주어야 우리가 사용할 수 있습니다.
                */
                /* uuid 적용 파일 이름 */
                String uuid = UUID.randomUUID().toString();
                vo.setUuid(uuid);

                uploadFileName = uuid + "-" + uploadFileName();

                /* 파일 위치, 파일 */
                // 파일 저장 위치인 uploadPath와 파일 이름인 uploadFileName을 활용하여 아래와 같이 File 타입의 saveFile 변수를 선언하고 파일 경로와 파일 이름을 포함하는 File 객체로 초기화해준다.
                File saveFile = new File(uploadPath, uploadFileName);

                /* 파일 저장 */
                try {

                    /* 
                    transferTo : 뷰로부터 전달 받은 파일을 폴더에 저장하기 위해 MultipartFile 클래스의 transferTo() 메서드 사용
                                IOException와 IllegalStatusException을 일으킬 가능성이 있기 때문에 컴파일러에서 try catch문을 사용하라는 경고문이 뜬다
                                따라서 파일을 저장하는 코드를 try catch 문으로 감싼다.

                    MultipartFile  객체에 저장하고자 하는 위치를 지정한 File 객체를 파라미터로 하여 transferTo() 메서드 호출
                    MultipartFile.transferTo(file detination);
                    */

                    // 파일을 저장하는 메서드인 transferTo()를 호출한다.
                    multipartFile.transferTo(saveFile);

                    /* 썸네일 생성(ImageIO) */
                    /*
                    썸네일 이미지를 만들기 위해 Java 에서 자체적으로 제공하는 ImageIO를 이용하거나
                    간단히 썸네일 이미지를 만들 수 있도록 도와주는 Scalar, Thumbnailator 등 을 이용하는 방법이 있습니다.
                    이번 포스팅에선 Thumbnailator을 통한 썸네일 이미지 작업을 해 볼것이다.

                    ImageIO : 클래스는 이미지를 읽어오거나 생성(작성?) 할 수 있도록 도와주는 메서드 입니다.
                    BufferedImage : 의 경우 이미지 데이터를 처리하거나 조작에 필요한 값과 메서드를 제공합니다.
                    Graphics2D : 의 경우 그림을 그리는데 필요로 한 설정값과 메서드를 제공하는 클래스입니다.  

                    File thumbnailFile = new File(uploadPath, "s_" + uploadFileName); 
                    
                    BufferedImage bo_image = ImageIO.read(saveFile);
                        //비율 : 소수점 값으로도 지정ㅇ할 수 있도록 double 타입을 지정
                        double ratio = 3;
                        //넓이 높이 : 원본 이미지 비율을 위에서 지정한 값 비율로 줄이기 위해선, 원본 이미지 높이 넓이를 위에서 지정한 비율값으로 나누어주면 된다.
                                        우리가 파라미터 부여해야 할 넓이, 높이 값은 int형이어야 하기 때문에 int로 형변한을 해주었다.
                        int width = (int) (bo_image.getWidth() / ratio);
                        int height = (int) (bo_image.getHeight() / ratio);				
                    
                    // 썸네일 이미지가 가져야 할 넓이와 높이를 얻었기 때문에 해당 값들이 필요로한 파라미터에 인자로 부여한다.
                        (BufferedImage 생성자, drawImage() 메서드 )
                    BufferedImage bt_image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
                    BufferedImage bt_image = new BufferedImage(300, 500m BufferedImage.TYPE_3BYTE_BGR);
                                    
                    Graphics2D graphic = bt_image.createGraphics();
                    
                    // drawImage 메서드를 호출하여 원본 이미지(원본 BuffedImage)를 썸네일 BufferedImage에 지정한 크기로 변경하여
                    왼쪽 상단에 "0, 0" 좌표부터 그려준다. (도화지에 이미지를 그리는 과정이라고 생각하면 된다.)
                    graphic.drawImage(bo_image, 0, 0,width,height, null);
                        
                    // 1.제작한 썸네일 이미지(bt_image)를 이제 파일로 만들어줄 차례이다. ImageIOd의 write 메서드를 호출하여 파일로 저장
                    2. (1) write() 메서드의 첫 번재 인자는 파일로 저장할 이미지이다. 우리가 만든 썸네일 이미지(bt_image)를 인자로 부여
                        (2) 두 번쩨 인자는 어떠한 이미지 형식으로 저장할 것인지 String 타입으로 작성한다. 'jpg', 'png' 등 자신이 원하는 형식을 지정
                        (3) 세 번째 인자는 우리가 앞서 썸네일 이미지가 저장될 경로와 이름으로 생성한 File 객체(ThumbnailFile)를 부여한다.
                    ImageIO.write(bt_image, "jpg", thumbnailFile);				
                    */
                    
                    /* 방법2 (이미지 생성 및 저장(Thumbnailator 라이브러리 사용 */ 
                    // 저장될 썸네일 이미지의 경우 기존 원본 파일이름("uuid_원본파일이름")에서 앞에 "S_"를 붙여 줄 것이다. 
                    File thumbnailFile = new File(uploadPath, "S_" + uploadFileName);

                        // bo_image (buffered original image)
                        BufferedImage bo_image = ImageIO.read(saveFile);

                        // 비율
                        double ratio = 3;
                        // 넓이 높이
                        int width = (int) (bo_image.getWidth() / ratio);
                        int height = (int) (bo_image.getHeight() / ratio);


                    Thumbnails.of(saveFile);
                .size(width, height);
                .toFile(thumbnailFile);


                } catch (Exception e) {

                    e.printStackTrace();

                }

                list.add(vo);

            } // for

            ResponseEntity<List<AttachImageVO>> result = new ResponseEntity<List<AttachImageVO>>(list, HttpStatus.OK);

            return result;

            /*
            복잡해 보이지만 전체적인 과정을 요약하자면 Java 내에서 크기를 지정한 이미지를 만들고,
            그 이미지에 맞게 원본 이미지를 그려 넣은 다음 해당 이미지를 파일로 저장한 것이다.
            */
        }

        /* 이미지 파일 삭제 */
        @PostMapping("/deleteFile")
        public ResponseEntity<String> deleteFile(String fileName) {

             /*
               파일 경로 및 이름 전달받기 위해 String 타입의 fileName 변수 파라미터 부여             
               반환 타입 ResponseEntity, HTTP Body에 String 데이터를 추가하기 위해, 타입 매개변수 String 부여
             */

            logger.info("deleteFile........" + fileName);

            File file = null;

            try {
                /* 썸네일 파일 삭제 */
                /*
                  1.decode() 메서드는 static 메서드이기 때문에 인스턴스화 없이 사용 가능,
                  2.두 번째 파라미터는 대상 문자열 데이터가 어떠한 타입으로 인코딩 되었는지에 대한 정보
                  3.리턴 타입은 디코딩된 문자열(String) 데이터이다.
                 */

                // 삭제할 파일을 대상으로 File 클래스를 인스턴스화 하여 앞서 선언한 file 참조 변수가 참조하도록 한다.
                file = new File("c:\\upload\\" + URLDecoder.decode(fileName, "UTF-8"));
                
                file.delete();
                
                /* 원본 파일 삭제 */
                String originFileName = file.getAbsolutePath().replace("s_", "");
                
                logger.info("originFileName : " + originFileName);
                
                file = new File(originFileName);
                
                file.delete();
                
                
            } catch(Exception e) {
                
                e.printStackTrace();
                
                return new ResponseEntity<String>("fail", HttpStatus.NOT_IMPLEMENTED);
                
            } // catch

            return new ResponseEntity<String>("success", HttpStatus.OK);

        }

        /* 주문 현황 페이지 */
        @GetMapping("/orderList")
        public String orderListGET(Criteria cri, Model model) {
            
            List<OrderDTO> list = adminService.getOrderList(cri);
            
            if(!list.isEmpty()) {
                model.addAttribute("list", list);
                model.addAttribute("pageMaker", new PageDTO(cri, adminService.getOrderTotal(cri)));
            } else {
                model.addAttribute("listCheck", "empty");
            }
            
            
            return "/admin/orderList";
        }	
        
        /* 주문삭제 */
        @PostMapping("/orderCancle")
        public String orderCanclePOST(OrderCancelDTO dto) {
            
            orderService.orderCancle(dto);
            
            return "redirect:/admin/orderList?keyword=" + dto.getKeyword() + "&amount=" + dto.getAmount() + "&pageNum=" + dto.getPageNum();
        }
}
