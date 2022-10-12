package com.vam.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.vam.model.AttachImageVO;
import com.vam.model.BookVO;
import com.vam.model.Criteria;
import com.vam.model.PageDTO;
import com.vam.model.ReplyDTO;
import com.vam.service.AttachService;
import com.vam.service.BookService;
import com.vam.service.ReplyService;

@Controller // 이 클래스가 컨트롤러 역할을 한다고 스프링에 선언하는 역할.
public class BookController {
    
    // 로그 기록을 남기기 위해서 Logger 클래스 인 logger 변수를 선언 (Lombok을 추가한경우 @Log4j 어노테이션 선언)
    private static final Logger logger = LoggerFactory(BookController.class);

    @Autowired
    private AttachService AttachService;

    @Autowired
    private BookService bookService;

    @Autowired
    private ReplyService replyService;


    // 메인 페이지 이동 
    // (main.jsp에 접속이 가능하도록 하는 메서드(mainPageGET()를 추가한다.))
    // @RequestMapping을 추가해서 url 경로("/main")를 설정한다.
    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public void mainPageGET() {

        logger.info("메인 페이지 진입");
    }

    /* 상품 검색 */
    @GetMapping("search")
    public String searchGoodsGET(Criteria cri, Model model) {

        logger.info("cri : " + cri);

        // 상품VO 변수 = 서비스클래스.상품리스트(cri)불러오기
        List<BookVO> list = bookService.getGoodsList(cri);

        logger.info("pre list : " + list);

        if(!list.isEmpty()) {           // list가 있는 경우

            model.addAttribute("list", list);

            logger.info("list : " + list);

        } else {                        // list가 없는 경우

            // 'empty'로 경고 창 띄우기
            model.addAttribute("listcheck", "empty");

            // search 화면으로 돌아오기
            return "search";
        }

        model.addAttribute("pageMaker", new PageDTO(cri, bookService.goodsGetTotal(cri)));

        String[] typeArr = cri.getType().SPLIT("");

        for(String s : typeArr) {

            if(s.equals("T") || s.equals("A")) {

                model.addAttribute("filter_info", bookService.getCateInfoList(cri));
                
            }
        }

        return "search";
        
    }

    /* 상품 상세 */
    /* 지정한 상품의 상품 페이지로 들어가기 위해서 식별자 값("bookId")을 파라미터 형식("?bookId=1")로 지정
        ex) localhost:8000/admin/goodsDetail?pageNum=1&amount=10&keyword=&bookId=61
     * Spring의 @PathVariable 어노테이션 기능을 사용하게 된다면 '식별자 값'을 파라미터로 넘기지 않고 URL 경로에
       삽입할 수 있게 된다. 사용법은 해당 구현 부분에서 간단히 설명하며 진행하겠다.
        @PathVarialbe 사용 경우
        -> "goodsDetail/61?pageNum=1&amount=10&keywod="
     */
    @GetMapping("/goodsDetail/{bookId}")
    public String goodDetailGET(@PathVariable("bookId") int bookId, Model model) {

        logger.info("goodsDetailGET().....");

        model.addAttribute("goodInfo", bookService.getGoodInfo(bookId));

        return "/goodsDetail";
    }

}